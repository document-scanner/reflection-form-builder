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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
import java.util.concurrent.Callable;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.components.SqlDatePicker;
import richtercloud.reflection.form.builder.components.UtilDatePicker;
import richtercloud.reflection.form.builder.retriever.CheckBoxRetriever;
import richtercloud.reflection.form.builder.retriever.SpinnerRetriever;
import richtercloud.reflection.form.builder.retriever.SqlDatePickerRetriever;
import richtercloud.reflection.form.builder.retriever.TextFieldRetriever;
import richtercloud.reflection.form.builder.retriever.UtilDatePickerRetriever;
import richtercloud.reflection.form.builder.retriever.ValueRetriever;

/**
 *
 * @author richter
 * @param <E> a generic type for the entity class
 */
public class ReflectionFormBuilder<E> {
    /*
    internal implementation notes:
    - see ValueRetriever's class comment in order to understand why there's no
    subclassing of JComponent
    */
    public static final Map<Class<?>, Class<? extends JComponent>> CLASS_MAPPING_DEFAULT;
    public static final Map<Class<? extends JComponent>, ValueRetriever<?, ?>> VALUE_RETRIEVER_MAPPING_DEFAULT;
    static {
        Map<Class<?>, Class<? extends JComponent>> classMappingDefault0 = new HashMap<>();
        classMappingDefault0.put(String.class, JTextField.class);
        classMappingDefault0.put(float.class, JSpinner.class);
        classMappingDefault0.put(Float.class, JSpinner.class);
        classMappingDefault0.put(int.class, JSpinner.class);
        classMappingDefault0.put(Integer.class, JSpinner.class);
        classMappingDefault0.put(double.class, JSpinner.class);
        classMappingDefault0.put(Double.class, JSpinner.class);
        classMappingDefault0.put(long.class, JSpinner.class);
        classMappingDefault0.put(Long.class, JSpinner.class);
        classMappingDefault0.put(Number.class, JSpinner.class);
        classMappingDefault0.put(boolean.class, JCheckBox.class);
        classMappingDefault0.put(Boolean.class, JCheckBox.class);
        classMappingDefault0.put(Date.class, UtilDatePicker.class);
        classMappingDefault0.put(java.sql.Date.class, SqlDatePicker.class);
        CLASS_MAPPING_DEFAULT = Collections.unmodifiableMap(classMappingDefault0);
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
    private Map<Class<?>, Class<? extends JComponent>> classMapping;
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
    private List<Pair<Class<? extends Annotation>, Callable<? extends JComponent>>> annotationMapping;

    /*
    internal implementation notes:
    - maps to a Callable<? extends JComponent> because some instances require
    constructor arguments (this makes it impossible to provide a default
    mapping and thus passing the argument is enforced in constructor)
    */
    public ReflectionFormBuilder(List<Pair<Class<? extends Annotation>, Callable<? extends JComponent>>> annotationMapping) {
        this(CLASS_MAPPING_DEFAULT, VALUE_RETRIEVER_MAPPING_DEFAULT, annotationMapping);
    }

    public ReflectionFormBuilder(Map<Class<?>, Class<? extends JComponent>> classMapping, Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping, List<Pair<Class<? extends Annotation>, Callable<? extends JComponent>>> annotationMapping) {
        if(classMapping == null) {
            throw new IllegalArgumentException("classMapping mustn't be null");
        }
        if(classMapping.values().contains(null)) {
            throw new IllegalArgumentException(String.format("classMapping mustn't contain null values"));
        }
        if(valueRetrieverMapping == null) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't be null");
        }
        if(valueRetrieverMapping.values().contains(null)) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't contain null values");
        }
        if(annotationMapping == null) {
            throw new IllegalArgumentException("annotationMapping mustn't be null");
        }
        this.classMapping = classMapping;
        this.valueRetrieverMapping = valueRetrieverMapping;
        this.annotationMapping = annotationMapping;
    }

    /**
     * recursively retrieves all fields from the inheritance hierachy of {@code entityClass}, except {@code static} fields.
     * @param entityClass
     * @return
     */
    /*
    internal implementation notes:
    - return a List in order to be able to modify order (it'd be nice to have
    @Id annotated property first9
    */
    public List<Field> retrieveRelevantFields(Class<? extends E> entityClass) {
        List<Field> retValue = new LinkedList<>();
        Class<?> hierarchyPointer = entityClass;
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
     * @return a {@link JComponent}, never {@code null}
     * @throws NoSuchMethodException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    protected  JComponent getClassComponent(Field field) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<? extends JComponent> clazz = this.classMapping.get(field.getType());
        JComponent retValue;
        if(clazz == null) {
            clazz = ReflectionFormBuilder.CLASS_MAPPING_DEFAULT.get(field.getType());
        }
        for(Pair<Class<? extends Annotation>, Callable<? extends JComponent>> pair : annotationMapping) {
            if(field.getAnnotation(pair.getKey()) != null) {
                try {
                    retValue = pair.getValue().call();
                    return retValue;
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if(clazz == null) {
            retValue = new JLabel(field.getType().getSimpleName());
        } else {
            Constructor<? extends JComponent> clazzConstructor = clazz.getDeclaredConstructor();
            retValue = clazzConstructor.newInstance();
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
    public ReflectionFormPanel<E> transform(Class<? extends E> entityClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
        final Map<Field, JComponent> fieldMapping = new HashMap<>();
        Constructor<? extends E> entityClassConstructor = null;
        try {
            entityClassConstructor = entityClass.getDeclaredConstructor();
        }catch(NoSuchMethodException ex) {
            throw new IllegalArgumentException("entityClass doesn't provide a zero-argument-constructor (see nested exception for details)", ex);
        }
        entityClassConstructor.setAccessible(true);
        E instance = entityClassConstructor.newInstance();
        ReflectionFormPanel<E> retValue = new ReflectionFormPanel<>(fieldMapping, instance, entityClass, this.valueRetrieverMapping, classMapping);
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
            JComponent comp = this.getClassComponent(field);
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

    public Map<Class<?>, Class<? extends JComponent>> getClassMapping() {
        return Collections.unmodifiableMap(this.classMapping);
    }

    /**
     * the initial list of entity class fields created with
     * {@link #retrieveRelevantFields(java.lang.Class) }
     * @return
     */
    public List<Field> getEntityClassFields() {
        return Collections.unmodifiableList(this.entityClassFields);
    }

}
