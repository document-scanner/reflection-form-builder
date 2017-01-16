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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jscience.economics.money.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
- A memory cache only makes sense if writing back the up-to-date data in memory
is a lot of work (this could only be handled via asynchronous writing, like in
QueryHistoryStorage in reflection-form-builder-jpa)
*/
public abstract class CachedOnlineAmountMoneyExchangeRateRetriever extends OnlineAmountMoneyExchangeRateRetriever {
    private final static Logger LOGGER = LoggerFactory.getLogger(CachedOnlineAmountMoneyExchangeRateRetriever.class);
    public final static long FILE_CACHE_EXPIRATION_MILLIS_DEFAULT = 60*60*1000; //1 hour
    private Pair<Map<Currency, Double>, Currency> result;
        //Needs to be one instance in order to allow passing of the result after
        //fetch in subclasses at once
    private Date fileCacheTimestamp = null;
    /**
     * The file to use for caching results across restarts of the application.
     * {@code null} indicates that the file cache ought not to be used.
     */
    private final File fileCacheFile;
    private final long fileCacheExpirationMillis;
    /**
     * The name of the resource to load with
     * {@link Class#getResourceAsStream(java.lang.String) } if an
     * {@link AmountMoneyExchangeRateRetrievalException} occurs in
     * {@link #fetchResult() }
     */
    private final String initialResultResourceName;

    public CachedOnlineAmountMoneyExchangeRateRetriever(File fileCacheFile,
            String initialResultResourceName) {
        this(fileCacheFile,
                FILE_CACHE_EXPIRATION_MILLIS_DEFAULT,
                initialResultResourceName);
    }

    public CachedOnlineAmountMoneyExchangeRateRetriever(File fileCacheFile,
            long fileCacheExpirationMillis,
            String initialResultResourceName) {
        this.fileCacheFile = fileCacheFile;
        this.fileCacheExpirationMillis = fileCacheExpirationMillis;
        if(initialResultResourceName == null) {
            throw new IllegalArgumentException("initialResultResourceName mustn't be null");
        }
        this.initialResultResourceName = initialResultResourceName;
    }

    public Pair<Map<Currency, Double>, Currency> getResult() throws AmountMoneyExchangeRateRetrieverException {
        Date timestampNew = new Date();
            //instantiated here in order to allow reusage if a new timestamp
            //needs to be stored
        XStream xStream = new XStream();
        if(result == null) {
            if(!fileCacheFile.exists()) {
                fileCacheTimestamp = timestampNew;
                try {
                    result = fetchResult();
                } catch (AmountMoneyExchangeRateRetrievalException ex) {
                    InputStream initialResultInputStream = CachedOnlineAmountMoneyExchangeRateRetriever.class.getResourceAsStream(initialResultResourceName);
                    Pair<Date, Pair<Map<Currency, Double>, Currency>> serializedResult = (Pair<Date, Pair<Map<Currency, Double>, Currency>>) xStream.fromXML(initialResultInputStream);
                    fileCacheTimestamp = serializedResult.getKey();
                    result = serializedResult.getValue();
                }
                try {
                    xStream.toXML(new ImmutablePair<>(timestampNew, result), new FileOutputStream(fileCacheFile));
                } catch (FileNotFoundException ex) {
                    throw new AmountMoneyExchangeRateRetrieverException(ex);
                }
            }else {
                LOGGER.debug(String.format("using cached result from '%s'",
                        fileCacheFile.getAbsolutePath()));
                Pair<Date, Pair<Map<Currency, Double>, Currency>> serializedResult = (Pair<Date, Pair<Map<Currency, Double>, Currency>>) xStream.fromXML(fileCacheFile);
                fileCacheTimestamp = serializedResult.getKey();
                result = serializedResult.getValue();
            }
        }
        if(timestampNew.getTime() - fileCacheTimestamp.getTime() > fileCacheExpirationMillis) {
            try {
                result = fetchResult();
            } catch (AmountMoneyExchangeRateRetrievalException ex) {
                //skip since an update doesn't make if network connection failed
                //or is missing -> return immediately
                return result;
            }
            try {
                xStream.toXML(new ImmutablePair<>(timestampNew, result), new FileOutputStream(fileCacheFile));
            } catch (FileNotFoundException ex) {
                throw new AmountMoneyExchangeRateRetrieverException(ex);
            }
            fileCacheTimestamp = timestampNew;
        }
        return result;
    }

    /**
     * Allows to retrieve the complete cache at once which makes sense for
     * subclasses which will retrieve a list of currency exchange rates rather
     * than a single result (probably all).
     * @return
     * @throws richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetrieverException
     * if an exception unrelated to a network failure or missing network
     * connection occured
     * @throws richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetrievalException
     * if an exception related to a network failure or missing network
     * connection occured
     */
    protected abstract Pair<Map<Currency, Double>, Currency> fetchResult() throws AmountMoneyExchangeRateRetrieverException,
            AmountMoneyExchangeRateRetrievalException;

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
