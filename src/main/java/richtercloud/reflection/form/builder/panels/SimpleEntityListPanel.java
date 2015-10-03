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

import java.util.List;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 *
 * @author richter
 * @param <T> the type of the managed instance
 */
public class SimpleEntityListPanel<T> extends AbstractSingleColumnListPanel<T, ListPanelItemListener<T>, SingleColumnListPanelTableModel<T>>{
    private static final long serialVersionUID = 1L;

    public SimpleEntityListPanel(ReflectionFormBuilder reflectionFormBuilder,
            List<T> initialValues,
            Class<? extends T> entityClass) {
        this(reflectionFormBuilder,
                initialValues,
                entityClass,
                new SimpleEntityListPanelCellEditor(),
                new SimpleEntityListPanelCellRenderer());
    }

    public SimpleEntityListPanel(ReflectionFormBuilder reflectionFormBuilder,
            List<T> initialValues,
            Class<? extends T> entityClass,
            ListPanelTableCellEditor mainListCellEditor,
            ListPanelTableCellRenderer mainListCellRenderer) {
        super(reflectionFormBuilder,
                mainListCellEditor,
                mainListCellRenderer,
                AbstractSingleColumnListPanel.<T>createMainListModel(entityClass));
        if(initialValues != null) {
            for(T initialValue : initialValues) {
                this.getMainListModel().setValueAt(initialValue, this.getMainListModel().getRowCount(), 0);
            }
        }
    }

    /**
     * returns {@code null} always in order to avoid persisting failures which
     * would occur if empty instances would be created.
     * @return
     */
    @Override
    protected T createNewElement() {
        return null;
    }

}
