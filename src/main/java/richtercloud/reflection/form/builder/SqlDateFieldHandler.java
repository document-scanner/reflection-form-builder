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
import richtercloud.reflection.form.builder.components.SqlDatePicker;

/**
 *
 * @author richter
 */
public class SqlDateFieldHandler implements FieldHandler {
    private final static SqlDateFieldHandler INSTANCE = new SqlDateFieldHandler();

    public static SqlDateFieldHandler getInstance() {
        return INSTANCE;
    }

    protected SqlDateFieldHandler() {
    }

    @Override
    public JComponent handle(Type type, ReflectionFormBuilder reflectionFormBuilder) {
        return new SqlDatePicker();
    }

}
