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

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandler;
import richtercloud.validation.tools.FieldRetrievalException;

/**
 *
 * @author richter
 * @param <U> the type of {@link ReflectionFormPanelUpdateListener} to use
 */
/*
internal implementation notes:
- in order to be able to add components to used GroupLayout it's necessary to
expose the horizontal and vertical layout group which are not retrievable from
GroupLayout
- implementing Scrollable messes up horizontal resizing of components in the
right column when a ReflectionFormPanel is added to a JTabbedPane inside a
JScrollPane
- collectionMapping is necessary in order provide a reset method in
ReflectionFormPanel which is necessary because a FieldHandler can't reset the
ReflectionFormPanel if it only delegates to the ReflectionFormPanel i
*/
public class ReflectionFormPanel<U extends ReflectionFormPanelUpdateListener> extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionFormPanel.class);

    public static String generateExceptionMessage(Throwable ex) {
        String retValue = ex.getMessage();
        if(ex.getCause() != null) {
            retValue = String.format("%s (caused by '%s')", retValue, ex.getCause().getMessage());
        }
        return retValue;
    }
    private GroupLayout.Group horizontalSequentialGroup;
    private GroupLayout.Group verticalSequentialGroup;
    private Map<Field, JComponent> fieldMapping = new HashMap<>();
    private final Object instance;
    private Class<?> entityClass;
    private final Set<U> updateListeners = new HashSet<>();
    /**
     * The field handler to use for reset actions.
     */
    private final FieldHandler fieldHandler;

    /**
     *
     * @param fieldMapping
     * @param instance
     * @param entityClass
     * @param fieldHandler the {@link FieldHandler} to perform reset actions
     */
    public ReflectionFormPanel(Map<Field, JComponent> fieldMapping,
            Object instance,
            Class<?> entityClass,
            FieldHandler fieldHandler) {
        super();
        this.initComponents();
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.horizontalSequentialGroup = layout.createSequentialGroup();
        this.verticalSequentialGroup = layout.createSequentialGroup();
        layout.setHorizontalGroup(horizontalSequentialGroup);
        layout.setVerticalGroup(verticalSequentialGroup);
        if(instance == null) {
            throw new IllegalArgumentException("instance mustn't be null");
        }
        if(fieldMapping == null) {
            throw new IllegalArgumentException("fieldMapping mustn't be null");
        }
        if(entityClass == null) {
            throw new IllegalArgumentException("entityClass mustn't be null");
        }
        this.fieldMapping = fieldMapping;
        this.instance = instance;
        this.entityClass = entityClass;
        this.fieldHandler = fieldHandler;
    }

    public void addUpdateListener(U updateListener) {
        this.updateListeners.add(updateListener);
    }

    public void removeUpdateListener(U updateListener) {
        this.updateListeners.remove(updateListener);
    }

    protected Set<U> getUpdateListeners() {
        return Collections.unmodifiableSet(this.updateListeners);
    }

    @Override
    public GroupLayout getLayout() {
        return (GroupLayout) super.getLayout();
    }

    public GroupLayout.Group getVerticalMainGroup() {
        return verticalSequentialGroup;
    }

    public GroupLayout.Group getHorizontalMainGroup() {
        return horizontalSequentialGroup;
    }

    public JComponent getComponentByField(Field field) {
        return this.fieldMapping.get(field);
    }

    /**
     * The instance which this {@code ReflectionFormPanel} manages as it's
     * passed at creation of the panel.
     * @return
     */
    public Object retrieveInstance() {
        return this.instance;
    }

    public Map<Field, JComponent> getFieldMapping() {
        return Collections.unmodifiableMap(fieldMapping);
    }

    public FieldHandler getFieldHandler() {
        return fieldHandler;
    }

    /**
     * returns the pointer to the instance field (to be used in subclasses). An
     * updated version of the instance can only be retrieved with {@link #retrieveInstance() }.
     * @return
     */
    public Object getInstance() {
        return instance;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public void reset() throws FieldRetrievalException {
        for(Component component : getFieldMapping().values()) {
            getFieldHandler().reset(component);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
