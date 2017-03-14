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
import richtercloud.message.handler.IssueHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.panels.AbstractListPanel;
import richtercloud.reflection.form.builder.typehandler.BooleanListTypeHandler;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 *
 * @author richter
 */
public class BooleanListFieldHandler extends AbstractListFieldHandler<List<Boolean>, FieldUpdateEvent<List<Boolean>>, ReflectionFormBuilder> implements FieldHandler<List<Boolean>, FieldUpdateEvent<List<Boolean>>, ReflectionFormBuilder, AbstractListPanel>{

    public BooleanListFieldHandler(IssueHandler issueHandler) {
        super(issueHandler,
                new BooleanListTypeHandler(issueHandler));
    }

    protected BooleanListFieldHandler(MessageHandler messageHandler,
            TypeHandler<List<Boolean>, FieldUpdateEvent<List<Boolean>>,ReflectionFormBuilder, AbstractListPanel> typeHandler) {
        super(messageHandler, typeHandler);
    }
}
