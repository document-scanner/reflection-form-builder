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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import org.apache.commons.lang3.tuple.Pair;

/**
 * A {@link TypeHandler} which allows to check for an exact type match (which is
 * handled immediately (and the result returned)) and retrieve the type of the
 * {@link List}.Furthermore check different class annotations for the base type
 * and the generic list type.
 *
 * @author richter
 * @param <R> the reflection form builder to use
 * @param <C> the component to produce
 */
public abstract class GenericListTypeHandler<R extends ReflectionFormBuilder, C extends Component> implements TypeHandler<List<Object>, FieldUpdateEvent<List<Object>>,R, C> {
    /**
     * The {@link TypeHandler} mapping for the generic type. Will be used if
     * there's no match in {@code typeHandlerMapping}.
     */
    private final Map<Type, TypeHandler<?, ?,?, ?>> genericsTypeHandlerMapping;
    /**
     * The {@link TypeHandler} mapping which is used if the type matches
     * exactly.
     */
    private final Map<Type, TypeHandler<?,?,?, ?>> typeHandlerMapping;

    public GenericListTypeHandler(Map<Type, TypeHandler<?, ?, ?, ?>> genericsTypeHandlerMapping,
            Map<Type, TypeHandler<?, ?, ?, ?>> typeHandlerMapping) {
        this.genericsTypeHandlerMapping = genericsTypeHandlerMapping;
        this.typeHandlerMapping = typeHandlerMapping;
    }

    /**
     * Checks {@code typeClassAnnotationHandlerMapping} and then
     * {@code typeHandlerMapping} for exact matches.If there're no results,
     * checks {@code typeClassAnnotationHandlerMapping} and
     * {@code genericsTypeClassAnnotationHandlerMapping}.
     * @param type the type to handle
     * @param fieldValue the current field value
     * @param fieldName the field name
     * @param declaringClass the class declaring standing on top of the hiearchy
     *     using the type
     * @param updateListener the update listener to use
     * @param reflectionFormBuilder the reflection form builder to use
     * @return the generated component/component handler pair
     * @throws FieldHandlingException wraps all exceptions occuring during read
     *     or write access to fields
     * @throws ResetException if an exception during resetting the field occurs
     */
    @Override
    public Pair<JComponent, ComponentHandler<?>> handle(Type type,
            List<Object> fieldValue,
            String fieldName,
            Class<?> declaringClass,
            final FieldUpdateListener<FieldUpdateEvent<List<Object>>> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException {
        List<Object> fieldValue0 = fieldValue;
        if(fieldValue0 == null) {
            fieldValue0 = new LinkedList<>(); //this is legitimate because all
                    //mechanisms will fail if the field value (retrieved with
                    //Field.get) is null (this allows to manage updates with
                    //this value which can be passed to updateListener then)
        }

        //check for an exact match (including all nested generics) first
        TypeHandler fieldTypeHandler = this.typeHandlerMapping.get(type);
        if(fieldTypeHandler != null) {
            Pair<JComponent, ComponentHandler<?>> retValue = fieldTypeHandler.handle(type,
                    fieldValue0,
                    fieldName,
                    declaringClass,
                    updateListener,
                    reflectionFormBuilder);
            return retValue;
        }

        Type genericType = retrieveTypeGenericType(type);
        //class annotations can be checked (field annotations not, of course)

        TypeHandler genericTypeHandler = this.genericsTypeHandlerMapping.get(genericType);
        if(genericTypeHandler != null) {
            return genericTypeHandler.handle(genericType,
                    fieldValue0, //here we use the field value because
                        //this refers to the top level
                    fieldName,
                    declaringClass,
                    updateListener,
                    reflectionFormBuilder);
        }

        return handleGenericType(genericType,
                fieldValue0,
                fieldName,
                declaringClass,
                updateListener,
                reflectionFormBuilder);
    }

    protected abstract Pair<JComponent, ComponentHandler<?>> handleGenericType(Type type,
            List<Object> fieldValue,
            String fieldName,
            Class<?> declaringClass,
            FieldUpdateListener<FieldUpdateEvent<List<Object>>> updateListener,
            R reflectionFormBuilder) throws FieldHandlingException,
            ResetException;

    public Type retrieveTypeGenericType(Type type) {
        Type retValue;
        if(!(type instanceof ParameterizedType)) {
            /*a simple
            @ElementCollection
            private List objectList;
            declaration*/
            return type;
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
