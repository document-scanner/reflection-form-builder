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

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.ComponentResettable;

/**
 * This interface specifies information which allow you to write custom field handlers. Due to the fact that {@link #handle(java.lang.reflect.Field, java.lang.Object, java.lang.String, java.lang.Class, richtercloud.reflection.form.builder.FieldUpdateListener, richtercloud.reflection.form.builder.ReflectionFormBuilder) } provides information about the handled field you can retrieve information as good and as easily as the Java Reflection API allows you.
 *
 * @author richter
 * @param <T> the type of the managed field
 * @param <E> the type of event emitted on field updates
 */
/*
internal implementation notes:
- About passing the handled java.lang.reflect.Field, the java.lang.reflect.Type
or both as argument to handle:
In order define stable interfaces in ReflectionFormBuilder and FieldHandler and
be able to reuse code for both fields and nested generic types it'd be
necessary to pass both a Field and a Type argument to handle (the latter would
serve to indicate the state in a possible recursion, e.g. if there was
List<Set<LinkedList<String>>> field and handle would be invoked for type
Set<LinkedList<String>> after being invoked for the field of type
List<Set<LinkedList<String>>>, the field would provide the information about
the root (= field) type and a type argument would indicate the state of the
recursion).
Yet, this tries to solve one problems which ought to be solved in two different
classes -> since field handler do handle types it makes sense to introduce a
type handler and reuse it's function in field handlers which can then recurse or iterate or perform arbitrary test.

Important note: The exposue of Field in handle evicts the need for any other handlers (e.g. for annotations) or mappings (e.g. for primitives, is separate to ensure type safety only, anyway) and precedences in ReflectionFormBuilder
-> Everything could be handled by one FieldHandler (this is better than letting ReflectionFormBuilder choose from multiple FieldHandlers because then it would handle field which should be the task of FieldHandler - also because ReflectionFormBuilder already builds the panel)
Old classes for class, field annotation and class annotation mapping can be preserved an used in an implementation of FieldHandler (for library users)

- since JComponents don't expose changes to their value to
PropertyChangeListener or VetoableChangeListener (both only for swing related
properties) introduce FieldUpdateListener in order to allow callers to specify
a listener. This callback will be called in a listener in a listerner which the
FieldHandler registers for each component individually.
- A generic field handler for lists can't handle immutable types because
changes to items can't be -> it is inevitable to provide custom field handler
for immutable types (eventually it's possible to provide generic immutable type
field handler)
- reset implementation notes:
  - The reset funtionality has to be placed on the
JComponent level in order to be able to write values on JComponents (otherwise there's no way to retrieve the component in the TypeHandler; other approaches include

  - ; FIXME event handler
*/
public interface FieldHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder, C extends Component> extends ComponentResettable<C> {

    /**
     * Information about the field value can be retrieved with {@link Field#get(java.lang.Object) } and {@code instance}, about the field name with {@link Field#getName() } and about the declaring class with {@link Field#getDeclaringClass() }.
     *
     * @param field the {@link Field} to be handled
     * @param instance
     * @param updateListener an {@link FieldUpdateListener} to notify on updates
     * @param reflectionFormBuilder a {@link ReflectionFormBuilder} used for
     * recursion
     * @return
     * @throws richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException
     * @throws java.lang.IllegalAccessException
     */
    /*
    internal implementation notes:
    - should pass java.lang.reflect.Field and the containing instance (don't
    call it entity because the base implementation works also without JPA) in
    order to provide a maximum of information which is also necessary, e.g. in
    order to do cross-field evaluation, e.g. in LongIdPanel.
    */
    JComponent handle(Field field,
            Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException,
            FieldHandlingException;
}
