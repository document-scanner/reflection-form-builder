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
package richtercloud.reflection.form.builder.panels;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
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
public class ListPanelTableModel<T> extends DefaultTableModel {
    private static final long serialVersionUID = 1L;

    public ListPanelTableModel() {
    }

    public List<?> getData() {
        List retValue = new LinkedList<>();
        for(Object datum : Collections.list(this.getDataVector().elements())) {
            Vector rowVector = (Vector) datum;
            retValue.add(rowVector.get(0));
        }
        return Collections.unmodifiableList(retValue); //should be unmodifiable in order to avoid callers thinking that
        //changes to data is written back
    }

    /**
     * A helper to deal with casts during access to dataVector and missing rows.
     */
    public void setRowValue(int row, T value) {
        if(this.getRowCount() <= row) {
            this.addRow(new Object[]{value});
        }else {
            ((Vector)this.getDataVector().get(row)).set(0, value);
        }
    }

    /**
     * A helper to deal with casts during access to dataVector which avoids
     * transformation of the complete model like in {@link #getData() }.
     * @param row
     * @return
     */
    public Object getRowValue(int row) {
        Object retValue = ((Vector)this.getDataVector().get(row)).get(0);
        return retValue;
    }

}
