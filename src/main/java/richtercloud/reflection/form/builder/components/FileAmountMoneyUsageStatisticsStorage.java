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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.jscience.economics.money.Currency;

/**
 *
 * @author richter
 */
public class FileAmountMoneyUsageStatisticsStorage implements AmountMoneyUsageStatisticsStorage {
    private final File file;
    private final Properties properties;

    /**
     * Creates a {@code FileAmountMoneyUsageStatisticsStorage} and loads info
     * from {@code file} if it exists.
     * @param file the file to load info from
     * @throws IOException if an {@link IOException} occurs during loading of
     * info from {@code file}.
     */
    public FileAmountMoneyUsageStatisticsStorage(File file) throws IOException {
        this.file = file;
        this.properties = new Properties();
        if(file.exists()) {
            this.properties.loadFromXML(new FileInputStream(file));
        }
    }

    @Override
    public int getUsageCount(Currency currency) {
        return Integer.valueOf(this.properties.getProperty(currency.getCode()));
    }

    @Override
    public void reset(Currency currency) throws AmountMoneyUsageStatisticsStorageException {
        this.properties.remove(currency);
        try {
            this.properties.storeToXML(new FileOutputStream(file), new Date().toString());
        } catch (IOException ex) {
            throw new AmountMoneyUsageStatisticsStorageException(ex);
        }
    }

    @Override
    public void incrementUsageCount(Currency currency) throws AmountMoneyUsageStatisticsStorageException {
        int usageCount = this.getUsageCount(currency);
        this.properties.setProperty(currency.getCode(), String.valueOf(usageCount+1));
        try {
            this.properties.storeToXML(new FileOutputStream(file), new Date().toString());
        } catch (IOException ex) {
            throw new AmountMoneyUsageStatisticsStorageException(ex);
        }
    }

}
