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

import de.richtercloud.message.handler.ExceptionMessage;
import de.richtercloud.message.handler.MessageHandler;
import java.awt.Frame;
import javax.swing.DefaultComboBoxModel;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
@SuppressWarnings({"PMD.FieldDeclarationsShouldBeAtStartOfClass",
    "PMD.SingularField"
})
public class AmountMoneyComponentEditDialog extends javax.swing.JDialog {
    private static final long serialVersionUID = 1L;
    private final DefaultComboBoxModel<Currency> referenceCurrencyComboBoxModel = new DefaultComboBoxModel<>();
    private final AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever;
    private Currency currency;
    private final MessageHandler messageHandler;

    /*
    internal implementation notes:
    - Since it's not possible to change code of a Currency, but the dialog ought
    to provide the possibility to change it, there's no way to rely to the
    passed reference to Currency in the constructor.
    */
    /**
     * Creates new form AmountMoneyPanelEditDialog.
     * @param currency the currency to edit
     * @param amountMoneyCurrencyStorage the currency storage to use to save the
     *     edit
     * @param amountMoneyExchangeRateRetriever the exchange rate retriever to
     *     use
     * @param messageHandler the message handler to use
     * @param parent the dialog parent
     * @throws AmountMoneyCurrencyStorageException if an exception during the
     *     check whether {@code currency} is contained into
     *     {@code amountMoneyCurrencyStorage} occurs
     */
    public AmountMoneyComponentEditDialog(Currency currency,
            AmountMoneyCurrencyStorage amountMoneyCurrencyStorage,
            AmountMoneyExchangeRateRetriever amountMoneyExchangeRateRetriever,
            Frame parent,
            MessageHandler messageHandler) throws AmountMoneyCurrencyStorageException {
        super(parent,
                true //modal
        );
        if(messageHandler == null) {
            throw new IllegalArgumentException("messageHandler mustn't be null");
        }
        this.messageHandler = messageHandler;
        if(currency != null) {
            if(!amountMoneyCurrencyStorage.getCurrencies().contains(currency)) {
                throw new IllegalArgumentException(String.format("Currency '%s' isn't managed in amountMoneyCurrencyStorage and thus can't be edited", currency.getCode()));
            }
            codeTextField.setText(currency.getCode());
            exchangeRateSpinner.setValue(currency.getExchangeRate());
        }
        for(Currency currency0 : amountMoneyCurrencyStorage.getCurrencies()) {
            this.referenceCurrencyComboBoxModel.addElement(currency0);
        }
        this.currency = currency;
        this.amountMoneyExchangeRateRetriever = amountMoneyExchangeRateRetriever;
        initComponents();
    }

    public Currency getCurrency() {
        return currency;
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

        codeTextField = new javax.swing.JTextField();
        codeTextFieldLabel = new javax.swing.JLabel();
        referenceCurrencyComboBox = new javax.swing.JComboBox<>();
        referenceCurrencyComboBoxLabel = new javax.swing.JLabel();
        exchangeRateSpinner = new javax.swing.JSpinner();
        exchangeRateSpinnerLabel = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        codeTextFieldLabel.setText("Code");

        referenceCurrencyComboBox.setModel(referenceCurrencyComboBoxModel);

        referenceCurrencyComboBoxLabel.setText("Reference currency");

        exchangeRateSpinner.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, null, 1.0d));
        exchangeRateSpinner.setValue(1.0);

        exchangeRateSpinnerLabel.setText("Exchange rate to reference currency");

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(codeTextFieldLabel)
                            .addComponent(referenceCurrencyComboBoxLabel)
                            .addComponent(exchangeRateSpinnerLabel))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(referenceCurrencyComboBox, 0, 100, Short.MAX_VALUE)
                            .addComponent(codeTextField)
                            .addComponent(exchangeRateSpinner, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(saveButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(codeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(codeTextFieldLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(referenceCurrencyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(referenceCurrencyComboBoxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(exchangeRateSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exchangeRateSpinnerLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 153, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.setVisible(false);
        this.dispose(); //reset at close
    }//GEN-LAST:event_cancelButtonActionPerformed

    @SuppressWarnings("PMD.UnusedFormalParameter")
    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        String code = this.codeTextField.getText();
        Currency referenceCurrency = (Currency) this.referenceCurrencyComboBox.getSelectedItem();
        Double exchangeRate = (Double) this.exchangeRateSpinner.getValue();
        if(currency == null) {
            currency = new Currency(code);
        }
        if(Currency.getReferenceCurrency() == null) {
            //reference currency has to be set in order to make setting exchange
            //rates work below
            Currency.setReferenceCurrency(AmountMoneyComponent.REFERENCE_CURRENCY);
        }
        if(!referenceCurrency.equals(Currency.getReferenceCurrency())) {
            //if the reference currency used to enter the exchange rate isn't
            //the same as the one internally used in the system, another
            //conversion has to be performed
            try {
                this.amountMoneyExchangeRateRetriever.retrieveExchangeRate(referenceCurrency);
            } catch (AmountMoneyExchangeRateRetrieverException ex) {
                messageHandler.handle(new ExceptionMessage(ex));
                return;
            }
            exchangeRate = referenceCurrency.getConverterTo(Currency.getReferenceCurrency()).convert(exchangeRate);
        }
        currency.setExchangeRate(exchangeRate);
        //handle changes to amountMoneyCurrencyStorage in caller
        this.setVisible(false);
        this.dispose(); //reset at close
    }//GEN-LAST:event_saveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField codeTextField;
    private javax.swing.JLabel codeTextFieldLabel;
    private javax.swing.JSpinner exchangeRateSpinner;
    private javax.swing.JLabel exchangeRateSpinnerLabel;
    private javax.swing.JComboBox<Currency> referenceCurrencyComboBox;
    private javax.swing.JLabel referenceCurrencyComboBoxLabel;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}