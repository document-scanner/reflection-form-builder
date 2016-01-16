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
package richtercloud.reflection.form.builder.fieldhandler;

import java.lang.reflect.Field;
import javax.swing.JComponent;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.components.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.AmountMoneyCurrencyStorageException;
import richtercloud.reflection.form.builder.components.AmountMoneyPanel;
import richtercloud.reflection.form.builder.components.AmountMoneyPanelUpdateEvent;
import richtercloud.reflection.form.builder.components.AmountMoneyPanelUpdateListener;
import richtercloud.reflection.form.builder.components.AmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- naming is strange, but conforms best to JScience structure with Amount<Unit>
types
*/
public class AmountMoneyFieldHandler implements FieldHandler<Amount<Money>, FieldUpdateEvent<Amount<Money>>, ReflectionFormBuilder, AmountMoneyPanel> {
    private final AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage;
    private final AmountMoneyCurrencyStorage additionalCurrencies;
    private final MessageHandler messageHandler;

    public AmountMoneyFieldHandler(AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage,
            AmountMoneyCurrencyStorage additionalCurrencies,
            MessageHandler messageHandler) {
        this.amountMoneyUsageStatisticsStorage = amountMoneyUsageStatisticsStorage;
        this.additionalCurrencies = additionalCurrencies;
        this.messageHandler = messageHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Amount<Money>>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        final AmountMoneyPanel retValue;
        try {
            retValue = new AmountMoneyPanel(additionalCurrencies,
                    amountMoneyUsageStatisticsStorage,
                    messageHandler);
        } catch (AmountMoneyCurrencyStorageException ex) {
            throw new FieldHandlingException(ex);
        }
        retValue.addUpdateListener(new AmountMoneyPanelUpdateListener() {
            @Override
            public void onUpdate(AmountMoneyPanelUpdateEvent amountMoneyPanelUpdateEvent) {
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.retrieveAmountMoney()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(AmountMoneyPanel component) {
        Amount<Money> fieldResetValue = Amount.valueOf(0.0, Currency.getReferenceCurrency());
        component.applyAmountMoney(fieldResetValue);
    }
}
