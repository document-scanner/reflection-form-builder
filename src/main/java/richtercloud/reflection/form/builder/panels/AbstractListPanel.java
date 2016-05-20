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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.reflection.form.builder.ReflectionFormBuilder;
import richtercloud.reflection.form.builder.fieldhandler.FieldHandlingException;
import richtercloud.reflection.form.builder.message.Message;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 * The superclass of all list panel (both with one or multiple columns). It supports multiple selection, also in multiple intervals of a size >= 1. Provides buttons to add a new item and remove the selected intervals(s). Provides a buttons to move the selected items up and down the list (if multiple items are selected they're moved as if they were the only selected one by one). The up button does nothing for the topmost item if it is selected as does the down button for the bottommost item. The edit button starts editing the item; implementation is up to subclasses (in {@link #editRow() }). The "Select all" and "Invert selection" buttons select all items and invert the selection of items respectively.
 *
 * @author richter
 * @param <T> the type of the managed values
 * @param <L> the type of the event listener to use
 * @param <M> the type of the main table model to use
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
public abstract class AbstractListPanel<T, L extends ListPanelItemListener<T>, M extends ListPanelTableModel<T>, R extends ReflectionFormBuilder> extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractListPanel.class);
    private R reflectionFormBuilder;
    public final static Comparator<Integer> DESCENDING_ORDER = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2.compareTo(o1);
        }
    };
    private ListSelectionModel mainListSelectionModel = new DefaultListSelectionModel();
    /**
     * The instance which manages the instances. There's no way to allow the
     * caller to change the specific type.
     */
    private Set<L> itemListeners = new HashSet<>();
    private ListPanelTableCellRenderer mainListCellRenderer;
    private ListPanelTableCellEditor mainListCellEditor;
    private M mainListModel;
    private MessageHandler messageHandler;
    private final List<T> initialValues;

    /**
     * Subclasses are strongly recommended to call {@link #reset() } to
     * initialize the values.
     *
     * @param reflectionFormBuilder
     * @param mainListCellEditor
     * @param mainListCellRenderer
     * @param mainListModel
     * @param initialValues
     * @param messageHandler
     * @param tableHeader a table header object which allows control over the
     * height of the table column header
     */
    /*
    internal implementation notes:
    - reset isn't called here because it uses an overridable call to
    TableModel.setValueAt which causes trouble in subclasses
    */
    public AbstractListPanel(R reflectionFormBuilder,
            ListPanelTableCellEditor mainListCellEditor,
            ListPanelTableCellRenderer mainListCellRenderer,
            M mainListModel,
            List<T> initialValues,
            MessageHandler messageHandler,
            JTableHeader tableHeader) {
        //not possible to call this() because of dependency on cell renderer and
        //editor
        this.reflectionFormBuilder = reflectionFormBuilder;
        //don't add an item initially because that is most certainly
        //unintentional
        this.mainListCellEditor = mainListCellEditor;
        this.mainListCellRenderer = mainListCellRenderer;
        this.mainListModel = mainListModel;
        initComponents();
        this.mainList.setTableHeader(tableHeader);
        this.mainList.setColumnModel(tableHeader.getColumnModel());
        this.mainList.setDefaultRenderer(Object.class, mainListCellRenderer);
        this.mainList.setDefaultEditor(Object.class, mainListCellEditor);
        this.messageHandler = messageHandler;
        this.initialValues = initialValues;
    }

    public void addItemListener(L itemListener) {
        this.itemListeners.add(itemListener);
    }

    public void removeItemListener(L itemListener) {
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
        mainListScrollPane = new javax.swing.JScrollPane();
        mainList = new javax.swing.JTable();
        editButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

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

        mainList.setModel(this.mainListModel);
        mainList.setCellEditor(mainListCellEditor);
        mainList.setSelectionModel(mainListSelectionModel);
        mainListScrollPane.setViewportView(mainList);

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        upButton.setText("Up");
        upButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });

        downButton.setText("Down");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invertSelectionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                    .addComponent(selectAllButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(upButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(downButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mainListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(upButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(downButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectAllButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(invertSelectionButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        this.mainListCellEditor.stopCellEditing();
        this.mainListCellEditor.resetCellEditorValue(); //avoid value of
            //previous edits to be set as initial value of new items

        //can't check field annotations, but class annotations and, of course,
        //use field handler
        T newElement = createNewElement();
        this.getMainListModel().addElement(newElement);
        for (L listener : this.getItemListeners()) {
            listener.onItemAdded(new ListPanelItemEvent<>(ListPanelItemEvent.EVENT_TYPE_ADDED,
                    AbstractListPanel.this.getMainListModel().getRowCount(),
                    AbstractListPanel.this.getMainListModel().getData()));
        }
        this.mainListModel.fireTableDataChanged();
        this.updateRowHeights();
        //open editing dialog immediately because the probability that the user
        //wants to edit immediately is high
        this.getMainList().getSelectionModel().setSelectionInterval(this.getMainListModel().getRowCount()-1,
                this.getMainListModel().getRowCount()-1);
        editRow0();
    }//GEN-LAST:event_addButtonActionPerformed

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        LOGGER.debug("removing selected rows {}", this.mainList.getSelectedRows());
        //- TableModel doesn't expose methods which allow smart iteration
        //- no way found to sort int[] (!= Integer[]) in descending order, so
        //copying int[] into List<Integer> to be able to apply sort functions
        //with Comparator (using PriorityQueue has no advantage because it needs
        //to be sorted before iterating over it (see doc of PriorityQueue for
        //details)
        if (this.mainList.getSelectedRowCount() == 0) {
            return;
        }
        //avoid ArrayIndexOutOfBoundsException if editor performs actions on
        //removed rows (this also resolves strange issue there not the currently
        //edited row is removed)
        this.mainListCellEditor.stopCellEditing();

        List<Integer> selectedRowsSorted = new LinkedList<>();
        for (int selectedRow : this.mainList.getSelectedRows()) {
            selectedRowsSorted.add(selectedRow);
        }
        Collections.sort(selectedRowsSorted, DESCENDING_ORDER);
        for (int selectedRow : selectedRowsSorted) {
            this.mainListModel.removeElement(selectedRow); //handles event firing
            for (ListPanelItemListener<T> itemListener : this.getItemListeners()) {
                itemListener.onItemRemoved(new ListPanelItemEvent<>(ListPanelItemEvent.EVENT_TYPE_REMOVED,
                        selectedRow,
                        this.mainListModel.getData()));
            }
        }
        this.mainListModel.fireTableDataChanged();
        this.updateRowHeights();
    }//GEN-LAST:event_removeButtonActionPerformed

    private void selectAllButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectAllButtonActionPerformed
        this.mainListSelectionModel.addSelectionInterval(0, this.mainList.getRowCount() - 1);
        this.mainListModel.fireTableDataChanged();
        this.updateRowHeights();
    }//GEN-LAST:event_selectAllButtonActionPerformed

    private void invertSelectionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invertSelectionButtonActionPerformed
        for (int i = 0; i < this.mainListModel.getRowCount(); i++) {
            if (this.mainListSelectionModel.isSelectedIndex(i)) {
                this.mainListSelectionModel.removeIndexInterval(i, i);
            } else {
                this.mainListSelectionModel.addSelectionInterval(i, i);
            }
        }
        this.mainListModel.fireTableDataChanged();
        this.updateRowHeights();
    }//GEN-LAST:event_invertSelectionButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        editRow0();
    }//GEN-LAST:event_editButtonActionPerformed

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upButtonActionPerformed
        this.mainListCellEditor.stopCellEditing();
        for(int selectedRow : this.mainList.getSelectedRows()) {
            if(selectedRow == 0) {
                //do nothing for the topmost item
                continue;
            }
            T toRemove = mainListModel.getData().get(selectedRow);
            LOGGER.debug(String.format("moving item %s from %d to %d", toRemove, selectedRow, selectedRow-1));
            mainListModel.removeElement(selectedRow);
            mainListModel.insertElementAt(selectedRow-1, toRemove);
        }
        this.mainListModel.fireTableDataChanged();
        this.updateRowHeights();
    }//GEN-LAST:event_upButtonActionPerformed

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downButtonActionPerformed
        this.mainListCellEditor.stopCellEditing();
        for(int selectedRow : this.mainList.getSelectedRows()) {
            if(selectedRow == mainList.getRowCount()-1) {
                //do nothing for the bottommost item
                continue;
            }
            T toRemove = mainListModel.getData().get(selectedRow);
            LOGGER.debug(String.format("moving item %s from %d to %d", toRemove, selectedRow, selectedRow+1));
            mainListModel.removeElement(selectedRow);
            mainListModel.insertElementAt(selectedRow+1, toRemove);
        }
        this.mainListModel.fireTableDataChanged();
        this.updateRowHeights();
    }//GEN-LAST:event_downButtonActionPerformed

    private void editRow0() {
        try {
            editRow();
        } catch (FieldHandlingException ex) {
            messageHandler.handle(new Message(String.format("An exception during editing the row occured: %s",
                    ExceptionUtils.getRootCauseMessage(ex)),
                    JOptionPane.ERROR_MESSAGE,
                    "Exception occured"));
        }
    }

    protected abstract void editRow() throws FieldHandlingException;

    protected void updateRowHeights() {
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

    public R getReflectionFormBuilder() {
        return reflectionFormBuilder;
    }

    public M getMainListModel() {
        return mainListModel;
    }

    public Set<L> getItemListeners() {
        return itemListeners;
    }

    public JTable getMainList() {
        return mainList;
    }

    public ListPanelTableCellEditor getMainListCellEditor() {
        return mainListCellEditor;
    }

    @SuppressWarnings("FinalMethod")
    public final void reset() {
        while(this.getMainListModel().getRowCount() > 0) {
            this.getMainListModel().removeElement(0);
        }
        if(initialValues != null) {
            for(T initialValue : initialValues) {
                this.getMainListModel().setValueAt(initialValue,
                        this.getMainListModel().getRowCount(),
                        0);
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private javax.swing.JButton editButton;
    private javax.swing.JButton invertSelectionButton;
    private javax.swing.JTable mainList;
    private javax.swing.JScrollPane mainListScrollPane;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton selectAllButton;
    private javax.swing.JButton upButton;
    // End of variables declaration//GEN-END:variables
}
