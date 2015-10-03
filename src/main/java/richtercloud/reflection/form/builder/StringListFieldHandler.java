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

import java.lang.reflect.Type;
import java.util.List;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.panels.EditableListPanelItemListener;
import richtercloud.reflection.form.builder.panels.ListPanelItemEvent;
import richtercloud.reflection.form.builder.panels.ListPanelItemListener;
import richtercloud.reflection.form.builder.panels.StringListPanel;

/**
 *
 * @author richter
 */
public class StringListFieldHandler implements FieldHandler<List<String>, StringListFieldUpdateEvent> {
    private final static StringListFieldHandler INSTANCE = new StringListFieldHandler();

    public static StringListFieldHandler getInstance() {
        return INSTANCE;
    }

    protected StringListFieldHandler() {
    }

    @Override
    public JComponent handle(Type type,
            List<String> fieldValue,
            final FieldUpdateListener<StringListFieldUpdateEvent> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        StringListPanel retValue = new StringListPanel(reflectionFormBuilder, fieldValue);
        retValue.addItemListener(new EditableListPanelItemListener<String>() {

            @Override
            public void onItemChanged(ListPanelItemEvent<String> event) {
                updateListener.onUpdate(new StringListFieldUpdateEvent(event.getItem()));
            }

            @Override
            public void onItemAdded(ListPanelItemEvent<String> event) {
                updateListener.onUpdate(new StringListFieldUpdateEvent(event.getItem()));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent<String> event) {
                updateListener.onUpdate(new StringListFieldUpdateEvent(event.getItem()));
            }
        });
        return retValue;
    }

}
