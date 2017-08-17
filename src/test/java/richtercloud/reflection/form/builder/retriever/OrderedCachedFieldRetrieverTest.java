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
package richtercloud.reflection.form.builder.retriever;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
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

    @Test
    public void testInit() throws FieldOrderValidationException {
        //test IllegalArgumentException after passing null for fieldOrderMap
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(EmptyClass.class));
        try {
            new OrderedCachedFieldRetriever(null, //fieldOrderMap
                    entityClasses);
            Assert.fail("IllegalArgumentException expected after passing null for fieldOrderMap");
        }catch(IllegalArgumentException expected) {
        }
        //test FieldOrderValidationException on FieldGroup without name
        entityClasses = new HashSet<>(Arrays.asList(EmptyFieldGroupName.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with empty field group name");
        }catch(FieldOrderValidationException expected) {
        }
        //test FieldOrderValidationException on non-disjoint group names
        entityClasses = new HashSet<>(Arrays.asList(NonDisjointGroupNames1.class,
                NonDisjointGroupNames2.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with non-disjoint field group names");
        }catch(FieldOrderValidationException expected) {
        }
        //test duplicate group names on one class
        entityClasses = new HashSet<>(Arrays.asList(DuplicateGroupNamesSingle.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with duplicate field group names");
        }catch(FieldOrderValidationException expected) {
        }
        //test empty FieldPosition
        entityClasses = new HashSet<>(Arrays.asList(EmptyFieldPosition.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with field with empty FieldPosition "
                    + "annotation");
        }catch(FieldOrderValidationException expected) {
        }
        //test duplicate group names in inheritance hierarchy
        entityClasses = new HashSet<>(Arrays.asList(DuplicateGroupNamesSuperclass.class,
                DuplicateGroupNamesSubclass.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with duplicate field group names in "
                    + "inheritance hierarchy");
        }catch(FieldOrderValidationException expected) {
        }
        //test field self-reference
        entityClasses = new HashSet<>(Arrays.asList(FieldSelfReference1.class,
                FieldSelfReference2.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with fields with self-refererence in "
                    + "position");
        }catch(FieldOrderValidationException expected) {
        }
        //test references to inexisting fields
        entityClasses = new HashSet<>(Arrays.asList(NonExistingFieldReference1.class,
                NonExistingFieldReference2.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with fields with refererence to "
                    + "inexisting fields");
        }catch(FieldOrderValidationException expected) {
        }
        //test reference outside the field group
        entityClasses = new HashSet<>(Arrays.asList(FieldRefeferenceOutsideGroup1.class,
                FieldRefeferenceOutsideGroup2.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with fields with refererence to "
                    + "fields outside the field group");
        }catch(FieldOrderValidationException expected) {
        }
        //test reference to inexisting field group
        entityClasses = new HashSet<>(Arrays.asList(NonExistingGroupReference.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with fields with refererence to "
                    + "inexisting field group");
        }catch(FieldOrderValidationException expected) {
        }
        //test order cycle
        entityClasses = new HashSet<>(Arrays.asList(Cycle.class));
        try {
            new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                    entityClasses);
            Assert.fail("FieldOrderValidationException expected after passing "
                    + "entityClasses with cyclic order specification");
        }catch(FieldOrderValidationException expected) {
        }
    }

    /**
     * Test of retrieveRelevantFields method, of class OrderedCachedFieldRetriever.
     */
    @Test
    public void testRetrieveRelevantFields() throws FieldOrderValidationException,
            NoSuchFieldException {
        //test IllegalArgumentException after requesting unmanaged class
        Set<Class<?>> entityClasses = new HashSet<>(Arrays.asList(EmptyClass.class));
        OrderedCachedFieldRetriever instance = new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
                entityClasses);
        try {
            instance.retrieveRelevantFields(OrderTestClass.class);
            Assert.fail("IllegalArgumentException expected after passing unmanaged class as argument");
        }catch(IllegalArgumentException expected) {
        }
        //test order
        Class<?> entityClass = OrderTestClass.class;
        entityClasses = new HashSet<>(Arrays.asList(entityClass));
        instance = new OrderedCachedFieldRetriever(new HashMap<>(), //fieldOrderMap
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
