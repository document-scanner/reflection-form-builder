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

import richtercloud.reflection.form.builder.panels.SimpleEntityListPanel;
import richtercloud.reflection.form.builder.panels.ListPanelItemEvent;
import richtercloud.reflection.form.builder.panels.ListPanelItemListener;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.panels.AbstractListPanel;

/**
 *
 * @author richter
 */
public class SimpleEntityListFieldHandler extends AbstractListFieldHandler<GenericListFieldUpdateEvent> implements FieldHandler<GenericListFieldUpdateEvent> {
    private final static SimpleEntityListFieldHandler INSTANCE = new SimpleEntityListFieldHandler();
    private final static Logger LOGGER = LoggerFactory.getLogger(SimpleEntityListFieldHandler.class);

    public static SimpleEntityListFieldHandler getInstance() {
        return INSTANCE;
    }

    protected SimpleEntityListFieldHandler() {
    }

    @Override
    public JComponent handle0(Type type, final UpdateListener<GenericListFieldUpdateEvent> updateListener, ReflectionFormBuilder reflectionFormBuilder) {
        LOGGER.debug("handling type {}", type);
        //don't assert that type is instanceof ParameterizedType because a
        //simple List can be treated as List<Object>
        Class<?> entityClass;
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
        final AbstractListPanel retValue = new SimpleEntityListPanel(entityClass, reflectionFormBuilder);
        retValue.addItemListener(new ListPanelItemListener() {

            @Override
            public void onItemAdded(ListPanelItemEvent event) {
                updateListener.onUpdate(new GenericListFieldUpdateEvent(GenericListFieldUpdateEvent.EVENT_TYPE_ADDED, //removed
                        event.getItem()));
            }

            @Override
            public void onItemRemoved(ListPanelItemEvent event) {
                updateListener.onUpdate(new GenericListFieldUpdateEvent(GenericListFieldUpdateEvent.EVENT_TYPE_REMOVED, //removed
                        event.getItem()));
            }

            @Override
            public void onItemChanged(ListPanelItemEvent event) {
                updateListener.onUpdate(new GenericListFieldUpdateEvent(GenericListFieldUpdateEvent.EVENT_TYPE_CHANGED, event.getItem()));
            }
        });
        return retValue;
    }

}

