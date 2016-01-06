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

import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;

/**
 *
 * @author richter
 */
public class AmountMoneyPanelUpdateEvent {
    private final Amount<Money> amountMoney;

    public AmountMoneyPanelUpdateEvent(Amount<Money> amountMoney) {
        this.amountMoney = amountMoney;
    }

    public Amount<Money> getAmountMoney() {
        return amountMoney;
    }

}
