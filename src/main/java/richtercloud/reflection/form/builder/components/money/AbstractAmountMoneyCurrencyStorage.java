/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package richtercloud.reflection.form.builder.components.money;

import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
public abstract class AbstractAmountMoneyCurrencyStorage implements AmountMoneyCurrencyStorage {

    /**
     * Translation is performed based on the naive assumption that it can occur
     * based on currency codes. There's no check whether codes of currencies
     * returned by {@link #getCurrencies() } are unique because that's assumed.
     *
     * @param currency
     * @return the equivalent currency or {@code null} if {@code currency} can't
     * be translated into a JScience currency or if data isn't available in this
     * storage
     */
    /*
    internal implementation notes:
    - Don't create a JScience currency based on the code (is possible) because
    it misses viable information, like the exchange rate (which can not
    deterministically be retrieved by an AmountMoneyExchangeRateRetriever
    because it might not be supported, so this information should be set by the
    user at (manual) creation of the currency)
    */
    @Override
    public Currency translate(java.util.Currency currency) throws AmountMoneyCurrencyStorageException {
        Currency retValue = null;
        for(Currency availableCurrency : getCurrencies()) {
            if(availableCurrency.getCode().equals(currency.getCurrencyCode())) {
                retValue = availableCurrency;
                break;
            }
        }
        return retValue;
    }
}
