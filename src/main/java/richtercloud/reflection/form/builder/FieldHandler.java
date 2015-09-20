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
 */
/*
internal implementation notes:
- Don't pass Field argument to handle in order to allow inspection of
annotations, etc. -> let the caller retrieve information and enforce them as
parameter or consider introducing a variable constraint type as argument. This
allows the FieldHandler to be used not only for fields, but nested generic types
as well.
*/
public interface FieldHandler {

    /**
     *
     * @param type
     * @param reflectionFormBuilder a {@link ReflectionFormBuilder} used for
     * recursion
     * @return
     */
    JComponent handle(Type type, ReflectionFormBuilder reflectionFormBuilder);
}
