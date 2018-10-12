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
package de.richtercloud.reflection.form.builder.fieldhandler;

import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import java.lang.reflect.Field;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author richter
 */
public class DoublePrimitiveFieldHandler implements FieldHandler<Double, FieldUpdateEvent<Double>, ReflectionFormBuilder, JSpinner> {
    private final static DoublePrimitiveFieldHandler INSTANCE = new DoublePrimitiveFieldHandler();

    public static DoublePrimitiveFieldHandler getInstance() {
        return INSTANCE;
    }

    protected DoublePrimitiveFieldHandler() {
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Double>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        double fieldValue;
        try {
            fieldValue = field.getDouble(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        JSpinner retValue = new JSpinner(new SpinnerNumberModel((double) fieldValue,
                Double.NEGATIVE_INFINITY, //minimum (Double.MIN_VALUE causes
                    //fieldValue to be lesser than minimum because
                    //Double.MIN_VALUE is > 0.0
                Double.POSITIVE_INFINITY,
                0.1));
        retValue.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateListener.onUpdate(new FieldUpdateEvent<>((Double) ((JSpinner)e.getSource()).getValue()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(JSpinner component) {
        component.setValue(0);
    }
}
