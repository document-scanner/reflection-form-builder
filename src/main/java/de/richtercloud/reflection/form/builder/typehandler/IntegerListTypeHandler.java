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
package de.richtercloud.reflection.form.builder.typehandler;

import de.richtercloud.message.handler.IssueHandler;
import de.richtercloud.reflection.form.builder.ComponentHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import de.richtercloud.reflection.form.builder.panels.EditableListPanelItemListener;
import de.richtercloud.reflection.form.builder.panels.IntegerListPanel;
import de.richtercloud.reflection.form.builder.panels.ListPanelItemEvent;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 */
public class IntegerListTypeHandler extends AbstractListTypeHandler<List<Integer>, FieldUpdateEvent<List<Integer>>,ReflectionFormBuilder> {

    public IntegerListTypeHandler(IssueHandler issueHandler) {
        super(issueHandler);
    }

    @Override
    protected Pair<JComponent, ComponentHandler<?>> handle0(Type type,
            List<Integer> fieldValue,
            final FieldUpdateListener<FieldUpdateEvent<List<Integer>>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        IntegerListPanel retValue = new IntegerListPanel(reflectionFormBuilder,
                fieldValue,
                getIssueHandler());
        retValue.addItemListener(new EditableListPanelItemListener<Integer>() {

            @Override
            public void onItemChanged(ListPanelItemEvent<Integer> event) {
                updateListener.onUpdate(new FieldUpdateEvent<List<Integer>>(new LinkedList<>(event.getItem())));
            }

            @Override
            public void onItemAdded(ListPanelItemEvent<Integer> event) {
                updateListener.onUpdate(new FieldUpdateEvent<List<Integer>>(new LinkedList<>(event.getItem())));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent<Integer> event) {
                updateListener.onUpdate(new FieldUpdateEvent<List<Integer>>(new LinkedList<>(event.getItem())));
            }
        });
        return new ImmutablePair<JComponent, ComponentHandler<?>>(retValue, this);
    }
}
