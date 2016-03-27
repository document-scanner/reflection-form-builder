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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import javax.measure.converter.ConversionException;
import org.jscience.economics.money.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An {@link AmountMoneyCurrencyStorage} which saves to a file. The current
 * implementation is very inefficient and reads and writes the file on every
 * operation, i.e. doesn't cache.
 *
 * @author richter
 */
/*
internal implementation notes:
- XMLEncoder doesn't write content of HashSet<Currency> (prints <code>
java.lang.InstantiationException: org.jscience.economics.money.Currency
Continuing ...
java.lang.Exception: XMLEncoder: discarding statement HashSet.add(Currency);
Continuing ...
</code> which might be the reason or not; XStream fails with nonsense exception
`java.lang.ArrayIndexOutOfBoundsException: -1`; using `ObjectOutputStream` and
`ObjectInputStream` doesn't work because `Currency` isn't serializable (only
`static final` constants in `Currency` work) -> use XStream with custom
`Converter`
*/
public class FileAmountMoneyCurrencyStorage implements AmountMoneyCurrencyStorage {
    private final static Logger LOGGER = LoggerFactory.getLogger(FileAmountMoneyCurrencyStorage.class);
    private final static String CURRENCY_CODE_ATTRIBUTE_NAME = "code";
    private final static String CURRENCY_EXCHANGE_RATE_ATTRIBUTE_NAME = "exchangeRate";
    private final static Converter CURRENCY_CONVERTER = new Converter() {
        @Override
        public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
            Currency currency = (Currency) o;
            //unnecessary to start an extra node with writer.startNode
            writer.addAttribute(CURRENCY_CODE_ATTRIBUTE_NAME, currency.getCode());
            try {
                writer.addAttribute(CURRENCY_EXCHANGE_RATE_ATTRIBUTE_NAME, String.valueOf(currency.getExchangeRate()));
            }catch(ConversionException ex) {
                //skip
            }
        }

        @Override
        public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
            String code = reader.getAttribute(CURRENCY_CODE_ATTRIBUTE_NAME);
            Currency currency = new Currency(code);
            String exchangeRateString = reader.getAttribute(CURRENCY_EXCHANGE_RATE_ATTRIBUTE_NAME);
                //seems to return null if attribute isn't present, improvement
                //of docs requested as
                //https://github.com/x-stream/xstream/issues/47
            if(exchangeRateString != null) {
                double exchangeRate = Double.valueOf(exchangeRateString);
                currency.setExchangeRate(exchangeRate);
            }
            return currency;
        }

        @Override
        public boolean canConvert(Class type) {
            boolean retValue = Currency.class.isAssignableFrom(type);
            return retValue;
        }
    };
    private final File file;

    public FileAmountMoneyCurrencyStorage(File file) {
        this.file = file;
    }

    @Override
    public Set<Currency> getCurrencies() throws AmountMoneyCurrencyStorageException {
        if(!file.exists()) {
            LOGGER.info(String.format("Currency storage file '%s' doesn't exist, creating it "
                    + "with default currencies", file));
            if(!file.getParentFile().exists()) {
                if(!file.getParentFile().mkdirs()) {
                    throw new AmountMoneyCurrencyStorageException(String.format("Currency "
                            + "storage file's parent directory '%s' couldn't "
                            + "be created", file.getParentFile()));
                }
            }
            saveCurrencies(AmountMoneyPanel.DEFAULT_CURRENCIES);
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            XStream xStream = new XStream();
            xStream.registerConverter(CURRENCY_CONVERTER);
            Set<Currency> currencies = (Set<Currency>) xStream.fromXML(inputStream);
            if(currencies.isEmpty()) {
                LOGGER.info(String.format("Currency storage file '%s' contains "
                        + "0 currency, using default currencies", file));
                saveCurrencies(AmountMoneyPanel.DEFAULT_CURRENCIES);
                return AmountMoneyPanel.DEFAULT_CURRENCIES;
            }
            return currencies;
        } catch (IOException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

    private void saveCurrencies(Set<Currency> currencies) throws AmountMoneyCurrencyStorageException {
        try {
            try (OutputStream outputStream = new FileOutputStream(file)) {
                XStream xStream = new XStream();
                xStream.registerConverter(CURRENCY_CONVERTER);
                xStream.toXML(currencies, outputStream);
                outputStream.flush();
            }
        } catch (IOException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

    @Override
    public void saveCurrency(Currency currency) throws AmountMoneyCurrencyStorageException {
        Set<Currency> additionalCurrencies = getCurrencies();
        additionalCurrencies.add(currency);
        saveCurrencies(additionalCurrencies);
    }

    @Override
    public void removeCurrency(Currency currency) throws AmountMoneyCurrencyStorageException {
        Set<Currency> currentCurrencies = getCurrencies();
        currentCurrencies.remove(currency);
        try {
            try (OutputStream outputStream = new FileOutputStream(file)) {
                XStream xStream = new XStream();
                xStream.registerConverter(CURRENCY_CONVERTER);
                xStream.toXML(currentCurrencies, outputStream);
                outputStream.flush();
            }
        } catch (IOException ex) {
            throw new AmountMoneyCurrencyStorageException(ex);
        }
    }

}
