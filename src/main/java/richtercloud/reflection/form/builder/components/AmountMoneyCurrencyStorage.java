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
package richtercloud.reflection.form.builder.components;

import java.util.Set;
import org.jscience.economics.money.Currency;

/**
 * Persists a list of used currencies across restarts of the program. This includes not only newly created custom currencies, but all used currencies which allows tracking of deletions from the default list (as specified in {@link AmountMoneyPanel#DEFAULT_CURRENCIES}).
 *
 * @author richter
 */
public interface AmountMoneyCurrencyStorage {

    Set<Currency> getCurrencies() throws AmountMoneyCurrencyStorageException;

    void saveCurrency(Currency currency) throws AmountMoneyCurrencyStorageException;

    void removeCurrency(Currency currency) throws AmountMoneyCurrencyStorageException;
}
