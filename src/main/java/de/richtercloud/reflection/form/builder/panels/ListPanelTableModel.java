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

import java.util.List;
import javax.swing.table.TableModel;

/**
 * Handles value queries and inserts for tables which don't care whether their
 * values are displayed in one or multiple columns (it's up to subclasses to
 * decide).This allows code reusage in {@link AbstractListPanel}.
 *
 * @author richter
 * @param <T> the type of elements to handle
 */
public interface ListPanelTableModel<T> extends TableModel {

    void fireTableDataChanged();

    void removeElement(int row);

    void addElement(T element);

    void insertElementAt(int row, T element);

    void removeElement(T element);

    /**
     * A read-only view of the managed data. Changes are enforced through
     * {@link #addElement(java.lang.Object) },
     * {@link #removeElement(java.lang.Object) }, {@link #removeElement(int) }
     * and {@link #insertElementAt(int, java.lang.Object) } in order to be able
     * to track changes.
     * @return a read-only view of the managed data
     */
    List<T> getData();

    void addColumn(String columnName);

}
