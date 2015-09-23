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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author richter
 */
public class FloatFieldHandler implements FieldHandler<FloatFieldUpdateEvent> {

    private final static FloatFieldHandler INSTANCE = new FloatFieldHandler();

    public static FloatFieldHandler getInstance() {
        return INSTANCE;
    }

    protected FloatFieldHandler() {
    }

    @Override
    public JComponent handle(Type type, final UpdateListener<FloatFieldUpdateEvent> updateListener, ReflectionFormBuilder reflectionFormBuilder) {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        final JSpinner retValue = new JSpinner(new SpinnerNumberModel((Float) 0.0f,
                (Float) (-10.0f),
                (Float) 10.0f,
                (Float) 0.1f));//the cast to Float is necessary otherwise Doubles are retrieved from component later
        retValue.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
                updateListener.onUpdate(new FloatFieldUpdateEvent((Float) retValue.getValue()));
            }
        });
        retValue.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                updateListener.onUpdate(new FloatFieldUpdateEvent((Float) retValue.getValue()));
            }

        });
        return retValue;
    }
}
