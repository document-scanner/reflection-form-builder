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
package richtercloud.reflection.form.builder.fieldhandler;

import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 *
 * @author richter
 */
public class IntegerPrimitiveFieldHandler implements FieldHandler<Integer, FieldUpdateEvent<Integer>, ReflectionFormBuilder> {
    private final static IntegerPrimitiveFieldHandler INSTANCE = new IntegerPrimitiveFieldHandler();

    public static IntegerPrimitiveFieldHandler getInstance() {
        return INSTANCE;
    }

    protected IntegerPrimitiveFieldHandler() {
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Integer>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        int fieldValue = field.getInt(instance);
        JSpinner retValue = new JSpinner(new SpinnerNumberModel((int) fieldValue, Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        retValue.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateListener.onUpdate(new FieldUpdateEvent<>((Integer) ((JSpinner)e.getSource()).getValue()));
            }
        });
        return retValue;
    }

}
