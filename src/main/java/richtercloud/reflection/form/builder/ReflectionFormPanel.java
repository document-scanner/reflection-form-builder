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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.retriever.ValueRetriever;

/**
 *
 * @author richter
 */
public class ReflectionFormPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionFormPanel.class);

    public static String generateExceptionMessage(Throwable ex) {
        String retValue = ex.getMessage();
        if(ex.getCause() != null) {
            retValue = String.format("%s (caused by '%s')", retValue, ex.getCause().getMessage());
        }
        return retValue;
    }
    private Map<Field, JComponent> fieldMapping = new HashMap<>();
    private Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping;
    private Map<Type, FieldHandler> classMapping;
    private Object instance;
    private Class<?> entityClass;

    /**
     * Creates new form ReflectionFormPanel
     */
    public ReflectionFormPanel() {
        this.initComponents();
    }

    public ReflectionFormPanel(Map<Field, JComponent> fieldMapping,
            Object instance,
            Class<?> entityClass,
            Map<Class<? extends JComponent>, ValueRetriever<?, ?>> valueRetrieverMapping,
            Map<Type, FieldHandler> classMapping) {
        this();
        if(instance == null) {
            throw new IllegalArgumentException("instance mustn't be null");
        }
        if(fieldMapping == null) {
            throw new IllegalArgumentException("fieldMapping mustn't be null");
        }
        if(valueRetrieverMapping == null) {
            throw new IllegalArgumentException("valueRetrieverMapping mustn't be null");
        }
        if(classMapping == null) {
            throw new IllegalArgumentException("classMapping mustn't be null");
        }
        if(entityClass == null) {
            throw new IllegalArgumentException("entityClass mustn't be null");
        }
        this.fieldMapping = fieldMapping;
        this.valueRetrieverMapping = valueRetrieverMapping;
        this.classMapping = classMapping;
        this.instance = instance;
        this.entityClass = entityClass;
    }

    public JComponent getComponentByField(Field field) {
        return this.fieldMapping.get(field);
    }

    public void updateInstance() throws IllegalArgumentException, IllegalAccessException {
        for(Field field : this.fieldMapping.keySet()) {
            JComponent comp = this.fieldMapping.get(field);
            //figure out what type comp is supposed to deliver
            ValueRetriever valueRetriever = this.valueRetrieverMapping.get(comp.getClass());
            if(valueRetriever == null) {
                if(this.classMapping.get(field.getType()) == null) {
                    LOGGER.debug("skipping update of instance for field '{}' of class '{}' because no component is mapped in class mapping", field.getName(), field.getDeclaringClass().getName());
                    continue;

                }else {
                    throw new IllegalArgumentException(String.format("valueRetriever mapped to component '%s' class is null", comp.getClass().getName()));
                }
            }
            Object value = valueRetriever.retrieve(comp);
            field.set(this.instance, value);
        }
    }

    public Object retrieveInstance() throws IllegalArgumentException, IllegalAccessException {
        this.updateInstance();
        return this.instance;
    }

    public Map<Class<? extends JComponent>, ValueRetriever<?, ?>> getValueRetrieverMapping() {
        return Collections.unmodifiableMap(this.valueRetrieverMapping);
    }

    public Map<Field, JComponent> getFieldMapping() {
        return fieldMapping;
    }

    /**
     * returns the pointer to the instance field (to be used in subclasses). An
     * updated version of the instance can only be retrieved with {@link #retrieveInstance() }.
     * @return
     */
    public Object getInstance() {
        return instance;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public Class<?> getEntityClass() {
        return entityClass;
    }

    public Map<Type, FieldHandler> getClassMapping() {
        return Collections.unmodifiableMap(classMapping);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanelScrollPane = new javax.swing.JScrollPane();
        mainPanel = new javax.swing.JPanel();

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 398, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 298, Short.MAX_VALUE)
        );

        mainPanelScrollPane.setViewportView(mainPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanelScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainPanelScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane mainPanelScrollPane;
    // End of variables declaration//GEN-END:variables

}
