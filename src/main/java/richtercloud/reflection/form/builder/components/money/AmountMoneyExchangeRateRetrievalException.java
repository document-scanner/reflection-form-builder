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

/**
 * Indicates a failure of retrieval which most likely occured due to a missing
 * network connection. It is used to initialize the file cache of
 * {@link CachedOnlineAmountMoneyExchangeRateRetriever} in case no previous data
 * is available and the network connection fails.
 *
 * @author richter
 */
public class AmountMoneyExchangeRateRetrievalException extends Exception {
    private static final long serialVersionUID = 1L;

    public AmountMoneyExchangeRateRetrievalException(String message) {
        super(message);
    }

    public AmountMoneyExchangeRateRetrievalException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountMoneyExchangeRateRetrievalException(Throwable cause) {
        super(cause);
    }

    public AmountMoneyExchangeRateRetrievalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
