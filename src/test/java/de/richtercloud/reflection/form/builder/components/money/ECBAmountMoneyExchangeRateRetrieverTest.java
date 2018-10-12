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
package de.richtercloud.reflection.form.builder.components.money;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jscience.economics.money.Currency;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author richter
 */
public class ECBAmountMoneyExchangeRateRetrieverTest {

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    public void testFetchCache() throws IOException,
            AmountMoneyExchangeRateRetrieverException {
        File tmpFile = Files.createTempDirectory(ECBAmountMoneyExchangeRateRetrieverTest.class.getSimpleName()).toFile();
        FileUtils.deleteQuietly(tmpFile);
        ECBAmountMoneyExchangeRateRetriever instance = new ECBAmountMoneyExchangeRateRetriever(tmpFile);
        Pair<Map<Currency, Double>, Currency> result = instance.getResult();
        //Since the service doesn't guarantee to return a specific set of values
        //just make sure that it does return any values
        assertFalse(result.getKey().isEmpty());
        assertNotNull(result.getValue());
    }
}
