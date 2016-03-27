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
import org.apache.commons.io.IOUtils;
import org.jscience.economics.money.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richter
 */
public class FixerAmountMoneyExchangeRateRetriever extends OnlineAmountMoneyExchangeRateRetriever {
    private final static Logger LOGGER = LoggerFactory.getLogger(FixerAmountMoneyExchangeRateRetriever.class);
    private final static String FIXER_URL = "http://api.fixer.io/latest";

    @Override
    protected double fetchConversionRate(Currency currency) throws AmountMoneyCurrencyStorageException {
        try {
            URLConnection uRLConnection = new URL(FIXER_URL).openConnection();
            InputStream inputStream = uRLConnection.getInputStream();
            String responseJsonString = IOUtils.toString(inputStream);
            LOGGER.debug(String.format("%s replied: %s", FIXER_URL, responseJsonString));
            FixerJsonResponse response = new ObjectMapper().readValue(responseJsonString, FixerJsonResponse.class);
            Currency referenceCurrency = getReferenceCurrency(response.getBase());
            if(Currency.getReferenceCurrency() == null) {
                Currency.setReferenceCurrency(referenceCurrency);
            }else {
                if(!Currency.getReferenceCurrency().equals(referenceCurrency)) {
                    throw new IllegalStateException("Online response base/reference currency changed"); //@TODO: this should be handled better
                }
            }
            Double exchangeRate = response.getRates().get(currency.getCode());
            if(exchangeRate == null) {
                throw new AmountMoneyCurrencyStorageException(String.format("The fixer API response from URL %s didn't contain the key for the currency %s", FIXER_URL, currency));
            }
            return exchangeRate;
        } catch (MalformedURLException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        } catch (IOException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

    /**
     * The base of a {@link FixerJsonResponse}.
     *
     * @param base
     * @return
     */
    private Currency getReferenceCurrency(String base) {
        for(Currency currency : AmountMoneyPanel.DEFAULT_CURRENCIES) {
            if(base.equals(currency.getCode())) {
                return currency;
            }
        }
        return null;
    }
}
