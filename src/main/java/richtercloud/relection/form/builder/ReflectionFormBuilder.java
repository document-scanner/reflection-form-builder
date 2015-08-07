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
package richtercloud.relection.form.builder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Transient;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 *
 * @author richter
 * @param <E> a generic type for the entity class
 */
public class ReflectionFormBuilder<E> {
    private static final Map<Class<?>, Class<? extends JComponent>> CLASS_MAPPING_DEFAULT = new HashMap<>();
    static {
        CLASS_MAPPING_DEFAULT.put(String.class, JTextField.class);
        CLASS_MAPPING_DEFAULT.put(Number.class, JSpinner.class);
    }

    public static <E> List<Field> retrieveRelevantFields(Class<? extends E> entityClass) {
        List<Field> retValue = new LinkedList<>();
        Class<?> hierarchyPointer = entityClass;
        while(!hierarchyPointer.equals(Object.class)) {
            retValue.addAll(Arrays.asList(entityClass.getDeclaredFields()));
            hierarchyPointer = entityClass.getSuperclass();
        }
        ListIterator<Field> entityClassFieldsIt = retValue.listIterator();
        while(entityClassFieldsIt.hasNext()) {
            Field entityClassFieldsNxt = entityClassFieldsIt.next();
            if(entityClassFieldsNxt.getAnnotation(Transient.class) != null) {
                entityClassFieldsIt.remove();
            }
            if(Modifier.isStatic(entityClassFieldsNxt.getModifiers())) {
                entityClassFieldsIt.remove();
            }
            entityClassFieldsNxt.setAccessible(true);
        }
        return retValue;
    }
    private Map<Class<?>, Class<? extends JComponent>> classMapping;
    private EntityManager entityManager;

    public ReflectionFormBuilder(EntityManager entityManager) {
        this(CLASS_MAPPING_DEFAULT, entityManager);
    }

    public ReflectionFormBuilder(Map<Class<?>, Class<? extends JComponent>> classMapping, EntityManager entityManager) {
        if(classMapping == null) {
            throw new IllegalArgumentException("classMapping mustn't be null");
        }
        if(entityManager == null) {
            throw new IllegalArgumentException("entityManager mustn't be null");
        }
        if(classMapping.values().contains(null)) {
            throw new IllegalArgumentException(String.format("classMapping mustn't contain null values"));
        }
        this.classMapping = classMapping;
        this.entityManager = entityManager;
    }
    
    public  JPanel transform(Class<? extends E> entityClass) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        JPanel retValue = new JPanel();
        List<Field> entityClassFields = retrieveRelevantFields(entityClass);
        
        GroupLayout retValueLayout = new GroupLayout(retValue);
        retValueLayout.setAutoCreateGaps(true);
        retValueLayout.setAutoCreateContainerGaps(true);
        for(Field field : entityClassFields) {
            Class<? extends JComponent> clazz = this.classMapping.get(field.getType());
            JComponent comp;
            if(clazz == null) {
                clazz = CLASS_MAPPING_DEFAULT.get(field.getType());
            }
            if(clazz == null) {
                comp = new QueryPanel<>(this.entityManager, entityClass);
            } else {
                comp = clazz.getConstructor().newInstance();
            }
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
        }
        return retValue;
    }
    
}
