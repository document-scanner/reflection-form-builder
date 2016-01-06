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

import javax.swing.JComponent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;

/**
 * Used to produce a {@link JComponent} if a field is encountered whose type is
 * annotated (on the class basis).
 * @author richter
 * @param <E> the type of the {@link FieldUpdateListener}
 */
public interface ClassAnnotationHandler<T, E extends FieldUpdateEvent<T>> {

    /**
     *
     * @param fieldClass the type of the field
     * @param fieldValue the value of the field
     * @param updateListener
     * @param reflectionFormBuilder
     * @return
     */
    /*
    internal implementation notes:
    - type Type instead of Class causes much less trouble, change only if it makes sense
    */
    JComponent handle(Class<? extends T> fieldClass,
            T fieldValue,
            FieldUpdateListener<E> updateListener,
            ReflectionFormBuilder reflectionFormBuilder);
}
