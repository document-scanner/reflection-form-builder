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
package richtercloud.reflection.form.builder.retriever;

import javax.swing.JComponent;

/**
 * Due to the fact that the {@code JComponent} inheritance hierarchy can't be
 * changed in order to provide a unique method to access value. That's why this
 * interface is introduced.
 * @author richter
 * @param <T>
 * @param <C>
 */
public interface ValueRetriever<T, C extends JComponent> {

    T retrieve(C comp);
}
