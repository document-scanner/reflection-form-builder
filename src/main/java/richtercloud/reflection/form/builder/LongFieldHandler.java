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

import java.lang.reflect.Type;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author richter
 */
public class LongFieldHandler implements FieldHandler<Long, LongFieldUpdateEvent> {
    private final static LongFieldHandler INSTANCE = new LongFieldHandler();

    public static LongFieldHandler getInstance() {
        return INSTANCE;
    }

    protected LongFieldHandler() {
    }

    @Override
    public JComponent handle(Type type,
            Long fieldValue,
            final FieldUpdateListener<LongFieldUpdateEvent> updateListener, ReflectionFormBuilder reflectionFormBuilder) {
        JSpinner retValue = new JSpinner(new SpinnerNumberModel(fieldValue, (Long)Long.MIN_VALUE, (Long)Long.MAX_VALUE, (Long)1L)); //the cast to Long is necessary otherwise Doubles are retrieved from component later
        retValue.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                updateListener.onUpdate(new LongFieldUpdateEvent((Long) ((JSpinner)e.getSource()).getValue()));
            }
        });
        return retValue;
    }

}
