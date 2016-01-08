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
package richtercloud.reflection.form.builder.components;

import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.MutableComboBoxModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jscience.economics.money.Currency;
import org.jscience.economics.money.Money;
import org.jscience.physics.amount.Amount;
import richtercloud.reflection.form.builder.message.Message;
import richtercloud.reflection.form.builder.message.MessageHandler;

/**
 * A GUI component which allows to specify amount and currency at once and
 * manage (adding and removing) currencies (the list of crypto currencies will
 * be outdated soon).
 *
 * The list of available currencies is exclusively managed by {@link AmountMoneyCurrencyStorage}.
 *
 * @author richter
 */
/*
internal implementation notes:
- there's no need to add a sorting function since currencies are already sorted by their usage count and there's no sense in manually overwriting this sorting if there's a function to reset the usage count
- managing all dialogs in extra classes reduces the need to synchronize different collections and models if dialogs are disposed on closure and recreated at reopening
*/
public class AmountMoneyPanel extends javax.swing.JPanel {
    private static final long serialVersionUID = 1L;
    private MutableComboBoxModel<Currency> currencyComboBoxModel = new DefaultComboBoxModel<>();
    public final static Set<Currency> DEFAULT_CURRENCIES = new HashSet<>(Arrays.asList(Currency.AUD,
            Currency.CAD,
            Currency.CNY,
            Currency.EUR,
            Currency.GBP,
            Currency.JPY,
            Currency.KRW,
            Currency.TWD,
            Currency.USD));
    private final Set<AmountMoneyPanelUpdateListener> updateListeners = new HashSet<>();
    private final AmountMoneyCurrencyStorage amountMoneyCurrencyStorage;
    private final AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage;
    private final MessageHandler messageHandler;

    /**
     * Creates a new AmountMoneyPanel with {@link #DEFAULT_CURRENCIES}.
     * @param amountMoneyUsageStatisticsStorage
     * @param messageHandler
     * @throws richtercloud.reflection.form.builder.components.AmountMoneyCurrencyStorageException
     */
    public AmountMoneyPanel(AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage,
            MessageHandler messageHandler) throws AmountMoneyCurrencyStorageException {
        this(null,
                amountMoneyUsageStatisticsStorage,
                messageHandler);
    }

    /**
     * Creates a new AmountMoneyPanel with {@link #DEFAULT_CURRENCIES} and
     * {@code additionalCurrencies}.
     * @param amountMoneyCurrencyStorage
     * @param amountMoneyUsageStatisticsStorage
     * @param messageHandler
     * @throws richtercloud.reflection.form.builder.components.AmountMoneyCurrencyStorageException
     */
    public AmountMoneyPanel(AmountMoneyCurrencyStorage amountMoneyCurrencyStorage,
            AmountMoneyUsageStatisticsStorage amountMoneyUsageStatisticsStorage,
            MessageHandler messageHandler) throws AmountMoneyCurrencyStorageException {
        this.messageHandler = messageHandler;
        for(Currency currency : amountMoneyCurrencyStorage.getCurrencies()) {
            currencyComboBoxModel.addElement(currency);
        }
        initComponents();
        this.amountIntegerSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for(AmountMoneyPanelUpdateListener updateListener : AmountMoneyPanel.this.updateListeners) {
                    updateListener.onUpdate(new AmountMoneyPanelUpdateEvent(retrieveAmountMoney()));
                }
            }
        });
        this.amountDecimalSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                for(AmountMoneyPanelUpdateListener updateListener : AmountMoneyPanel.this.updateListeners) {
                    updateListener.onUpdate(new AmountMoneyPanelUpdateEvent(retrieveAmountMoney()));
                }
            }
        });
        this.currencyComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                //convert after currency change (not necessary, but useful)
                Currency oldCurrency = (Currency) e.getItem();
                Currency newCurrency = (Currency) e.getItemSelectable().getSelectedObjects()[0];
                double newAmount = oldCurrency.getConverterTo(newCurrency).convert(retrieveAmount());
                AmountMoneyPanel.this.amountIntegerSpinner.setValue((int)newAmount);
                AmountMoneyPanel.this.amountDecimalSpinner.setValue(newAmount%1);
                //notify registered update listeners
                for(AmountMoneyPanelUpdateListener updateListener : AmountMoneyPanel.this.updateListeners) {
                    updateListener.onUpdate(new AmountMoneyPanelUpdateEvent(retrieveAmountMoney()));
                }
            }
        });
        this.amountMoneyCurrencyStorage = amountMoneyCurrencyStorage;
        this.amountMoneyUsageStatisticsStorage = amountMoneyUsageStatisticsStorage;
    }

    private double retrieveAmount() {
        double amount = ((double)amountIntegerSpinner.getValue())+((double)amountDecimalSpinner.getValue());
        return amount;
    }

    public Amount<Money> retrieveAmountMoney() {
        double amount = retrieveAmount();
        return Amount.valueOf(amount, (Currency)currencyComboBoxModel.getSelectedItem());
    }

    public void applyAmountMoney(Amount<Money> amountMoney) {
        double amount = amountMoney.doubleValue(amountMoney.getUnit());
        this.amountIntegerSpinner.setValue((int)amount);
        this.amountDecimalSpinner.setValue(amount % 1);
        this.currencyComboBox.setSelectedItem(amountMoney.getUnit());
    }

    public void addUpdateListener(AmountMoneyPanelUpdateListener updateListener) {
        this.updateListeners.add(updateListener);
    }

    public void removeUpdateListener(AmountMoneyPanelUpdateListener updateListener) {
        this.updateListeners.remove(updateListener);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        currencyComboBox = new javax.swing.JComboBox<>();
        currencyLabel = new javax.swing.JLabel();
        amountDecimalSpinner = new javax.swing.JSpinner();
        amountIntegerSpinner = new javax.swing.JSpinner();
        amountLabel = new javax.swing.JLabel();
        currencyManageButton = new javax.swing.JButton();

        currencyComboBox.setModel(currencyComboBoxModel);

        currencyLabel.setText("Curreny:");

        amountDecimalSpinner.setMinimumSize(new java.awt.Dimension(40, 28));

        amountIntegerSpinner.setMinimumSize(new java.awt.Dimension(40, 28));

        amountLabel.setText("Amount:");

        currencyManageButton.setText("Manage");
        currencyManageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currencyManageButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(amountLabel)
                .addGap(18, 18, 18)
                .addComponent(amountIntegerSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(amountDecimalSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(currencyLabel)
                .addGap(18, 18, 18)
                .addComponent(currencyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(currencyManageButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currencyComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currencyLabel)
                    .addComponent(amountDecimalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amountIntegerSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(amountLabel)
                    .addComponent(currencyManageButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void currencyManageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currencyManageButtonActionPerformed
        AmountMoneyPanelManageDialog amountMoneyPanelManageDialog;
        try {
            amountMoneyPanelManageDialog = new AmountMoneyPanelManageDialog(amountMoneyCurrencyStorage,
                    messageHandler,
                    (Frame) SwingUtilities.getWindowAncestor(this));
        } catch (AmountMoneyCurrencyStorageException ex) {
            this.messageHandler.handle(new Message(String.format("An exception occured during retrieval of currencies from the storage: %s", ExceptionUtils.getRootCauseMessage(ex)), JOptionPane.ERROR_MESSAGE));
            return;
        }
        amountMoneyPanelManageDialog.pack();
        amountMoneyPanelManageDialog.setLocationRelativeTo(this);
        amountMoneyPanelManageDialog.setVisible(true);
    }//GEN-LAST:event_currencyManageButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner amountDecimalSpinner;
    private javax.swing.JSpinner amountIntegerSpinner;
    private javax.swing.JLabel amountLabel;
    private javax.swing.JComboBox<Currency> currencyComboBox;
    private javax.swing.JLabel currencyLabel;
    private javax.swing.JButton currencyManageButton;
    // End of variables declaration//GEN-END:variables
}
