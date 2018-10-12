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
import de.richtercloud.reflection.form.builder.ResetException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import java.awt.Component;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;

/**
 *
 * @author richter
 * @param <T> the type to handle
 * @param <E> the type of field update events
 * @param <R> the type of reflection for builder to use
 */
public class MappingTypeHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder> implements TypeHandler<T,E,R, Component>{
    private final Map<Type, TypeHandler<T,E,R, Component>> classMapping = new HashMap<>();
    /**
     * Since the type handler delegates to mapped type handlers it's sufficient
     * to track the created components internally.
     */
    private final Map<JComponent, ComponentHandler<?>> componentMapping = new HashMap<>();

    @Override
    @SuppressWarnings("FinalMethod") //enforce everything being handled in handle0
    public final Pair<JComponent, ComponentHandler<?>> handle(Type type,
            T fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException {
        Pair<JComponent, ComponentHandler<?>> retValueEntry = handle0(type, fieldValue, fieldName, declaringClass, updateListener, reflectionFormBuilder);
        if(retValueEntry == null) {
            throw new IllegalArgumentException("handle0 mustn't return null");
        }
        ComponentHandler<?> componentResettable = retValueEntry.getValue();
        if(componentResettable == null) {
            throw new IllegalArgumentException("ComponentResettable in Pair returned by handle0 mustn't be null");
        }
        JComponent retValue = retValueEntry.getKey();
        this.componentMapping.put(retValue, componentResettable);
        return retValueEntry;
    }

    protected Pair<JComponent, ComponentHandler<?>> handle0(Type type,
            T fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<E> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException {
        TypeHandler<T,E,R, Component> typeHandler = classMapping.get(type);
        if(typeHandler == null) {
            throw new IllegalArgumentException(String.format("Type '%s' isn't mapped.", type));
        }
        Pair<JComponent, ComponentHandler<?>> retValue = typeHandler.handle(type,
                fieldValue,
                fieldName,
                declaringClass,
                updateListener,
                reflectionFormBuilder);
        return retValue;
    }

    @Override
    public void reset(Component component) throws ResetException {
        ComponentHandler componentResettable = this.componentMapping.get(component);
        componentResettable.reset(component);
    }
}
