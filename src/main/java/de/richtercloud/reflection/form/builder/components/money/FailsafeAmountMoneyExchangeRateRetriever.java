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
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.jscience.economics.money.Currency;

/*
internal implementation notes:
- providing a flag to check availability at every operation is difficult because
it interferes with caching functionality
*/
/**
 * Has multiple {@link AmountMoneyExchangeRateRetriever}s available for
 * retrieval and wraps calls to {@link #retrieveExchangeRate(org.jscience.economics.money.Currency) }
 * in a try-catch block and delegates to the next candidate if the call to the
 * preceeding fails.
 *
 * Supported currencies are those supported by at least one
 * {@link AmountMoneyExchangeRateRetriever} which returns a result. That's
 * necessary because otherwise one failing
 * {@link AmountMoneyExchangeRateRetriever} causes the
 * {@link FailsafeAmountMoneyExchangeRateRetriever} to fail which isn't
 * intended.
 *
 * Note that this doesn't make the call really, but only
 * almost failsafe. The name is still fine imo.
 *
 * @author richter
 */
public class FailsafeAmountMoneyExchangeRateRetriever implements AmountMoneyExchangeRateRetriever {
    public final static int AVAILABLE_RETRIEVER_MIN_DEFAULT = 1;
    private final Queue<AmountMoneyExchangeRateRetriever> retrieverQueue = new LinkedList<>();
    /**
     * How many {@link AmountMoneyExchangeRateRetriever} have to be available.
     */
    private final int availableRetrieverMin;

    public FailsafeAmountMoneyExchangeRateRetriever(File cacheFileDir) throws IOException {
        this(AVAILABLE_RETRIEVER_MIN_DEFAULT,
                cacheFileDir,
                CachedOnlineAmountMoneyExchangeRateRetriever.FILE_CACHE_EXPIRATION_MILLIS_DEFAULT);
    }

    public FailsafeAmountMoneyExchangeRateRetriever(File cacheFileDir,
            long fileCacheExpirationMillis) throws IOException {
        this(AVAILABLE_RETRIEVER_MIN_DEFAULT,
                cacheFileDir,
                fileCacheExpirationMillis);
    }

    /**
     *
     * @param availableRetrieverMin the minimum number of available retrievers
     *     which are tried
     * @param cacheFileDir Since
     *     {@code FailsafeAmountMoneyExchangeRateRetriever} will manage multiple
     *     possibly caching retrievers, a directory needs to be used for cache
     *     files.
     * @param fileCacheExpirationMillis the expiraton time of the file cache in
     *     milliseconds
     * @throws IOException if the creation of {@code cacheFileDir} fails
     */
    public FailsafeAmountMoneyExchangeRateRetriever(int availableRetrieverMin,
            File cacheFileDir,
            long fileCacheExpirationMillis) throws IOException {
        this.availableRetrieverMin = availableRetrieverMin;
        if(cacheFileDir == null) {
            throw new IllegalArgumentException("fileCacheFileDir mustn't be null");
        }
        if(cacheFileDir.exists()) {
            if(!cacheFileDir.isDirectory()) {
                throw new IllegalArgumentException("fileCacheFileDir exists, but is no directory");
            }
        }else {
            FileUtils.forceMkdir(cacheFileDir);
        }
        File fixerCacheFile = new File(cacheFileDir,
                "fixer.xml");
        File eCBCacheFile = new File(cacheFileDir,
                "ecb.xml");
        FixerAmountMoneyExchangeRateRetriever fixerAmountMoneyExchangeRateRetriever = new FixerAmountMoneyExchangeRateRetriever(fixerCacheFile,
                fileCacheExpirationMillis);
        ECBAmountMoneyExchangeRateRetriever eCBAmountMoneyExchangeRateRetriever = new ECBAmountMoneyExchangeRateRetriever(eCBCacheFile,
                fileCacheExpirationMillis);
        retrieverQueue.add(fixerAmountMoneyExchangeRateRetriever);
        retrieverQueue.add(eCBAmountMoneyExchangeRateRetriever);
    }

    public FailsafeAmountMoneyExchangeRateRetriever(Set<AmountMoneyExchangeRateRetriever> fallbackRetrievers) {
        this(AVAILABLE_RETRIEVER_MIN_DEFAULT, fallbackRetrievers);
    }

    public FailsafeAmountMoneyExchangeRateRetriever(int availableRetrieverMin,
            Set<AmountMoneyExchangeRateRetriever> fallbackRetrievers) {
        this.availableRetrieverMin = availableRetrieverMin;
        this.retrieverQueue.addAll(fallbackRetrievers);
    }

    /**
     * In order to be really failsafe this method only returns currencies which
     * are supported by all used exchange rate retrievers.
     * @return the set of supported currencies
     * @throws AmountMoneyExchangeRateRetrieverException if an exception
     *     unrelated to a network failure or missing network connection occured
     */
    @Override
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public Set<Currency> getSupportedCurrencies() throws AmountMoneyExchangeRateRetrieverException {
        assert retrieverQueue.size() >= 2; //otherwise failsafe doesn't make
            //sense
        Set<Currency> retValue = new HashSet<>();
        int availableRetrieverCount = 0;
        for(AmountMoneyExchangeRateRetriever retriever : retrieverQueue) {
            try {
                Set<Currency> retrieverSupportedCurrencies = retriever.getSupportedCurrencies();
                retValue.addAll(retrieverSupportedCurrencies);
                availableRetrieverCount++;
            }catch(AmountMoneyExchangeRateRetrieverException ex) {
                //skip (assertion checked above)
            }
        }
        if(availableRetrieverCount < this.availableRetrieverMin) {
            throw new AmountMoneyExchangeRateRetrieverException(String.format("Only %d of %d required retrievers were available",
                    availableRetrieverCount,
                    this.availableRetrieverMin));
        }
        return retValue;
    }

    @Override
    @SuppressWarnings("PMD.EmptyCatchBlock")
    public void retrieveExchangeRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException {
        for(AmountMoneyExchangeRateRetriever retriever : retrieverQueue) {
            try {
                retriever.retrieveExchangeRate(currency);
                return;
            }catch(AmountMoneyExchangeRateRetrieverException ex) {
                //continue
            }
        }
        throw new AmountMoneyExchangeRateRetrieverException("all retrievers "
                + "failed to retrieve exchange rate");
    }
}
