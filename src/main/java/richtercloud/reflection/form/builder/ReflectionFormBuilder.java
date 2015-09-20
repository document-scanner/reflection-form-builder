/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.reflection.form.builder;

import com.google.common.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.components.SqlDatePicker;
import richtercloud.reflection.form.builder.components.UtilDatePicker;
import richtercloud.reflection.form.builder.retriever.CheckBoxRetriever;
import richtercloud.reflection.form.builder.retriever.SpinnerRetriever;
import richtercloud.reflection.form.builder.retriever.SqlDatePickerRetriever;
import richtercloud.reflection.form.builder.retriever.TextFieldRetriever;
import richtercloud.reflection.form.builder.retriever.UtilDatePickerRetriever;
import richtercloud.reflection.form.builder.retriever.ValueRetriever;

/**
 * Builds a {@link ReflectionFormPanel} by recursing over all fields of all
 * subclasses of an class and arranging form components for each item. The
 * component to choose is configurable.
 *
 * Handles fields with collection types with {@link CollectionFieldHandler}s in
 * order to provide maximal extendability.
 *
 * @author richter
 */
public class ReflectionFormBuilder {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionFormBuilder.class);
    /*
    internal implementation notes:
    - see ValueRetriever's class comment in order to understand why there's no
    subclassing of JComponent
    - separate class mapping and primitive mapping because a primitive can't be
    used in TypeToken and if primitive mapping is checked to contain primitives
    as keys only, then there's no need to deal with precedences and overwriting
    - Rather than mapping static class types maps factory instances because that
    allows to cover generic types (which require argument like the generic type
    to be evaluated) with the same mechanism (this is not necessary for the
    primitive mapping). This allows to set default values on components as well
    easily (can't be done by specifying a subclass of the component).
    */
    public static final Map<Type, FieldHandler> CLASS_MAPPING_DEFAULT;
    public static final Map<Class<?>, Class<? extends JComponent>> PRIMITIVE_MAPPING_DEFAULT;
    public static final Map<Class<? extends JComponent>, ValueRetriever<?, ?>> VALUE_RETRIEVER_MAPPING_DEFAULT;
    /*
    internal implementation notes:
    - can' suppress serial warning lower than on class level because using a
    method causes "might not have been intialized" error for map(s) (asked
    http://stackoverflow.com/questions/32666745/how-to-suppress-serial-warning-for-a-static-block-where-a-final-static-variable for input)
    */
    static {
        Map<Type, FieldHandler> classMappingDefault0 = new HashMap<>();
        classMappingDefault0.put(new TypeToken<String>(){}.getType(), StringFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Float>(){}.getType(), FloatFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Integer>(){}.getType(), IntegerFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Double>(){}.getType(), DoubleFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Long>(){}.getType(), LongFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Number>(){}.getType(), NumberFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Boolean>(){}.getType(), BooleanFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<Date>(){}.getType(), DateFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<java.sql.Date>(){}.getType(), SqlDateFieldHandler.getInstance());
        classMappingDefault0.put(new TypeToken<List<AnyType>>(){}.getType(), GenericListFieldHandler.getInstance());
        CLASS_MAPPING_DEFAULT = Collections.unmodifiableMap(classMappingDefault0);
    }
    static {
        Map<Class<?>, Class<? extends JComponent>> primitiveMappingDefault0 = new HashMap<>();
        primitiveMappingDefault0.put(float.class, JSpinner.class);
        primitiveMappingDefault0.put(int.class, JSpinner.class);
        primitiveMappingDefault0.put(double.class, JSpinner.class);
        primitiveMappingDefault0.put(long.class, JSpinner.class);
        primitiveMappingDefault0.put(boolean.class, JCheckBox.class);
        PRIMITIVE_MAPPING_DEFAULT = Collections.unmodifiableMap(primitiveMappingDefault0);
    }
    static {
        Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping0 = new HashMap<>();
        valueRetrieverMapping0.put(JTextField.class, TextFieldRetriever.getInstance());
        valueRetrieverMapping0.put(JSpinner.class, SpinnerRetriever.getInstance());
        valueRetrieverMapping0.put(JCheckBox.class, CheckBoxRetriever.getInstance());
        valueRetrieverMapping0.put(UtilDatePicker.class, UtilDatePickerRetriever.getInstance());
        valueRetrieverMapping0.put(SqlDatePicker.class, SqlDatePickerRetriever.getInstance());
        VALUE_RETRIEVER_MAPPING_DEFAULT = Collections.unmodifiableMap(valueRetrieverMapping0);
    }
    public static final List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> FIELD_ANNOTATION_MAPPING_DEFAULT = Collections.unmodifiableList(new LinkedList<Pair<Class<? extends Annotation>, FieldAnnotationHandler>>());
    public static final List<Pair<Class<? extends Annotation>, ClassAnnotationHandler>> CLASS_ANNOTATION_MAPPING_DEFAULT = Collections.unmodifiableList(new LinkedList<Pair<Class<? extends Annotation>, ClassAnnotationHandler>>());
    private Map<Type, FieldHandler> classMapping;
    private Map<Class<?>, Class<? extends JComponent>> primitiveMapping;
    private Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping;
    private List<Field> entityClassFields;

    /**
     * the order in the list defines the precedence of annotations
     */
    /*
    internal implementation notes:
    - maps to a Callable<? extends JComponent> because some instances require
    constructor arguments
    */
    private List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping;
    private List<Pair<Class<? extends Annotation>, ClassAnnotationHandler>> classAnnotationMapping;

    /*
    internal implementation notes:
    - maps to a Callable<? extends JComponent> because some instances require
    constructor arguments (this makes it impossible to provide a default
    mapping and thus passing the argument is enforced in constructor)
    */
    public ReflectionFormBuilder(List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping, List<Pair<Class<? extends Annotation>, ClassAnnotationHandler>> classAnnotationMapping) {
        this(CLASS_MAPPING_DEFAULT, PRIMITIVE_MAPPING_DEFAULT, VALUE_RETRIEVER_MAPPING_DEFAULT, fieldAnnotationMapping, classAnnotationMapping);
    }

    public ReflectionFormBuilder(Map<Type, FieldHandler> classMapping,
            Map<Class<?>, Class<? extends JComponent>> primitiveMapping,
            Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping,
            List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping,
            List<Pair<Class<? extends Annotation>, ClassAnnotationHandler>> classAnnotationMapping) {
        if(classMapping == null) {
            throw new IllegalArgumentException("classMapping mustn't be null");
        }
        if(classMapping.values().contains(null)) {
            throw new IllegalArgumentException(String.format("classMapping mustn't contain null values"));
        }
        if(primitiveMapping == null) {
            throw new IllegalArgumentException("primitiveMapping musn't be null");
        }
        if(primitiveMapping.values().contains(null)) {
            throw new IllegalArgumentException("primitiveMapping mustn't contain null values");
        }
        for(Class<?> primitiveMappingKey : primitiveMapping.keySet()) {
            if(!primitiveMappingKey.isPrimitive()) {
                throw new IllegalArgumentException("primitiveMapping only allows primitive classes as keys");
            }
        }
        if(valueRetrieverMapping == null) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't be null");
        }
        if(valueRetrieverMapping.values().contains(null)) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't contain null values");
        }
        if(fieldAnnotationMapping == null) {
            throw new IllegalArgumentException("fieldAnnotationMapping mustn't be null");
        }
        if(fieldAnnotationMapping.contains(null)) {
            throw new IllegalArgumentException("fieldAnnotationMapping mustn't contain null");
        }
        if(classAnnotationMapping == null) {
            throw new IllegalArgumentException("classAnnotationMapping mustn't be null");
        }
        if(classAnnotationMapping.contains(null)) {
            throw new IllegalArgumentException("classAnnotationMapping mustn't contain null");
        }
        this.classMapping = classMapping;
        this.primitiveMapping = primitiveMapping;
        this.valueRetrieverMapping = valueRetrieverMapping;
        this.fieldAnnotationMapping = fieldAnnotationMapping;
        this.classAnnotationMapping = classAnnotationMapping;
    }

    /**
     * recursively retrieves all fields from the inheritance hierachy of {@code entityClass}, except {@code static} fields.
     * @param clazz
     * @return
     */
    /*
    internal implementation notes:
    - return a List in order to be able to modify order (it'd be nice to have
    @Id annotated property first
     */
    public List<Field> retrieveRelevantFields(Class<?> clazz) {
        List<Field> retValue = new LinkedList<>();
        Class<?> hierarchyPointer = clazz;
        while(!hierarchyPointer.equals(Object.class)) {
            retValue.addAll(Arrays.asList(hierarchyPointer.getDeclaredFields()));
            hierarchyPointer = hierarchyPointer.getSuperclass();
        }
        Set<Field> seenEntityClassFields = new HashSet<>();
        ListIterator<Field> entityClassFieldsIt = retValue.listIterator();
        while(entityClassFieldsIt.hasNext()) {
            Field entityClassFieldsNxt = entityClassFieldsIt.next();
            if(Modifier.isStatic(entityClassFieldsNxt.getModifiers())) {
                entityClassFieldsIt.remove();
                continue;
            }
            if(seenEntityClassFields.contains(entityClassFieldsNxt)) {
                entityClassFieldsIt.remove();
                continue;
            }
            seenEntityClassFields.add(entityClassFieldsNxt);
            entityClassFieldsNxt.setAccessible(true);
        }
        return retValue;
    }

    /**
     * Retrieves the associated {@link JComponent} to be displayed in the form.
     * @param field
     * @param entityClass
     * @return a {@link JComponent}, never {@code null}
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    /*
    internal implementation entityClass is added here for subclasses
    */
    protected  JComponent getClassComponent(Field field, Class<?> entityClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JComponent retValue;
        // annotation have precedence
        // field annotations (have precedence over class annotations)
        for(Pair<Class<? extends Annotation>, FieldAnnotationHandler> pair : fieldAnnotationMapping) {
            if(field.getAnnotation(pair.getKey()) != null) {
                try {
                    Object entity;
                    Constructor<?> entityClassConstructor = entityClass.getDeclaredConstructor();
                    entityClassConstructor.setAccessible(true);
                    entity = entityClassConstructor.newInstance();
                    retValue = pair.getValue().handle(field.getType(), entity, this);
                    return retValue;
                } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        // class annotations
        for(Pair<Class<? extends Annotation>, ClassAnnotationHandler> pair : classAnnotationMapping) {
            if(field.getType().getAnnotation(pair.getKey()) != null) {
                try {
                    retValue = pair.getValue().handle(entityClassFields, field.getType());
                    return retValue;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        Class<? extends JComponent> clazz = null;
        if(field.getType().isPrimitive()) {
            clazz = primitiveMapping.get(field.getType());
            if(clazz == null) {
                clazz = ReflectionFormBuilder.PRIMITIVE_MAPPING_DEFAULT.get(field.getType());
            }
        }
        if(clazz != null) {
            Constructor<? extends JComponent> clazzConstructor = clazz.getDeclaredConstructor();
            retValue = clazzConstructor.newInstance();
            return retValue;
        }
        // check exact type match
        FieldHandler fieldHandler = retrieveFieldHandler(field.getGenericType());
        if(fieldHandler == null) {
            retValue = new JLabel(field.getType().getSimpleName());
        } else {
            retValue = fieldHandler.handle(field.getGenericType(), //very important to call with generic type in order to not confuse setup
                    this);
        }
        return retValue;
    }

    /**
     *
     * @param entityClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException if {@code entityClass} doesn't provide
     * a zero-argument-constructor
     */
    public ReflectionFormPanel transform(Class<?> entityClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        final Map<Field, JComponent> fieldMapping = new HashMap<>();
        Constructor<?> entityClassConstructor = null;
        try {
            entityClassConstructor = entityClass.getDeclaredConstructor();
        }catch(NoSuchMethodException ex) {
            throw new IllegalArgumentException(String.format("entityClass %s doesn't provide a zero-argument-constructor (see nested exception for details)", entityClass), ex);
        }
        entityClassConstructor.setAccessible(true);
        Object instance = entityClassConstructor.newInstance();
        ReflectionFormPanel retValue = new ReflectionFormPanel(fieldMapping, instance, entityClass, this.valueRetrieverMapping, classMapping);
        this.entityClassFields = this.retrieveRelevantFields(entityClass);

        GroupLayout layout = new GroupLayout(retValue.getMainPanel());
        retValue.getMainPanel().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        Group horizontalSequentialGroup = layout.createSequentialGroup();
        Group horizontalLabelParallelGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        Group horizontalCompParallelGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        Group verticalSequentialGroup = layout.createSequentialGroup();
        for(Field field : this.entityClassFields) {
            JComponent comp = this.getClassComponent(field, entityClass);
            JLabel label = new JLabel(field.getName());
            horizontalLabelParallelGroup.addComponent(label);
            horizontalCompParallelGroup.addComponent(comp);
            Group fieldGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            fieldGroup.addComponent(label);
            fieldGroup.addComponent(comp);
            verticalSequentialGroup.addGroup(fieldGroup);
            fieldMapping.put(field, comp);
        }
        horizontalSequentialGroup.addGroup(horizontalLabelParallelGroup).addGroup(horizontalCompParallelGroup);
        layout.setHorizontalGroup(horizontalSequentialGroup);
        layout.setVerticalGroup(verticalSequentialGroup);
        retValue.getMainPanel().validate();
        retValue.validate();
        return retValue;
    }

    /**
     * figures out candidates which the longest common prefix in the
     * {@code fieldParameterizedType} chain of (nested) generic types ignoring
     * specifications of {@link AnyType}. Then determines the candidates with
     * the smallest number of {@link AnyType}  specifications in the chain. If
     * there're multiple with the same number of {@link AnyType} chooses the
     * first it finds which might lead to random choices.
     * @param fieldParameterizedType the chain of generic types (remember to
     * retrieve this information with {@link Field#getGenericType() } instead of
     * {@link Field#getType() } from fields)
     * @return the choice result as described above or {@code null} if no
     * candidate exists
     */
    protected Type retrieveClassMappingBestMatch(ParameterizedType fieldParameterizedType) {
        //check in a row (walking a tree doesn't make sense because it's
        //agnostic of the position of the type
        SortedMap<Integer, List<ParameterizedType>> candidates = new TreeMap<>(); //TreeMap is a SortedMap
        for(Type mappingType: classMapping.keySet()) {
            if(!(mappingType instanceof ParameterizedType)) {
                continue;
            }
            ParameterizedType mappingParameterizedType = (ParameterizedType) mappingType;
            Type[] parameterizedTypeArguments = mappingParameterizedType.getActualTypeArguments();
            Type[] fieldParameterizedTypeArguments = fieldParameterizedType.getActualTypeArguments();
            for(int i=0; i<Math.min(parameterizedTypeArguments.length, fieldParameterizedTypeArguments.length); i++) {
                if(fieldParameterizedTypeArguments[i].equals(AnyType.class)) {
                    throw new IllegalArgumentException(String.format("type %s must only be used to declare placeholders in class mapping, not in classes (was used in field type %s", AnyType.class, fieldParameterizedType));
                }
                // only compare raw type to raw type in the chain
                Type fieldParameterizedTypeArgument = fieldParameterizedTypeArguments[i];
                if(fieldParameterizedTypeArgument instanceof ParameterizedType) {
                    fieldParameterizedTypeArgument = ((ParameterizedType)fieldParameterizedTypeArgument).getRawType();
                }
                Type parameterizedTypeArgument = parameterizedTypeArguments[i];
                if(parameterizedTypeArgument instanceof ParameterizedType) {
                    parameterizedTypeArgument = ((ParameterizedType)parameterizedTypeArgument).getRawType();
                }
                //record AnyType matches as well
                boolean anyTypeMatch = AnyType.class.equals(parameterizedTypeArgument); //work around sucky debugger
                if(!parameterizedTypeArgument.equals(fieldParameterizedTypeArgument) && !anyTypeMatch) {
                    break;
                }
                int matchCount = i+1;
                List<ParameterizedType> candidateList = candidates.get(matchCount);
                if(candidateList == null) {
                    candidateList = new LinkedList<>();
                    candidates.put(matchCount, candidateList);
                }
                candidateList.add(mappingParameterizedType);
            }
        }
        if(candidates.isEmpty()) {
            return null; //avoid NoSuchElementException
        }
        List<ParameterizedType> higestCandidatesList = candidates.get(candidates.lastKey());
        int lowestAnyCount = Integer.MAX_VALUE;
        ParameterizedType lowestAnyCountCandidate = null;
        for(ParameterizedType highestCandidateCandidate : higestCandidatesList) {
            int highestCandidateCandidateAnyCount = retrieveAnyCountRecursively(highestCandidateCandidate);
            if(highestCandidateCandidateAnyCount < lowestAnyCount) {
                lowestAnyCount = highestCandidateCandidateAnyCount;
                lowestAnyCountCandidate = highestCandidateCandidate;
            }
        }
        return lowestAnyCountCandidate;
    }

    protected int retrieveAnyCountRecursively(ParameterizedType type) {
        int retValue = 0;
        for(Type typeArgument : type.getActualTypeArguments()) {
            if(AnyType.class.equals(typeArgument)) {
                retValue += 1;
            }
            if(typeArgument instanceof ParameterizedType) {
                int recRetValue = retrieveAnyCountRecursively((ParameterizedType) typeArgument);
                retValue += recRetValue;
            }
        }
        return retValue;
    }

    public Map<Type, FieldHandler> getClassMapping() {
        return Collections.unmodifiableMap(this.classMapping);
    }

    /**
     *
     * @return an unmodifiable version of the class annotation mapping
     */
    public List<Pair<Class<? extends Annotation>, ClassAnnotationHandler>> getClassAnnotationMapping() {
        return Collections.unmodifiableList(classAnnotationMapping);
    }

    /**
     * the initial list of entity class fields created with
     * {@link #retrieveRelevantFields(java.lang.Class) }
     * @return
     */
    public List<Field> getEntityClassFields() {
        return Collections.unmodifiableList(this.entityClassFields);
    }

    /**
     *
     * @param fieldType always call with return value of {@link Field#getGenericType() } in order to be the correct match
     * @return
     */
    protected FieldHandler retrieveFieldHandler(Type fieldType) {
        Type classMappingKey = fieldType;
        if(fieldType instanceof ParameterizedType) {
            classMappingKey = retrieveClassMappingBestMatch((ParameterizedType) fieldType);
        }
        FieldHandler fieldHandler = this.classMapping.get(classMappingKey);
        if(fieldHandler == null) {
            fieldHandler = ReflectionFormBuilder.CLASS_MAPPING_DEFAULT.get(classMappingKey);
        }
        // if exact type didn't match check closest match (only makes sense for
        // parameterized types as the others would have been found as direct
        // match already
        if(fieldHandler == null && fieldType instanceof ParameterizedType) {
            ParameterizedType fieldParameterizedType = (ParameterizedType) fieldType;
            Type candidate = retrieveClassMappingBestMatch(fieldParameterizedType);
            fieldHandler = classMapping.get(candidate);
        }
        return fieldHandler;
    }

}
