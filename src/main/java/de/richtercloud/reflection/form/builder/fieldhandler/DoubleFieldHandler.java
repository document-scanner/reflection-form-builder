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
import de.richtercloud.reflection.form.builder.panels.DoublePanel;
import de.richtercloud.reflection.form.builder.typehandler.DoubleTypeHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public class DoubleFieldHandler implements FieldHandler<Double, FieldUpdateEvent<Double>, ReflectionFormBuilder, DoublePanel> {
    private final static DoubleFieldHandler INSTANCE = new DoubleFieldHandler();
    private final DoubleTypeHandler doubleTypeHandler;

    public static DoubleFieldHandler getInstance() {
        return INSTANCE;
    }

    protected DoubleFieldHandler() {
        this(DoubleTypeHandler.getInstance());
    }

    public DoubleFieldHandler(DoubleTypeHandler doubleTypeHandler) {
        this.doubleTypeHandler = doubleTypeHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Double>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        Double fieldValue;
        try {
            fieldValue = (Double) field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        Type fieldType = field.getType();
        return this.doubleTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder).getKey();
    }

    @Override
    public void reset(DoublePanel component) {
        component.reset();
    }
}
