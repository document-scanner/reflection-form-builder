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
import de.richtercloud.reflection.form.builder.components.date.SqlDatePicker;
import de.richtercloud.reflection.form.builder.typehandler.SqlDateTypeHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Date;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public class SqlDateFieldHandler implements FieldHandler<Date,FieldUpdateEvent<Date>, ReflectionFormBuilder, SqlDatePicker> {
    private final static SqlDateFieldHandler INSTANCE = new SqlDateFieldHandler();
    private final SqlDateTypeHandler sqlDateTypeHandler;

    public static SqlDateFieldHandler getInstance() {
        return INSTANCE;
    }

    protected SqlDateFieldHandler() {
        this(SqlDateTypeHandler.getInstance());
    }

    public SqlDateFieldHandler(SqlDateTypeHandler sqlDateTypeHandler) {
        this.sqlDateTypeHandler = sqlDateTypeHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Date>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        Date fieldValue;
        try {
            fieldValue = (Date) field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldHandlingException(ex);
        }
        Type fieldType = field.getType();
        return this.sqlDateTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder).getKey();
    }

    @Override
    public void reset(SqlDatePicker component) {
        component.reset();
    }
}
