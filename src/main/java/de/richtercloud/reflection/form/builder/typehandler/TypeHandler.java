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

import de.richtercloud.reflection.form.builder.ComponentHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import de.richtercloud.reflection.form.builder.ResetException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import java.awt.Component;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;

/*
internal implementation notes:
- don't implement ClassPartHandler because it enforce the reset method, but a
TypeHandler isn't a ClassPartHandler and some ClassPartHandlers (like
FieldHandler delegate reset to TypeHandler)
*/
/**
 *
 * @author richter
 * @param <T> allows to enforce a field type
 * @param <E> the type of update event for the field
 * @param <R> the reflection form builder type to use
 * @param <C> the type of components to produce
 */
public interface TypeHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder, C extends Component> extends ComponentHandler<C> {

    /**
     *
     * @param type the {@link Type} to be handled
     * @param fieldValue the value of the field or the nested generic property
     * @param fieldName the name of the field (the same for nested generics)
     * @param declaringClass the class declaring the field
     * @param updateListener an {@link FieldUpdateListener} to notify on updates
     * @param reflectionFormBuilder a {@link ReflectionFormBuilder} used for
     *     recursion
     * @return a {@link Pair} of the handling result and the generating
     *     {@link ComponentResettable}. The latter reference can be used to
     *     easily keep track of the actual generating handler in a long line of
     *     delegations of handlers which avoids {@code ClassCastException}s at
     *     runtime as long as {@link ComponentResettable}s implemented correctly
     * @throws FieldHandlingException wraps all exceptions occuring during read
     *     or write access to fields
     * @throws ResetException if an exception during resetting the field occurs
     */
    /*
    internal implementation notes:
    - Since there's no need to store the component mapping (JComponent ->
    ComponentResettable) for types, but for fields (which are actually reset
    (recursively)) only, there's no need for a final handle and a handle0 method
    like in FieldHandler. Even if some callers like AbstractListFieldHandler
    don't need a reference to a ComponentResettable because they use their own
    JComponent subclasses which implement a reset method the information can
    still be provided by TypeHandler and be discarded in those FieldHandlers.
    */
    Pair<JComponent, ComponentHandler<?>> handle(Type type,
            T fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException;

}
