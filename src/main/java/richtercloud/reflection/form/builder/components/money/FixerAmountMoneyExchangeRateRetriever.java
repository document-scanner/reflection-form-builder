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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
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
it's fine to cache the HTTP response here although
CachedOnlineAmountMoneyExchangeRateRetriever already caches interface return
values because it's a different thing and the interface can't know that
fetchConversionRate and getSupportedCurrencies are retrieved from the same
response; consider abstracting a HTTP/JSON retriever to share the code for
caching responses
*/
public class FixerAmountMoneyExchangeRateRetriever extends CachedOnlineAmountMoneyExchangeRateRetriever {
    private final static Logger LOGGER = LoggerFactory.getLogger(FixerAmountMoneyExchangeRateRetriever.class);
    private final static String FIXER_URL = "http://api.fixer.io/latest";
    public final static String INITIAL_RESOURCE_RESOURCE_NAME_DEFAULT = "/fixer-initial-result.xml";

    public FixerAmountMoneyExchangeRateRetriever(File fileCacheFile) {
        super(fileCacheFile,
                INITIAL_RESOURCE_RESOURCE_NAME_DEFAULT);
    }

    public FixerAmountMoneyExchangeRateRetriever(File fileCacheFile,
            long fileCacheExpirationMillis) {
        super(fileCacheFile,
                fileCacheExpirationMillis,
                INITIAL_RESOURCE_RESOURCE_NAME_DEFAULT);
    }

    private FixerJsonResponse retrieveResponse() throws AmountMoneyExchangeRateRetrievalException {
        try {
            URLConnection uRLConnection = new URL(FIXER_URL).openConnection();
            InputStream inputStream = uRLConnection.getInputStream();
            String responseJsonString = IOUtils.toString(inputStream);
            LOGGER.debug(String.format("%s replied: %s", FIXER_URL, responseJsonString));
            FixerJsonResponse response = new ObjectMapper().readValue(responseJsonString, FixerJsonResponse.class);
            return response;
        } catch (IOException ex) {
            throw new AmountMoneyExchangeRateRetrievalException(ex);
        }
    }

    @Override
    protected Pair<Map<Currency, Double>, Currency> fetchResult() throws AmountMoneyExchangeRateRetrievalException {
        Map<Currency, Double> exchangeRates = new HashMap<>();
        FixerJsonResponse fixerJsonResponse = retrieveResponse();
        for(String currencyCode : fixerJsonResponse.getRates().keySet()) {
            Currency currency = new Currency(currencyCode);
            Double exchangeRate = fixerJsonResponse.getRates().get(currencyCode);
            assert exchangeRate != null;
            exchangeRates.put(currency, exchangeRate);
        }
        exchangeRates.put(new Currency(fixerJsonResponse.getBase()), 1.0);
        return new ImmutablePair<>(exchangeRates,
                new Currency(fixerJsonResponse.getBase()));
    }

    @Override
    protected String getUrl() {
        return FIXER_URL;
    }
}
