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
package richtercloud.reflection.form.builder;

import richtercloud.reflection.form.builder.panels.ListPanelItemEvent;
import richtercloud.reflection.form.builder.panels.ListPanelItemListener;
import java.lang.reflect.Type;
import java.util.List;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.panels.BooleanListPanel;
import richtercloud.reflection.form.builder.panels.EditableListPanelItemListener;

/**
 *
 * @author richter
 */
public class BooleanListFieldHandler extends AbstractListFieldHandler<List<Boolean>, BooleanListFieldUpdateEvent> implements FieldHandler<List<Boolean>, BooleanListFieldUpdateEvent>{
    private final static BooleanListFieldHandler INSTANCE = new BooleanListFieldHandler();

    public static BooleanListFieldHandler getInstance() {
        return INSTANCE;
    }

    protected BooleanListFieldHandler() {
    }

    @Override
    public JComponent handle0(Type type,
            List<Boolean> fieldValue,
            final FieldUpdateListener<BooleanListFieldUpdateEvent> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        BooleanListPanel retValue = new BooleanListPanel(reflectionFormBuilder, fieldValue);
        retValue.addItemListener(new EditableListPanelItemListener<Boolean>() {

            @Override
            public void onItemChanged(ListPanelItemEvent<Boolean> event) {
                updateListener.onUpdate(new BooleanListFieldUpdateEvent(event.getItem()));
            }

            @Override
            public void onItemAdded(ListPanelItemEvent<Boolean> event) {
                updateListener.onUpdate(new BooleanListFieldUpdateEvent(event.getItem()));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent<Boolean> event) {
                updateListener.onUpdate(new BooleanListFieldUpdateEvent(event.getItem()));
            }
        });
        return retValue;
    }

}
