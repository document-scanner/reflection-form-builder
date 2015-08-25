/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import junit.framework.Assert;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author richter
 */
public class ReflectionFormBuilderTest {
    
    /**
     * Test of retrieveRelevantFields method, of class ReflectionFormBuilder.
     */
    @Test
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public void testRetrieveRelevantFields() throws NoSuchFieldException {
        Class entityClass = TestEntity.class;
        try {
            new ReflectionFormBuilder<>(null);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        List<Pair<Class<? extends Annotation>, Callable<? extends JComponent>>> annotationMapping = new LinkedList<>();
        ReflectionFormBuilder<?> instance = new ReflectionFormBuilder<>(annotationMapping);
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
     */
    @Test
    public void testGetClassComponent() throws Exception {
        List<Pair<Class<? extends Annotation>, Callable<? extends JComponent>>> annotationMapping = new LinkedList<>();
        ReflectionFormBuilder<?> instance = new ReflectionFormBuilder<>(annotationMapping);
        Field field = TestEntity.class.getDeclaredField("a");
        JComponent result = instance.getClassComponent(field);
        assertEquals(JTextField.class, result.getClass());
    }

    /**
     * Test of transform method, of class ReflectionFormBuilder.
     */
    @Test
    public void testTransform() throws Exception {
        List<Pair<Class<? extends Annotation>, Callable<? extends JComponent>>> annotationMapping = new LinkedList<>();
        ReflectionFormBuilder<?> instance = new ReflectionFormBuilder<>(annotationMapping);
        Class entityClass = TestEntity.class;
        ReflectionFormPanel result = instance.transform(entityClass);
        assertEquals(2, result.getMainPanel().getComponentCount());
        entityClass = TestEntitySubclass.class;
        result = instance.transform(entityClass);
        assertEquals(4, result.getMainPanel().getComponentCount());
    }
    
}
