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
import de.richtercloud.reflection.form.builder.panels.IntegerPanel;
import de.richtercloud.reflection.form.builder.typehandler.IntegerTypeHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public class IntegerFieldHandler implements FieldHandler<Integer, FieldUpdateEvent<Integer>, ReflectionFormBuilder, IntegerPanel> {
    private final static IntegerFieldHandler INSTANCE = new IntegerFieldHandler();
    private final IntegerTypeHandler integerTypeHandler;

    public static IntegerFieldHandler getInstance() {
        return INSTANCE;
    }

    protected IntegerFieldHandler() {
        this(IntegerTypeHandler.getInstance());
    }

    public IntegerFieldHandler(IntegerTypeHandler integerTypeHandler) {
        this.integerTypeHandler = integerTypeHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Integer>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        Integer fieldValue;
        try {
            fieldValue = (Integer) field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        Type fieldType = field.getType();
        return this.integerTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder).getKey();
    }

    @Override
    public void reset(IntegerPanel component) {
        component.reset();
    }
}
