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
package richtercloud.reflection.form.builder.components;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
    private final Map<Currency, Double> cache = new HashMap<>();
    private final Map<Currency, Date> cacheTimestamps = new HashMap<>();
    private long cacheExpirationMillis = 60*60*1000; //1 hour
    private long supportedCurrencyExpirationMillis = 60*60*1000; //1 hour
    private Date supportedCurrencyCacheTimestamp;
    private Set<Currency> supportedCurrencyCache;

    @Override
    @SuppressWarnings("FinalMethod")
    protected final double fetchConversionRate(Currency currency) throws AmountMoneyCurrencyStorageException {
        Double retValue = cache.get(currency);
        Date timestamp = new Date();
        if(retValue == null
                || timestamp.getTime()-cacheTimestamps.get(currency).getTime() > cacheExpirationMillis) {
            retValue = fetchConversionRate0(currency);
            cache.put(currency, retValue);
            cacheTimestamps.put(currency, timestamp);
        }
        return retValue;
    }

    protected abstract double fetchConversionRate0(Currency currency) throws AmountMoneyCurrencyStorageException;

    protected Map<Currency, Double> getCache() {
        return cache;
    }

    @Override
    @SuppressWarnings("FinalMethod")
    public final Set<Currency> getSupportedCurrencies() throws AmountMoneyCurrencyStorageException {
        Date timestamp = new Date();
        if(supportedCurrencyCache == null
                || timestamp.getTime()-supportedCurrencyCacheTimestamp.getTime() > supportedCurrencyExpirationMillis) {
            supportedCurrencyCache = getSupportedCurrencies0();
            supportedCurrencyCacheTimestamp = new Date();
        }
        return Collections.unmodifiableSet(supportedCurrencyCache);
    }

    protected abstract Set<Currency> getSupportedCurrencies0() throws AmountMoneyCurrencyStorageException;
}
