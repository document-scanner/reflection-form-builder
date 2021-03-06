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
import de.richtercloud.reflection.form.builder.ResetException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import de.richtercloud.reflection.form.builder.panels.AbstractListPanel;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 * @param <T> the type to handle
 * @param <E> the field update event
 * @param <R> the reflection for builder to use
 */
public abstract class AbstractListTypeHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder> implements TypeHandler<T, E,R, AbstractListPanel> {
    private final IssueHandler issueHandler;

    public AbstractListTypeHandler(IssueHandler issueHandler) {
        this.issueHandler = issueHandler;
    }

    public IssueHandler getIssueHandler() {
        return issueHandler;
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle(Type type,
            T fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException {
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if(!parameterizedType.getRawType().equals(List.class)) {
                throw new IllegalArgumentException(String.format("list field handlers are only allowed to be used with types with raw type %s (type is %s)", List.class, type));
            }
        }
        return handle0(type, fieldValue, updateListener, reflectionFormBuilder);
    }

    protected abstract Pair<JComponent, ComponentHandler<?>> handle0(Type type,
            T fieldValue,
            FieldUpdateListener<E> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException,
            ResetException;

    @Override
    public void reset(AbstractListPanel component) {
        component.reset();
    }
}
