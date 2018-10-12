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
import de.richtercloud.reflection.form.builder.components.BooleanWrapperComboBox;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Type;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 */
public class BooleanTypeHandler implements TypeHandler<Boolean, FieldUpdateEvent<Boolean>,ReflectionFormBuilder, BooleanWrapperComboBox> {
    private final static BooleanTypeHandler INSTANCE = new BooleanTypeHandler();

    public static BooleanTypeHandler getInstance() {
        return INSTANCE;
    }

    protected BooleanTypeHandler() {
    }

    @Override
    public Pair<JComponent, ComponentHandler<?>> handle(Type type,
            Boolean fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<Boolean>> updateListener,
            ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
        final BooleanWrapperComboBox retValue = new BooleanWrapperComboBox(fieldValue);
        retValue.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                updateListener.onUpdate(new FieldUpdateEvent<>(retValue.getValue()));
            }
        });
        return new ImmutablePair<JComponent, ComponentHandler<?>>(retValue, this);
    }

    @Override
    public void reset(BooleanWrapperComboBox component) {
        component.reset();
    }
}
