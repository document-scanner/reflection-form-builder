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
package richtercloud.reflection.form.builder.fieldhandler;

import com.google.common.reflect.TypeToken;
import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JComponent;
import javax.swing.JPasswordField;
import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.AnyType;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.TestEntity;
import richtercloud.reflection.form.builder.TestEntityCollection;
import richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory;
import richtercloud.reflection.form.builder.message.LoggerMessageHandler;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 *
 * @author richter
 */
public class MappingFieldHandlerTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MappingFieldHandlerTest.class);
    private MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
    private MappingFieldHandlerFactory classMappingFactory = new MappingFieldHandlerFactory(messageHandler);

    /**
     * Test of validateMapping method, of class MappingFieldHandler.
     */
    @Test
    public void testValidateMapping() {
        try {
            MappingFieldHandler.validateMapping(null, null);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        try {
            MappingFieldHandler.validateMapping(null, "argumentName");
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
    }

    /**
     * Test of handle method, of class MappingFieldHandler.
     */
    @Test
    public void testHandle() throws Exception {
        System.out.println("handle");
        MappingFieldHandler instance = null;
        JComponent expResult = null;
//        JComponent result = instance.handle(null);
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of retrieveFieldHandler method, of class MappingFieldHandler.
     */
    @Test
    public void testRetrieveFieldHandler() {
        System.out.println("retrieveFieldHandler");
        Type fieldType = null;
        Map<Type, FieldHandler<?, ?, ?, ?>> classMapping = null;
        MappingFieldHandler instance = null;
        FieldHandler expResult = null;
        FieldHandler result = instance.retrieveFieldHandler(fieldType, classMapping);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    @Test
    @SuppressWarnings("serial")
    public void testRetrieveClassMappingBestMatch() throws NoSuchFieldException {
        ParameterizedType type = (ParameterizedType) TestEntityCollection.class.getDeclaredField("gs").getGenericType();
        Map<Type, FieldHandler<?,?, ?, ?>> classMapping = new HashMap<>();
        classMapping.put(type,
                NumberFieldHandler.getInstance() //any handler
        );
        classMapping.put(new TypeToken<String>() {}.getType(), StringFieldHandler.getInstance()); //a type without common prefix
        classMapping.put(new TypeToken<List<String>>() {}.getType(), new FieldHandler() {
            @Override
            public JComponent handle(Field field,
                    Object instance,
                    FieldUpdateListener updateListener,
                    ReflectionFormBuilder reflectionFormBuilder) {
                return new JPasswordField();
            }

            @Override
            public void reset(Component component) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });// a type with common prefix
        Map<Class<?>, FieldHandler<?,?, ?, ?>> primitiveMapping = new HashMap<>();
        MappingFieldHandler instance = new MappingFieldHandler(classMapping,
                primitiveMapping);
        Type result = instance.retrieveClassMappingBestMatch(type);
        assertEquals(type, result);
        // check that empty class mapping returns null
        classMapping = new HashMap<>();
        primitiveMapping = new HashMap<>();
        instance = new MappingFieldHandler(classMapping,
                primitiveMapping);
        result = instance.retrieveClassMappingBestMatch(type);
        assertEquals(null, result);
        //check that AnyType is matched needs to be in testGetClassComponent

        //check that a generic field type doesn't match a generic type in the classMapping with AnyType in the the generics, but not the same class (e.g. a field of type Amount<Money> should bring a result if List<AnyType> is specified in classMapping)
        classMapping = new HashMap<>();
        MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
        classMapping.put(new TypeToken<List<AnyType>>() {}.getType(),
                new IntegerListFieldHandler(messageHandler) //mapping IntegerListFieldHandler to List<AnyType> shouldn't influence the test case
        ); //a type without common prefix
        primitiveMapping = new HashMap<>();
        type = (ParameterizedType) TestEntity.class.getDeclaredField("m").getGenericType();
        instance = new MappingFieldHandler(classMapping,
                primitiveMapping);
        result = instance.retrieveClassMappingBestMatch(type);
        assertEquals(null, result);
    }

    @Test
    @SuppressWarnings("serial")
    public void testRetrieveAnyCountRecursively() {
        MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
        MappingFieldHandler instance = new MappingFieldHandler(classMappingFactory.generateClassMapping(),
                classMappingFactory.generatePrimitiveMapping());
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
