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

import java.util.Collections;
import java.util.List;

/**
 *
 * @author richter
 * @param <T> the type of values managed by the {@link AbstractListPanel} which
 * produces this event
 */
/*
internal implementation notes:
- different types require different event types which dependent on the usage of lists:
  - primitive lists which allow editing entries have event types (added, removed, edited) -> ListPanelItemEvent
  - a query list panel for a list of references many-to-1 or many-to-many has added and removed only -> ListPanelItemEvent
  - a query panel for one reference 1-to-1 or 1-to-many has a selection change event, but no editing event at all -> ListPanelSelectionEvent
*/
public class ListPanelItemEvent<T> {
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
    private List<T> item;

    /**
     *
     * @param eventType
     * @param index
     * @param item automatically internally transformed into unmodifiable collection (i.e. there's no need for callers to do that transformation)
     */
    public ListPanelItemEvent(int eventType, int index, List<T> item) {
        this.eventType = eventType;
        this.index = index;
        this.item = item;
    }

    public int getEventType() {
        return eventType;
    }

    /*
    internal implementation notes:
    - must not be unmodifiable because Hibernate will invoke clear on it
    */
    public List<T> getItem() {
        return Collections.unmodifiableList(item);
    }

    public int getIndex() {
        return index;
    }

}
