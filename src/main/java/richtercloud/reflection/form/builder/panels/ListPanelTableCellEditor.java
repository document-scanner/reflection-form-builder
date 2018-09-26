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
package richtercloud.reflection.form.builder.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * Manages {@link JComponent}s in a list. Editor values aren't managed
 * because they are already managed by the model components.
 * @param <C>
 */
/*
internal implementation notes:
- DefaultCellEditor doesn't provide a default constructor and causes
ClassCastExceptions
- update of model when editing is stopped occurs automatically in
AbstractCellEditor
 */
public abstract class ListPanelTableCellEditor<C extends JComponent> extends AbstractCellEditor implements TableCellEditor {
    private static final long serialVersionUID = 1L;
    private JPanel componentPanel = new JPanel(new BorderLayout());
    private C component;
    private Object cellEditorValue = null;

    public ListPanelTableCellEditor(C component) {
        this.componentPanel.add(component);
        this.component = component;
    }

    public C getComponent() {
        return component;
    }

    @Override
    public Object getCellEditorValue() {
        return cellEditorValue;
    }

    /**
     * Sets the editor value to {@code null}. Can be called after stopping
     * editing (which also retrieves the editor value).
     */
    public void resetCellEditorValue() {
        this.cellEditorValue = null;
    }

    @Override
    public boolean stopCellEditing() {
        this.cellEditorValue = stopCellEditing0();
        boolean retValue = super.stopCellEditing();
        this.resetCellEditorValue();
        return retValue;
    }

    protected abstract Object stopCellEditing0();

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if(isSelected) {
            this.componentPanel.setBorder(BorderFactory.createEtchedBorder());
            //this.componentPanel.setBackground(Color.red);
        }else {
            this.componentPanel.setBorder(BorderFactory.createEmptyBorder());
            //this.componentPanel.setBackground(Color.white);
        }
        component.setSize(this.component.getPreferredSize());
        return component;
    }

}
