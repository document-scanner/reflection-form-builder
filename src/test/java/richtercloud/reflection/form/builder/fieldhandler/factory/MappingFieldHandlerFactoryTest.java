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
package richtercloud.reflection.form.builder.fieldhandler.factory;

import java.lang.reflect.Type;
import java.util.Map;
import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.components.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.AmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.message.LoggerMessageHandler;
import richtercloud.reflection.form.builder.message.MessageHandler;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 *
 * @author richter
 */
public class MappingFieldHandlerFactoryTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MappingFieldHandlerFactoryTest.class);

    /**
     * Test of generateClassMapping method, of class MappingFieldHandler.
     */
    @Test
    public void testGenerateClassMapping() {
        System.out.println("generateClassMapping");
        MessageHandler messageHandler = null;
        AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage = null;
        AmountMoneyCurrencyStorage additionalCurrencies = null;
        MappingFieldHandlerFactory instance = new MappingFieldHandlerFactory(messageHandler);
        Map<Type, FieldHandler<?, ?, ?>> expResult = null;
        Map<Type, FieldHandler<?, ?, ?>> result = instance.generateClassMapping();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInstance method, of class MappingFieldHandlerFactory.
     */
    @Test
    public void testGetInstance() {
        try {
            MappingFieldHandlerFactory result = new MappingFieldHandlerFactory(null);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        MessageHandler messageHandler = new LoggerMessageHandler(LOGGER);
        MappingFieldHandlerFactory result = new MappingFieldHandlerFactory(messageHandler);
        assertNotNull(result);
    }

    /**
     * Test of generateTypeHandlerMapping method, of class MappingFieldHandlerFactory.
     */
    @Test
    public void testGeneratePrimitiveMapping() {
        System.out.println("generateTypeHandlerMapping");
        MessageHandler messageHandler = null;
        MappingFieldHandlerFactory instance = null;
        Map<Type, TypeHandler<?, ?, ?>> expResult = null;
        Map<Class<?>, FieldHandler<?, ?, ?>> result = instance.generatePrimitiveMapping();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
