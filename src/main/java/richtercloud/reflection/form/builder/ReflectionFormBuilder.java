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

import richtercloud.reflection.form.builder.retriever.ValueRetriever;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import richtercloud.reflection.form.builder.retriever.SpinnerRetriever;
import richtercloud.reflection.form.builder.retriever.TextFieldRetriever;

/**
 *
 * @author richter
 * @param <E> a generic type for the entity class
 */
public class ReflectionFormBuilder<E> {
    public static final Map<Class<?>, Class<? extends JComponent>> CLASS_MAPPING_DEFAULT;
    static {
        Map<Class<?>, Class<? extends JComponent>> classMappingDefault0 = new HashMap<>();
        classMappingDefault0.put(String.class, JTextField.class);
        classMappingDefault0.put(Number.class, JSpinner.class);
        CLASS_MAPPING_DEFAULT = Collections.unmodifiableMap(classMappingDefault0);
    }
    public static final Map<Class<? extends JComponent>, ValueRetriever<?, ?>> VALUE_RETRIEVER_MAPPING_DEFAULT;
    static {
        Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping0 = new HashMap<>();
        valueRetrieverMapping0.put(JTextField.class, TextFieldRetriever.getInstance());
        valueRetrieverMapping0.put(JSpinner.class, SpinnerRetriever.getInstance());
        VALUE_RETRIEVER_MAPPING_DEFAULT = Collections.unmodifiableMap(valueRetrieverMapping0);
    }
    private Map<Class<?>, Class<? extends JComponent>> classMapping;
    private Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping;
    private List<Field> entityClassFields;

    public ReflectionFormBuilder() {
        this(CLASS_MAPPING_DEFAULT, VALUE_RETRIEVER_MAPPING_DEFAULT);
    }

    public ReflectionFormBuilder(Map<Class<?>, Class<? extends JComponent>> classMapping, Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping) {
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
        this.classMapping = classMapping;
        this.valueRetrieverMapping = valueRetrieverMapping;
    }

    public List<Field> retrieveRelevantFields(Class<? extends E> entityClass) {
        List<Field> retValue = new LinkedList<>();
        Class<?> hierarchyPointer = entityClass;
        while(!hierarchyPointer.equals(Object.class)) {
            retValue.addAll(Arrays.asList(entityClass.getDeclaredFields()));
            hierarchyPointer = hierarchyPointer.getSuperclass();
        }
        ListIterator<Field> entityClassFieldsIt = retValue.listIterator();
        while(entityClassFieldsIt.hasNext()) {
            Field entityClassFieldsNxt = entityClassFieldsIt.next();
            if(Modifier.isStatic(entityClassFieldsNxt.getModifiers())) {
                entityClassFieldsIt.remove();
            }
            entityClassFieldsNxt.setAccessible(true);
        }
        return retValue;
    }

    protected  JComponent getClassComponent(Class<?> type) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<? extends JComponent> clazz = classMapping.get(type);
        JComponent retValue;
        if(clazz == null) {
            clazz = ReflectionFormBuilder.CLASS_MAPPING_DEFAULT.get(type);
        }
        if(clazz == null) {
            retValue = new JLabel(type.getSimpleName());
        } else {
            retValue = clazz.getConstructor().newInstance();
        }
        return retValue;
    }

    public ReflectionFormPanel transform(Class<? extends E> entityClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        final Map<Field, JComponent> fieldMapping = new HashMap<>();
        E instance = entityClass.getDeclaredConstructor().newInstance();
        ReflectionFormPanel retValue = new ReflectionFormPanel(fieldMapping, instance, valueRetrieverMapping);
        this.entityClassFields = retrieveRelevantFields(entityClass);

        GroupLayout retValueLayout = new GroupLayout(retValue);
        retValueLayout.setAutoCreateGaps(true);
        retValueLayout.setAutoCreateContainerGaps(true);
        for(Field field : this.entityClassFields) {
            JComponent comp = getClassComponent(field.getType());
            JLabel label = new JLabel(field.getName());
            retValueLayout.setHorizontalGroup(
                    retValueLayout.createSequentialGroup()
                            .addComponent(label)
                            .addComponent(comp)
            );
            retValueLayout.setVerticalGroup(
                    retValueLayout.createSequentialGroup()
                            .addGroup(retValueLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                    .addComponent(label)
                                    .addComponent(comp))
            );
//            label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));
//            comp.setPreferredSize(new Dimension(100, comp.getPreferredSize().height));
            retValue.add(label);
            retValue.add(comp);
            fieldMapping.put(field, comp);
        }

        return retValue;
    }

    public Map<Class<?>, Class<? extends JComponent>> getClassMapping() {
        return classMapping;
    }

    /**
     * the initial list of entity class fields created with
     * {@link #retrieveRelevantFields(java.lang.Class) }
     * @return
     */
    public List<Field> getEntityClassFields() {
        return entityClassFields;
    }

}
