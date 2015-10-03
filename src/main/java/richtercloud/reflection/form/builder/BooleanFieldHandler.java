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
package richtercloud.reflection.form.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public class BooleanFieldHandler implements FieldHandler<Boolean, BooleanFieldUpdateEvent> {
    private final static BooleanFieldHandler INSTANCE = new BooleanFieldHandler();

    public static BooleanFieldHandler getInstance() {
        return INSTANCE;
    }

    protected BooleanFieldHandler() {
    }

    @Override
    public JComponent handle(Type type,
            Boolean fieldValue,
            final FieldUpdateListener<BooleanFieldUpdateEvent> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) {
        JCheckBox retValue = new JCheckBox("",
                fieldValue);
        retValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateListener.onUpdate(new BooleanFieldUpdateEvent(((JCheckBox)e.getSource()).isSelected()));
            }
        });
        return retValue;
    }

}
