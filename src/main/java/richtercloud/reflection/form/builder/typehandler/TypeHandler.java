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

import java.awt.Component;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ComponentResettable;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;

/**
 *
 * @author richter
 * @param <T>
 * @param <E>
 */
/*
internal implementation notes:
- don't implement ClassPartHandler because it enforce the reset method, but a
TypeHandler isn't a ClassPartHandler and some ClassPartHandlers (like
FieldHandler delegate reset to TypeHandler)
*/
public interface TypeHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder, C extends Component> extends ComponentResettable<C> {

    /**
     *
     * @param type the {@link Type} to be handled
     * @param fieldValue the value of the field or the nested generic property
     * @param fieldName the name of the field (the same for nested generics)
     * @param declaringClass the class declaring the field
     * @param updateListener an {@link FieldUpdateListener} to notify on updates
     * @param reflectionFormBuilder a {@link ReflectionFormBuilder} used for
     * recursion
     * @return
     * @throws java.lang.IllegalAccessException
     * @throws richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException
     */
    JComponent handle(Type type,
            T fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException;

}
