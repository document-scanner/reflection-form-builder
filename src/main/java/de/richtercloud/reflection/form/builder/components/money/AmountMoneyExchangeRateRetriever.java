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

import java.util.Set;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
public interface AmountMoneyExchangeRateRetriever {

    /*
    internal implementation notes:
    - handling setting of exchange rate inside
    AmountMoneyExchangeRateRetriever offers maximal hiding of details for
    callers
    */
    /**
     * Sets the {@code exchangeRate} property on the passed {@code currency}
     * relative to a reference currency.
     *
     * @param currency the currency to retrieve for
     * @throws AmountMoneyExchangeRateRetrieverException if an exception
     *     unrelated to a network failure or missing network connection occured
     */
    void retrieveExchangeRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException;

    /**
     * Gets a set of supported currencies from the mechanism used for retrieval
     * of exchange rates.
     * @return the set of supported currencies
     * @throws AmountMoneyExchangeRateRetrieverException if an exception
     *     unrelated to a network failure or missing network connection occured
     */
    Set<Currency> getSupportedCurrencies() throws AmountMoneyExchangeRateRetrieverException;
}
