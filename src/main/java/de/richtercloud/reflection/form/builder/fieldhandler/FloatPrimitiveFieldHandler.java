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
public class FloatPrimitiveFieldHandler implements FieldHandler<Float, FieldUpdateEvent<Float>, ReflectionFormBuilder, JSpinner> {
    private final static FloatPrimitiveFieldHandler INSTANCE = new FloatPrimitiveFieldHandler();

    public static FloatPrimitiveFieldHandler getInstance() {
        return INSTANCE;
    }

    protected FloatPrimitiveFieldHandler() {
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Float>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        float fieldValue;
        try {
            fieldValue = field.getFloat(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        final JSpinner retValue = new JSpinner(new SpinnerNumberModel((Float)fieldValue,
                (Float) (-10.0f),
                (Float) 10.0f,
                (Float) 0.1f));//the cast to Float is necessary otherwise Doubles are retrieved from component later
        retValue.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateListener.onUpdate(new FieldUpdateEvent<>((Float) retValue.getValue()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(JSpinner component) {
        component.setValue(0);
    }
}
