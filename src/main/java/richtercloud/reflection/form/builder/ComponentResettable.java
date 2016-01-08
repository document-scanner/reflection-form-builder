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
package richtercloud.reflection.form.builder;

import java.awt.Component;
import richtercloud.reflection.form.builder.fieldhandler.FieldAnnotationHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;

/**
 * A superclass for all handlers of class properties ({@link FieldHandler},
 * {@link FieldAnnotationHandler}, {@link ClassAnnotationHandler}, etc.) which
 * defines methods to reset a handled field (which all handlers should have in
 * common). Since the resetting is done on a component level this interface
 * can be used for both field handlers and handlers a field handler might
 * delegate to.
 *
 * @author richter
 */
public interface ComponentResettable<C extends Component> {

    void reset(C component);
}
