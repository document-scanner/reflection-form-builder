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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author richter
 */
/*
internal implementation notes:
- reusage of Java Message Service API classes doesn't work because
javax.jms.Message provides too many options
- moving summary (e.g. to be used in a dialog) to a subclass causes trouble
using MessageHandler in library classes
*/
public class Message {
    public final static Set<Integer> ALLOWED_TYPES = new HashSet<>(Arrays.asList(JOptionPane.ERROR_MESSAGE,
            JOptionPane.INFORMATION_MESSAGE,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.WARNING_MESSAGE,
            JOptionPane.QUESTION_MESSAGE));
    private final String text;
    /**
     * reuse {@link JOptionPane#ERROR_MESSAGE}, {@link JOptionPane#INFORMATION_MESSAGE}, {@link JOptionPane#PLAIN_MESSAGE}, {@link JOptionPane#WARNING_MESSAGE} ({@link JOptionPane#QUESTION_MESSAGE} isn't used because {@link MessageHandler} doesn't allow interaction.
     */
    private final int type;
    /**
     * A summary of the message which can be used in a dialog title, but isn't
     * necessarily specified.
     */
    private final String summary;

    public Message(String text, int type, String summary) {
        if(!ALLOWED_TYPES.contains(type)) {
            throw new IllegalArgumentException(String.format("type has to be one of '%s'", ALLOWED_TYPES));
        }
        this.text = text;
        this.type = type;
        this.summary = summary;
    }

    public Message(Throwable throwable, int type) {
        this(ExceptionUtils.getRootCauseMessage(throwable),
                type,
                throwable.getClass().getSimpleName());
    }

    public int getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getSummary() {
        return summary;
    }
}
