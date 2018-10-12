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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public class BooleanListPanel extends AbstractSingleColumnListPanel<Boolean, EditableListPanelItemListener<Boolean>, SingleColumnListPanelTableModel<Boolean>, ReflectionFormBuilder> {
    private static final long serialVersionUID = 1L;

    public BooleanListPanel(ReflectionFormBuilder reflectionFormBuilder,
            List<Boolean> initialValues,
            MessageHandler messageHandler) {
        super(reflectionFormBuilder,
                new BooleanListPanelCellEditor(),
                new BooleanListPanelCellRenderer(),
                AbstractSingleColumnListPanel.<Boolean>createMainListModel(Boolean.class),
                initialValues,
                messageHandler);
        reset();
    }

    @Override
    protected Boolean createNewElement() {
        return Boolean.FALSE;
    }

    protected JComponent createComponentForModelValue(Object value) {
        final JCheckBox newElement = new JCheckBox("", (boolean) value);
        newElement.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                int row = BooleanListPanel.this.getMainList().getSelectedRow();
                if(row > -1) {
                    for(EditableListPanelItemListener<Boolean> listener : BooleanListPanel.this.getItemListeners()) {
                        listener.onItemChanged(new ListPanelItemEvent<>(ListPanelItemEvent.EVENT_TYPE_CHANGED,
                                row,
                                BooleanListPanel.this.getMainListModel().getData()));
                    }
                }
            }
        });
        return newElement;
    }

}
