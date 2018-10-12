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

import javax.measure.converter.ConversionException;
import org.jscience.economics.money.Currency;

/*
internal implementation notes:
- set Currency.setReferenceCurreny in fetchConversionRate in order to be able to
deal with change of reference currency in online results
*/
/**
 *
 * @author richter
 */
public abstract class OnlineAmountMoneyExchangeRateRetriever implements AmountMoneyExchangeRateRetriever {

    @Override
    @SuppressWarnings("PMD.PreserveStackTrace")
    public void retrieveExchangeRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException {
        try {
            currency.getExchangeRate();
        }catch(ConversionException ex) {
            if(Currency.getReferenceCurrency() == null) {
                throw new IllegalStateException("Reference currency has to be set in fetchConversionRate or some place else");
            }
            double exchangeRate = fetchConversionRate(currency);
            currency.setExchangeRate(exchangeRate);
        }
    }

    /**
     * Fetches online information about a exchange rate for {@code currency}
     * relative to a reference currency.
     *
     * @param currency the currency to fetch for
     * @return the conversion rate
     * @throws AmountMoneyExchangeRateRetrieverException if an exception
     *     unrelated to a network failure or missing network connection occured
     */
    protected abstract double fetchConversionRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException;
}
