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
package richtercloud.reflection.form.builder.panels;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Implementations have to notify all {@link CancelablePanelListener}s when a
 * GUI component starts the task which are available in
 * {@link #getListeners() }.
 *
 * @author richter
 */
public abstract class CancelablePanelPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private final Set<CancelablePanelListener> listeners = new HashSet<>();

    public void addCancelablePanelListener(CancelablePanelListener listener) {
        this.listeners.add(listener);
    }

    public void removeCancelablePanelListener(CancelablePanelListener listener) {
        this.listeners.remove(listener);
    }

    protected Set<CancelablePanelListener> getListeners() {
        return Collections.unmodifiableSet(listeners);
    }
}
