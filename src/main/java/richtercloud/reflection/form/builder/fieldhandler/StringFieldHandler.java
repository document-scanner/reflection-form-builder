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
import javax.swing.JTextField;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.typehandler.StringTypeHandler;

/**
 *
 * @author richter
 */
public class StringFieldHandler implements FieldHandler<String, FieldUpdateEvent<String>, ReflectionFormBuilder, JTextField> {
    private final static StringFieldHandler INSTANCE = new StringFieldHandler();

    public static StringFieldHandler getInstance() {
        return INSTANCE;
    }
    private final StringTypeHandler stringTypeHandler;

    protected StringFieldHandler() {
        this(StringTypeHandler.getInstance());
    }

    public StringFieldHandler(StringTypeHandler stringTypeHandler) {
        this.stringTypeHandler = stringTypeHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<String>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        Type fieldType = field.getType();
        String fieldValue = (String) field.get(instance);
        return this.stringTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder).getKey();
    }

    @Override
    public void reset(JTextField component) {
        component.setText("");
    }
}
