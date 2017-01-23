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
package richtercloud.reflection.form.builder.components.money;

import com.thoughtworks.xstream.XStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
public class StaticAmountMoneyExchangeRateRetriever implements AmountMoneyExchangeRateRetriever {
    public final static String INITIAL_RESOURCE_RESOURCE_NAME_DEFAULT = "/ecb-initial-result.xml";
    private final Pair<Date, Pair<Map<Currency, Double>, Currency>> serializedResult;

    public StaticAmountMoneyExchangeRateRetriever() {
        XStream xStream = new XStream();
        InputStream initialResultInputStream = CachedOnlineAmountMoneyExchangeRateRetriever.class.getResourceAsStream(INITIAL_RESOURCE_RESOURCE_NAME_DEFAULT);
        serializedResult = (Pair<Date, Pair<Map<Currency, Double>, Currency>>) xStream.fromXML(initialResultInputStream);
    }

    @Override
    public void retrieveExchangeRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException {
        Currency.setReferenceCurrency(serializedResult.getValue().getValue());
        double exchangeRate = serializedResult.getValue().getKey().get(currency);
        currency.setExchangeRate(exchangeRate);
    }

    @Override
    public Set<Currency> getSupportedCurrencies() throws AmountMoneyExchangeRateRetrieverException {
        Set<Currency> retValue = serializedResult.getValue().getKey().keySet();
        return retValue;
    }
}
