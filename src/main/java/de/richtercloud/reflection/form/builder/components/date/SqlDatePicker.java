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
package de.richtercloud.reflection.form.builder.components.date;

import de.richtercloud.reflection.form.builder.components.NullableComponent;
import de.richtercloud.reflection.form.builder.components.NullableComponentUpdateEvent;
import de.richtercloud.reflection.form.builder.components.NullableComponentUpdateListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import org.jdatepicker.JSqlDatePicker;

/**
 *
 * @author richter
 */
public class SqlDatePicker extends NullableComponent<Date, JSqlDatePicker> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    public SqlDatePicker(Date initialValue) {
        super(initialValue,
                new JSqlDatePicker());
        getMainComponent().addActionListener((ActionEvent e) -> {
            for(NullableComponentUpdateListener updateListener : getUpdateListeners()) {
                updateListener.onUpdate(new NullableComponentUpdateEvent(getValue()));
            }
        });
    }

    @Override
    protected void setValue0(Date value) {
        this.getMainComponent().getModel().setValue(value);
    }

    @Override
    protected Date getValue0() {
        return this.getMainComponent().getModel().getValue();
    }
}