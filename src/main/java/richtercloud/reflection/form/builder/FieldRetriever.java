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

import java.lang.reflect.Field;
import java.util.List;

/**
 * Interface to share code of field retrieval in a class hierarchy. Implementations
 * might implement caching and such.
 *
 * @author richter
 */
/*
internal implementation notes:
- this interface might appear unnecessary, but it allows very elegant code
sharing with ReflectionFormPanel and follows "composition over inheritance"
*/
public interface FieldRetriever {

    /**
     * Retrieves relevant fields. What that means in up to the implementation.
     * @param clazz
     * @return the list of relevant field as specified by implementation
     */
    /*
     internal implementation notes:
     - return a List in order to be able to modify order (it's nice to have
     @Id annotated property first)
     */
    List<Field> retrieveRelevantFields(Class<?> clazz);
}
