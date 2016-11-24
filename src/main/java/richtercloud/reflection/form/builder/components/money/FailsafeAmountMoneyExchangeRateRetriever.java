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

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.Set;
import org.jscience.economics.money.Currency;

/**
 * Has multiple {@link AmountMoneyExchangeRateRetriever}s available for
 * retrieval and wraps calls to {@link #retrieveExchangeRate(org.jscience.economics.money.Currency) }
 * in a try-catch block and delegates to the next candidate if the call to the
 * preceeding fails.
 *
 * Note that this doesn't make the call really, but only
 * almost failsafe. The name is still fine imo.
 *
 * @author richter
 */
public class FailsafeAmountMoneyExchangeRateRetriever implements AmountMoneyExchangeRateRetriever {
    private final Queue<AmountMoneyExchangeRateRetriever> retrieverQueue = new LinkedList<>();

    public FailsafeAmountMoneyExchangeRateRetriever() {
        FixerAmountMoneyExchangeRateRetriever fixerAmountMoneyExchangeRateRetriever = new FixerAmountMoneyExchangeRateRetriever();
        ECBAmountMoneyExchangeRateRetriever eCBAmountMoneyExchangeRateRetriever = new ECBAmountMoneyExchangeRateRetriever();
        retrieverQueue.add(fixerAmountMoneyExchangeRateRetriever);
        retrieverQueue.add(eCBAmountMoneyExchangeRateRetriever);
    }

    public FailsafeAmountMoneyExchangeRateRetriever(Set<AmountMoneyExchangeRateRetriever> fallbackRetrievers) {
        this.retrieverQueue.addAll(fallbackRetrievers);
    }

    /**
     * In order to be really failsafe this method only returns currencies which
     * are supported by all used exchange rate retrievers.
     * @return
     * @throws AmountMoneyExchangeRateRetrieverException
     */
    @Override
    public Set<Currency> getSupportedCurrencies() throws AmountMoneyExchangeRateRetrieverException {
        assert retrieverQueue.size() >= 2; //otherwise failsafe doesn't make
            //sense
        Iterator<AmountMoneyExchangeRateRetriever> retrieverItr = retrieverQueue.iterator();
            //needs to be a list in order to allow deletion with ListIterator
            //below
        List<Currency> firstRetrieverSupportedCurrencies = new LinkedList<>();
        try {
            firstRetrieverSupportedCurrencies = new LinkedList<>(retrieverItr.next().getSupportedCurrencies());
        } catch(AmountMoneyExchangeRateRetrieverException ex) {
            //skip (assertion tested above)
        }
        while(retrieverItr.hasNext()) {
            AmountMoneyExchangeRateRetriever retrieverNxt = retrieverItr.next();
            ListIterator<Currency> firstRetrieverSupportedCurrenciesItr = firstRetrieverSupportedCurrencies.listIterator();
            while(firstRetrieverSupportedCurrenciesItr.hasNext()) {
                Currency firstRetrieverSupportedCurrency = firstRetrieverSupportedCurrenciesItr.next();
                Set<Currency> retrieverNxtSupportedCurrencies = new HashSet<>();
                try {
                    retrieverNxtSupportedCurrencies = retrieverNxt.getSupportedCurrencies();
                }catch(AmountMoneyExchangeRateRetrieverException ex) {
                    //skip
                }
                if(!retrieverNxtSupportedCurrencies.contains(firstRetrieverSupportedCurrency)) {
                    firstRetrieverSupportedCurrenciesItr.remove();
                }
            }
        }
        return new HashSet<>(firstRetrieverSupportedCurrencies);
    }

    @Override
    public void retrieveExchangeRate(Currency currency) throws AmountMoneyExchangeRateRetrieverException {
        for(AmountMoneyExchangeRateRetriever retriever : retrieverQueue) {
            try {
                retriever.retrieveExchangeRate(currency);
                return;
            }catch(AmountMoneyExchangeRateRetrieverException ex) {
                //continue
            }
        }
        throw new AmountMoneyExchangeRateRetrieverException("all retrievers "
                + "failed to retrieve exchange rate");
    }
}
