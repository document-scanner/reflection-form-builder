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

/**
 * An interface to implement different ways of passing messages (confirmations,
 * warnings, errors, etc.) to the user and log files.
 * @author richter
 */
/*
internal implementation notes:
- There's no sense in handling logging of message in MessageHandler because it's
hard to define a generic interface and SLF4J logger doesn't make it too easy to
log exceptions based on metadata (there's no even a method to log with a numeric
severity) and logging at the place where the exception occurs/is caught provides
more information anyway.
*/
public interface MessageHandler {

    void handle(Message message);
}
