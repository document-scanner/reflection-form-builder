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

/**
 * Indicates any exception which might have occured during retrieval of a
 * conversion rate from a {@link AmountMoneyConversionRateRetriever}.
 *
 * @author richter
 */
public class AmountMoneyExchangeRateRetrieverException extends Exception {
    private static final long serialVersionUID = 1L;

    public AmountMoneyExchangeRateRetrieverException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountMoneyExchangeRateRetrieverException(String message) {
        super(message);
    }

    public AmountMoneyExchangeRateRetrieverException(Throwable cause) {
        super(cause);
    }
}
