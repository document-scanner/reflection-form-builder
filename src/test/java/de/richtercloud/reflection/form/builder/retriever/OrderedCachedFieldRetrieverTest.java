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

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests {@link OrderedCachedFieldRetriever}. Test entity classes in separate
 * files are necessary in order to ensure that real field names are used instead
 * of {@code this$0} which is the case for private classes.
 *
 * @author richter
 */
public class OrderedCachedFieldRetrieverTest {
    protected final static String FIELD_GROUP_NAME_1 = "field-group-1";
    protected final static String FIELD_GROUP_NAME_2 = "field-group-2";
    protected final static String FIELD_GROUP_NAME_3 = "field-group-3";

    @Test(expected = IllegalArgumentException.class)
    public void testInitNull() throws FieldOrderValidationException {
        //test IllegalArgumentException after passing null for fieldOrderMap
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(EmptyClass.class));
        new OrderedCachedFieldRetriever(null, //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitNoName() throws FieldOrderValidationException {
        //test FieldOrderValidationException on FieldGroup without name
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(EmptyFieldGroupName.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitNonDisjointGroupNames() throws FieldOrderValidationException {
        //test FieldOrderValidationException on non-disjoint group names
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(NonDisjointGroupNames1.class,
                NonDisjointGroupNames2.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitDuplicateGroupNames() throws FieldOrderValidationException {
        //test duplicate group names on one class
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(DuplicateGroupNamesSingle.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitEmptyFieldPosition() throws FieldOrderValidationException {
        //test empty FieldPosition
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(EmptyFieldPosition.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitDuplicateGroupNamesSuperclass() throws FieldOrderValidationException {
        //test duplicate group names in inheritance hierarchy
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(DuplicateGroupNamesSuperclass.class,
                DuplicateGroupNamesSubclass.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitSelfReference() throws FieldOrderValidationException {
        //test field self-reference
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(FieldSelfReference1.class,
                FieldSelfReference2.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitNonExistingFieldReference() throws FieldOrderValidationException {
        //test references to inexisting fields
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(NonExistingFieldReference1.class,
                NonExistingFieldReference2.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInit() throws FieldOrderValidationException {
        //test reference outside the field group
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(FieldRefeferenceOutsideGroup1.class,
                FieldRefeferenceOutsideGroup2.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitNonExistingGroupReference() throws FieldOrderValidationException {
        //test reference to inexisting field group
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(NonExistingGroupReference.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = FieldOrderValidationException.class)
    public void testInitCycle() throws FieldOrderValidationException {
        //test order cycle
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(Cycle.class));
        new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRetrieveRelevantFieldsUnmanagedClass() throws FieldOrderValidationException {
        //test IllegalArgumentException after requesting unmanaged class
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(EmptyClass.class));
        OrderedCachedFieldRetriever instance = new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
        instance.retrieveRelevantFields(OrderTestClass.class);
    }

    @Test
    public void testRetrieveRelevantFields() throws FieldOrderValidationException,
            NoSuchFieldException {
        //test order
        Class<?> entityClass = OrderTestClass.class;
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(entityClass));
        OrderedCachedFieldRetriever instance = new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses,
                true);
        List<Field> expResult = new LinkedList<>(Arrays.asList(entityClass.getDeclaredField("c"),
                entityClass.getDeclaredField("b"),
                entityClass.getDeclaredField("a")));
        List<Field> result = instance.retrieveRelevantFields(entityClass);
        assertEquals(expResult,
                result);
    }
}
