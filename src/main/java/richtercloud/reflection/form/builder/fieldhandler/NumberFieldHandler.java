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
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.ComponentHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.panels.NumberPanel;
import richtercloud.reflection.form.builder.typehandler.NumberTypeHandler;

/**
 *
 * @author richter
 */
public class NumberFieldHandler extends ResettableFieldHandler<Number, FieldUpdateEvent<Number>, ReflectionFormBuilder, NumberPanel> {
    private final static NumberFieldHandler INSTANCE = new NumberFieldHandler();

    public static NumberFieldHandler getInstance() {
        return INSTANCE;
    }
    private final NumberTypeHandler numberTypeHandler;

    protected NumberFieldHandler() {
        this(NumberTypeHandler.getInstance());
    }

    public NumberFieldHandler(NumberTypeHandler numberTypeHandler) {
        this.numberTypeHandler = numberTypeHandler;
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle0(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Number>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        Type fieldType = field.getType();
        Number fieldValue = (Number) field.get(instance);
        return this.numberTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder);
    }

    @Override
    public void reset(NumberPanel component) {
        component.reset();
    }
}
