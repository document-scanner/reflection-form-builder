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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
public class MemoryAmountMoneyCurrencyStorage extends AbstractAmountMoneyCurrencyStorage {
    private Set<Currency> currencies = new HashSet<>();

    @Override
    public Set<Currency> getCurrencies() throws AmountMoneyCurrencyStorageException {
        return Collections.unmodifiableSet(currencies);
    }

    @Override
    public void saveCurrency(Currency currency) throws AmountMoneyCurrencyStorageException {
        this.currencies.add(currency);
    }

    @Override
    public void removeCurrency(Currency currency) throws AmountMoneyCurrencyStorageException {
        this.currencies.remove(currency);
    }

}
