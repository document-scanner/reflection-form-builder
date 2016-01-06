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
package richtercloud.reflection.form.builder.fieldhandler.factory;

import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import richtercloud.reflection.form.builder.fieldhandler.FieldAnnotationHandler;

/**
 *
 * @author richter
 */
public class MappingFieldAnnotationFactory {

    public MappingFieldAnnotationFactory() {
    }

    public List<Pair<Class<? extends Annotation>, FieldAnnotationHandler>> generateFieldAnnotationMapping() {
        return new LinkedList<>();
    }
}
