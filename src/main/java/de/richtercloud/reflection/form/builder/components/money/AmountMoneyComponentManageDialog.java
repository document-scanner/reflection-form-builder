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
package de.richtercloud.reflection.form.builder.components.money;

import de.richtercloud.message.handler.Message;
import de.richtercloud.message.handler.MessageHandler;
import java.awt.Frame;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
@SuppressWarnings({"PMD.FieldDeclarationsShouldBeAtStartOfClass",
    "PMD.SingularField"
})
public class AmountMoneyComponentManageDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;
    private final static String CURRENCY_STORAGE_EXCEPTION_TEMPLATE = "An exception occured during retrieval of currencies from the storage: %s";
    private final static String CURRENCY_STORAGE_EXCEPTION_SUMMARY = "Exception during currency storage occured";
    private final AmountMoneyCurrencyStorage amountMoneyCurrencyStorage;
    private final AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever;
    private final DefaultListModel<Currency> currencyListModel = new DefaultListModel<>();
    private final MessageHandler messageHandler;

    /**
     * Creates new form AmountMoneyPanelManageDialog
     * @param amountMoneyCurrencyStorage the currency storage to use
     * @param amountMoneyExchangeRateRetriever the exchange rate retriever to
     *     use
     * @param messageHandler the message handler to use
     * @param parent the dialog parent
     * @throws AmountMoneyCurrencyStorageException if access to the currency
     *     storage fails
     */
    public AmountMoneyComponentManageDialog(AmountMoneyCurrencyStorage amountMoneyCurrencyStorage,
            AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever,
            MessageHandler messageHandler,
            Frame parent) throws AmountMoneyCurrencyStorageException {
        super(parent,
                true //modal
        );
        for(Currency currency : amountMoneyCurrencyStorage.getCurrencies()) {
            currencyListModel.addElement(currency);
        }
        this.amountMoneyCurrencyStorage = amountMoneyCurrencyStorage;
        this.amountMoneyExchangeRateRetriever = amountMoneyExchangeRateRetriever;
        this.messageHandler = messageHandler;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({"unchecked",
        "PMD.AccessorMethodGeneration"
    })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        currencyListScrollPane = new javax.swing.JScrollPane();
        currencyList = new javax.swing.JList<>();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        resetUsageCountButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        currencyList.setModel(currencyListModel);
        currencyListScrollPane.setViewportView(currencyList);

        addButton.setText("+");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText("-");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        editButton.setText("Edit");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });

        resetUsageCountButton.setText("Reset usage count");

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(currencyListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(resetUsageCountButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(removeButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(closeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(resetUsageCountButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(currencyListScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addButtonActionPerformed
        AmountMoneyComponentEditDialog amountMoneyPanelEditDialog;
        try {
            amountMoneyPanelEditDialog = new AmountMoneyComponentEditDialog(null, //currency (null indicates creation of a new currency)
                    this.amountMoneyCurrencyStorage,
                    this.amountMoneyExchangeRateRetriever,
                    (Frame) SwingUtilities.getWindowAncestor(this), //parent
                    messageHandler
            );
        } catch (AmountMoneyCurrencyStorageException ex) {
            this.messageHandler.handle(new Message(String.format(CURRENCY_STORAGE_EXCEPTION_TEMPLATE,
                    ExceptionUtils.getRootCauseMessage(ex)),
                    JOptionPane.ERROR_MESSAGE,
                    CURRENCY_STORAGE_EXCEPTION_SUMMARY));
            return;
        }
        amountMoneyPanelEditDialog.pack();
        amountMoneyPanelEditDialog.setVisible(true);
        Currency newCurrency = amountMoneyPanelEditDialog.getCurrency();
        try {
            this.amountMoneyCurrencyStorage.saveCurrency(newCurrency);
        } catch (AmountMoneyCurrencyStorageException ex) {
            this.messageHandler.handle(new Message(String.format(CURRENCY_STORAGE_EXCEPTION_TEMPLATE,
                    ExceptionUtils.getRootCauseMessage(ex)),
                    JOptionPane.ERROR_MESSAGE,
                    CURRENCY_STORAGE_EXCEPTION_SUMMARY));
            return;
        }
        currencyListModel.add(0, newCurrency);
    }//GEN-LAST:event_addButtonActionPerformed

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed
        Currency selectedCurrency = currencyList.getSelectedValue();
        if(selectedCurrency == null) {
            return;
        }
        try {
            //since AmountMoneyCurrencyStorage doesn't keep track of the order of
            //currencies, simply remove and add the old and new instance
            this.amountMoneyCurrencyStorage.removeCurrency(selectedCurrency);
        } catch (AmountMoneyCurrencyStorageException ex) {
            this.messageHandler.handle(new Message(String.format(CURRENCY_STORAGE_EXCEPTION_TEMPLATE,
                    ExceptionUtils.getRootCauseMessage(ex)),
                    JOptionPane.ERROR_MESSAGE,
                    CURRENCY_STORAGE_EXCEPTION_SUMMARY));
            return;
        }
        currencyListModel.remove(currencyList.getSelectedIndex());
    }//GEN-LAST:event_removeButtonActionPerformed

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        Currency selectedCurrency = this.currencyList.getSelectedValue();
        AmountMoneyComponentEditDialog amountMoneyPanelEditDialog;
        try {
            amountMoneyPanelEditDialog = new AmountMoneyComponentEditDialog(selectedCurrency, //currency
                    this.amountMoneyCurrencyStorage,
                    this.amountMoneyExchangeRateRetriever,
                    (Frame) SwingUtilities.getWindowAncestor(this), //parent
                    messageHandler
            );
        } catch (AmountMoneyCurrencyStorageException ex) {
            this.messageHandler.handle(new Message(String.format(CURRENCY_STORAGE_EXCEPTION_TEMPLATE,
                    ExceptionUtils.getRootCauseMessage(ex)),
                    JOptionPane.ERROR_MESSAGE,
                    CURRENCY_STORAGE_EXCEPTION_SUMMARY));
            return;
        }
        amountMoneyPanelEditDialog.pack();
        amountMoneyPanelEditDialog.setVisible(true);
        Currency editedCurrency = amountMoneyPanelEditDialog.getCurrency();
        try {
            //since AmountMoneyCurrencyStorage doesn't keep track of the order of
            //currencies, simply remove and add the old and new instance
            this.amountMoneyCurrencyStorage.removeCurrency(selectedCurrency);
            this.amountMoneyCurrencyStorage.saveCurrency(editedCurrency);
        } catch (AmountMoneyCurrencyStorageException ex) {
            this.messageHandler.handle(new Message(String.format(CURRENCY_STORAGE_EXCEPTION_TEMPLATE,
                    ExceptionUtils.getRootCauseMessage(ex)),
                    JOptionPane.ERROR_MESSAGE,
                    CURRENCY_STORAGE_EXCEPTION_SUMMARY));
            return;
        }
        int selectedCurrencyIndex = currencyListModel.indexOf(selectedCurrency);
        currencyListModel.remove(selectedCurrencyIndex);
        currencyListModel.add(selectedCurrencyIndex, editedCurrency);
    }//GEN-LAST:event_editButtonActionPerformed

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JList<Currency> currencyList;
    private javax.swing.JScrollPane currencyListScrollPane;
    private javax.swing.JButton editButton;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton resetUsageCountButton;
    // End of variables declaration//GEN-END:variables
}