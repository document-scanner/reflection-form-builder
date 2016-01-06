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
package richtercloud.reflection.form.builder.typehandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.sql.Date;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.components.SqlDatePicker;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;

/**
 *
 * @author richter
 */
public class SqlDateTypeHandler implements TypeHandler<Date, FieldUpdateEvent<Date>,ReflectionFormBuilder> {

    private final static SqlDateTypeHandler INSTANCE = new SqlDateTypeHandler();

    public static SqlDateTypeHandler getInstance() {
        return INSTANCE;
    }

    protected SqlDateTypeHandler() {
    }

    @Override
    public JComponent handle(Type type,
            Date fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Date>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException {
        final SqlDatePicker retValue = new SqlDatePicker(fieldValue);
        retValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //e.getSource points to a org.jdatepicker.JUtilDatePanel (which isn't 100 % correct given the fact that the action listener is added to a JDatePicker)
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.getModel().getValue()));
            }
        });
        return retValue;
    }
}
