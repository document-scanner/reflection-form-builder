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
package richtercloud.reflection.form.builder.typehandler;

import java.awt.Component;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;

/**
 *
 * @author richter
 */
public class MappingTypeHandler<T, E extends FieldUpdateEvent<T>, R extends ReflectionFormBuilder> implements TypeHandler<T,E,R, Component>{
    private Map<Type, TypeHandler<T,E,R, Component>> classMapping = new HashMap<>();
    /**
     * Since the type handler delegates to mapped type handlers it's sufficient
     * to track the created components internally.
     */
    private final Map<JComponent, TypeHandler> componentMapping = new HashMap<>();

    @Override
    public JComponent handle(Type type, T fieldValue, String fieldName, Class<?> declaringClass, FieldUpdateListener<E> updateListener, R reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {
        TypeHandler<T,E,R, Component> typeHandler = classMapping.get(type);
        if(typeHandler == null) {
            throw new IllegalArgumentException(String.format("Type '%s' isn't mapped.", type));
        }
        JComponent retValue = typeHandler.handle(type, fieldValue, fieldName, declaringClass, updateListener, reflectionFormBuilder);
        this.componentMapping.put(retValue, typeHandler);
        return retValue;
    }

    @Override
    public void reset(Component component) {
        this.componentMapping.get(component).reset(component);
    }

}
