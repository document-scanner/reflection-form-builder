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

import org.jscience.economics.money.Currency;

/**
 * Abstracts storage of usage information of {@link Currency}s in
 * {@link AmountMoneyPanel} which can be used to sort the list of available
 * currencies.
 * @author richter
 */
public interface AmountMoneyUsageStatisticsStorage {

    /**
     * Informs how often {@code currency} has been used.
     * @param currency the currency whose usage count to get
     * @return the usage count
     */
    int getUsageCount(Currency currency);

    /**
     * Increments the usage count of {@code currency} and stores the
     * information.
     * @param currency the currency whose usage count to increment
     * @throws AmountMoneyUsageStatisticsStorageException if an exception during
     *     saving the changes occurs
     */
    void incrementUsageCount(Currency currency) throws AmountMoneyUsageStatisticsStorageException;

    /**
     * Resets the stored information for {@code currency}.
     * @param currency the currency whose information to reset
     * @throws AmountMoneyUsageStatisticsStorageException if an exception during
     *     saving the changes occurs
     */
    void reset(Currency currency) throws AmountMoneyUsageStatisticsStorageException;
}
