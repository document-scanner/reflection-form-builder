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

import org.slf4j.Logger;

/**
 * Forwards all messages as info messages to the specified logger.
 * @author richter
 */
public class LoggerMessageHandler implements MessageHandler<Message> {
    private Logger logger;

    public LoggerMessageHandler(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void handle(Message message) {
        logger.info(message.getText());
    }
}
