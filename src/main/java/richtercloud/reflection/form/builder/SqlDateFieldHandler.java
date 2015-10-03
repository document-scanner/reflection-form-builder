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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import java.sql.Date;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.components.SqlDatePicker;

/**
 *
 * @author richter
 */
public class SqlDateFieldHandler implements FieldHandler<java.sql.Date, SqlDateFieldUpdateEvent> {
    private final static SqlDateFieldHandler INSTANCE = new SqlDateFieldHandler();

    public static SqlDateFieldHandler getInstance() {
        return INSTANCE;
    }

    protected SqlDateFieldHandler() {
    }

    @Override
    public JComponent handle(Type type,
            Date fieldValue,
            final FieldUpdateListener<SqlDateFieldUpdateEvent> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        final SqlDatePicker retValue = new SqlDatePicker(fieldValue);
        retValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //e.getSource points to a org.jdatepicker.JUtilDatePanel (which isn't 100 % correct given the fact that the action listener is added to a JDatePicker)
                updateListener.onUpdate(new SqlDateFieldUpdateEvent(retValue.getModel().getValue()));
            }
        });
        return retValue;
    }

}
