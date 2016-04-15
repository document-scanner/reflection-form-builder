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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
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

    private FixerJsonResponse retrieveResponse() throws AmountMoneyCurrencyStorageException {
        try {
            URLConnection uRLConnection = new URL(FIXER_URL).openConnection();
            InputStream inputStream = uRLConnection.getInputStream();
            String responseJsonString = IOUtils.toString(inputStream);
            LOGGER.debug(String.format("%s replied: %s", FIXER_URL, responseJsonString));
            FixerJsonResponse response = new ObjectMapper().readValue(responseJsonString, FixerJsonResponse.class);
            return response;
        } catch (MalformedURLException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        } catch (IOException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

    @Override
    protected double fetchConversionRate0(Currency currency) throws AmountMoneyCurrencyStorageException {
        FixerJsonResponse response = retrieveResponse();
        Currency referenceCurrency = new Currency(response.getBase());
        if(Currency.getReferenceCurrency() == null) {
            Currency.setReferenceCurrency(referenceCurrency);
        }else {
            if(!Currency.getReferenceCurrency().equals(referenceCurrency)) {
                throw new IllegalStateException("Online response base/reference currency changed"); //@TODO: this should be handled better
            }
        }

        //fill cache
        for(String currencyCode : response.getRates().keySet()) {
            Double exchangeRate = response.getRates().get(currencyCode);
            if(exchangeRate == null) {
                throw new AmountMoneyCurrencyStorageException(String.format("The fixer API response from URL %s contained key %s, but no exchange rate for it",
                        FIXER_URL,
                        currencyCode));
            }
            Currency newCurrency = new Currency(currencyCode);
            newCurrency.setExchangeRate(exchangeRate);
            getCache().put(newCurrency, exchangeRate);
        }
        getCache().put(new Currency(response.getBase()), 1.0);

        //handle return value
        if(currency.equals(referenceCurrency)) {
            return 1.0;
        }
        Double retValue = response.getRates().get(currency.getCode());
        if(retValue == null) {
            throw new AmountMoneyCurrencyStorageException(String.format("The fixer API response from URL %s didn't contain the key for the currency %s",
                    FIXER_URL,
                    currency));
        }
        return retValue;
    }

    /**
     *
     * @return
     * @throws AmountMoneyCurrencyStorageException
     */
    /*
    internal implementation notes:
    - this wastes one request as long as the HTTP/JSON response isn't cached
    */
    @Override
    public Set<Currency> getSupportedCurrencies0() throws AmountMoneyCurrencyStorageException {
        FixerJsonResponse response = retrieveResponse();
        Set<Currency> retValue = new HashSet<>();
        for(String currencyCode : response.getRates().keySet()) {
            retValue.add(new Currency(currencyCode));
        }
        retValue.add(new Currency(response.getBase()));
        return retValue;
    }
}
