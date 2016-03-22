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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 *
 * @author richter
 */
public class BooleanWrapperComboBox extends JComboBox<Boolean> {
    private static final long serialVersionUID = 1L;
    private final Boolean initialValue;

    public BooleanWrapperComboBox(Boolean initialValue) {
        super(new DefaultComboBoxModel<>(new Boolean[] {Boolean.TRUE, Boolean.FALSE, null}));
        this.initialValue = initialValue;
        reset0();
    }

    public Boolean getValue() {
        return (Boolean) this.getSelectedItem();
    }

    public void reset() {
        reset0();
    }

    private void reset0() {
        this.setSelectedItem(initialValue);
    }

}
