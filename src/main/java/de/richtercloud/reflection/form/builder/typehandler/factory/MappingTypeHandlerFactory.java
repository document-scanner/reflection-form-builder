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
package de.richtercloud.reflection.form.builder.typehandler.factory;

import de.richtercloud.message.handler.IssueHandler;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createAnyTypeListTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createBooleanListTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createBooleanTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createDateTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createDoubleTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createFloatTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createIntegerListTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createIntegerTypetoken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createLongTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createNumberTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createSqlDateTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createStringListTypeToken;
import static de.richtercloud.reflection.form.builder.fieldhandler.factory.MappingFieldHandlerFactory.createStringTypeToken;
import de.richtercloud.reflection.form.builder.typehandler.BooleanListTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.BooleanTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.DateTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.DoubleTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.FloatTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.IntegerListTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.IntegerTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.LongTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.NumberTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.SimpleEntityListTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.SqlDateTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.StringListTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.StringTypeHandler;
import de.richtercloud.reflection.form.builder.typehandler.TypeHandler;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
