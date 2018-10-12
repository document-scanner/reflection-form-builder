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
 * A renderer for entity list entries. Does nothing, but display a label
 * representing created instances with default values.
 * @author richter
 */
public class SimpleEntityListPanelCellRenderer extends ListPanelTableCellRenderer<JLabel> {

    public SimpleEntityListPanelCellRenderer() {
        super(new JLabel());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        this.getComponent().setText(value != null ? value.toString() : null);
        return super.getTableCellRendererComponent(table,
                value,
                isSelected,
                hasFocus,
                row,
                column);
    }
}
