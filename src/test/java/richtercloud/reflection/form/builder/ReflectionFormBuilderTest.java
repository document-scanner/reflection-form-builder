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
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.Pair;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import richtercloud.reflection.form.builder.panels.BooleanListPanel;
import richtercloud.reflection.form.builder.retriever.ValueRetriever;

/**
 *
 * @author richter
 */
public class ReflectionFormBuilderTest {

    /**
     * Test of retrieveRelevantFields method, of class ReflectionFormBuilder.
     * @throws java.lang.NoSuchFieldException
     */
    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testRetrieveRelevantFields() throws NoSuchFieldException {
        Class<?> entityClass = TestEntity.class;
        List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping = new LinkedList<>();
        List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<?>>> classAnnotationMapping = new LinkedList<>();
        try {
            new ReflectionFormBuilder(null, classAnnotationMapping);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        try {
            new ReflectionFormBuilder(fieldAnnotationMapping, null);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        try {
            new ReflectionFormBuilder(null, null);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        ReflectionFormBuilder instance = new ReflectionFormBuilder(fieldAnnotationMapping, classAnnotationMapping);
        List<Field> expResult = new LinkedList<>(Arrays.asList(TestEntity.class.getDeclaredField("a")));
        List<Field> result = instance.retrieveRelevantFields(entityClass);
        assertEquals(expResult, result);
        entityClass = TestEntitySubclass.class;
        expResult = new LinkedList<>(Arrays.asList(TestEntity.class.getDeclaredField("a"), TestEntitySubclass.class.getDeclaredField("b")));
        result = instance.retrieveRelevantFields(entityClass);
        assertEquals(new HashSet<>(expResult), new HashSet<>(result)); //ReflectionFormBuilder doesn't give any guarantees about the order of returned fields
    }

    /**
     * Test of getClassComponent method, of class ReflectionFormBuilder.
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("serial")
    public void testGetClassComponent() throws Exception {
        List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping = new LinkedList<>();
        List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<?>>> classAnnotationMapping = new LinkedList<>();
        ReflectionFormBuilder instance = new ReflectionFormBuilder(fieldAnnotationMapping, classAnnotationMapping);
        Class<?> entityClass = TestEntity.class;
        Field field = entityClass.getDeclaredField("a");
        TestEntity entity = TestEntity.class.getDeclaredConstructor().newInstance();
        JComponent result = instance.getClassComponent(field, entityClass, entity);
        assertEquals(JTextField.class, result.getClass());
        //test that AnyType specifiecation is matched
        Map<Type, FieldHandler<?>> classMapping = new HashMap<>();
        Type listAnyType = new TypeToken<List<AnyType>>() {}.getType();
        Type listBooleanType = new TypeToken<List<Boolean>>() {}.getType();
        classMapping.put(listAnyType, StringFieldHandler.getInstance());
        classMapping.put(listBooleanType, BooleanListFieldHandler.getInstance());
        Map<Class<?>, FieldHandler<?>> primitiveMapping = new HashMap<>();
        Map<Class<? extends JComponent>, ValueRetriever<?,?>> valueRetrieverMapping = new HashMap<>();
        Class<? extends TestEntityCollection> entityClassCollection = TestEntityCollection.class;
        TestEntityCollection entityCollection = TestEntityCollection.class.getDeclaredConstructor().newInstance();
        instance = new ReflectionFormBuilder(classMapping, primitiveMapping, valueRetrieverMapping, fieldAnnotationMapping, classAnnotationMapping);
        result = instance.getClassComponent(TestEntityCollection.class.getDeclaredField("gs"), //is a List<Set<Boolean>>
                entityClassCollection, entityCollection);
        assertEquals(BooleanListPanel.class, result.getClass());
    }

    @Test
    @SuppressWarnings("serial")
    public void testRetrieveClassMappingBestMatch() throws NoSuchFieldException {
        List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping = new LinkedList<>();
        List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<?>>> classAnnotationMapping = new LinkedList<>();
        Type type = TestEntityCollection.class.getDeclaredField("gs").getGenericType();
        Map<Type, FieldHandler<?>> classMapping = new HashMap<>();
        classMapping.put(type,
                NumberFieldHandler.getInstance() //any handler
        );
        classMapping.put(new TypeToken<String>() {}.getType(), StringFieldHandler.getInstance()); //a type without common prefix
        classMapping.put(new TypeToken<List<String>>() {}.getType(), new FieldHandler() {
            @Override
            public JComponent handle(Type type, UpdateListener updateListener, ReflectionFormBuilder reflectionFormBuilder) {
                return new JPasswordField();
            }
        });// a type with common prefix
        Map<Class<?>, FieldHandler<?>> primitiveMapping = new HashMap<>();
        Map<Class<? extends JComponent>, ValueRetriever<?,?>> valueRetrieverMapping = new HashMap<>();
        ReflectionFormBuilder instance = new ReflectionFormBuilder(classMapping, primitiveMapping, valueRetrieverMapping, fieldAnnotationMapping, classAnnotationMapping);
        Type result = instance.retrieveClassMappingBestMatch((ParameterizedType) type);
        assertEquals(type, result);
        // check that empty class mapping returns null
        classMapping = new HashMap<>();
        valueRetrieverMapping = new HashMap<>();
        primitiveMapping = new HashMap<>();
        instance = new ReflectionFormBuilder(classMapping, primitiveMapping, valueRetrieverMapping, fieldAnnotationMapping, classAnnotationMapping);
        result = instance.retrieveClassMappingBestMatch((ParameterizedType) type);
        assertEquals(null, result);
        //check that AnyType is matched needs to be in testGetClassComponent
    }

    /**
     * Test of transform method, of class ReflectionFormBuilder.
     * @throws java.lang.Exception
     */
    @Test
    @SuppressWarnings("serial")
    public void testTransform() throws Exception {
        //test entity without collection fields
        List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping = new LinkedList<>();
        List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<?>>> classAnnotationMapping = new LinkedList<>();
        ReflectionFormBuilder instance = new ReflectionFormBuilder(fieldAnnotationMapping, classAnnotationMapping);
        Class<?> entityClass = TestEntity.class;
        ReflectionFormPanel result = instance.transform(entityClass);
        assertEquals(2, result.getComponentCount());
        entityClass = TestEntitySubclass.class;
        result = instance.transform(entityClass);
        assertEquals(4, result.getComponentCount());

        //test entity with collection
        entityClass = TestEntityCollection.class;
        Map<Type, FieldHandler<?>> classMapping = new HashMap<>(ReflectionFormBuilder.CLASS_MAPPING_DEFAULT);
        classMapping.put(new TypeToken<List<TestEntityCollection>>() {}.getType(), SimpleEntityListFieldHandler.getInstance());
        instance = new ReflectionFormBuilder(classMapping, ReflectionFormBuilder.PRIMITIVE_MAPPING_DEFAULT, ReflectionFormBuilder.VALUE_RETRIEVER_MAPPING_DEFAULT, fieldAnnotationMapping, classAnnotationMapping);
        result = instance.transform(entityClass);
        assertEquals(8, result.getComponentCount());
    }

    @Test
    @SuppressWarnings("serial")
    public void testRetrieveAnyCountRecursively() {
        List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> fieldAnnotationMapping = new LinkedList<>();
        List<Pair<Class<? extends Annotation>, ClassAnnotationHandler<?>>> classAnnotationMapping = new LinkedList<>();
        ReflectionFormBuilder instance = new ReflectionFormBuilder(fieldAnnotationMapping, classAnnotationMapping);
        //test 0
        int result = instance.retrieveAnyCountRecursively((ParameterizedType) new TypeToken<List<Set<Integer>>>() {
}.getType());
        assertEquals(0, result);
        //test 1
        result = instance.retrieveAnyCountRecursively((ParameterizedType) new TypeToken<List<Set<AnyType>>>() {
}.getType());
        assertEquals(1, result);
        //test nested
        result = instance.retrieveAnyCountRecursively((ParameterizedType) new TypeToken<Map<Set<AnyType>, AnyType>>() {
}.getType());
        assertEquals(2, result);
    }
}
