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
import de.richtercloud.reflection.form.builder.panels.ListPanelItemEvent;
import de.richtercloud.reflection.form.builder.panels.SimpleEntityListPanel;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richter
 */
public class SimpleEntityListTypeHandler extends AbstractListTypeHandler<List<Object>, FieldUpdateEvent<List<Object>>,ReflectionFormBuilder> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEntityListTypeHandler.class);

    public SimpleEntityListTypeHandler(IssueHandler issueHandler) {
        super(issueHandler);
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle0(Type type,
            List<Object> fieldValue,
            final FieldUpdateListener<FieldUpdateEvent<List<Object>>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        LOGGER.debug("handling type {}", type);
        //don't assert that type is instanceof ParameterizedType because a
        //simple List can be treated as List<Object>
        Class<? extends Object> entityClass;
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if(parameterizedType.getActualTypeArguments().length == 0) {
                //can happen according to docs
                entityClass = Object.class;
            }else if(parameterizedType.getActualTypeArguments().length > 1) {
                throw new IllegalArgumentException(String.format("can't handle more than one type argument with a %s (type is %s)", List.class, type));
            }
            Type listGenericType = parameterizedType.getActualTypeArguments()[0];
            if(!(listGenericType instanceof Class)) {
                throw new IllegalArgumentException(String.format("first type argument of type %s isn't an instance of %s", type, Class.class));
            }
            entityClass = (Class<?>) listGenericType;
        }else {
            entityClass = Object.class;
        }
        final SimpleEntityListPanel<Object> retValue = new SimpleEntityListPanel<>(reflectionFormBuilder,
                fieldValue,
                entityClass,
                getIssueHandler());
        retValue.addItemListener(new EditableListPanelItemListener<Object>() {

            @Override
            public void onItemAdded(ListPanelItemEvent<Object> event) {
                updateListener.onUpdate(new FieldUpdateEvent<List<Object>>(new LinkedList<>(event.getItem())));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent<Object> event) {
                updateListener.onUpdate(new FieldUpdateEvent<List<Object>>(new LinkedList<>(event.getItem())));
            }

            @Override
            public void onItemChanged(ListPanelItemEvent<Object> event) {
                updateListener.onUpdate(new FieldUpdateEvent<List<Object>>(new LinkedList<>(event.getItem())));
            }
        });
        return new ImmutablePair<JComponent, ComponentHandler<?>>(retValue, this);
    }
}
