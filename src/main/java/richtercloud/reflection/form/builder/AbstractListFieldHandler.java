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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public abstract class AbstractListFieldHandler<T, E extends FieldUpdateEvent<T>> implements FieldHandler<T, E> {

    @Override
    public JComponent handle(Type type,
            T fieldValue,
            FieldUpdateListener<E> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException {
        if(type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            if(!parameterizedType.getRawType().equals(List.class)) {
                throw new IllegalArgumentException(String.format("list field handlers are only allowed to be used with types with raw type %s (type is %s)", List.class, type));
            }
        }
        JComponent retValue = handle0(type, fieldValue, updateListener, reflectionFormBuilder);
        return retValue;
    }

    protected abstract JComponent handle0(Type type,
            T fieldValue,
            FieldUpdateListener<E> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException;
}
