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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;

/**
 * A {@link TypeHandler} which allows to check for an exact type match (which is
 * handled immediately (and the result returned)) and retrieve the type of the
 * {@link List}.
 *
 * @author richter
 */
public abstract class GenericListTypeHandler<R extends ReflectionFormBuilder, C extends Component> implements TypeHandler<List<Object>, FieldUpdateEvent<List<Object>>,R, C> {
    /**
     * The {@link TypeHandler} mapping for the generic type of the field (excluding the file type itself. Will be used if there's no match in {@code fieldTypeHandlerMapping}.
     */
    private final Map<Type, TypeHandler<?, ?,?, ?>> genericsTypeHandlerMapping;
    /**
     * A mapping for {@link TypeHandler which is used is the field type matches
     * exactly.
     */
    private final Map<Type, TypeHandler<?,?,?, ?>> fieldTypeHandlerMapping;

    public GenericListTypeHandler(Map<Type, TypeHandler<?, ?, ?, ?>> genericsTypeHandlerMapping,
            Map<Type, TypeHandler<?, ?, ?, ?>> fieldTypeHandlerMapping) {
        this.genericsTypeHandlerMapping = genericsTypeHandlerMapping;
        this.fieldTypeHandlerMapping = fieldTypeHandlerMapping;
    }

    @Override
    public JComponent handle(Type type, List<Object> fieldValue, String fieldName, Class<?> declaringClass, final FieldUpdateListener<FieldUpdateEvent<List<Object>>> updateListener, R reflectionFormBuilder) throws IllegalArgumentException, IllegalAccessException, FieldHandlingException {

        if(fieldValue == null) {
            fieldValue = new LinkedList<>(); //this is legitimate because all
                    //mechanisms will fail if the field value (retrieved with
                    //Field.get) is null (this allows to manage updates with
                    //this value which can be passed to updateListener then)
        }

        //check for an exact match (including all nested generics) first
        TypeHandler fieldTypeHandler = this.fieldTypeHandlerMapping.get(type);
        if(fieldTypeHandler != null) {
            JComponent retValue = fieldTypeHandler.handle(type,
                    fieldValue,
                    fieldName,
                    declaringClass,
                    updateListener,
                    reflectionFormBuilder);
            return retValue;
        }

        Type genericType = retrieveTypeGenericType(type);
        TypeHandler genericTypeHandler = this.genericsTypeHandlerMapping.get(genericType);
        if(genericTypeHandler != null) {
            return genericTypeHandler.handle(genericType,
                    fieldValue, //here we use the field value because
                        //this refers to the top level
                    fieldName,
                    declaringClass,
                    updateListener,
                    reflectionFormBuilder);
        }

        return handleGenericType(genericType,
                fieldValue,
                fieldName,
                declaringClass,
                updateListener,
                reflectionFormBuilder);
    }

    protected abstract JComponent handleGenericType(Type type,
            List<Object> fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<FieldUpdateEvent<List<Object>>> updateListener,
            R reflectionFormBuilder) throws IllegalAccessException;

    public Type retrieveTypeGenericType(Type type) {
        Type retValue;
        if(!(type instanceof ParameterizedType)) {
            /*a simple
            @ElementCollection
            private List objectList;
            declaration*/
            retValue = Object.class;
        }else {
            ParameterizedType fieldTypeParameterized = (ParameterizedType) type;
            Type[] genericTypeArguments = fieldTypeParameterized.getActualTypeArguments();
            if(genericTypeArguments.length == 0) {
                //can happen according to ParameterizedType.getActualTypeArguments
                retValue = Object.class;
            }else if(genericTypeArguments.length > 1) {
                throw new IllegalArgumentException("more than 1 type argument not supported");
            }else {
                retValue = genericTypeArguments[0];
            }
        }
        return retValue;
    }
}
