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

import java.awt.Component;
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
import richtercloud.message.handler.LoggerMessageHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import richtercloud.validation.tools.CachedFieldRetriever;
import richtercloud.validation.tools.FieldRetrievalException;

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
    public void testRetrieveRelevantFields() throws NoSuchFieldException, FieldRetrievalException {
        Class<?> entityClass = TestEntity.class;
        FieldHandler fieldHandler = new FieldHandler() {
            @Override
            public JComponent handle(Field field, Object instance, FieldUpdateListener updateListener, ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, FieldHandlingException {
                throw new UnsupportedOperationException("Won't ever be called.");
            }

            @Override
            public void reset(Component component) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
