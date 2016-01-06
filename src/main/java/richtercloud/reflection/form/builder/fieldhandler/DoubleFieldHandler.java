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
import java.lang.reflect.Type;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.typehandler.DoubleTypeHandler;

/**
 *
 * @author richter
 */
public class DoubleFieldHandler implements FieldHandler<Double, FieldUpdateEvent<Double>, ReflectionFormBuilder> {
    private final static DoubleFieldHandler INSTANCE = new DoubleFieldHandler();

    public static DoubleFieldHandler getInstance() {
        return INSTANCE;
    }
    private final DoubleTypeHandler doubleTypeHandler;

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
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        Type fieldType = field.getType();
        Double fieldValue = (Double) field.get(instance);
        return this.doubleTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder);
    }

}
