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
package richtercloud.reflection.form.builder.fieldhandler.factory;

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.components.money.AmountMoneyCurrencyStorage;
import richtercloud.reflection.form.builder.components.money.AmountMoneyExchangeRateRetriever;
import richtercloud.reflection.form.builder.components.money.AmountMoneyUsageStatisticsStorage;
import richtercloud.reflection.form.builder.fieldhandler.AmountMoneyFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;

/**
 *
 * @author richter
 */
public class AmountMoneyMappingFieldHandlerFactory extends MappingFieldHandlerFactory {

    @SuppressWarnings("serial")
    public static Type createAmountMoneyTypeToken() {
        return new TypeToken<Amount<Money>>() {}.getType();
    }
    private final AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage;
    private final AmountMoneyCurrencyStorage amountMoneyCurrencyStorage;
    private final AmountMoneyExchangeRateRetriever amountMoneyConversionRateRetriever;

    public AmountMoneyMappingFieldHandlerFactory(AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage,
            AmountMoneyCurrencyStorage amountMoneyCurrencyStorage,
            AmountMoneyExchangeRateRetriever amountMoneyConversionRateRetriever,
            MessageHandler messageHandler) {
        super(messageHandler);
        this.amountMoneyUsageStatisticsStorage = amountMoneyUsageStatisticsStorage;
        this.amountMoneyCurrencyStorage = amountMoneyCurrencyStorage;
        this.amountMoneyConversionRateRetriever = amountMoneyConversionRateRetriever;
    }

    /**
     * A separate method to add a {@link AmountMoneyFieldHandler} which avoids
     * messing up the constructor call hierarchy.
     *
     * @return
     */
    @Override
    public Map<Type, FieldHandler<?, ?, ?, ?>> generateClassMapping() {
        Map<Type, FieldHandler<?, ?, ?, ?>> classMapping0 = new HashMap<>(super.generateClassMapping());
        classMapping0.put(createAmountMoneyTypeToken(),
                new AmountMoneyFieldHandler(amountMoneyUsageStatisticsStorage,
                        amountMoneyConversionRateRetriever,
                        amountMoneyCurrencyStorage,
                        getMessageHandler()));
        return classMapping0;
    }
}
