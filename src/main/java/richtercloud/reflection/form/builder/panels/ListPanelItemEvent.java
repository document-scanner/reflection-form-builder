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

import java.util.List;

/**
 *
 * @author richter
 */
public class ListPanelItemEvent {
    public final static int EVENT_TYPE_ADDED = 1;
    public final static int EVENT_TYPE_REMOVED = 2;
    public final static int EVENT_TYPE_CHANGED = 4;
    /**
     * indicates whether the item has been added or removed
     */
    private int eventType;
    /**
     * Need an index in order to deal with types which don't have an identity,
     * but only equality comparison (e.g. wrappers).
     */
    private int index;
    private List<?> item;

    public ListPanelItemEvent(int eventType, int index, List<?> item) {
        this.eventType = eventType;
        this.index = index;
        this.item = item;
    }

    public int getEventType() {
        return eventType;
    }

    public List<?> getItem() {
        return item;
    }

    public int getIndex() {
        return index;
    }

}
