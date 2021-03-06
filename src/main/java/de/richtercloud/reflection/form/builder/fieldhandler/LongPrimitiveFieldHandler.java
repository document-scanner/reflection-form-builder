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
public class LongPrimitiveFieldHandler implements FieldHandler<Long, FieldUpdateEvent<Long>, ReflectionFormBuilder, JSpinner> {
    private final static LongPrimitiveFieldHandler INSTANCE = new LongPrimitiveFieldHandler();

    public static LongPrimitiveFieldHandler getInstance() {
        return INSTANCE;
    }

    protected LongPrimitiveFieldHandler() {
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Long>> updateListener, ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        long fieldValue;
        try {
            fieldValue = field.getLong(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        final JSpinner retValue = new JSpinner(new SpinnerNumberModel((Long)fieldValue,
                (Long)Long.MIN_VALUE,
                (Long)Long.MAX_VALUE,
                (Long)1L));
        retValue.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateListener.onUpdate(new FieldUpdateEvent<>((Long) retValue.getValue()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(JSpinner component) {
        component.setValue(0);
    }
}
