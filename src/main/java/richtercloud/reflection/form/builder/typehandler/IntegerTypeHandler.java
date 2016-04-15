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
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.ComponentResettable;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import richtercloud.reflection.form.builder.panels.IntegerPanel;
import richtercloud.reflection.form.builder.panels.NumberPanelUpdateEvent;
import richtercloud.reflection.form.builder.panels.NumberPanelUpdateListener;

/**
 *
 * @author richter
 */
public class IntegerTypeHandler implements TypeHandler<Integer, FieldUpdateEvent<Integer>,ReflectionFormBuilder, IntegerPanel> {

    private final static IntegerTypeHandler INSTANCE = new IntegerTypeHandler();

    public static IntegerTypeHandler getInstance() {
        return INSTANCE;
    }

    protected IntegerTypeHandler() {
    }

    @Override
    public Pair<JComponent, ComponentResettable<?>> handle(Type type,
            Integer fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Integer>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        final IntegerPanel retValue = new IntegerPanel(fieldValue);
        retValue.addUpdateListener(new NumberPanelUpdateListener<Integer>() {

            @Override
            public void onUpdate(NumberPanelUpdateEvent<Integer> event) {
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.getValue()));
            }
        });
        return new ImmutablePair<JComponent, ComponentResettable<?>>(retValue, this);
    }

    @Override
    public void reset(IntegerPanel component) {
        component.reset();
    }
}
