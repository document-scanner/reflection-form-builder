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
package de.richtercloud.reflection.form.builder.panels;

import java.awt.Component;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author richter
 */
public class IntegerListPanelCellEditor extends ListPanelTableCellEditor<JSpinner> {
    private static final long serialVersionUID = 1L;

    public IntegerListPanelCellEditor() {
        super(new JSpinner(new SpinnerNumberModel(0, Integer.MIN_VALUE, Integer.MAX_VALUE, 1)));
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,
            Object value,
            boolean isSelected,
            int row,
            int column) {
        this.getComponent().setValue(value);
        return super.getTableCellEditorComponent(table,
                value,
                isSelected,
                row,
                column);
    }

    @Override
    protected Object stopCellEditing0() {
        return this.getComponent().getValue();
    }

}
