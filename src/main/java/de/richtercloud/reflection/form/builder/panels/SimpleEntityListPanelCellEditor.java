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
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 * Displays the same component as {@link EntityListPanelCellRenderer}.
 *
 * Editing doesn't have any effect.
 * @author richter
 */
public class SimpleEntityListPanelCellEditor extends ListPanelTableCellEditor<JLabel> {
    private static final long serialVersionUID = 1L;

    public SimpleEntityListPanelCellEditor() {
        super(new JLabel());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        getComponent().setText(value != null ? value.toString() : null);
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    /**
     * editing doesn't have any effect
     * @return
     */
    @Override
    protected Object stopCellEditing0() {
        return this.getComponent().getText();
    }
}
