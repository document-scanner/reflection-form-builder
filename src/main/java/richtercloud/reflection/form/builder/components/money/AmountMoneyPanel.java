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

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.components.NullableComponent;
import richtercloud.reflection.form.builder.components.NullableComponentUpdateEvent;
import richtercloud.reflection.form.builder.components.NullableComponentUpdateListener;

/**
 *
 * @author richter
 */
public class AmountMoneyPanel extends NullableComponent<Amount<Money>, AmountMoneyComponent> {

    private static final long serialVersionUID = 1L;

    public AmountMoneyPanel(Amount<Money> initialValue,
            AmountMoneyCurrencyStorage amountMoneyCurrencyStorage,
            AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage,
            AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever,
            MessageHandler messageHandler) throws AmountMoneyCurrencyStorageException, AmountMoneyExchangeRateRetrieverException {
        super(initialValue,
                new AmountMoneyComponent(amountMoneyCurrencyStorage,
                        amountMoneyUsageStatisticsStorage,
                        messageHandler));
        getMainComponent().addUpdateListener(new AmountMoneyComponentUpdateListener() {
            @Override
            public void onUpdate(AmountMoneyComponentUpdateEvent amountMoneyPanelUpdateEvent) {
                for(NullableComponentUpdateListener updateListener : getUpdateListeners()) {
                    updateListener.onUpdate(new NullableComponentUpdateEvent(amountMoneyPanelUpdateEvent.getAmountMoney()));
                }
            }
        });
    }

    @Override
    protected void setValue0(Amount<Money> value) {
        getMainComponent().setValue(value);
    }

    @Override
    protected Amount<Money> getValue0() {
        Amount<Money> retValue = getMainComponent().getValue();
        return retValue;
    }
}
