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
package richtercloud.reflection.form.builder.fieldhandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.message.MessageHandler;
import richtercloud.reflection.form.builder.panels.AbstractListPanel;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 *
 * @author richter
 */
public abstract class AbstractListFieldHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder> implements FieldHandler<T, E, R, AbstractListPanel> {
    private MessageHandler messageHandler;
    private TypeHandler<T,E,R,AbstractListPanel> typeHandler;

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
            R reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException, InstantiationException, InvocationTargetException {
        Type fieldGenericType = field.getGenericType();
        JComponent retValue = this.typeHandler.handle(fieldGenericType,
                (T) field.get(instance), //fieldValue
                field.getName(),
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder);
        return retValue;
    }

    @Override
    public void reset(AbstractListPanel component) {
        this.typeHandler.reset(component);
    }
}
