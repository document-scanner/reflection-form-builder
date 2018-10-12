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

import de.richtercloud.message.handler.MessageHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import java.util.List;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 *
 * @author richter
 */
public class IntegerListPanel extends AbstractSingleColumnListPanel<Integer, EditableListPanelItemListener<Integer>, SingleColumnListPanelTableModel<Integer>, ReflectionFormBuilder> {
    private static final long serialVersionUID = 1L;

    public IntegerListPanel(ReflectionFormBuilder reflectionFormBuilder,
            List<Integer> initialValues,
            MessageHandler messageHandler) {
        super(reflectionFormBuilder,
                new IntegerListPanelCellEditor(),
                new IntegerListPanelCellRenderer(),
                AbstractSingleColumnListPanel.<Integer>createMainListModel(Integer.class),
                initialValues,
                messageHandler);
        getMainListCellEditor().addCellEditorListener(new CellEditorListener() {

            @Override
            public void editingStopped(ChangeEvent e) {
                int row = IntegerListPanel.this.getMainList().getSelectedRow();
                if(row > -1) {
                    for(EditableListPanelItemListener<Integer> itemListener : IntegerListPanel.this.getItemListeners()) {
                        itemListener.onItemChanged(new ListPanelItemEvent<>(ListPanelItemEvent.EVENT_TYPE_CHANGED,
                                row,
                                IntegerListPanel.this.getMainListModel().getData()));
                    }
                }
            }

            @Override
            public void editingCanceled(ChangeEvent e) {
                //do nothing because there're no changes to the model
            }
        });
        reset();
    }

    @Override
    protected Integer createNewElement() {
        return 0;
    }

}
