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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- implement more efficient so that data vector isn't copied at retrieval, but
target collection List used initially (add a delegate and wrap or change
implementation)
*/
public class SingleColumnListPanelTableModel<T> extends DefaultTableModel implements ListPanelTableModel<T> {
    private static final long serialVersionUID = 1L;
    private List<T> values = new ArrayList<>();
    private Class<? extends T> columnClass;

    public SingleColumnListPanelTableModel(Class<? extends T> columnClass) {
        super();
        this.columnClass = columnClass;
    }

    /**
     *
     * @return
     */
    /*
    internal implementation notes:
    - returning an unmodifiable view corresponds to interface specifications
    */
    @Override
    public List<T> getData() {
        return Collections.unmodifiableList(this.values);
    }

    @Override
    public void addColumn(String columnName) {
        super.addColumn(columnName);//should fire data change event
    }

    @Override
    public void removeElement(int row) {
        this.values.remove(row);
        this.fireTableDataChanged();
    }

    @Override
    public void addElement(T element) {
        this.values.add(element);
        this.fireTableDataChanged();
    }

    @Override
    public void insertElementAt(int row, T element) {
        this.values.add(row, element);
        this.fireTableDataChanged();
    }

    @Override
    public void removeElement(T element) {
        this.values.remove(element);
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        if(this.values == null) {
            //during initialization (this is inefficient, but due to the bad design of DefaultTableModel
            return 0;
        }
        return this.values.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClass;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.values.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex >= this.values.size()) {
            this.values.add((T) aValue);
        }else {
            this.values.set(rowIndex, (T) aValue);
        }
    }
}
