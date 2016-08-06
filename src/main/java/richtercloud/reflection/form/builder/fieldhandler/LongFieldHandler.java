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
import richtercloud.reflection.form.builder.panels.LongPanel;
import richtercloud.reflection.form.builder.typehandler.LongTypeHandler;

/**
 *
 * @author richter
 */
public class LongFieldHandler implements FieldHandler<Long, FieldUpdateEvent<Long>, ReflectionFormBuilder, LongPanel> {
    private final static LongFieldHandler INSTANCE = new LongFieldHandler();

    public static LongFieldHandler getInstance() {
        return INSTANCE;
    }
    private final LongTypeHandler longTypeHandler;

    protected LongFieldHandler() {
        this(LongTypeHandler.getInstance());
    }

    public LongFieldHandler(LongTypeHandler longTypeHandler) {
        this.longTypeHandler = longTypeHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Long>> updateListener, ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        Type fieldType = field.getType();
        Long fieldValue = (Long) field.get(instance);
        return this.longTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder).getKey();
    }

    @Override
    public void reset(LongPanel component) {
        component.reset();
    }
}
