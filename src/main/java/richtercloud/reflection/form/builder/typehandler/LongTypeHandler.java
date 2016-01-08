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
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import richtercloud.reflection.form.builder.panels.LongPanel;
import richtercloud.reflection.form.builder.panels.NumberPanelUpdateEvent;
import richtercloud.reflection.form.builder.panels.NumberPanelUpdateListener;

/**
 *
 * @author richter
 */
public class LongTypeHandler implements TypeHandler<Long, FieldUpdateEvent<Long>,ReflectionFormBuilder, LongPanel> {

    private final static LongTypeHandler INSTANCE = new LongTypeHandler();

    public static LongTypeHandler getInstance() {
        return INSTANCE;
    }

    protected LongTypeHandler() {
    }

    @Override
    public JComponent handle(Type type,
            Long fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Long>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException {
        final LongPanel retValue = new LongPanel(fieldValue);
        retValue.addUpdateListener(new NumberPanelUpdateListener<Long>() {

            @Override
            public void onUpdate(NumberPanelUpdateEvent<Long> event) {
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.retrieveValue()));
            }
        });
        return retValue;
    }

    @Override
    public void reset(LongPanel component) {
        component.reset();
    }
}
