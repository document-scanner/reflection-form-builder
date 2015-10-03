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
import richtercloud.reflection.form.builder.panels.EditableListPanelItemListener;
import richtercloud.reflection.form.builder.panels.IntegerListPanel;

/**
 *
 * @author richter
 */
public class IntegerListFieldHandler implements FieldHandler<List<Integer>, IntegerListFieldUpdateEvent>{
    private final static IntegerListFieldHandler INSTANCE = new IntegerListFieldHandler();

    public static IntegerListFieldHandler getInstance() {
        return INSTANCE;
    }

    protected IntegerListFieldHandler() {
    }

    @Override
    public JComponent handle(Type type,
            List<Integer> fieldValue,
            final FieldUpdateListener<IntegerListFieldUpdateEvent> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        IntegerListPanel retValue = new IntegerListPanel(reflectionFormBuilder, fieldValue);
        retValue.addItemListener(new EditableListPanelItemListener<Integer>() {

            @Override
            public void onItemChanged(ListPanelItemEvent<Integer> event) {
                updateListener.onUpdate(new IntegerListFieldUpdateEvent(event.getItem()));
            }

            @Override
            public void onItemAdded(ListPanelItemEvent<Integer> event) {
                updateListener.onUpdate(new IntegerListFieldUpdateEvent(event.getItem()));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent<Integer> event) {
                updateListener.onUpdate(new IntegerListFieldUpdateEvent(event.getItem()));
            }
        });
        return retValue;
    }

}
