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
 *
 * @author richter
 * @param <T> the type of the managed field
 * @param <E> the type of event emitted on field updates
 */
public interface FieldAnnotationHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder, C extends Component> extends ComponentResettable<C> {

    /**
     *
     * @param field
     * @param instance
     * @param updateListener
     * @param reflectionFormBuilder
     * @return
     * @throws richtercloud.reflection.form.builder.FieldHandlingException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.InstantiationException
     */
    /*
    internal implementation notes:
    - Type needs to be passed in order to be able to retrieve type of List
    (e.g. List<String> for simple nested Serializable type or nested Embeddable
    type (field annotion handler should handle both)
    - throw all exceptions since framework users might want to catch them
    separately (consider wrapping them in a FieldHandlerException which is fine
    if documented)
    */
    JComponent handle(Field field,
            Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            IllegalAccessException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException;
}
