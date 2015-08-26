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
package richtercloud.reflection.form.builder.retriever;

import java.util.Date;
import richtercloud.reflection.form.builder.components.SqlDatePicker;

/**
 *
 * @author richter
 */
public class SqlDatePickerRetriever implements ValueRetriever<Date, SqlDatePicker> {
    private final static SqlDatePickerRetriever INSTANCE = new SqlDatePickerRetriever();

    public static SqlDatePickerRetriever getInstance() {
        return INSTANCE;
    }

    protected SqlDatePickerRetriever() {
    }

    @Override
    public Date retrieve(SqlDatePicker comp) {
        Date retValue = comp.getModel().getValue();
        return retValue;
    }
    
}
