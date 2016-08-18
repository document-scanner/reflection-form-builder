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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.components.AmountMoneyPanel;
import richtercloud.reflection.form.builder.components.BooleanWrapperComboBox;
import richtercloud.reflection.form.builder.typehandler.BooleanTypeHandler;

/**
 *
 * @author richter
 */
public class BooleanFieldHandler implements FieldHandler<Boolean, FieldUpdateEvent<Boolean>, ReflectionFormBuilder, BooleanWrapperComboBox> {
    private final static BooleanFieldHandler INSTANCE = new BooleanFieldHandler();
    private final Map<Pair<Field, Integer>, AmountMoneyPanel> handledFieldMapping = new HashMap<>();

    public static BooleanFieldHandler getInstance() {
        return INSTANCE;
    }
    private final BooleanTypeHandler booleanTypeHandler;

    protected BooleanFieldHandler() {
        this(BooleanTypeHandler.getInstance());
    }

    public BooleanFieldHandler(BooleanTypeHandler stringTypeHandler) {
        this.booleanTypeHandler = stringTypeHandler;
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Boolean>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        Type fieldType = field.getType();
        Boolean fieldValue = (Boolean) field.get(instance);
        return this.booleanTypeHandler.handle(fieldType,
                fieldValue,
                field.getName(), //fieldName,
                field.getDeclaringClass(), //declaringClass
                updateListener,
                reflectionFormBuilder).getKey();
    }

    @Override
    public void reset(BooleanWrapperComboBox component) {
        component.setSelectedItem(Boolean.FALSE);
    }
}
