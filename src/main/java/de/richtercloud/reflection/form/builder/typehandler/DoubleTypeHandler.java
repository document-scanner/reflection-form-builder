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
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import de.richtercloud.reflection.form.builder.panels.DoublePanel;
import de.richtercloud.reflection.form.builder.panels.NumberPanelUpdateEvent;
import de.richtercloud.reflection.form.builder.panels.NumberPanelUpdateListener;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 */
public class DoubleTypeHandler implements TypeHandler<Double, FieldUpdateEvent<Double>,ReflectionFormBuilder, DoublePanel> {

    private final static DoubleTypeHandler INSTANCE = new DoubleTypeHandler();

    public static DoubleTypeHandler getInstance() {
        return INSTANCE;
    }

    protected DoubleTypeHandler() {
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle(Type type,
            Double fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Double>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        //@TODO: handle validaton annotations (should cover all cases, so no
        // need to develop own annotations
        final DoublePanel retValue = new DoublePanel(fieldValue,
                false //readOnly (doesn't make sense to specify true)
        );
        retValue.addUpdateListener(new NumberPanelUpdateListener<Double>() {

            @Override
            public void onUpdate(NumberPanelUpdateEvent<Double> event) {
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.getValue()));
            }
        });
        return new ImmutablePair<JComponent, ComponentHandler<?>>(retValue, this);
    }

    @Override
    public void reset(DoublePanel component) {
        component.reset();
    }
}
