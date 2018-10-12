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

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author richter
 * @param <C> the component to use for rendering
 */
public abstract class ListPanelTableCellRenderer<C extends JComponent> implements TableCellRenderer {
    private final JPanel componentPanel = new JPanel(new BorderLayout());
    private final C component;

    public ListPanelTableCellRenderer(C component) {
        this.componentPanel.add(component);
        this.component = component;
    }


    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value,
            boolean isSelected,
            boolean hasFocus,
            int row,
            int column) {
        if(isSelected) {
            this.componentPanel.setBorder(BorderFactory.createEtchedBorder());
            //this.componentPanel.setBackground(Color.red);
        }else {
            this.componentPanel.setBorder(BorderFactory.createEmptyBorder());
            //this.componentPanel.setBackground(Color.white);
        }
        this.componentPanel.setSize(this.componentPanel.getPreferredSize());
        return this.componentPanel;
    }

    protected C getComponent() {
        return this.component;
    }
}
