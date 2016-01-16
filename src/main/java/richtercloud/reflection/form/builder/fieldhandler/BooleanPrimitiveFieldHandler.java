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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 *
 * @author richter
 */
public class BooleanPrimitiveFieldHandler implements FieldHandler<Boolean, FieldUpdateEvent<Boolean>, ReflectionFormBuilder, JCheckBox> {
    private final static BooleanPrimitiveFieldHandler INSTANCE = new BooleanPrimitiveFieldHandler();

    public static BooleanPrimitiveFieldHandler getInstance() {
        return INSTANCE;
    }

    protected BooleanPrimitiveFieldHandler() {
    }

    @Override
    public JComponent handle(Field field,
            Object instance,
            final FieldUpdateListener<FieldUpdateEvent<Boolean>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException {
        boolean fieldValue = field.getBoolean(instance);
        final JCheckBox retValue = new JCheckBox("",
                fieldValue);
        retValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.isSelected()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(JCheckBox component) {
        component.setSelected(false);
    }

}