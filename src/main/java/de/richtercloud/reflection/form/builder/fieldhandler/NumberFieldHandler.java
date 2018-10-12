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

import de.richtercloud.reflection.form.builder.ComponentHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import de.richtercloud.reflection.form.builder.panels.NumberPanel;
import de.richtercloud.reflection.form.builder.typehandler.NumberTypeHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 */
public class NumberFieldHandler extends ResettableFieldHandler<Number, FieldUpdateEvent<Number>, ReflectionFormBuilder, NumberPanel> {
    private final static NumberFieldHandler INSTANCE = new NumberFieldHandler();
    private final NumberTypeHandler numberTypeHandler;

    public static NumberFieldHandler getInstance() {
        return INSTANCE;
    }

    protected NumberFieldHandler() {
        this(NumberTypeHandler.getInstance());
    }

    public NumberFieldHandler(NumberTypeHandler numberTypeHandler) {
        super();
        this.numberTypeHandler = numberTypeHandler;
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle0(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Number>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        Number fieldValue;
        try {
            fieldValue = (Number) field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        Type fieldType = field.getType();
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
