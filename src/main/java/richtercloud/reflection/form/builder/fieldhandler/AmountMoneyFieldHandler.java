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
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.components.NullableComponentUpdateEvent;
import richtercloud.reflection.form.builder.components.NullableComponentUpdateListener;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorageException;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetrieverException;
import richtercloud.reflection.form.builder.components.money.AmountMoneyPanel;
import richtercloud.reflection.form.builder.components.money.AmountMoneyUsageStatisticsStorage;

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
    private final AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever;
    private final AmountMoneyCurrencyStorage amountMoneyCurrencyStorage;
    private final MessageHandler messageHandler;

    public AmountMoneyFieldHandler(AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage,
            AmountMoneyExchangeRateRetriever amountMoneyConversionRateRetriever,
            AmountMoneyCurrencyStorage amountMoneyCurrencyStorage,
            MessageHandler messageHandler) {
        this.amountMoneyUsageStatisticsStorage = amountMoneyUsageStatisticsStorage;
        this.amountMoneyExchangeRateRetriever = amountMoneyConversionRateRetriever;
        this.amountMoneyCurrencyStorage = amountMoneyCurrencyStorage;
        this.messageHandler = messageHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Amount<Money>>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException, IllegalArgumentException, IllegalAccessException {
        final AmountMoneyPanel retValue;
        try {
            Amount<Money> fieldValue = (Amount<Money>) field.get(instance);
            retValue = new AmountMoneyPanel(fieldValue, //initialValue
                    amountMoneyCurrencyStorage,
                    amountMoneyUsageStatisticsStorage,
                    amountMoneyExchangeRateRetriever,
                    messageHandler);
        } catch (AmountMoneyExchangeRateRetrieverException | AmountMoneyCurrencyStorageException ex) {
            throw new FieldHandlingException(ex);
        }
        retValue.addUpdateListener(new NullableComponentUpdateListener<NullableComponentUpdateEvent<Amount<Money>>>() {
            @Override
            public void onUpdate(NullableComponentUpdateEvent<Amount<Money>> amountMoneyPanelUpdateEvent) {
                updateListener.onUpdate(new FieldUpdateEvent<>(amountMoneyPanelUpdateEvent.getNewValue()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(AmountMoneyPanel component) {
        Amount<Money> fieldResetValue = Amount.valueOf(0.0, Currency.getReferenceCurrency());
        component.setValue(fieldResetValue);
    }
}
