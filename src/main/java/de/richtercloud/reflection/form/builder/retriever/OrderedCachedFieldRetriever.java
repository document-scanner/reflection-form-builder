/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.richtercloud.reflection.form.builder.retriever;

import com.google.common.collect.MoreCollectors;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.swing.mxGraphComponent;
import de.richtercloud.validation.tools.CachedFieldRetriever;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link FieldRetriever} which allows callers to pass a reference to a
 * mutable map where they can specify order to field retrieval. The map can be
 * empty or miss keys for certain classes because they will be filled by values
 * from the superclass implementation.
 *
 * @author richter
 */
public class OrderedCachedFieldRetriever extends CachedFieldRetriever {
    private final static Logger LOGGER = LoggerFactory.getLogger(OrderedCachedFieldRetriever.class);
    /**
     * The mapping which contains the information to override the result
     * returned by the subclass in
     * {@link #retrieveRelevantFields(java.lang.Class) }.
     */
    private final Map<Class<?>, List<Field>> fieldOrderMap;
    /**
     * The set of managed entity classes. The set's field order and grouping
     * annotations are validated at creation of the retriever instance which
     * allows to enforce throwing a {@link FieldOrderValidationException} in the
     * constructor and not changing the FieldRetriever interface. If an entity
     * class is passed to {@link #retrieveRelevantFields(java.lang.Class) }
     * which is not included in this set, an {@link IllegalArgumentException} is
     * thrown.
     */
    private final Set<Class<?>> entityClasses;

    /**
     * Creates a new {@code OrderedCachedFieldRetriever} with an empty
     * {@code fieldOrderMap}.
     * @param entityClasses the entity classes to use
     * @throws FieldOrderValidationException if the field order validation fails
     */
    public OrderedCachedFieldRetriever(Set<Class<?>> entityClasses) throws FieldOrderValidationException {
        this(new HashMap<>(), //fieldOrderMap
                entityClasses,
                false //visualizeDependencyGraphOnError
        );
    }

    public OrderedCachedFieldRetriever(Map<Class<?>, List<Field>> fieldOrderMap,
            Set<Class<?>> entityClasses) throws FieldOrderValidationException {
        this(fieldOrderMap,
                entityClasses,
                false //visualizeDependencyGraphOnError
        );
    }

    /*
    internal implementation notes:
    - making visualizeDependencyGraphOnError a static field causes trouble in
    parallel unit tests (which shouldn't display a dialog, but it's very helpful
    in this situation, see commont on visualizeGraph below after search for
    console/text UI based graph visualization)
    */
    /**
     * Creates a new {@code OrderedJPACachedFieldRetriever}.
     * @param fieldOrderMap the field order map
     * @param entityClasses the entity classes to use
     * @param visualizeDependencyGraphOnError A flag to indicate that the field order dependency graph which is
     *     constructed in order to check for cyclic references ought to be
     *     displayed in a {@link JDialog}.
     * @throws FieldOrderValidationException if the field order validation fails
     */
    public OrderedCachedFieldRetriever(Map<Class<?>, List<Field>> fieldOrderMap,
            Set<Class<?>> entityClasses,
            boolean visualizeDependencyGraphOnError) throws FieldOrderValidationException {
        super();
        if(fieldOrderMap == null) {
            throw new IllegalArgumentException("fieldOrderMap mustn't be null");
        }
        this.fieldOrderMap = fieldOrderMap;
        this.entityClasses = entityClasses;
        //validate field order and grouping annotations
        init(visualizeDependencyGraphOnError);
    }

    @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops",
        "PMD.AvoidDeeplyNestedIfStmts"
    })
    private void init(boolean visualizeDependencyGraphOnError) throws FieldOrderValidationException {
        Map<String, Class<?>> fieldGroupNameClassMapping = new HashMap<>();
        //FieldGroups is not a repeatable annotation, so there's no need for
        //a check
        for(Class<?> entityClass : entityClasses) {
            //check that all field group names on all entity classes are
            //non-empty and disjoint
            Map<FieldGroups, Class<?>> fieldGroupsClassMap = generateFieldGroupsClassMap(entityClass);
            for(FieldGroups fieldGroupsAnnotation : fieldGroupsClassMap.keySet()) {
                assert fieldGroupsAnnotation != null:
                        "null keys shouldn't be returned by "
                        + "generateFieldGroupsClassMap";
                FieldGroup[] fieldGroups = fieldGroupsAnnotation.fieldGroups();
                assert fieldGroups != null;
                Set<String> fieldGroupsFieldGroupNames = new HashSet<>();
                    //names of `FieldGroup`s in the same `FieldGroups`
                    //annotation
                for(FieldGroup fieldGroup : fieldGroups) {
                    assert fieldGroup.name() != null;
                    if(fieldGroup.name().isEmpty()) {
                        throw new FieldOrderValidationException(String.format(
                                "Class %s specifies a %s annotation with "
                                + "an empty name",
                                fieldGroupsClassMap.get(fieldGroupsAnnotation).getName(),
                                FieldGroup.class.getName()));
                    }
                    //check that the same FieldGroups annotation doesn't
                    //contain two FieldGroup annotations which have the same
                    //name
                    if(fieldGroupsFieldGroupNames.contains(fieldGroup.name())) {
                        throw new FieldOrderValidationException(String.format(
                                "Class %s specifies a %s annotation which "
                                + "contains two %s annotations with the "
                                + "same name value '%s'",
                                fieldGroupsClassMap.get(fieldGroupsAnnotation).getName(),
                                FieldGroups.class.getName(),
                                FieldGroup.class.getName(),
                                fieldGroup.name()));
                    }
                    fieldGroupsFieldGroupNames.add(fieldGroup.name());
                    //check that there's no duplicate name in the
                    //inheritance hierarchy
                    Class<?> existingDeclarationClass = fieldGroupNameClassMapping.get(fieldGroup.name());
                    Class<?> currentDeclarationClass = fieldGroupsClassMap.get(fieldGroupsAnnotation);
                    if(existingDeclarationClass != null
                            && !existingDeclarationClass.equals(currentDeclarationClass)) {
                        throw new FieldOrderValidationException(String.format(
                                "Both the classes %s and %s declare a %s "
                                + "annotation with name %s",
                                existingDeclarationClass.getName(),
                                currentDeclarationClass.getName(),
                                FieldGroup.class.getName(),
                                fieldGroup.name()));
                    }
                    fieldGroupNameClassMapping.put(fieldGroup.name(),
                            currentDeclarationClass);
                }
            }
            //check that no references to inexisting field groups are made
            List<Field> relevantFields = super.retrieveRelevantFields(entityClass);
            Map<FieldGroup, Class<?>> fieldGroupClassMap = generateFieldGroupClassMap(entityClass);
            for(Field relevantField : relevantFields) {
                FieldPosition relevantFieldPosition = relevantField.getAnnotation(FieldPosition.class);
                if(relevantFieldPosition != null) {
                    assert relevantFieldPosition.fieldGroup() != null;
                    if(!relevantFieldPosition.fieldGroup().isEmpty()) {
                        Set<String> fieldGroupNames = fieldGroupClassMap.keySet().stream()
                                .map(fieldGroup -> fieldGroup.name())
                                .collect(Collectors.toSet());
                        if(!fieldGroupNames.contains(relevantFieldPosition.fieldGroup())) {
                            throw new FieldOrderValidationException(String.format(
                                    "Field %s references the field "
                                    + "group %s which doesn't exist in the "
                                    + "inheritance hierarchy of the class",
                                    getFieldString(relevantField),
                                    relevantFieldPosition.fieldGroup()));
                        }
                    }
                }
            }
            //check that no empty @FieldPosition annotations have been made
            for(Field relevantField : relevantFields) {
                FieldPosition relevantFieldPosition = relevantField.getAnnotation(FieldPosition.class);
                if(relevantFieldPosition != null
                        && relevantFieldPosition.fieldGroup().isEmpty()
                        && relevantFieldPosition.afterFields().length == 0
                        && relevantFieldPosition.beforeFields().length == 0) {
                    throw new FieldOrderValidationException(String.format(
                            "Field %s has a %s annotation "
                            + "which references no field group and has an "
                            + "empty list of afterFields and beforeFields",
                            getFieldString(relevantField),
                            FieldPosition.class.getName()));
                }
            }
            //check that the after and before references are valid field names
            //and that there're no self-references
            for(Field relevantField : relevantFields) {
                FieldPosition relevantFieldPosition = relevantField.getAnnotation(FieldPosition.class);
                if(relevantFieldPosition != null) {
                    for(String afterFieldName : relevantFieldPosition.afterFields()) {
                        List<Field> afterFieldCandidates = relevantFields.stream()
                                .filter(relevantField0 -> relevantField0.getName().equals(afterFieldName))
                                .collect(Collectors.toList());
                            //can't work with relevantField.getDeclaringClass().getDeclaredField
                            //here because that doesn't cover superclasses
                        if(afterFieldCandidates.isEmpty()) {
                            throw new FieldOrderValidationException(String.format(
                                    "Field %s specifies a field in "
                                    + "the afterFields value of its %s "
                                    + "annotation which can't be accessed",
                                    getFieldString(relevantField),
                                    FieldPosition.class.getName()));
                        }else if(afterFieldCandidates.size() > 1) {
                            throw new FieldOrderValidationException(String.format(
                                    "Field %s specifies a field name in the "
                                    + "afterFields value of its %s "
                                    + "annoatation which occurs more than "
                                    + "once in the inheritance hierarchy "
                                    + "which is valid Java, but not supported "
                                    + "by this class",
                                    getFieldString(relevantField),
                                    FieldPosition.class.getName()));
                        }
                    }
                    for(String beforeFieldName : relevantFieldPosition.beforeFields()) {
                        List<Field> beforeFieldCandidates = relevantFields.stream()
                                .filter(relevantField0 -> relevantField0.getName().equals(beforeFieldName))
                                .collect(Collectors.toList());
                            //can't work with relevantField.getDeclaringClass().getDeclaredField
                            //here because that doesn't cover superclasses
                        if(beforeFieldCandidates.isEmpty()) {
                            throw new FieldOrderValidationException(String.format(
                                    "Field %s specifies a field in "
                                    + "the beforeFields value of its %s "
                                    + "annotation which can't be accessed",
                                    getFieldString(relevantField),
                                    FieldPosition.class.getName()));
                        }else if(beforeFieldCandidates.size() > 1) {
                            throw new FieldOrderValidationException(String.format(
                                    "Field %s specifies a field name in the "
                                    + "beforeFields value of its %s "
                                    + "annoatation which occurs more than "
                                    + "once in the inheritance hierarchy "
                                    + "which is valid Java, but not supported "
                                    + "by this class",
                                    getFieldString(relevantField),
                                    FieldPosition.class.getName()));
                        }
                    }
                    //self-references
                    if(Arrays.asList(relevantFieldPosition.afterFields()).contains(relevantField.getName())) {
                        throw new FieldOrderValidationException(String.format(
                                "Field %s%s specifies the field "
                                + "itself in the afterFields value of its %s "
                                + "annotation",
                                relevantField.getDeclaringClass().getName(),
                                relevantField.getName(),
                                FieldPosition.class.getName()));
                    }
                    if(Arrays.asList(relevantFieldPosition.beforeFields()).contains(relevantField.getName())) {
                        throw new FieldOrderValidationException(String.format(
                                "Field %s specifies the field "
                                + "itself in the beforeFields value of its %s "
                                + "annotation",
                                getFieldString(relevantField),
                                FieldPosition.class.getName()));
                    }
                }
            }
            //check that after and before references are in the same group
            Map<FieldGroup, List<Field>> fieldGroupFieldMap = generateFieldGroupFieldMap(relevantFields,
                    fieldGroupClassMap);
            for(FieldGroup fieldGroup : fieldGroupFieldMap.keySet()) {
                List<Field> fieldGroupFields = fieldGroupFieldMap.get(fieldGroup);
                for(Field fieldGroupField : fieldGroupFields) {
                    FieldPosition fieldGroupFieldPosition = fieldGroupField.getAnnotation(FieldPosition.class);
                    assert fieldGroupFieldPosition != null;
                    Set<String> fieldGroupFieldNames = fieldGroupFields.stream()
                                .map(fieldGroupField0 -> fieldGroupField0.getName())
                                .collect(Collectors.toSet());
                    LOGGER.trace(String.format("fieldGroupFieldNames: %s",
                            fieldGroupFieldNames));
                    if(fieldGroupFieldPosition.afterFields().length > 0
                            && Stream.of(fieldGroupFieldPosition.afterFields())
                                    .anyMatch(afterFieldName -> !fieldGroupFieldNames.contains(afterFieldName))) {
                        throw new FieldOrderValidationException(String.format(
                                "Field %s specifies fields in the "
                                + "afterFields value of its %s annoation which "
                                + "are not in the same field group '%s' "
                                + "specified on class %s",
                                getFieldString(fieldGroupField),
                                FieldPosition.class.getName(),
                                fieldGroup.name(),
                                fieldGroupClassMap.get(fieldGroup).getName()));
                    }
                    if(fieldGroupFieldPosition.beforeFields().length > 0
                            && Stream.of(fieldGroupFieldPosition.beforeFields())
                                    .anyMatch(beforeFieldName -> !fieldGroupFieldNames.contains(beforeFieldName))) {
                        throw new FieldOrderValidationException(String.format(
                                "Field %s specifies fields in the "
                                + "beforeFields value of its %s annoation which "
                                + "are not in the same field group '%s' "
                                + "specified on class %s",
                                getFieldString(fieldGroupField),
                                FieldPosition.class.getName(),
                                fieldGroup.name(),
                                fieldGroupClassMap.get(fieldGroup).getName()));
                    }
                }
            }
            //check that groups in beforeGroups and afterGroups exist in the
            //inheritance hierarchy
            for(FieldGroup fieldGroup : fieldGroupClassMap.keySet()) {
                for(String beforeGroupName : fieldGroup.beforeGroups()) {
                    try {
                        fieldGroupClassMap.keySet().stream()
                                .filter(fieldGroup0 -> fieldGroup0.name().equals(beforeGroupName))
                                .collect(MoreCollectors.onlyElement());
                    }catch(NoSuchElementException ex) {
                        throw new FieldOrderValidationException(String.format(
                                "Field group %s specified on class %s "
                                + "specifies field group %s in its "
                                + "beforeGroups value which doesn't exist in "
                                + "the inheritance hierarchy",
                                fieldGroup.name(),
                                fieldGroupClassMap.get(fieldGroup).getName(),
                                beforeGroupName),
                                ex);
                    }
                }
                for(String afterGroupName : fieldGroup.afterGroups()) {
                    try {
                        fieldGroupClassMap.keySet().stream()
                                .filter(fieldGroup0 -> fieldGroup0.name().equals(afterGroupName))
                                .collect(MoreCollectors.onlyElement());
                    }catch(NoSuchElementException ex) {
                        throw new FieldOrderValidationException(String.format(
                                "Field group %s specified on class %s "
                                + "specifies field group %s in its "
                                + "afterGroups value which doesn't exist in "
                                + "the inheritance hierarchy",
                                fieldGroup.name(),
                                fieldGroupClassMap.get(fieldGroup).getName(),
                                afterGroupName),
                                ex);
                    }
                }
            }
            //check for cycles
            DirectedGraph<Field, DefaultEdge> graph = generateGraph(relevantFields,
                    fieldGroupFieldMap);
            CycleDetector<Field, DefaultEdge> cycleDetector = new CycleDetector<>(graph);
            Set<Field> cycles = cycleDetector.findCycles();
            if(!cycles.isEmpty()) {
                if(visualizeDependencyGraphOnError) {
                    visualizeGraph(graph,
                        field0 -> new FieldVertex(field0));
                }
                throw new FieldOrderValidationException(String.format(
                        "Class %s contains the following cyclic in its "
                        + "field order specification: %s (the complete graph was %s)",
                        entityClass.getName(),
                        cycles.toString(),
                        graph.toString()));
            }
        }
    }

    @Override
    @SuppressWarnings("PMD.AvoidDuplicateLiterals")
    public List<Field> retrieveRelevantFields(Class<?> entityClass) {
        if(!entityClasses.contains(entityClass)) {
            throw new IllegalArgumentException(String.format("entityClass %s "
                    + "isn't contained in the set of managed entity classes",
                    entityClass.getName()));
        }
        List<Field> retValue = fieldOrderMap.get(entityClass);
        if(retValue == null) {
            //retrieve fields
            final List<Field> relevantFields = new LinkedList<>(super.retrieveRelevantFields(entityClass));
                //copy list in avoid overwriting cached values
            retValue = new LinkedList<>();
            //order fields
            Map<FieldGroup, Class<?>> fieldGroupClassMap = generateFieldGroupClassMap(entityClass);
                //is free of duplicate groups since otherwise
                //FieldOrderValidationException would have been thrown in the
                //constructor
            Map<FieldGroup, List<Field>> fieldGroupFieldMap = generateFieldGroupFieldMap(relevantFields,
                    fieldGroupClassMap);
            //general comments:
            //- Sorting with Java Timsort is very complex because of the
            //comparison of only two fields, after some days of
            //reflection there seems no way to figure out how to reliably move a
            //field group from the first (before sorting) to the last position
            //(after sorting) without checking all possible chains of field
            //groups in every comparsion which will result in very complicated
            //code.
            //- Using a graph in order to determine the order only makes sense
            //if I'd know a path algorithm which retrieves the order immediately
            //or with few work. The only thing that comes to mind is finding a
            //spanning tree on the graph generated by generateGraph, but it
            //doesn't respect order and might have change two elements. This is
            //the case both for graphs which have only fields as vertices and
            //graphs which have field without groups and field groups as
            //vertices and somehow differentiate between them into the
            //comparsion routine.

            //sort fields inside field groups
            for(FieldGroup fieldGroup : fieldGroupFieldMap.keySet()) {
                List<Field> fieldGroupFields = fieldGroupFieldMap.get(fieldGroup);
                assert fieldGroupFields != null;
                fieldGroupFields.sort((Field field0, Field field1) -> {
                    assert field0 != null;
                    assert field1 != null;
                    if(field0.equals(field1)) {
                        int compareValue = 0;
                        LOGGER.trace(String.format("returning %d for %s and %s since "
                                + "they're equal",
                                compareValue,
                                getFieldString(field0),
                                getFieldString(field1),
                                getFieldString(field1),
                                getFieldString(field0)));
                        return compareValue;
                    }
                    FieldPosition field0Position = field0.getAnnotation(FieldPosition.class);
                    FieldPosition field1Position = field1.getAnnotation(FieldPosition.class);
                    assert field0Position != null;
                    assert field1Position != null;
                    assert !field0Position.fieldGroup().isEmpty();
                    assert field0Position.fieldGroup().equals(field1Position.fieldGroup());
                    if(Arrays.asList(field0Position.afterFields()).contains(field1.getName())) {
                        assert !Arrays.asList(field1Position.afterFields()).contains(field0.getName());
                        int compareValue = 1;
                        LOGGER.trace(String.format("returning %d for %s and %s since "
                                + "%s is an afterField of %s",
                                compareValue,
                                getFieldString(field0),
                                getFieldString(field1),
                                getFieldString(field1),
                                getFieldString(field0)));
                        return compareValue;
                    }
                    if(Arrays.asList(field0Position.beforeFields()).contains(field1.getName())) {
                        assert !Arrays.asList(field1Position.beforeFields()).contains(field0.getName());
                        int compareValue = -1;
                        LOGGER.trace(String.format("returning %d for %s and %s since "
                                + "%s is a beforeField of %s",
                                compareValue,
                                getFieldString(field0),
                                getFieldString(field1),
                                getFieldString(field1),
                                getFieldString(field0)));
                        return compareValue;
                    }
                    if(Arrays.asList(field1Position.afterFields()).contains(field0.getName())) {
                        assert !Arrays.asList(field0Position.afterFields()).contains(field1.getName());
                        int compareValue = -1;
                        LOGGER.trace(String.format("returning %d for %s and %s since "
                                + "%s is an afterField of %s",
                                compareValue,
                                getFieldString(field0),
                                getFieldString(field1),
                                getFieldString(field0),
                                getFieldString(field1)));
                        return compareValue;
                    }
                    if(Arrays.asList(field1Position.beforeFields()).contains(field0.getName())) {
                        assert !Arrays.asList(field0Position.beforeFields()).contains(field1.getName());
                        int compareValue = 1;
                        LOGGER.trace(String.format("returning %d for %s and %s since "
                                + "%s is a beforeField of %s",
                                compareValue,
                                getFieldString(field0),
                                getFieldString(field1),
                                getFieldString(field0),
                                getFieldString(field1)));
                        return compareValue;
                    }
                    int compareValue = Integer.compare(relevantFields.indexOf(field0),
                            relevantFields.indexOf(field1));
                    LOGGER.trace(String.format("returning %d for %s and %s since "
                            + "both don't have relevant order annotation value",
                            compareValue,
                            getFieldString(field0),
                            getFieldString(field1)));
                    return compareValue;
                });
            }
            //build topological graph for field groups
            DirectedGraph<FieldGroup, DefaultEdge> sortingGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
            for(FieldGroup fieldGroup : fieldGroupClassMap.keySet()) {
                    //need to use fieldGroupClassMap because
                    //fieldGroupFieldMap might have no entry for an unused
                    //group (with no fields)
                sortingGraph.addVertex(fieldGroup);
            }
            for(FieldGroup fieldGroup : sortingGraph.vertexSet()) {
                for(String beforeGroupName : fieldGroup.beforeGroups()) {
                    List<FieldGroup> beforeFieldGroupCandidates = fieldGroupClassMap.keySet().stream()
                            .filter(fieldGroup0 -> fieldGroup0.name().equals(beforeGroupName))
                            .collect(Collectors.toList());
                    FieldGroup beforeFieldGroup = beforeFieldGroupCandidates.stream()
                            .collect(MoreCollectors.onlyElement());
                    sortingGraph.addEdge(fieldGroup,
                            beforeFieldGroup);
                }
                for(String afterGroupName : fieldGroup.afterGroups()) {
                    FieldGroup afterFieldGroup = fieldGroupClassMap.keySet().stream()
                            .filter(fieldGroup0 -> fieldGroup0.name().equals(afterGroupName))
                            .collect(MoreCollectors.onlyElement());
                    sortingGraph.addEdge(afterFieldGroup,
                            fieldGroup);
                }
            }
            TopologicalOrderIterator<FieldGroup, DefaultEdge> sortingGraphItr = new TopologicalOrderIterator<>(sortingGraph);
                //DepthFirstIterator provides wrong (inverse?) order
            while(sortingGraphItr.hasNext()) {
                FieldGroup sortingGraphNxt = sortingGraphItr.next();
                List<Field> fieldGroupFields = fieldGroupFieldMap.get(sortingGraphNxt);
                if(fieldGroupFields != null) {
                    retValue.addAll(fieldGroupFields);
                }
            }
            for(Field relevantField : relevantFields) {
                if(!retValue.contains(relevantField)) {
                    retValue.add(relevantField);
                }
            }
            //store result
            fieldOrderMap.put(entityClass,
                    retValue);
                //if the value in fieldOrderMap was null it's fine to overwrite
                //it since the result after ordering the field based on
                //annotations is better than the ordering of the superclass
        }
        return retValue;
    }

    /**
     * Retrieves a mapping between each {@link FieldGroup} in the inheritance
     * hierarchy and a reference to the class on which it has been declared.
     *
     * @param entityClass the class to retrieve the mapping for
     * @return the field group class mapping
     */
    private Map<FieldGroup, Class<?>> generateFieldGroupClassMap(Class<?> entityClass) {
        Map<FieldGroup, Class<?>> fieldGroupMap = new HashMap<>();
        List<Class<?>> inheritanceClasses = generateInheritanceHierarchy(entityClass);
        for(Class<?> inheritanceClass : inheritanceClasses) {
            FieldGroups fieldGroupsAnnotation = inheritanceClass.getAnnotation(FieldGroups.class);
            if(fieldGroupsAnnotation != null) {
                for(FieldGroup fieldGroup : fieldGroupsAnnotation.fieldGroups()) {
                    fieldGroupMap.put(fieldGroup,
                            inheritanceClass);
                }
            }
        }
        assert fieldGroupMap.keySet().stream().map(fieldGroup -> fieldGroup.name()).collect(Collectors.toList()).size()
                == fieldGroupMap.keySet().stream().map(fieldGroup -> fieldGroup.name()).collect(Collectors.toSet()).size();
        return fieldGroupMap;
    }

    /**
     * Retrieves a mapping between field groups and all fields which reference
     * it.
     *
     * @param relevantFields the list of relevant fields
     * @param fieldOrderMap the field order map
     * @return the mapping between field group and fields
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    private Map<FieldGroup, List<Field>> generateFieldGroupFieldMap(List<Field> relevantFields,
            Map<FieldGroup, Class<?>> fieldGroupMap) {
        Map<FieldGroup, List<Field>> fieldGroupFieldMap = new HashMap<>();
        for(Field relevantField : relevantFields) {
            FieldPosition relevantFieldPosition = relevantField.getAnnotation(FieldPosition.class);
            if(relevantFieldPosition != null) {
                assert relevantFieldPosition.fieldGroup() != null;
                if(!relevantFieldPosition.fieldGroup().isEmpty()) {
                    Set<Entry<FieldGroup, Class<?>>> sameNameEntries = fieldGroupMap.entrySet().stream()
                            .filter(fieldGroupPair -> fieldGroupPair.getKey().name().equals(relevantFieldPosition.fieldGroup()))
                            .collect(Collectors.toSet());
                    LOGGER.trace(String.format("sameNameEntries: %s",
                            sameNameEntries));
                    FieldGroup fieldGroup = sameNameEntries.stream().collect(MoreCollectors.onlyElement()).getKey();
                    List<Field> fieldGroupFields = fieldGroupFieldMap.get(fieldGroup);
                    if(fieldGroupFields == null) {
                        fieldGroupFields = new LinkedList<>();
                        fieldGroupFieldMap.put(fieldGroup,
                                fieldGroupFields);
                    }
                    fieldGroupFields.add(relevantField);
                }
            }
        }
        return fieldGroupFieldMap;
    }

    private DirectedGraph<Field, DefaultEdge> generateGraph(List<Field> relevantFields,
            Map<FieldGroup, List<Field>> fieldGroupFieldMap) {
        DirectedGraph<Field, DefaultEdge> graph = new DefaultDirectedGraph<>(DefaultEdge.class);
            //DefaultEdge might have some advantage over Object and that's what
            //is used in the demo at https://github.com/jgrapht/jgrapht/wiki/DirectedGraphDemo
            //as well
        for(Field relevantField : relevantFields) {
            graph.addVertex(relevantField);
        }
        //add all after and before field relations regardless of membership
        //in a field group
        for(Field relevantField : relevantFields) {
            FieldPosition relevantFieldPosition = relevantField.getAnnotation(FieldPosition.class);
            if(relevantFieldPosition != null) {
                for(String beforeFieldName : relevantFieldPosition.beforeFields()) {
                    Field beforeField = relevantFields.stream().filter(relevantField0 -> relevantField0.getName().equals(beforeFieldName)).collect(MoreCollectors.onlyElement());
                    graph.addEdge(relevantField,
                            beforeField);
                    //add references from fields to all fields of a group of a
                    //referenced field if the referenced field is another field
                    //group
                    FieldPosition beforeFieldPosition = beforeField.getAnnotation(FieldPosition.class);
                    if(beforeFieldPosition != null && !beforeFieldPosition.fieldGroup().isEmpty()) {
                        FieldGroup beforeFieldGroup = fieldGroupFieldMap.keySet().stream().filter(fieldGroup -> fieldGroup.name().equals(beforeFieldPosition.fieldGroup())).collect(MoreCollectors.onlyElement());
                        if(!beforeFieldGroup.name().equals(relevantFieldPosition.fieldGroup())) {
                            List<Field> beforeFieldGroupFields = fieldGroupFieldMap.get(beforeFieldGroup);
                            assert beforeFieldGroupFields != null;
                            for(Field beforeFieldGroupField : beforeFieldGroupFields) {
                                graph.addEdge(relevantField,
                                        beforeFieldGroupField);
                            }
                        }
                    }
                }
                for(String afterFieldName : relevantFieldPosition.afterFields()) {
                    Field afterField = relevantFields.stream().filter(relevantField0 -> relevantField0.getName().equals(afterFieldName)).collect(MoreCollectors.onlyElement());
                    graph.addEdge(afterField,
                            relevantField);
                    //add references from fields to all fields of a group of a
                    //referenced field
                    FieldPosition afterFieldPosition = afterField.getAnnotation(FieldPosition.class);
                    if(afterFieldPosition != null && !afterFieldPosition.fieldGroup().isEmpty()) {
                        FieldGroup afterFieldGroup = fieldGroupFieldMap.keySet().stream().filter(fieldGroup -> fieldGroup.name().equals(afterFieldPosition.fieldGroup())).collect(MoreCollectors.onlyElement());
                        if(!afterFieldGroup.name().equals(relevantFieldPosition.fieldGroup())) {
                            List<Field> afterFieldGroupFields = fieldGroupFieldMap.get(afterFieldGroup);
                            assert afterFieldGroupFields != null;
                            for(Field afterFieldGroupField : afterFieldGroupFields) {
                                graph.addEdge(afterFieldGroupField,
                                        relevantField);
                            }
                        }
                    }
                }
            }
        }
        //add references between all fields of all field groups
        for(FieldGroup fieldGroup : fieldGroupFieldMap.keySet()) {
            List<Field> fieldGroupFields = fieldGroupFieldMap.get(fieldGroup);
            for(String beforeFieldGroupName : fieldGroup.beforeGroups()) {
                List<Entry<FieldGroup, List<Field>>> sameNameEntries = fieldGroupFieldMap.entrySet().stream()
                        .filter(entry -> entry.getKey().name().equals(beforeFieldGroupName))
                        .collect(Collectors.toList());
                LOGGER.trace(String.format("sameNameEntries: %s",
                        sameNameEntries));
                if(sameNameEntries.isEmpty()) {
                    //a field group in beforeGroups has been used which isn't
                    //used by any field in the inheritance hierarchy
                    continue;
                }
                List<Field> beforeFieldGroupFields = sameNameEntries.stream().collect(MoreCollectors.onlyElement()).getValue();
                for(Field beforeFieldGroupField : beforeFieldGroupFields) {
                    for(Field fieldGroupField : fieldGroupFields) {
                        graph.addEdge(beforeFieldGroupField,
                                fieldGroupField);
                    }
                }
            }
            for(String afterFieldGroupName : fieldGroup.afterGroups()) {
                List<Entry<FieldGroup, List<Field>>> sameNameEntries = fieldGroupFieldMap.entrySet().stream()
                        .filter(entry -> entry.getKey().name().equals(afterFieldGroupName))
                        .collect(Collectors.toList());
                LOGGER.trace(String.format("sameNameEntries: %s",
                        sameNameEntries));
                if(sameNameEntries.isEmpty()) {
                    //a field group in afterGroups has been used which isn't
                    //used by any field in the inheritance hierarchy
                    continue;
                }
                List<Field> afterFieldGroupFields = sameNameEntries.stream().collect(MoreCollectors.onlyElement()).getValue();
                for(Field afterFieldGroupField : afterFieldGroupFields) {
                    for(Field fieldGroupField : fieldGroupFields) {
                        graph.addEdge(fieldGroupField,
                                afterFieldGroupField);
                    }
                }
            }
        }
        return graph;
    }

    private Map<FieldGroups, Class<?>> generateFieldGroupsClassMap(Class<?> entityClass) {
        Map<FieldGroups, Class<?>> retValue = new HashMap<>();
        List<Class<?>> hierarchyClasses = generateInheritanceHierarchy(entityClass);
        for(Class<?> hierarchyClass : hierarchyClasses) {
            FieldGroups hierarchyClassFieldGroups = hierarchyClass.getAnnotation(FieldGroups.class);
            if(hierarchyClassFieldGroups != null) {
                retValue.put(hierarchyClassFieldGroups,
                        hierarchyClass);
            }
        }
        return retValue;
    }

    /**
     * Transforms {@code graph} in a graph with visualizable vertex and edge
     * types and displays it in a {@link JDialog} which blocks until it is
     * closed.
     *
     * There's apparently no graph library which allows printing layed out
     * graphs to console (asked
     * https://softwarerecs.stackexchange.com/questions/45148/java-graph-library-with-visualization-on-console
     * for input).
     *
     * @param graph the graph to visualize
     */
    @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
    private <O, I> void visualizeGraph(DirectedGraph<I, DefaultEdge> graph,
            VisualizationVertexFactory<O, I> visualizationEdgeFactory) {
        try {
            Runnable runnable = () -> {
                //This code is from
                //https://stackoverflow.com/questions/24517434/drawing-a-simpleweightedgraph-on-a-jpanel/24519791#24519791
                JDialog dialog = new JDialog((Frame)null, //parent
                        "Failure: The field order dependency graph contains cycles!",
                        true //modal
                );
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                DirectedGraph<O, FieldEdge> graph0 = new DefaultDirectedGraph<>(FieldEdge.class);
                for(I vertex : graph.vertexSet()) {
                    O transformedVertex = visualizationEdgeFactory.create(vertex);
                    graph0.addVertex(transformedVertex);
                }
                for(DefaultEdge edge : graph.edgeSet()) {
                    O transformedSource = visualizationEdgeFactory.create(graph.getEdgeSource(edge));
                    O transformedTarget = visualizationEdgeFactory.create(graph.getEdgeTarget(edge));
                    graph0.addEdge(transformedSource,
                            transformedTarget);
                }
                JGraphXAdapter<O, FieldEdge> graphAdapter =
                        new JGraphXAdapter<>(graph0);
                mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
                layout.execute(graphAdapter.getDefaultParent());
                dialog.add(new mxGraphComponent(graphAdapter));
                dialog.pack();
                dialog.setLocationByPlatform(true);
                LOGGER.info("displayed field order dependency graph "
                        + "visualization dialog and waiting for it to be "
                        + "closed before continueing");
                dialog.setVisible(true);
            };
            if(SwingUtilities.isEventDispatchThread()) {
                runnable.run();
            }else {
                SwingUtilities.invokeAndWait(runnable);
            }
        } catch (InterruptedException | InvocationTargetException ex) {
            throw new RuntimeException(ex); //@TODO:
        } catch(HeadlessException ex) {
            LOGGER.warn("experienced headless exception during visualization "
                    + "of field and field group dependency order graph, so "
                    + "naively assuming a headless environment and skipping "
                    + "this silently",
                    ex);
        }
    }

    @FunctionalInterface
    private interface VisualizationVertexFactory<O, I> {
        O create(I vertexObject);
    }

    /**
     * Allows to change the way fields are represented as strings in logging and
     * exception messages.
     * @param field the field to represent
     * @return the representation string
     */
    private String getFieldString(Field field) {
        return String.format("%s.%s",
                field.getDeclaringClass().getName(),
                field.getName());
    }
}
