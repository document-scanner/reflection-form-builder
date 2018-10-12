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
package de.richtercloud.reflection.form.builder.fieldhandler;

import de.richtercloud.message.handler.MessageHandler;
import de.richtercloud.reflection.form.builder.ComponentHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import de.richtercloud.reflection.form.builder.ResetException;
import de.richtercloud.reflection.form.builder.panels.AbstractListPanel;
import de.richtercloud.reflection.form.builder.typehandler.TypeHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 * @param <T> the type of field to handle
 * @param <E> the type of field update events
 * @param <R> the type of reflection form builder to use
 */
public abstract class AbstractListFieldHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder> implements FieldHandler<T, E, R, AbstractListPanel> {
    private final MessageHandler messageHandler;
    private final TypeHandler<T,E,R,AbstractListPanel> typeHandler;

    public AbstractListFieldHandler(MessageHandler messageHandler,
            TypeHandler<T,E,R,AbstractListPanel> typeHandler) {
        this.messageHandler = messageHandler;
        this.typeHandler = typeHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException {
        Type fieldGenericType = field.getGenericType();
        Pair<JComponent, ComponentHandler<?>> retValue;
        try {
            retValue = this.typeHandler.handle(fieldGenericType,
                    (T) field.get(instance), //fieldValue
                    field.getName(),
                    field.getDeclaringClass(), //declaringClass
                    updateListener,
                    reflectionFormBuilder);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        return retValue.getKey();
    }

    @Override
    public void reset(AbstractListPanel component) throws ResetException {
        this.typeHandler.reset(component);
    }
}
