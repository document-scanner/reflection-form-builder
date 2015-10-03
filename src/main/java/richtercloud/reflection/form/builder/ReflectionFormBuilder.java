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
 * components to choose are configurable.
 *
 * The handling of fields is conrolled with different mappings which are
 * {@code fieldAnnotationMapping}, {@code classAnnotationMapping},
 * {@code classAnnotationMapping} and {@code primitiveMapping} which all
 * associate a characteristic of a class field to an instance of
 * {@link JComponent} which is put into the panel (some components are generated
 * using the Reflection API, others use more complex factories, like
 * {@link FieldAnnotationHandler} and {@link ClassAnnotationHandler}.
 *
 * The precedence of the mappings is static and reads as listed above. It can be
 * overwritten with sets of ignores which can be specified for each mapping
 * individually ({@code ignoresFieldAnnotationMapping},
 * {@code ignoresClassAnnotationMapping} and {@code ignoresPrimitiveMapping} -
 * note that it doesn't make sense to provide ignores for the last mapping
 * because adding an item to it would be the same as omitting the type in all
 * mappings).
 *
 * @author richter
 */
/*
 internal implementation notes:
 - an existing entity can be updated through the transform method, but is not preserved as class property in order to maximize immutability
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
     - Rather than mapping static class types map factory instances because that
     allows to cover generic types (which require argument like the generic type
     to be evaluated) with the same mechanism (this is necessary for the
     primitive mapping as soon as component ought to be responsible to manage
     properties of instances autonomously). This allows to set default values on
     components as well easily (can't be done by specifying a subclass of the
     component).
     */
    public static final Map<Type, FieldHandler<?, ?>> CLASS_MAPPING_DEFAULT;
    public static final Map<Class<? extends JComponent>, ValueRetriever<?, ?>> VALUE_RETRIEVER_MAPPING_DEFAULT;

    static {
        Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping0 = new HashMap<>();
        valueRetrieverMapping0.put(JTextField.class, TextFieldRetriever.getInstance());
        valueRetrieverMapping0.put(JSpinner.class, SpinnerRetriever.getInstance());
        valueRetrieverMapping0.put(JCheckBox.class, CheckBoxRetriever.getInstance());
        valueRetrieverMapping0.put(UtilDatePicker.class, UtilDatePickerRetriever.getInstance());
        valueRetrieverMapping0.put(SqlDatePicker.class, SqlDatePickerRetriever.getInstance());
        VALUE_RETRIEVER_MAPPING_DEFAULT = Collections.unmodifiableMap(valueRetrieverMapping0);
    }

    static {
        Map<Type, FieldHandler<?, ?>> classMappingDefault0 = new HashMap<>();
        classMappingDefault0.put(createStringTypeToken(), StringFieldHandler.getInstance());
        classMappingDefault0.put(createFloatTypeToken(), FloatFieldHandler.getInstance());
        classMappingDefault0.put(createIntegerTypetoken(), IntegerFieldHandler.getInstance());
        classMappingDefault0.put(createDoubleTypeToken(), DoubleFieldHandler.getInstance());
        classMappingDefault0.put(createLongTypeToken(), LongFieldHandler.getInstance());
        classMappingDefault0.put(createNumberTypeToken(), NumberFieldHandler.getInstance());
        classMappingDefault0.put(createBooleanTypeToken(), BooleanFieldHandler.getInstance());
        classMappingDefault0.put(createDateTypeToken(), DateFieldHandler.getInstance());
        classMappingDefault0.put(createSqlDateTypeToken(), SqlDateFieldHandler.getInstance());
        classMappingDefault0.put(createBooleanListTypeToken(), BooleanListFieldHandler.getInstance());
        classMappingDefault0.put(createIntegerListTypeToken(), IntegerListFieldHandler.getInstance());
        classMappingDefault0.put(createAnyTypeListTypeToken(), SimpleEntityListFieldHandler.getInstance());
        classMappingDefault0.put(createStringListTypeToken(), StringListFieldHandler.getInstance());
        CLASS_MAPPING_DEFAULT = Collections.unmodifiableMap(classMappingDefault0);
    }
    public static final Map<Class<?>, FieldHandler<?, ?>> PRIMITIVE_MAPPING_DEFAULT;

    static {
        Map<Class<?>, FieldHandler<?, ?>> primitiveMappingDefault0 = new HashMap<>();
        primitiveMappingDefault0.put(float.class, FloatFieldHandler.getInstance());
        primitiveMappingDefault0.put(int.class, IntegerFieldHandler.getInstance());
        primitiveMappingDefault0.put(double.class, DoubleFieldHandler.getInstance());
        primitiveMappingDefault0.put(long.class, LongFieldHandler.getInstance());
        primitiveMappingDefault0.put(boolean.class, BooleanFieldHandler.getInstance());
        PRIMITIVE_MAPPING_DEFAULT = Collections.unmodifiableMap(primitiveMappingDefault0);
    }
    public static final List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> FIELD_ANNOTATION_MAPPING_DEFAULT = Collections.unmodifiableList(new LinkedList<Pair<Class<? extends Annotation>, FieldAnnotationHandler>>());
    public static final List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>>> CLASS_ANNOTATION_MAPPING_DEFAULT = Collections.unmodifiableList(new LinkedList<Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>>>());
    private Map<Type, FieldHandler<?, ?>> classMapping;
    private Map<Class<?>, FieldHandler<?, ?>> primitiveMapping;
    private Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping;

    /*
     internal implementation notes:
     - creating in methos in order to be able to specify
     @SuppressWarning("serial")
     */
    @SuppressWarnings("serial")
    public static Type createStringTypeToken() {
        return new TypeToken<String>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createFloatTypeToken() {
        return new TypeToken<Float>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createIntegerTypetoken() {
        return new TypeToken<Integer>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createDoubleTypeToken() {
        return new TypeToken<Double>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createLongTypeToken() {
        return new TypeToken<Long>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createNumberTypeToken() {
        return new TypeToken<Number>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createBooleanTypeToken() {
        return new TypeToken<Boolean>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createDateTypeToken() {
        return new TypeToken<Date>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createSqlDateTypeToken() {
        return new TypeToken<java.sql.Date>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createBooleanListTypeToken() {
        return new TypeToken<List<Boolean>>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createIntegerListTypeToken() {
        return new TypeToken<List<Integer>>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createAnyTypeListTypeToken() {
        return new TypeToken<List<AnyType>>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createStringListTypeToken() {
        return new TypeToken<List<String>>() {
        }.getType();
    }

    /**
     * the order in the list defines the precedence of annotations
     */
    /*
     internal implementation notes:
     - maps to a Callable<? extends JComponent> because some instances require
     constructor arguments
     */
    private List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping;
    private List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>>> classAnnotationMapping;
    /**
     * A cache for return values of {@link #retrieveRelevantFields(java.lang.Class)
     * }.
     */
    private Map<Class<?>, List<Field>> relevantFieldsCache = new HashMap<>();
    private Set<Type> ignoresFieldAnnotationMapping;
    private Set<Type> ignoresClassAnnotationMapping;
    private Set<Type> ignoresPrimitiveMapping;

    /*
     internal implementation notes:
     - maps to a Callable<? extends JComponent> because some instances require
     constructor arguments (this makes it impossible to provide a default
     mapping and thus passing the argument is enforced in constructor)
     */
    public ReflectionFormBuilder(List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping,
            List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>>> classAnnotationMapping) {
        this(CLASS_MAPPING_DEFAULT,
                PRIMITIVE_MAPPING_DEFAULT,
                VALUE_RETRIEVER_MAPPING_DEFAULT,
                fieldAnnotationMapping,
                classAnnotationMapping,
                new HashSet<Type>(),
                new HashSet<Type>(),
                new HashSet<Type>());
    }

    public ReflectionFormBuilder(Map<Type, FieldHandler<?, ?>> classMapping,
            Map<Class<?>, FieldHandler<?, ?>> primitiveMapping,
            Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping,
            List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping,
            List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>>> classAnnotationMapping,
            Set<Type> ignoresFieldAnnotationMapping,
            Set<Type> ignoresClassAnnotationMapping,
            Set<Type> ignoresPrimitiveMapping) {
        if (classMapping == null) {
            throw new IllegalArgumentException("classMapping mustn't be null");
        }
        if (classMapping.values().contains(null)) {
            throw new IllegalArgumentException(String.format("classMapping mustn't contain null values"));
        }
        if (primitiveMapping == null) {
            throw new IllegalArgumentException("primitiveMapping musn't be null");
        }
        if (primitiveMapping.values().contains(null)) {
            throw new IllegalArgumentException("primitiveMapping mustn't contain null values");
        }
        if(ignoresFieldAnnotationMapping == null) {
            throw new IllegalArgumentException("ignoresFieldAnnotationMapping mustn't be null");
        }
        if(ignoresClassAnnotationMapping == null) {
            throw new IllegalArgumentException("ignoresClassAnnotationMapping mustn't be null");
        }
        if(ignoresPrimitiveMapping == null) {
            throw new IllegalArgumentException("ignoresPrimitiveMapping mustn't be null");
        }
        for (Class<?> primitiveMappingKey : primitiveMapping.keySet()) {
            if (!primitiveMappingKey.isPrimitive()) {
                throw new IllegalArgumentException("primitiveMapping only allows primitive classes as keys");
            }
        }
        if (valueRetrieverMapping == null) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't be null");
        }
        if (valueRetrieverMapping.values().contains(null)) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't contain null values");
        }
        if (fieldAnnotationMapping == null) {
            throw new IllegalArgumentException("fieldAnnotationMapping mustn't be null");
        }
        if (fieldAnnotationMapping.contains(null)) {
            throw new IllegalArgumentException("fieldAnnotationMapping mustn't contain null");
        }
        if (classAnnotationMapping == null) {
            throw new IllegalArgumentException("classAnnotationMapping mustn't be null");
        }
        if (classAnnotationMapping.contains(null)) {
            throw new IllegalArgumentException("classAnnotationMapping mustn't contain null");
        }
        this.classMapping = classMapping;
        this.primitiveMapping = primitiveMapping;
        this.valueRetrieverMapping = valueRetrieverMapping;
        this.fieldAnnotationMapping = fieldAnnotationMapping;
        this.classAnnotationMapping = classAnnotationMapping;
        this.ignoresFieldAnnotationMapping = ignoresFieldAnnotationMapping;
        this.ignoresClassAnnotationMapping = ignoresClassAnnotationMapping;
        this.ignoresPrimitiveMapping = ignoresPrimitiveMapping;
    }

    /**
     * recursively retrieves all fields from the inheritance hierachy of
     * {@code entityClass}, except {@code static} and {@code transient} fields.
     * Results are cached in order to ensure that future calls return the same
     * result for the same argument value.
     *
     * @param clazz
     * @return
     */
    /*
     internal implementation notes:
     - return a List in order to be able to modify order (it'd be nice to have
     @Id annotated property first
     */
    public List<Field> retrieveRelevantFields(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz mustn't be null");
        }
        List<Field> retValue = this.relevantFieldsCache.get(clazz);
        if (retValue != null) {
            return retValue;
        }
        retValue = new LinkedList<>();
        Class<?> hierarchyPointer = clazz;
        while (hierarchyPointer != null //Class.getSuperclass returns null for topmost interface
                && !hierarchyPointer.equals(Object.class)) {
            retValue.addAll(Arrays.asList(hierarchyPointer.getDeclaredFields()));
            hierarchyPointer = hierarchyPointer.getSuperclass();
        }
        Set<Field> seenEntityClassFields = new HashSet<>();
        ListIterator<Field> entityClassFieldsIt = retValue.listIterator();
        while (entityClassFieldsIt.hasNext()) {
            Field entityClassFieldsNxt = entityClassFieldsIt.next();
            if (Modifier.isStatic(entityClassFieldsNxt.getModifiers())) {
                entityClassFieldsIt.remove();
                continue;
            }
            if (Modifier.isTransient(entityClassFieldsNxt.getModifiers())) {
                entityClassFieldsIt.remove();
                continue;
            }
            if (seenEntityClassFields.contains(entityClassFieldsNxt)) {
                entityClassFieldsIt.remove();
                continue;
            }
            seenEntityClassFields.add(entityClassFieldsNxt);
            entityClassFieldsNxt.setAccessible(true);
        }
        this.relevantFieldsCache.put(clazz, retValue);
        return retValue;
    }

    /**
     * Retrieves the associated {@link JComponent} to be displayed in the form.
     *
     * @param field
     * @param entityClass
     * @param entity
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
    protected JComponent getClassComponent(final Field field,
            Class<?> entityClass,
            final Object entity) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (!field.getDeclaringClass().isAssignableFrom(entityClass)) {
            throw new IllegalArgumentException(String.format("field %s has to be declared by entityClass", field));
        }
        field.setAccessible(true); //avoid error 'can not access a member of class Bla with modifiers "private"'
        //create component
        JComponent retValue;
        // annotation have precedence
        // field annotations (have precedence over class annotations)
        if(!this.ignoresFieldAnnotationMapping.contains(field.getGenericType())) {
            for (Pair<Class<? extends Annotation>, FieldAnnotationHandler> pair : fieldAnnotationMapping) {
                if (field.getAnnotation(pair.getKey()) != null) {
                    retValue = pair.getValue().handle(field.getGenericType(), //fieldClass
                            field.get(entity), //fieldValue
                            entity,
                            new FieldUpdateListener() {
                                @Override
                                public void onUpdate(FieldUpdateEvent event) {
                                    try {
                                        field.set(entity, event.getNewValue());
                                    } catch (IllegalArgumentException | IllegalAccessException ex) {
                                        throw new RuntimeException(ex);
                                    }
                                }
                            },
                            this);
                    return retValue;
                }
            }
        }else {
            LOGGER.info("skipping field {} because ignoresFieldAnnotationMapping contains its type {}", field, field.getGenericType());
        }
        // class annotations
        if(!this.ignoresClassAnnotationMapping.contains(field.getGenericType())) {
            for (Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>> pair : classAnnotationMapping) {
                if (field.getType().getAnnotation(pair.getKey()) != null) {
                    try {
                        retValue = pair.getValue().handle(field.getType(),
                                field.get(entity), //initialValue
                                new FieldUpdateListener() {
                                    @Override
                                    public void onUpdate(FieldUpdateEvent event) {
                                        try {
                                            field.set(entity, event.getNewValue());
                                        } catch (IllegalArgumentException | IllegalAccessException ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                },
                                this);
                        return retValue;
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }else {
            LOGGER.info("skipping field {} because ignoresClassAnnotationMapping contains its type {}", field, field.getGenericType());
        }
        FieldHandler fieldHandler = null;
        if (field.getType().isPrimitive()) {
            if(!this.ignoresPrimitiveMapping.contains(field.getGenericType())) {
                fieldHandler = primitiveMapping.get(field.getType());
                if (fieldHandler == null) {
                    fieldHandler = ReflectionFormBuilder.PRIMITIVE_MAPPING_DEFAULT.get(field.getType());
                }
            }else {
                LOGGER.info("skipping field {} because ignoresPrimitiveMapping contains its type {}", field, field.getGenericType());
            }
        } else {
            // check exact type match
            fieldHandler = retrieveFieldHandler(field.getGenericType());
        }
        if (fieldHandler == null) {
            retValue = new JLabel(field.getType().getSimpleName());
        } else {
            retValue = fieldHandler.handle(field.getGenericType(), //very important to call with generic type in order to not confuse setup
                    field.get(entity), //fieldValue
                    new FieldUpdateListener() {
                        @Override
                        public void onUpdate(FieldUpdateEvent event) {
                            try {
                                field.set(entity, event.getNewValue());
                            } catch (IllegalArgumentException | IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    },
                    this);
        }
        return retValue;
    }

    /**
     *
     * @param entityClass
     * @param entityToUpdate an entity to update (instead of creating a new
     * instance of {@code entityClass}). {@code null} indicates to create a new
     * instance and write changes of the {@link ReflectionFormPanel} into it.
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException if {@code entityClass} doesn't provide a
     * zero-argument-constructor
     */
    public ReflectionFormPanel transform(Class<?> entityClass,
            Object entityToUpdate) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        final Map<Field, JComponent> fieldMapping = new HashMap<>();
        Object instance = entityToUpdate;
        if (instance == null) {
            Constructor<?> entityClassConstructor = null;
            try {
                entityClassConstructor = entityClass.getDeclaredConstructor();
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(String.format("entityClass %s doesn't provide a zero-argument-constructor (see nested exception for details)", entityClass), ex);
            }
            entityClassConstructor.setAccessible(true);
            instance = entityClassConstructor.newInstance();
        }
        ReflectionFormPanel retValue = new ReflectionFormPanel(fieldMapping, instance, entityClass, this.valueRetrieverMapping, classMapping);
        List<Field> entityClassFields = this.retrieveRelevantFields(entityClass);

        GroupLayout layout = new GroupLayout(retValue);
        retValue.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        Group horizontalSequentialGroup = layout.createSequentialGroup();
        Group horizontalLabelParallelGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        Group horizontalCompParallelGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        Group verticalSequentialGroup = layout.createSequentialGroup();
        for (Field field : entityClassFields) {
            JComponent comp = this.getClassComponent(field, entityClass, instance);
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
        retValue.validate();
        return retValue;
    }

    /**
     * figures out candidates which the longest common prefix in the
     * {@code fieldParameterizedType} chain of (nested) generic types ignoring
     * specifications of {@link AnyType}. Then determines the candidates with
     * the smallest number of {@link AnyType} specifications in the chain. If
     * there're multiple with the same number of {@link AnyType} chooses the
     * first it finds which might lead to random choices.
     *
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
        for (Type mappingType : classMapping.keySet()) {
            if (!(mappingType instanceof ParameterizedType)) {
                continue;
            }
            ParameterizedType mappingParameterizedType = (ParameterizedType) mappingType;
            Type[] parameterizedTypeArguments = mappingParameterizedType.getActualTypeArguments();
            Type[] fieldParameterizedTypeArguments = fieldParameterizedType.getActualTypeArguments();
            for (int i = 0; i < Math.min(parameterizedTypeArguments.length, fieldParameterizedTypeArguments.length); i++) {
                if (fieldParameterizedTypeArguments[i].equals(AnyType.class)) {
                    throw new IllegalArgumentException(String.format("type %s must only be used to declare placeholders in class mapping, not in classes (was used in field type %s", AnyType.class, fieldParameterizedType));
                }
                // only compare raw type to raw type in the chain
                Type fieldParameterizedTypeArgument = fieldParameterizedTypeArguments[i];
                if (fieldParameterizedTypeArgument instanceof ParameterizedType) {
                    fieldParameterizedTypeArgument = ((ParameterizedType) fieldParameterizedTypeArgument).getRawType();
                }
                Type parameterizedTypeArgument = parameterizedTypeArguments[i];
                if (parameterizedTypeArgument instanceof ParameterizedType) {
                    parameterizedTypeArgument = ((ParameterizedType) parameterizedTypeArgument).getRawType();
                }
                //record AnyType matches as well
                boolean anyTypeMatch = AnyType.class.equals(parameterizedTypeArgument); //work around sucky debugger
                if (!parameterizedTypeArgument.equals(fieldParameterizedTypeArgument) && !anyTypeMatch) {
                    break;
                }
                int matchCount = i + 1;
                List<ParameterizedType> candidateList = candidates.get(matchCount);
                if (candidateList == null) {
                    candidateList = new LinkedList<>();
                    candidates.put(matchCount, candidateList);
                }
                candidateList.add(mappingParameterizedType);
            }
        }
        if (candidates.isEmpty()) {
            return null; //avoid NoSuchElementException
        }
        List<ParameterizedType> higestCandidatesList = candidates.get(candidates.lastKey());
        int lowestAnyCount = Integer.MAX_VALUE;
        ParameterizedType lowestAnyCountCandidate = null;
        for (ParameterizedType highestCandidateCandidate : higestCandidatesList) {
            int highestCandidateCandidateAnyCount = retrieveAnyCountRecursively(highestCandidateCandidate);
            if (highestCandidateCandidateAnyCount < lowestAnyCount) {
                lowestAnyCount = highestCandidateCandidateAnyCount;
                lowestAnyCountCandidate = highestCandidateCandidate;
            }
        }
        return lowestAnyCountCandidate;
    }

    protected int retrieveAnyCountRecursively(ParameterizedType type) {
        int retValue = 0;
        for (Type typeArgument : type.getActualTypeArguments()) {
            if (AnyType.class.equals(typeArgument)) {
                retValue += 1;
            }
            if (typeArgument instanceof ParameterizedType) {
                int recRetValue = retrieveAnyCountRecursively((ParameterizedType) typeArgument);
                retValue += recRetValue;
            }
        }
        return retValue;
    }

    public Map<Type, FieldHandler<?, ?>> getClassMapping() {
        return Collections.unmodifiableMap(this.classMapping);
    }

    /**
     *
     * @return an unmodifiable version of the class annotation mapping
     */
    public List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<Object, FieldUpdateEvent<Object>>>> getClassAnnotationMapping() {
        return Collections.unmodifiableList(classAnnotationMapping);
    }

    /**
     *
     * @param fieldType always call with return value of {@link Field#getGenericType()
     * } in order to be the correct match
     * @return
     */
    public FieldHandler retrieveFieldHandler(Type fieldType) {
        Type classMappingKey = fieldType;
        if (fieldType instanceof ParameterizedType) {
            classMappingKey = retrieveClassMappingBestMatch((ParameterizedType) fieldType);
        }
        FieldHandler fieldHandler = this.classMapping.get(classMappingKey);
        if (fieldHandler == null) {
            fieldHandler = ReflectionFormBuilder.CLASS_MAPPING_DEFAULT.get(classMappingKey);
        }
        // if exact type didn't match check closest match (only makes sense for
        // parameterized types as the others would have been found as direct
        // match already
        if (fieldHandler == null && fieldType instanceof ParameterizedType) {
            ParameterizedType fieldParameterizedType = (ParameterizedType) fieldType;
            Type candidate = retrieveClassMappingBestMatch(fieldParameterizedType);
            fieldHandler = classMapping.get(candidate);
        }
        return fieldHandler;
    }

}
