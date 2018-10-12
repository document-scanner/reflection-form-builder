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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.jscience.economics.money.Currency;
import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

/**
 *
 * @author richter
 */
public class FailsafeAmountMoneyExchangeRateRetrieverTest {

    @Test
    public void testGetSupportedCurrencies0() throws AmountMoneyExchangeRateRetrieverException {
        AmountMoneyExchangeRateRetriever retriever1 = mock(AmountMoneyExchangeRateRetriever.class);
        AmountMoneyExchangeRateRetriever retriever2 = mock(AmountMoneyExchangeRateRetriever.class);
        Set<Currency> retriever1SupportedCurrencies = new HashSet<>(Arrays.asList(Currency.AUD, Currency.CAD, Currency.CNY));
        Set<Currency> retriever2SupportedCurrencies = new HashSet<>(Arrays.asList(Currency.CAD, Currency.CNY, Currency.EUR));
        when(retriever1.getSupportedCurrencies()).thenReturn(retriever1SupportedCurrencies);
        when(retriever2.getSupportedCurrencies()).thenReturn(retriever2SupportedCurrencies);
        FailsafeAmountMoneyExchangeRateRetriever instance = new FailsafeAmountMoneyExchangeRateRetriever(new HashSet<>(Arrays.asList(retriever1,
                retriever2)));
        Set<Currency> expResult = new HashSet<>(Arrays.asList(Currency.AUD, Currency.CAD, Currency.CNY, Currency.EUR));
        Set<Currency> result = instance.getSupportedCurrencies();
        assertEquals(expResult, result);
    }
}
