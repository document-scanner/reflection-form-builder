/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import richtercloud.reflection.form.builder.message.LoggerMessageHandler;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 *
 * @author richter
 */
public class CachedFieldRetrieverTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(CachedFieldRetrieverTest.class);
    private final MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);

    /**
     * Test of retrieveRelevantFields method, of class ReflectionFormBuilder.
     * @throws java.lang.NoSuchFieldException
     */
    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testRetrieveRelevantFields() throws NoSuchFieldException {
        Class<?> entityClass = TestEntity.class;
        FieldHandler fieldHandler = new FieldHandler() {
            @Override
            public JComponent handle(Field field, Object instance, FieldUpdateListener updateListener, ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, FieldHandlingException {
                throw new UnsupportedOperationException("Won't ever be called.");
            }
        };
        CachedFieldRetriever instance = new CachedFieldRetriever();
        List<Field> expResult = new LinkedList<>(Arrays.asList(TestEntity.class.getDeclaredField("a"), TestEntity.class.getDeclaredField("m")));
        List<Field> result = instance.retrieveRelevantFields(entityClass);
        assertEquals(expResult, result);
        entityClass = TestEntitySubclass.class;
        expResult = new LinkedList<>(Arrays.asList(TestEntity.class.getDeclaredField("a"), TestEntity.class.getDeclaredField("m"), TestEntitySubclass.class.getDeclaredField("b")));
        result = instance.retrieveRelevantFields(entityClass);
        assertEquals(new HashSet<>(expResult), new HashSet<>(result)); //ReflectionFormBuilder doesn't give any guarantees about the order of returned fields
    }
}
