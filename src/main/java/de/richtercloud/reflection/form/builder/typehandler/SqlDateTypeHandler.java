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
package de.richtercloud.reflection.form.builder.typehandler;

import de.richtercloud.reflection.form.builder.ComponentHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import de.richtercloud.reflection.form.builder.components.NullableComponentUpdateEvent;
import de.richtercloud.reflection.form.builder.components.NullableComponentUpdateListener;
import de.richtercloud.reflection.form.builder.components.date.SqlDatePicker;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import java.lang.reflect.Type;
import java.sql.Date;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 */
public class SqlDateTypeHandler implements TypeHandler<Date, FieldUpdateEvent<Date>,ReflectionFormBuilder, SqlDatePicker> {

    private final static SqlDateTypeHandler INSTANCE = new SqlDateTypeHandler();

    public static SqlDateTypeHandler getInstance() {
        return INSTANCE;
    }

    protected SqlDateTypeHandler() {
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle(Type type,
            Date fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Date>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        final SqlDatePicker retValue = new SqlDatePicker(fieldValue);
        retValue.addUpdateListener(new NullableComponentUpdateListener<NullableComponentUpdateEvent<Date>>() {
            @Override
            public void onUpdate(NullableComponentUpdateEvent<Date> updateEvent) {
                updateListener.onUpdate(new FieldUpdateEvent<>(updateEvent.getNewValue()));
            }
        });
        return new ImmutablePair<JComponent, ComponentHandler<?>>(retValue, this);
    }

    @Override
    public void reset(SqlDatePicker component) {
        component.reset();
    }
}
