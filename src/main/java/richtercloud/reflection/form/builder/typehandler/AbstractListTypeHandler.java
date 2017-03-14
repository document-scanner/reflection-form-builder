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
package richtercloud.reflection.form.builder.typehandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.message.handler.IssueHandler;
import richtercloud.reflection.form.builder.ComponentHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import richtercloud.reflection.form.builder.panels.AbstractListPanel;

/**
 *
 * @author richter
 */
public abstract class AbstractListTypeHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder> implements TypeHandler<T, E,R, AbstractListPanel> {
    private IssueHandler issueHandler;

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
            R reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if(!parameterizedType.getRawType().equals(List.class)) {
                throw new IllegalArgumentException(String.format("list field handlers are only allowed to be used with types with raw type %s (type is %s)", List.class, type));
            }
        }
        Pair<JComponent, ComponentHandler<?>> retValue = handle0(type, fieldValue, updateListener, reflectionFormBuilder);
        return retValue;
    }

    protected abstract Pair<JComponent, ComponentHandler<?>> handle0(Type type,
            T fieldValue,
            FieldUpdateListener<E> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException;

    @Override
    public void reset(AbstractListPanel component) {
        component.reset();
    }
}
