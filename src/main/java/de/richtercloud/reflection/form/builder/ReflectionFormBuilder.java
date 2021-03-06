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

import de.richtercloud.message.handler.ExceptionMessage;
import de.richtercloud.message.handler.IssueHandler;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import de.richtercloud.reflection.form.builder.fieldhandler.FieldUpdateEvent;
import de.richtercloud.validation.tools.FieldRetriever;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 internal implementation notes:
 - an existing entity can be updated through the transform method, but is not
preserved as class property in order to maximize immutability
- ignores: in order to allow overriding of precedences which are static
in order to KISS (for both users and developers) ignores need to be added. They
can be specified at the level of the ReflectionFormBuilder, but then they don't
allow usage of arbitrary extended information because it can't be passed (e.g.
if ignores for field annotation mapping are made based on type there's no way
to evaluate in implementations. Yet, there's always the way to provide
individual implementations and deal with ignores in code -> at some point this
has to happen anyway (e.g. reusage of class mapping in @ElementCollection
annotated fields managed by ElementCollectionFieldAnnotationHandler), so
ignores in ReflectionFormBuilder are a potential YAGNI feature and is
potentially unintuitive (excluding a type from all mapping requires
specification at three ignores set).
- specification of classMapping is enforced because over the time more and more
FieldHandlers appeared which need to be initialized by callers and it's
impossible to handle it elegantly in the constructor call hierarchy (and it
perfectly fine to enforce it) -> don't expose the CLASS_MAPPING_DEFAULT
constant since using it results in a very incomplete setup which raises a lot of
expections for default use cases
- Extraction of FieldRetriever interface allows ReflectionFormBuilder to
concentrate on creating components (which is enforced by forbidding
ReflectionFormBuilder.getClassComponent to return null). This design follows
"composition over inheritance"
 */
/**
 * Builds a {@link ReflectionFormPanel} by recursing over all fields of all
 * subclasses of an class and arranging form components for each item. The
 * components to choose are configurable.
 *
 * The handling of fields is conrolled with different mappings which are
 * {@code fieldAnnotationMapping}, {@code classAnnotationMapping},
 * {@code classMapping} and {@code primitiveMapping} which all
 * associate a characteristic of a class field to an instance of
 * {@link JComponent} which is put into the panel (some components are generated
 * using the Reflection API, others use more complex factories, like
 * {@link FieldAnnotationHandler} and {@link ClassAnnotationHandler}.
 *
 * The precedence of the mappings is static and reads as listed above. It can be
 * overwritten by subclassing field and class annotation handlers to ignore
 * certain type or fields (or what ever criteria you can imagine) and omitting
 * entires in class mapping.
 *
 * @author richter
 * @param <F> allows enforcing a type of {@link FieldRetriever}
 */
public class ReflectionFormBuilder<F extends FieldRetriever> {
    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionFormBuilder.class);
    private final static int LABEL_WIDTH_MIN = 150;
    /**
     * Dialog title used for creating {@link ReflectionFormFieldLabel}s.
     */
    private final String fieldDescriptionDialogTitle;
    private final IssueHandler issueHandler;
    private final F fieldRetriever;

    public static void validateMapping(Map<?,?> mapping, String argumentName) {
        if (mapping == null) {
            throw new IllegalArgumentException(String.format("%s mustn't be null", argumentName));
        }
        if (mapping.values().contains(null)) {
            throw new IllegalArgumentException(String.format("%s mustn't contain null values", argumentName));
        }
    }

    /**
     * Creates a new reflection form builder.
     * @param fieldRetriever the field retriever to use
     * @param fieldDescriptionDialogTitle the field description dialog title
     * @param issueHandler the issue handler to use
     */
    /*
     internal implementation notes:
     - maps to a Callable<? extends JComponent> because some instances require
     constructor arguments (this makes it impossible to provide a default
     mapping and thus passing the argument is enforced in constructor)
     */
    public ReflectionFormBuilder(String fieldDescriptionDialogTitle,
            IssueHandler issueHandler,
            F fieldRetriever) {
        if(issueHandler == null) {
            throw new IllegalArgumentException("messageHandler mustn't be null");
        }
        this.fieldDescriptionDialogTitle = fieldDescriptionDialogTitle;
        this.issueHandler = issueHandler;
        this.fieldRetriever = fieldRetriever;
    }

    public IssueHandler getIssueHandler() {
        return issueHandler;
    }

    protected F getFieldRetriever() {
        return fieldRetriever;
    }

    /*
    internal implementation notes:
    - entityClass is added here to allow subclasses to get the root entity class
    - don't allow returning of null in order to indicate that field ought to be
    skipped because that's the task of FieldRetriever
     */
    /**
     * Retrieves the associated {@link JComponent} to be displayed in the form.
     *
     * @param field the field to get the class component for
     * @param entityClass the entity class declaring the field
     * @param instance the instance to create for
     * @param fieldHandler the field handler to use
     * @return the created component, never {@code null}
     * @throws FieldHandlingException wraps all exceptions occuring during read
     *     or write access to fields
     * @throws ResetException if an exception during resetting the field occurs
     */
    protected JComponent getClassComponent(final Field field,
            Class<?> entityClass,
            final Object instance,
            FieldHandler fieldHandler) throws FieldHandlingException,
            ResetException {
        if (!field.getDeclaringClass().isAssignableFrom(entityClass)) {
            throw new IllegalArgumentException(String.format("field %s has to be declared by entityClass", field));
        }
        field.setAccessible(true); //avoid error 'can not access a member of class Bla with modifiers "private"'
        //create component
        JComponent retValue;
        retValue = fieldHandler.handle(field,
                instance, //instance
            (FieldUpdateEvent event) -> {
                try {
                    onFieldUpdate(event, field, instance);
                } catch (FieldUpdateException ex) {
                    LOGGER.error("unexpected exception during field update occured",
                            ex);
                    issueHandler.handleUnexpectedException(new ExceptionMessage(ex));
                }
            },
                this);
        return retValue;
    }

    protected void onFieldUpdate(FieldUpdateEvent event, Field field, Object instance) throws FieldUpdateException {
        Object eventNewValue = event.getNewValue();
        try {
            field.set(instance,
                    eventNewValue);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new FieldUpdateException(ex);
        }
    }

    /*
    internal implementtion notes:
    - takes a ReflectionFormPanel as argument in order to allow caller to create
    an instance of the required subtype (in order to avoid confusion return type
    has been removed)
    */
    /**
     * Code used by entity and embeddable transformation.
     * @param clazz the class to transform
     * @param instance the instance to transform
     * @param fieldMapping the field mapping to use
     * @param reflectionFormPanel the resulting reflection form panel to add to
     * @param fieldHandler the field handler to use
     * @throws TransformationException wraps any exception which occurs during
     *     field access
     * @throws ResetException if an exception during resetting the field occurs
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    protected void transformClass(Class<?> clazz,
            Object instance,
            Map<Field, JComponent> fieldMapping,
            ReflectionFormPanel reflectionFormPanel,
            FieldHandler fieldHandler) throws TransformationException,
            ResetException {
        List<Field> clazzFields;
        clazzFields = this.fieldRetriever.retrieveRelevantFields(clazz);
        LOGGER.trace(String.format("relevant fields for class %s are %s",
                clazz.getName(),
                clazzFields));

        GroupLayout layout = reflectionFormPanel.getLayout();
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        Group horizontalLabelParallelGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        Group horizontalCompParallelGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
        for (Field field : clazzFields) {
            JComponent comp;
            try {
                comp = this.getClassComponent(field,
                        clazz,
                        instance,
                        fieldHandler);
            } catch (FieldHandlingException ex) {
                throw new TransformationException(ex);
            }
            String fieldName;
            String fieldDescription;
            FieldInfo fieldInfo = field.getAnnotation(FieldInfo.class);
            if(fieldInfo != null) {
                fieldName = String.format("%s (%s)",
                        fieldInfo.name(),
                        field.getName());
                fieldDescription = fieldInfo.description();
            }else {
                fieldName = field.getName();
                fieldDescription = "";
            }
            ReflectionFormFieldLabel label = new ReflectionFormFieldLabel(fieldName,
                    fieldDescription,
                    fieldDescriptionDialogTitle);
            horizontalLabelParallelGroup.addComponent(label,
                    (int) (label.getMinimumSize().getWidth() < LABEL_WIDTH_MIN ? label.getMinimumSize().getWidth() : LABEL_WIDTH_MIN), //min
                    GroupLayout.PREFERRED_SIZE, //pref
                    GroupLayout.PREFERRED_SIZE //max
            );
            horizontalCompParallelGroup.addComponent(comp,
                    0, //0 allows to specify comp.getMinimumSize().getWidth()
                        //(otherwise `java.lang.IllegalArgumentException:
                        //Following is not met: min<=pref<=max` can occur)
                    (int) comp.getMinimumSize().getWidth(), //pref
                    Short.MAX_VALUE //max
            );
            Group verticalFieldGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
            verticalFieldGroup.addComponent(label);
            verticalFieldGroup.addComponent(comp);
            reflectionFormPanel.getVerticalMainGroup()
                    .addGroup(verticalFieldGroup);
            fieldMapping.put(field, comp);
        }
        reflectionFormPanel.getHorizontalMainGroup()
                .addGroup(horizontalLabelParallelGroup)
                .addGroup(horizontalCompParallelGroup);
    }

    /**
     *
     * @param entityClass the entity class
     * @param entityToUpdate an entity to update (instead of creating a new
     *     instance of {@code entityClass}). {@code null} indicates to create a
     *     new instance and write changes of the {@link ReflectionFormPanel}
     *     into it.
     * @param fieldHandler the field handler to sue
     * @return the created component
     * @throws TransformationException wraps all exception occuring during field
     *     access
     * @throws ResetException if an exception during resetting the field occurs
     */
    public ReflectionFormPanel transformEntityClass(Class<?> entityClass,
            Object entityToUpdate,
            FieldHandler fieldHandler) throws TransformationException,
            ResetException {
        final Map<Field, JComponent> fieldMapping = new HashMap<>();
        Object instance = prepareInstance(entityClass, entityToUpdate);
        ReflectionFormPanel retValue = new ReflectionFormPanel(fieldMapping,
                instance,
                entityClass,
                fieldHandler);
        transformClass(entityClass,
                instance,
                fieldMapping,
                retValue,
                fieldHandler);
        return retValue;
    }

    protected Object prepareInstance(Class<?> entityClass, Object entityToUpdate) throws TransformationException {
        Object retValue = entityToUpdate;
        if (retValue == null) {
            Constructor<?> entityClassConstructor = null;
            try {
                entityClassConstructor = entityClass.getDeclaredConstructor();
            } catch (NoSuchMethodException ex) {
                throw new IllegalArgumentException(String.format("entityClass %s doesn't provide a zero-argument-constructor (see nested exception for details)", entityClass), ex);
            }
            entityClassConstructor.setAccessible(true);
            try {
                retValue = entityClassConstructor.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new TransformationException(ex);
            }
        }
        return retValue;
    }

}
