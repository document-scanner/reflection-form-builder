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
package richtercloud.reflection.form.builder.panels;

import java.awt.Component;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;

/**
 *
 * @author richter
 * @param <T>
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
 - creation of new instances and value binding, i.e. updates based on component
 listener notifications are possible as long as the field handler (e.g. GenericListFieldHandler) discovers entities which can be instantiated and distinguishes them from others (e.g. collection interfaces which can't be instantiated).
 - Caller needs to create instance in order to be able to set it on the valueList's field (-> needs to be created in field handler)
 because field information isn't passed to AbstractListPanel (doesn't make sense for nested types), but AbstractListPanel needs to create instances of managed types. Thereby it needs to distungish if the type is a primitive wrapper or too abstract to be instantiable
 - in order to handle updates with listeners and event types it's necessary to
 either cast event or provide different FieldHandlers for lists of immutable
 types and mutable types -> casting should facilitate the reusage of code because
 everything will be handled in GenericListFieldHandler and AbstractListPanel
 - Due to the fact that DefaultTableModel handles data in a Vector and a List
 needs to be retrieved from AbstractListPanel, a transformation needs to take
 place -> this should be handled in the model exclusively in order to allow
 exchangable implementations of this transformation
 */
public abstract class AbstractListPanel<T> extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractListPanel.class);
    private ReflectionFormBuilder reflectionFormBuilder;
    public final static Comparator<Integer> DESCENDING_ORDER = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    };
    private DefaultTableColumnModel mainListColumnModel = new DefaultTableColumnModel();
    private ListPanelTableModel mainListModel = new ListPanelTableModel() {
        private static final long serialVersionUID = 1L;
        /*
         internal implementation notes:
         - without this nothing is displayed (specification of column renderer
         and editor in DefaultTableModel.addColumn doesn't have any effect
         */

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Object.class;
            } else {
                throw new AssertionError();
            }
        }
    };
    private ListSelectionModel mainListSelectionModel = new DefaultListSelectionModel();
    /**
     * The instance which manages the instances. There's no way to allow the
     * caller to change the specific type.
     */
    private Set<ListPanelItemListener> itemListeners = new HashSet<>();
    private ListPanelTableCellRenderer mainListCellRenderer;
    private ListPanelTableCellEditor mainListCellEditor;

    /**
     * Creates new form ListPanel
     */
    protected AbstractListPanel() {
    }

    /**
     *
     * @param reflectionFormBuilder
     * @param mainListCellEditor
     * @param mainListCellRenderer
     */
    public AbstractListPanel(ReflectionFormBuilder reflectionFormBuilder,
            ListPanelTableCellEditor mainListCellEditor,
            ListPanelTableCellRenderer mainListCellRenderer) {
        //not possible to call this() because of dependency on cell renderer and
        //editor
        this.reflectionFormBuilder = reflectionFormBuilder;
        //don't add an item initially because that is most certainly
        //unintentional
        this.mainListModel.addColumn(""); //before initComponents
        this.mainListColumnModel.addColumn(new TableColumn(0, 100, mainListCellRenderer, mainListCellEditor));
        this.mainListCellEditor = mainListCellEditor;
        this.mainListCellRenderer = mainListCellRenderer;
        initComponents();
        this.mainList.setDefaultRenderer(Object.class, mainListCellRenderer);
        this.mainList.setDefaultEditor(Object.class, mainListCellEditor);
    }

    public void addItemListener(ListPanelItemListener itemListener) {
        this.itemListeners.add(itemListener);
    }

    public void removeItemListener(ListPanelItemListener itemListener) {
        this.itemListeners.remove(itemListener);
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
        mainList.setTableHeader(new JTableHeader(this.mainListColumnModel));
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
        T newElement = createNewElement();
        this.mainListModel.addRow(new Object[]{newElement}); //handles event firing
        for (ListPanelItemListener listener : this.itemListeners) {
            listener.onItemChanged(new ListPanelItemEvent(ListPanelItemEvent.EVENT_TYPE_ADDED,
                    AbstractListPanel.this.mainListModel.getRowCount(),
                    this.mainListModel.getData()));
        }
        this.updateRowHeights();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        LOGGER.debug("removing selected rows {}", this.mainList.getSelectedRows());
        //- TableModel doesn't expose methods which allow smart iteration
        //- no way found to sort int[] (!= Integer[]) in descending order, so
        //copying int[] into List<Integer> to be able to apply sort functions
        //with Comparator (using PriorityQueue to make copying and creation one
        //step)
        if (this.mainList.getSelectedRowCount() == 0) {
            return;
        }
        //avoid ArrayIndexOutOfBoundsException if editor performs actions on
        //removed rows (this also resolves strange issue there not the currently
        //edited row is removed)
        this.mainListCellEditor.stopCellEditing();

        PriorityQueue<Integer> selectedRowsSorted = new PriorityQueue<>(this.mainList.getSelectedRows().length, DESCENDING_ORDER);
        for (int selectedRow : this.mainList.getSelectedRows()) {
            selectedRowsSorted.add(selectedRow);
        }
        for (int selectedRow : selectedRowsSorted) {
            this.mainListModel.removeRow(selectedRow); //handles event firing
            for (ListPanelItemListener itemListener : this.getItemListeners()) {
                itemListener.onItemRemoved(new ListPanelItemEvent(ListPanelItemEvent.EVENT_TYPE_REMOVED,
                        selectedRow,
                        this.mainListModel.getData()));
            }
        }
    }//GEN-LAST:event_removeButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        this.mainListSelectionModel.addSelectionInterval(0, this.mainList.getRowCount() - 1);
        this.mainListModel.fireTableDataChanged();
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void invertSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionButtonActionPerformed
        for (int i = 0; i < this.mainListModel.getRowCount(); i++) {
            if (mainListSelectionModel.isSelectedIndex(i)) {
                this.mainListSelectionModel.removeIndexInterval(i, i);
            } else {
                this.mainListSelectionModel.addSelectionInterval(i, i);
            }
        }
        this.mainListModel.fireTableDataChanged();
    }//GEN-LAST:event_invertSelectionButtonActionPerformed

    private void updateRowHeights() {
        for (int row = 0; row < this.mainList.getRowCount(); row++) {
            int rowHeight = this.mainList.getRowHeight();

            for (int column = 0; column < this.mainList.getColumnCount(); column++) {
                Component comp = this.mainList.prepareRenderer(this.mainList.getCellRenderer(row, column), row, column);
                rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
            }

            this.mainList.setRowHeight(row, rowHeight);
        }
    }

    /**
     * Both needs to create the component which is added to the list in order to
     * manage elements of this list representation and handle adding the element
     * to {@code valueList}. This design allows to create the value and pass it
     * to the created component and as well to {@code valueList} (note that this
     * might not have an effect for mutable types).
     *
     * @return
     */
    protected abstract T createNewElement();

    public ReflectionFormBuilder getReflectionFormBuilder() {
        return reflectionFormBuilder;
    }

    public ListPanelTableModel getMainListModel() {
        return mainListModel;
    }

    public Set<ListPanelItemListener> getItemListeners() {
        return itemListeners;
    }

    public JTable getMainList() {
        return mainList;
    }

    public ListPanelTableCellEditor getMainListCellEditor() {
        return mainListCellEditor;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton invertSelectionButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable mainList;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton selectAllButton;
    // End of variables declaration//GEN-END:variables
}
