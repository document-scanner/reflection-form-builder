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

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.ComponentHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 * Provides reset functionality by making {@link FieldHandler#handle(java.lang.reflect.Field, java.lang.Object, richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener, richtercloud.reflection.form.builder.ReflectionFormBuilder) } final and enforcing implementation in helper {@link #handle0
 * @author richter
 */
public abstract class ResettableFieldHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder, C extends Component> implements FieldHandler<T, E, R, C> {
    private static final ComponentHandler<JLabel> JLABEL_COMPONENT_RESETTABLE = new ComponentHandler<JLabel>() {
        @Override
        public void reset(JLabel component) {
            component.setText("");
        }
    };
    /**
     * Mapping between components and their {@link ComponentResettable} used in
     * {@link #reset(java.awt.Component) }.
     */
    /*
    internal implementation notes:
    - There's no point in enforcing ComponentResettables with the a matching
    generic type being mapped to a component because that inevitably requires
    specification of a generic type which needs to be initialized and also be
    assigned with JLabel which is the fallback. No such type exists. -> A
    ClassCastException might occur in the reset method.
    */
    private final Map<JComponent, ComponentHandler<?>> componentMapping = new HashMap<>();

    @Override
    @SuppressWarnings("FinalMethod") //enforce everything being handled in handle0
    public final JComponent handle(final Field field,
            final Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException {
        Pair<JComponent, ComponentHandler<?>> retValueEntry = handle0(field, instance, updateListener, reflectionFormBuilder);
        if(retValueEntry == null) {
            JLabel retValue = new JLabel(field.getType().getSimpleName());
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

    protected abstract Pair<JComponent, ComponentHandler<?>> handle0(final Field field,
            final Object instance,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws IllegalArgumentException,
            IllegalAccessException,
            FieldHandlingException,
            InvocationTargetException,
            NoSuchMethodException,
            InstantiationException;

    @Override
    public void reset(C component) {
        ComponentHandler classPartHandler = this.componentMapping.get(component);
        classPartHandler.reset(component);
    }
}
