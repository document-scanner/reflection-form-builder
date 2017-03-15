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
package richtercloud.reflection.form.builder.typehandler.factory;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import richtercloud.message.handler.IssueHandler;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createAnyTypeListTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createBooleanListTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createBooleanTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createDateTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createDoubleTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createFloatTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createIntegerListTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createIntegerTypetoken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createLongTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createNumberTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createSqlDateTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createStringListTypeToken;
import static richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createStringTypeToken;
import richtercloud.reflection.form.builder.typehandler.BooleanListTypeHandler;
import richtercloud.reflection.form.builder.typehandler.BooleanTypeHandler;
import richtercloud.reflection.form.builder.typehandler.DateTypeHandler;
import richtercloud.reflection.form.builder.typehandler.DoubleTypeHandler;
import richtercloud.reflection.form.builder.typehandler.FloatTypeHandler;
import richtercloud.reflection.form.builder.typehandler.IntegerListTypeHandler;
import richtercloud.reflection.form.builder.typehandler.IntegerTypeHandler;
import richtercloud.reflection.form.builder.typehandler.LongTypeHandler;
import richtercloud.reflection.form.builder.typehandler.NumberTypeHandler;
import richtercloud.reflection.form.builder.typehandler.SimpleEntityListTypeHandler;
import richtercloud.reflection.form.builder.typehandler.SqlDateTypeHandler;
import richtercloud.reflection.form.builder.typehandler.StringListTypeHandler;
import richtercloud.reflection.form.builder.typehandler.StringTypeHandler;
import richtercloud.reflection.form.builder.typehandler.TypeHandler;

/**
 *
 * @author richter
 */
public class MappingTypeHandlerFactory {
    private static final Map<Type, TypeHandler<?, ?,?,?>> TYPE_HANDLER_MAPPING_DEFAULT;
    static {
        Map<Type, TypeHandler<?, ?,?,?>> typeHandlerMappingDefault0 = new HashMap<>();
        typeHandlerMappingDefault0.put(createStringTypeToken(), StringTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createFloatTypeToken(), FloatTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createIntegerTypetoken(), IntegerTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createDoubleTypeToken(), DoubleTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createLongTypeToken(), LongTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createNumberTypeToken(), NumberTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createBooleanTypeToken(), BooleanTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createDateTypeToken(), DateTypeHandler.getInstance());
        typeHandlerMappingDefault0.put(createSqlDateTypeToken(), SqlDateTypeHandler.getInstance());
        TYPE_HANDLER_MAPPING_DEFAULT = Collections.unmodifiableMap(typeHandlerMappingDefault0);
    }
    private final IssueHandler issueHandler;

    public MappingTypeHandlerFactory(IssueHandler issueHandler) {
        this.issueHandler = issueHandler;
    }

    public IssueHandler getIssueHandler() {
        return issueHandler;
    }

    public Map<Type, TypeHandler<?, ?,?,?>> generateTypeHandlerMapping() {
        Map<Type, TypeHandler<?, ?,?,?>> typeHandlerMapping0 = new HashMap<>();
        typeHandlerMapping0.putAll(TYPE_HANDLER_MAPPING_DEFAULT);
        //in addition to CLASS_MAPPING_DEFAULT
        typeHandlerMapping0.put(createBooleanListTypeToken(), new BooleanListTypeHandler(issueHandler));
        typeHandlerMapping0.put(createIntegerListTypeToken(), new IntegerListTypeHandler(issueHandler));
        typeHandlerMapping0.put(createAnyTypeListTypeToken(), new SimpleEntityListTypeHandler(issueHandler));
        typeHandlerMapping0.put(createStringListTypeToken(), new StringListTypeHandler(issueHandler));
        return typeHandlerMapping0;
    }
}
