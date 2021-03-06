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
package de.richtercloud.reflection.form.builder;

import java.awt.Component;

/*
internal implementation notes:
- start method could be in a separate interface, but it isn't much more elegant
to separate it.
*/
/**
 * A superclass for all handlers of class properties ({@link FieldHandler},
 * {@link FieldAnnotationHandler}, {@link ClassAnnotationHandler}, etc.) which
 * defines methods to reset a handled field (which all handlers should have in
 * common) and allows to start a component after it has been added to a
 * {@link Container}.
 *
 * Since the resetting is done on a component level this interface
 * can be used for both field handlers and handlers a field handler might
 * delegate to.
 *
 * @author richter
 * @param <C> the type of component to handle
 */
public interface ComponentHandler<C extends Component> {

    void reset(C component) throws ResetException;
}
