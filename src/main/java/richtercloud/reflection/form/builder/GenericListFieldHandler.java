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
package richtercloud.reflection.form.builder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import javax.swing.JComponent;

/**
 *
 * @author richter
 */
public class GenericListFieldHandler implements FieldHandler {
    private final static GenericListFieldHandler INSTANCE = new GenericListFieldHandler();

    public static GenericListFieldHandler getInstance() {
        return INSTANCE;
    }

    protected GenericListFieldHandler() {
    }

    @Override
    public JComponent handle(Type type, ReflectionFormBuilder reflectionFormBuilder) {
        Type listTypeClass;
        if(!(type instanceof ParameterizedType)) {
            listTypeClass = type;
        }else {
            ParameterizedType fieldParameterizedType = (ParameterizedType) type;
            if(fieldParameterizedType.getActualTypeArguments().length == 0) {
                //can be empty according to docs
                listTypeClass = Object.class;
            }else {
                listTypeClass = fieldParameterizedType.getActualTypeArguments()[0];
            }
        }
        return new ListPanel(listTypeClass, reflectionFormBuilder);
    }

}

