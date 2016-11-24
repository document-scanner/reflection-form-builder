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

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- Since the AmountMoneyExchangeRateRetriever retriever requires to provide both
information about exchange rates and supported currencies and results are
fetched online in a very different way, there has to be a decision what is
cached (online response or information) -> cache interface responses and allow
subclasses to fill the cache
*/
public abstract class CachedOnlineAmountMoneyExchangeRateRetriever extends OnlineAmountMoneyExchangeRateRetriever {
    private Pair<Map<Currency, Double>, Currency> result;
        //Needs to be one instance in order to allow passing of the result after
        //fetch in subclasses at once
    private final Date cacheTimestamp = null;
    private long cacheExpirationMillis = 60*60*1000; //1 hour

    public Pair<Map<Currency, Double>, Currency> getResult() throws AmountMoneyExchangeRateRetrieverException {
        Date cacheTimestampNew = new Date();
            //instantiated here in order to allow reusage if a new timestamp
            //needs to be stored
        if(result == null
                || cacheTimestamp == null
                || cacheTimestampNew.getTime() - cacheTimestamp.getTime() > cacheExpirationMillis) {
            result = fetchResult();
        }
        return result;
    }

    /**
     * Allows to retrieve the complete cache at once which makes sense for
     * subclasses which will retrieve a list of currency exchange rates rather
     * than a single result (probably all).
     * @return
     */
    protected abstract Pair<Map<Currency, Double>, Currency> fetchResult() throws AmountMoneyExchangeRateRetrieverException;

    @Override
    public Set<Currency> getSupportedCurrencies() throws AmountMoneyExchangeRateRetrieverException {
        Set<Currency> retValue = getResult().getKey().keySet();
        return Collections.unmodifiableSet(retValue);
    }

    /**
     * Doesn't cache since results are already cached in super class.
     * @param currency
     * @return
     * @throws AmountMoneyExchangeRateRetrieverException
     */
    @Override
    public double fetchConversionRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException {
        Pair<Map<Currency, Double>, Currency> result = getResult();
        if(Currency.getReferenceCurrency() == null) {
            Currency.setReferenceCurrency(result.getValue());
        }else {
            if(!Currency.getReferenceCurrency().equals(result.getValue())) {
                throw new IllegalStateException("Online response base/reference currency changed"); //@TODO: this should be handled better
            }
        }

        //handle return value
        if(currency.equals(result.getValue())) {
            return 1.0;
        }
        assert result != null;
        Double retValue = result.getKey().get(currency);
        if(retValue == null) {
            throw new AmountMoneyExchangeRateRetrieverException(String.format("The API response from URL %s didn't contain the key for the currency %s",
                    getUrl(),
                    currency));
        }
        return retValue;
    }

    protected abstract String getUrl();
}
