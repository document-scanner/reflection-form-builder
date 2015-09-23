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

/**
 *
 * @author richter
 * @param <T> the type of the field value to be set after an update
 */
public interface UpdateEvent<T> {

    /**
     * The new value to be to the field. The field is most likely only known
     * by the creator of listeners working with this event type.
     * @return
     */
    T getNewValue();
}
