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

import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 *
 * @author richter
 */
public class IntegerListPanel extends AbstractListPanel<Integer> {

    private static final long serialVersionUID = 1L;

    protected IntegerListPanel() {
    }

    public IntegerListPanel(ReflectionFormBuilder reflectionFormBuilder) {
        super(reflectionFormBuilder, new IntegerListPanelCellEditor(), new IntegerListPanelCellRenderer());
        getMainListCellEditor().addCellEditorListener(new CellEditorListener() {

            @Override
            public void editingStopped(ChangeEvent e) {
                int index = IntegerListPanel.this.getMainList().getSelectedRow();
                if(index > -1) {
                    IntegerListPanel.this.getMainListModel().setRowValue(index, ((IntegerListPanelCellEditor) e.getSource()).getCellEditorValue());
                    for(ListPanelItemListener itemListener : IntegerListPanel.this.getItemListeners()) {
                        itemListener.onItemChanged(new ListPanelItemEvent(ListPanelItemEvent.EVENT_TYPE_CHANGED, index, IntegerListPanel.this.getMainListModel().getData()));
                    }
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                //do nothing because there're no changes to the model
            }
        });
    }

    @Override
    protected Integer createNewElement() {
        return 0;
    }

}
