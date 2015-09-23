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
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import richtercloud.reflection.form.builder.panels.IntegerListPanel;
import richtercloud.reflection.form.builder.panels.ListPanelItemEvent;
import richtercloud.reflection.form.builder.panels.ListPanelItemListener;
import richtercloud.reflection.form.builder.panels.StringListPanel;

/**
 *
 * @author richter
 */
public class StringListFieldHandler implements FieldHandler {
    private final static StringListFieldHandler INSTANCE = new StringListFieldHandler();

    public static StringListFieldHandler getInstance() {
        return INSTANCE;
    }

    protected StringListFieldHandler() {
    }

    @Override
    public JComponent handle(Type type, final UpdateListener updateListener, ReflectionFormBuilder reflectionFormBuilder) {
        StringListPanel retValue = new StringListPanel(reflectionFormBuilder);
        retValue.addItemListener(new ListPanelItemListener() {

            @Override
            public void onItemChanged(ListPanelItemEvent event) {
                updateListener.onUpdate(new GenericListFieldUpdateEvent(GenericListFieldUpdateEvent.EVENT_TYPE_CHANGED, event.getItem()));
            }

            @Override
            public void onItemAdded(ListPanelItemEvent event) {
                updateListener.onUpdate(new GenericListFieldUpdateEvent(GenericListFieldUpdateEvent.EVENT_TYPE_ADDED, event.getItem()));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent event) {
                updateListener.onUpdate(new GenericListFieldUpdateEvent(GenericListFieldUpdateEvent.EVENT_TYPE_REMOVED, event.getItem()));
            }
        });
        return retValue;
    }

}
