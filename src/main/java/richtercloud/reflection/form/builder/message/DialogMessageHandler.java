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
package richtercloud.reflection.form.builder.message;

import java.awt.Frame;
import javax.swing.JOptionPane;

/**
 *
 * @author richter
 */
public class DialogMessageHandler implements MessageHandler {
    private final Frame parent;
    private final String title;

    /**
     *
     * @param parent the parent of the {@link JOptionPane} to be displayed when
     * a message is handled
     * @param title the title of the {@link JOptionPane} to be displayed when a
     * message is handled
     */
    public DialogMessageHandler(Frame parent, String title) {
        this.parent = parent;
        this.title = title;
    }

    @Override
    public void handle(Message message) {
        JOptionPane.showMessageDialog(parent,
                message.getText(),
                title,
                message.getType());
    }
}
