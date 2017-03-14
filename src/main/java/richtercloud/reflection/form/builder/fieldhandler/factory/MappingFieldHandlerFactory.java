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

import com.google.common.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import richtercloud.message.handler.IssueHandler;
import richtercloud.message.handler.MessageHandler;
import richtercloud.reflection.form.builder.AnyType;
import richtercloud.reflection.form.builder.fieldhandler.BooleanFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.BooleanListFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.BooleanPrimitiveFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.DateFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.DoubleFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.DoublePrimitiveFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FloatFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.FloatPrimitiveFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.IntegerFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.IntegerListFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.IntegerPrimitiveFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.LongFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.LongPrimitiveFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.NumberFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.SimpleEntityListFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.SqlDateFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.StringFieldHandler;
import richtercloud.reflection.form.builder.fieldhandler.StringListFieldHandler;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- it shouldn't be necessary to expose constants as protected (and separate this
class in package to prevent usage of constants and enforce usage of factory
methods)
- there's no sense in providing a static method to retrieve a singleton because
there's no parameterless constructor (if anything one'd have to create a
ClassMappingFactoryFactory with a generate method acceping the constructor
arguments and that doesn't make sense neither)
*/
public class MappingFieldHandlerFactory {

    private static final Map<Class<?>, FieldHandler<?, ?, ?, ?>> PRIMITIVE_MAPPING_DEFAULT;
    static {
        Map<Class<?>, FieldHandler<?, ?, ?, ?>> primitiveMappingDefault0 = new HashMap<>();
        primitiveMappingDefault0.put(float.class, FloatPrimitiveFieldHandler.getInstance());
        primitiveMappingDefault0.put(int.class, IntegerPrimitiveFieldHandler.getInstance());
        primitiveMappingDefault0.put(double.class, DoublePrimitiveFieldHandler.getInstance());
        primitiveMappingDefault0.put(long.class, LongPrimitiveFieldHandler.getInstance());
        primitiveMappingDefault0.put(boolean.class, BooleanPrimitiveFieldHandler.getInstance());
        PRIMITIVE_MAPPING_DEFAULT = Collections.unmodifiableMap(primitiveMappingDefault0);
    }

    /*
     internal implementation notes:
     - creating in methos in order to be able to specify
     @SuppressWarning("serial")
     */
    @SuppressWarnings("serial")
    public static Type createStringTypeToken() {
        return new TypeToken<String>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createFloatTypeToken() {
        return new TypeToken<Float>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createIntegerTypetoken() {
        return new TypeToken<Integer>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createDoubleTypeToken() {
        return new TypeToken<Double>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createLongTypeToken() {
        return new TypeToken<Long>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createNumberTypeToken() {
        return new TypeToken<Number>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createBooleanTypeToken() {
        return new TypeToken<Boolean>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createDateTypeToken() {
        return new TypeToken<Date>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createSqlDateTypeToken() {
        return new TypeToken<java.sql.Date>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createBooleanListTypeToken() {
        return new TypeToken<List<Boolean>>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createIntegerListTypeToken() {
        return new TypeToken<List<Integer>>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createAnyTypeListTypeToken() {
        return new TypeToken<List<AnyType>>() {
        }.getType();
    }

    @SuppressWarnings("serial")
    public static Type createStringListTypeToken() {
        return new TypeToken<List<String>>() {
        }.getType();
    }
    private IssueHandler issueHandler;

    public MappingFieldHandlerFactory(IssueHandler issueHandler) {
        if(issueHandler == null) {
            throw new IllegalArgumentException("messageHandler mustn't be null");
        }
        this.issueHandler = issueHandler;
    }

    public MessageHandler getMessageHandler() {
        return issueHandler;
    }

    /*
     internal implementation notes:
     - see ValueRetriever's class comment in order to understand why there's no
     subclassing of JComponent
     - separate class mapping and primitive mapping because a primitive can't be
     used in TypeToken and if primitive mapping is checked to contain primitives
     as keys only, then there's no need to deal with precedences and overwriting
     - Rather than mapping static class types map factory instances because that
     allows to cover generic types (which require argument like the generic type
     to be evaluated) with the same mechanism (this is necessary for the
     primitive mapping as soon as component ought to be responsible to manage
     properties of instances autonomously). This allows to set default values on
     components as well easily (can't be done by specifying a subclass of the
     component).
     */
    public Map<Type, FieldHandler<?, ?, ?, ?>> generateClassMapping() {
        Map<Type, FieldHandler<?, ?, ?, ?>> classMapping0 = new HashMap<>();
        classMapping0.put(createStringTypeToken(), StringFieldHandler.getInstance());
        classMapping0.put(createFloatTypeToken(), FloatFieldHandler.getInstance());
        classMapping0.put(createIntegerTypetoken(), IntegerFieldHandler.getInstance());
        classMapping0.put(createDoubleTypeToken(), DoubleFieldHandler.getInstance());
        classMapping0.put(createLongTypeToken(), LongFieldHandler.getInstance());
        classMapping0.put(createNumberTypeToken(), NumberFieldHandler.getInstance());
        classMapping0.put(createBooleanTypeToken(), BooleanFieldHandler.getInstance());
        classMapping0.put(createDateTypeToken(), DateFieldHandler.getInstance());
        classMapping0.put(createSqlDateTypeToken(), SqlDateFieldHandler.getInstance());
        //in addition to CLASS_MAPPING_DEFAULT
        classMapping0.put(createBooleanListTypeToken(), new BooleanListFieldHandler(issueHandler));
        classMapping0.put(createIntegerListTypeToken(), new IntegerListFieldHandler(issueHandler));
        classMapping0.put(createAnyTypeListTypeToken(), new SimpleEntityListFieldHandler(issueHandler));
        classMapping0.put(createStringListTypeToken(), new StringListFieldHandler(issueHandler));
        return classMapping0;
    }

    public Map<Class<?>, FieldHandler<?, ?, ?, ?>> generatePrimitiveMapping() {
        Map<Class<?>, FieldHandler<?, ?, ?, ?>> primitiveMapping0 = new HashMap<>();
        primitiveMapping0.putAll(PRIMITIVE_MAPPING_DEFAULT);
        return Collections.unmodifiableMap(primitiveMapping0);
    }
}
