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
package richtercloud.reflection.form.builder;

import java.util.List;

/**
 * The event doesn't care about the list added because a caller is most likely
 only interested in updating the field with the passed listreference.
 * @author richter
 */
public class GenericListFieldUpdateEvent implements UpdateEvent<List<?>> {
    public final static int EVENT_TYPE_ADDED = 1;
    public final static int EVENT_TYPE_REMOVED = 2;
    public final static int EVENT_TYPE_CHANGED = 4;
    private int eventType;
    private List<?> newValue;

    public GenericListFieldUpdateEvent(int eventType, List<?> newValue) {
        this.eventType = eventType;
        this.newValue = newValue;
    }

    public int getEventType() {
        return eventType;
    }

    @Override
    public List<?> getNewValue() {
        return newValue;
    }
}
