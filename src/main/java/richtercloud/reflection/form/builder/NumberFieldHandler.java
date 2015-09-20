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
import java.lang.reflect.Type;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author richter
 */
public class NumberFieldHandler implements FieldHandler {
    private final static NumberFieldHandler INSTANCE = new NumberFieldHandler();

    public static NumberFieldHandler getInstance() {
        return INSTANCE;
    }

    protected NumberFieldHandler() {
    }

    @Override
    public JComponent handle(Type type, ReflectionFormBuilder reflectionFormBuilder) {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        return new JSpinner(new SpinnerNumberModel(0, null, null, 1));
    }

}
