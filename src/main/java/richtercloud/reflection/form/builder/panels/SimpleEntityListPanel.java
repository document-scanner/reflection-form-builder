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

import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 *
 * @author richter
 */
public class SimpleEntityListPanel<T> extends AbstractListPanel<T>{
    private static final long serialVersionUID = 1L;
    private Class<T> entityClass;

    protected SimpleEntityListPanel() {
    }

    public SimpleEntityListPanel(Class<T> entityClass, ReflectionFormBuilder reflectionFormBuilder) {
        this(entityClass, reflectionFormBuilder, new SimpleEntityListPanelCellEditor(), new SimpleEntityListPanelCellRenderer());
    }

    public SimpleEntityListPanel(Class<T> entityClass, ReflectionFormBuilder reflectionFormBuilder, ListPanelTableCellEditor mainListCellEditor, ListPanelTableCellRenderer mainListCellRenderer) {
        super(reflectionFormBuilder, mainListCellEditor, mainListCellRenderer);
        this.entityClass = entityClass;
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
