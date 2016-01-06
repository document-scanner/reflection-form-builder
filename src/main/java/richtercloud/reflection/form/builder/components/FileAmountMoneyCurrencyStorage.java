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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Set;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
public class FileAmountMoneyCurrencyStorage implements AmountMoneyCurrencyStorage {
    private final File file;

    public FileAmountMoneyCurrencyStorage(File file) {
        this.file = file;
    }

    @Override
    public Set<Currency> getCurrencies() throws AmountMoneyCurrencyStorageException {
        try {
            XMLDecoder xMLDecoder = new XMLDecoder(new FileInputStream(file));
            Set<Currency> additionalCurrencies = (Set<Currency>) xMLDecoder.readObject();
            return additionalCurrencies;
        } catch (FileNotFoundException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

    @Override
    public void saveCurrency(Currency currency) throws AmountMoneyCurrencyStorageException {
        Set<Currency> additionalCurrencies = getCurrencies();
        additionalCurrencies.add(currency);
        XMLEncoder xMLEncoder;
        try {
            xMLEncoder = new XMLEncoder(new FileOutputStream(file));
            xMLEncoder.writeObject(additionalCurrencies);
            xMLEncoder.flush();
            xMLEncoder.close();
        } catch (FileNotFoundException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

    @Override
    public void removeCurrency(Currency currency) throws AmountMoneyCurrencyStorageException {
        Set<Currency> additionalCurrencies = getCurrencies();
        additionalCurrencies.remove(currency);
        XMLEncoder xMLEncoder;
        try {
            xMLEncoder = new XMLEncoder(new FileOutputStream(file));
            xMLEncoder.writeObject(additionalCurrencies);
        } catch (FileNotFoundException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

}
