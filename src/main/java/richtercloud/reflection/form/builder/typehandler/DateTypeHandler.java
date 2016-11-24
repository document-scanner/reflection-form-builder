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
package richtercloud.reflection.form.builder.typehandler;

import java.lang.reflect.Type;
import java.util.Date;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.ComponentHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.components.NullableComponentUpdateEvent;
import richtercloud.reflection.form.builder.components.NullableComponentUpdateListener;
import richtercloud.reflection.form.builder.components.date.UtilDatePicker;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;

/**
 *
 * @author richter
 */
public class DateTypeHandler implements TypeHandler<Date, FieldUpdateEvent<Date>,ReflectionFormBuilder, UtilDatePicker> {
    private final static DateTypeHandler INSTANCE = new DateTypeHandler();

    public static DateTypeHandler getInstance() {
        return INSTANCE;
    }

    protected DateTypeHandler() {
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle(Type type,
            Date fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Date>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException {
        UtilDatePicker retValue = new UtilDatePicker(fieldValue);
        retValue.addUpdateListener(new NullableComponentUpdateListener<NullableComponentUpdateEvent<Date>> () {
            @Override
            public void onUpdate(NullableComponentUpdateEvent<Date> updateEvent) {
                updateListener.onUpdate(new FieldUpdateEvent<>(updateEvent.getNewValue()));
            }
        });
        return new ImmutablePair<JComponent, ComponentHandler<?>>(retValue, this);
    }

    @Override
    public void reset(UtilDatePicker component) {
        component.reset();
    }
}
