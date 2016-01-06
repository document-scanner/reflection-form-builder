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

import java.util.List;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.message.MessageHandler;
import richtercloud.reflection.form.builder.typehandler.StringListTypeHandler;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 *
 * @author richter
 */
public class StringListFieldHandler extends AbstractListFieldHandler<List<String>, FieldUpdateEvent<List<String>>, ReflectionFormBuilder> implements FieldHandler<List<String>, FieldUpdateEvent<List<String>>, ReflectionFormBuilder> {

    public StringListFieldHandler(MessageHandler messageHandler) {
        super(messageHandler,
                new StringListTypeHandler(messageHandler));
    }

    protected StringListFieldHandler(MessageHandler messageHandler, TypeHandler<List<String>, FieldUpdateEvent<List<String>>,ReflectionFormBuilder> typeHandler) {
        super(messageHandler, typeHandler);
    }
}
