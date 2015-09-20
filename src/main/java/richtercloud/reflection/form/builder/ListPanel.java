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
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- mainList is a JTable in order to work around the fact that JList isn't
designed to be editable
- selection in a one-column table with component rows is difficult to handle ->
add a selection column with checkboxes which provides an excelent selection
mechanism without a large need of implementation and test and doesn't confuse
the user too much (selection without to be implemented highlighting isn't very
clear neither)
*/
public class ListPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(ListPanel.class);
    private ReflectionFormBuilder reflectionFormBuilder;
    @SuppressWarnings("serial")
    private TableCellRenderer mainListCellRenderer = new ComponentTableCellRenderer();
    private final static Comparator<Integer> DESCENDING_ORDER = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    };
    private TableCellEditor mainListCellEditor = new ComponentTableCellEditor();
    private DefaultTableColumnModel mainListColumnModel = new DefaultTableColumnModel();
    private DefaultTableModel mainListModel = new DefaultTableModel() {
        private static final long serialVersionUID = 1L;
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if(columnIndex == 0) {
                return Boolean.class;
            }else if(columnIndex == 1) {
                return JComponent.class;
            }else {
                throw new AssertionError();
            }
        }
    };
    private ListSelectionModel mainListSelectionModel = new DefaultListSelectionModel();
    private Type listGenericType;

    /**
     * Creates new form ListPanel
     */
    protected ListPanel() {
        //don't add an item initially because that is most certainly
        //unintentional
        this.mainListModel.addColumn("Select");
        this.mainListModel.addColumn("Item"); //before initComponents
        TableColumn mainListSelectColumn = new TableColumn(0,
                24,
                new DefaultTableCellRenderer() {
                    private static final long serialVersionUID = 1L;
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        assert value instanceof Boolean;
                        return new JCheckBox("", (boolean) value);
                    }
                },
                new DefaultCellEditor(new JCheckBox())
        );
        mainListSelectColumn.setMaxWidth(new JCheckBox().getPreferredSize().width);
        this.mainListColumnModel.addColumn(mainListSelectColumn);
        this.mainListColumnModel.addColumn(new TableColumn(1, 100, mainListCellRenderer, mainListCellEditor));
        initComponents();
    }

    /**
     *
     * @param listTypeClass the generic type of the list (i.e. the list part of
     * the type is already stripped)
     * @param reflectionFormBuilder
     */
    public ListPanel(Type listTypeClass, ReflectionFormBuilder reflectionFormBuilder) {
        this();
        this.listGenericType = listTypeClass;
        this.reflectionFormBuilder = reflectionFormBuilder;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        selectAllButton = new javax.swing.JButton();
        invertSelectionButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        mainList = new javax.swing.JTable();

        addButton.setText("+");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText("-");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        selectAllButton.setText("Select all");
        selectAllButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectAllButtonActionPerformed(evt);
            }
        });

        invertSelectionButton.setText("Invert selection");
        invertSelectionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invertSelectionButtonActionPerformed(evt);
            }
        });

        mainList.setModel(mainListModel);
        mainList.setCellEditor(mainListCellEditor);
        mainList.setSelectionModel(mainListSelectionModel);
        jScrollPane2.setViewportView(mainList);
        mainList.setColumnModel(mainListColumnModel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(invertSelectionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(selectAllButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invertSelectionButton)
                        .addGap(0, 127, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        //can't check field annotations, but class annotations and, of course, use field handler
        JComponent newElement = null;
        for(Pair<Class<? extends Annotation>, ClassAnnotationHandler> pair : this.reflectionFormBuilder.getClassAnnotationMapping()) {
            if(((Class<?>)this.listGenericType).getAnnotation(pair.getKey()) != null) {
                try {
                    newElement = pair.getValue().handle(this.reflectionFormBuilder.retrieveRelevantFields((Class<?>) this.listGenericType), (Class<?>) this.listGenericType);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        if(newElement == null) {
            FieldHandler fieldHandler = this.reflectionFormBuilder.retrieveFieldHandler(this.listGenericType);
            if(fieldHandler == null) {
                throw new IllegalArgumentException(String.format("no class annotation and no %s is mapped to type %s in the associated %s", FieldHandler.class, this.listGenericType, ReflectionFormBuilder.class));
            }
            newElement = fieldHandler.handle(this.listGenericType, reflectionFormBuilder);
        }
        this.mainListModel.addRow(new Object[]{false, newElement});
        this.mainList.setRowHeight(this.mainListModel.getRowCount()-1, newElement.getPreferredSize().height);
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        LOGGER.debug("removing selected rows {}", this.mainList.getSelectedRows());
        //TableModel doesn't expose methods which allow smart iteration
        List<Integer> removes = new LinkedList<>();
        for(int i=0; i< this.mainListModel.getRowCount(); i++) {
            Vector rowVector = (Vector) this.mainListModel.getDataVector().get(i);
            if((Boolean)rowVector.get(0)) {
                removes.add(i);
            }
        }
        Collections.sort(removes, DESCENDING_ORDER);
        for(int remove : removes) {
            this.mainListModel.removeRow(remove);
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        for(int i=0; i<this.mainListModel.getRowCount(); i++) {
            ((Vector)this.mainListModel.getDataVector().get(i)).set(0, true);
        }
        this.mainListModel.fireTableDataChanged();
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void invertSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionButtonActionPerformed
        for(int i=0; i<this.mainListModel.getRowCount(); i++) {
            Vector rowVector = (Vector)this.mainListModel.getDataVector().get(i);
            rowVector.set(0, !((Boolean)rowVector.get(0)));
        }
        this.mainListModel.fireTableDataChanged();
    }//GEN-LAST:event_invertSelectionButtonActionPerformed

    /**
     * Manages {@link JComponent}s in a list. Editor values aren't managed
     * because they are already managed by the model components.
     */
    /*
    internal implementation notes:
    - DefaultCellEditor doesn't provide a default constructor and causes
    ClassCastExceptions
    */
    private class ComponentTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;
        private Object editorValue = null;

        ComponentTableCellEditor() {
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            assert value instanceof JComponent;
            this.editorValue = value;
            JComponent component = (JComponent) value;
            component.setSize(component.getPreferredSize());
            ListPanel.this.mainList.setRowHeight(row, component.getPreferredSize().height);
            return (Component) value;
        }

        @Override
        public Object getCellEditorValue() {
            return editorValue;
        }
    };

    private class ComponentTableCellRenderer implements TableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable list, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            assert value instanceof JComponent;
            JComponent component = (JComponent) value;
            component.setSize(component.getPreferredSize());
            ListPanel.this.mainList.setRowHeight(row, component.getPreferredSize().height);
            return (Component) value;
        }
    };

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton invertSelectionButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable mainList;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton selectAllButton;
    // End of variables declaration//GEN-END:variables
}
