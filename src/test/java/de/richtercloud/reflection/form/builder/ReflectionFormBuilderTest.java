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
package de.richtercloud.reflection.form.builder;

import com.google.common.reflect.TypeToken;
import de.richtercloud.message.handler.IssueHandler;
import de.richtercloud.message.handler.LoggerIssueHandler;
import de.richtercloud.reflection.form.builder.fieldhandler.BooleanListFieldHandler;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateListener;
import de.richtercloud.reflection.form.builder.fieldhandler.MappingFieldHandler;
import de.richtercloud.reflection.form.builder.fieldhandler.StringFieldHandler;
import de.richtercloud.reflection.form.builder.panels.BooleanListPanel;
import de.richtercloud.reflection.form.builder.typehandler.StringTypeHandler;
import de.richtercloud.validation.tools.CachedFieldRetriever;
import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richter
 */
public class ReflectionFormBuilderTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionFormBuilderTest.class);
    private static final String FIELD_DESCRIPTION = "field description";
    private final IssueHandler issueHandler = new LoggerIssueHandler(LOGGER);

    @Test
    @SuppressWarnings({"serial",
        "PMD.JUnitTestContainsTooManyAsserts"
    })
    public void testGetClassComponent() throws NoSuchFieldException,
            NoSuchMethodException,
            InstantiationException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException,
            FieldHandlingException,
            ResetException {
        final Map<Type, FieldHandler<?,?, ?, ?>> classMapping = new HashMap<>();
        Type listAnyType = new TypeToken<List<AnyType>>() {}.getType();
        Type listBooleanType = new TypeToken<List<Boolean>>() {}.getType();
        classMapping.put(listAnyType, StringFieldHandler.getInstance());
        classMapping.put(listBooleanType, new BooleanListFieldHandler(issueHandler));
        classMapping.put(String.class,
                new StringFieldHandler(StringTypeHandler.getInstance()));
        FieldHandler fieldHandler = new MappingFieldHandler(classMapping,
                new HashMap<>(),
                issueHandler);
        ReflectionFormBuilder instance = new ReflectionFormBuilder(
                FIELD_DESCRIPTION,
                issueHandler,
                new CachedFieldRetriever());
        Class<?> entityClass = TestEntity.class;
        Field field = entityClass.getDeclaredField("a");
        TestEntity entity = TestEntity.class.getDeclaredConstructor().newInstance();
        JComponent result = instance.getClassComponent(field,
                entityClass,
                entity,
                fieldHandler);
        assertEquals(JTextField.class, result.getClass());
        //test that AnyType specifiecation is matched
        Class<? extends TestEntityCollection> entityClassCollection = TestEntityCollection.class;
        TestEntityCollection entityCollection = TestEntityCollection.class.getDeclaredConstructor().newInstance();
        instance = new ReflectionFormBuilder(FIELD_DESCRIPTION,
                issueHandler,
                new CachedFieldRetriever());
        result = instance.getClassComponent(TestEntityCollection.class.getDeclaredField("gs"), //is a List<Set<Boolean>>
                entityClassCollection,
                entityCollection,
                fieldHandler);
        assertEquals(BooleanListPanel.class, result.getClass());
    }

    @Test
    @SuppressWarnings({"serial",
        "PMD.JUnitTestContainsTooManyAsserts"
    })
    public void testTransform() throws TransformationException, ResetException {
        //test entity without collection fields
        FieldHandler fieldHandler = new FieldHandler() {
            @Override
            public JComponent handle(Field field, Object instance, FieldUpdateListener updateListener, ReflectionFormBuilder reflectionFormBuilder) throws FieldHandlingException {
                return new JLabel("Test label");
            }

            @Override
            public void reset(Component component) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        ReflectionFormBuilder instance = new ReflectionFormBuilder(
                FIELD_DESCRIPTION,
                issueHandler,
                new CachedFieldRetriever());
        Class<?> entityClass = TestEntity.class;
        ReflectionFormPanel result = instance.transformEntityClass(entityClass,
                null, //entityToUpdate
                fieldHandler
        );
        assertEquals(4, result.getComponentCount());
        entityClass = TestEntitySubclass.class;
        result = instance.transformEntityClass(entityClass,
                null, //entityToUpdate
                fieldHandler
        );
        assertEquals(6, result.getComponentCount());

        //test entity with collection
        entityClass = TestEntityCollection.class;
//        classMapping.put(new TypeToken<List<TestEntityCollection>>() {}.getType(),
//                new SimpleEntityListFieldHandler(messageHandler));
        instance = new ReflectionFormBuilder(FIELD_DESCRIPTION,
                issueHandler,
                new CachedFieldRetriever());
        result = instance.transformEntityClass(entityClass,
                null, //entityToUpdate
                fieldHandler
        );
        assertEquals(8, result.getComponentCount());
    }
}
