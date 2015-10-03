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
import javax.swing.JComponent;

/**
 *
 * @author richter
 * @param <T> the type of the managed field
 * @param <E> the type of event emitted on field updates
 */
/*
internal implementation notes:
- Don't pass Field argument to handle in order to allow inspection of
annotations, etc. -> let the caller retrieve information and enforce them as
parameter or consider introducing a variable constraint type as argument. This
allows the FieldHandler to be used not only for fields, but nested generic types
as well.
- since JComponents don't expose changes to their value to
PropertyChangeListener or VetoableChangeListener (both only for swing related
properties) introduce FieldUpdateListener in order to allow callers to specify a
listener. This callback will be called in a listener in a listerner which the
FieldHandler registers for each component individually.
- A generic field handler for lists can't handle immutable types because
changes to items can't be -> it is inevitable to provide custom field handler
for immutable types (eventually it's possible to provide generic immutable type
field handler)
*/
public interface FieldHandler<T, E extends FieldUpdateEvent<T>> {

    /**
     *
     * @param fieldType the {@link Type} of the field or the nested generic type
     * @param fieldValue the value of the field or the nested generic property
     * @param updateListener an {@link FieldUpdateListener} to notify on updates
     * @param reflectionFormBuilder a {@link ReflectionFormBuilder} used for
     * recursion
     * @return
     * @throws java.lang.IllegalAccessException
     */
    JComponent handle(Type fieldType,
            T fieldValue,
            FieldUpdateListener<E> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException;
}
