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
package de.richtercloud.reflection.form.builder.fieldhandler;

import de.richtercloud.reflection.form.builder.ComponentHandler;
import de.richtercloud.reflection.form.builder.ReflectionFormBuilder;
import de.richtercloud.reflection.form.builder.ResetException;
import java.awt.Component;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Provides reset functionality by making
 * {@link FieldHandler#handle(java.lang.reflect.Field, java.lang.Object, de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener, de.richtercloud.reflection.form.builder.ReflectionFormBuilder) }
 * final and enforcing implementation in helper {@link #handle0(java.lang.reflect.Field, java.lang.Object, de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener, de.richtercloud.reflection.form.builder.ReflectionFormBuilder) }.
 *
 * @author richter
 * @param <T> the type of field to handle
 * @param <E> the type of field events to handle
 * @param <R> the type of reflection form builder to use
 * @param <C> the type of component to produce
 */
public abstract class ResettableFieldHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder, C extends Component> implements FieldHandler<T, E, R, C> {
    private static final ComponentHandler<JLabel> JLABEL_COMPONENT_RESETTABLE = (JLabel component) -> {
        component.setText("");
    };
    /*
    internal implementation notes:
    - There's no point in enforcing ComponentResettables with the a matching
    generic type being mapped to a component because that inevitably requires
    specification of a generic type which needs to be initialized and also be
    assigned with JLabel which is the fallback. No such type exists. -> A
    ClassCastException might occur in the reset method.
    */
    /**
     * Mapping between components and their {@link ComponentResettable} used in
     * {@link #reset(java.awt.Component) }.
     */
    private final Map<JComponent, ComponentHandler<?>> componentMapping = new HashMap<>();

    @Override
    @SuppressWarnings("FinalMethod") //enforce everything being handled in handle0
    public final JComponent handle(final Field field,
            final Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException {
        Pair<JComponent, ComponentHandler<?>> retValueEntry = handle0(field, instance, updateListener, reflectionFormBuilder);
        if(retValueEntry == null) {
            JComponent retValue = retrieveDefaultComponent(field);
            this.componentMapping.put(retValue, JLABEL_COMPONENT_RESETTABLE);
            return retValue;
        }
        ComponentHandler<?> componentResettable = retValueEntry.getValue();
        if(componentResettable == null) {
            throw new IllegalArgumentException("ComponentResettable in Pair returned by handle0 mustn't be null");
        }
        JComponent retValue = retValueEntry.getKey();
        this.componentMapping.put(retValue, componentResettable);
        return retValue;
    }

    public JComponent retrieveDefaultComponent(Field field) {
        return new JLabel(field.getType().getSimpleName());
    }

    protected abstract Pair<JComponent, ComponentHandler<?>> handle0(final Field field,
            final Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException;

    @Override
    public void reset(C component) throws ResetException {
        ComponentHandler classPartHandler = this.componentMapping.get(component);
        if(classPartHandler == null) {
            throw new IllegalArgumentException(String.format("component '%s' doesn't have a %s mapped in componentMapping", component, ComponentHandler.class));
        }
        classPartHandler.reset(component);
    }
}
