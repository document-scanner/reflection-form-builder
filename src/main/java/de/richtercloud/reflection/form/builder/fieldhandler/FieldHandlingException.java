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
package de.richtercloud.reflection.form.builder.fieldhandler;

/**
 * Wraps all exception which can occur during read or write access to fields
 * using the Java reflection API. Since fields are retrieved using well-tested
 * mechanisms such exceptions are unlikely to occur and are thus provided in
 * this more convenient way.
 *
 * @author richter
 */
public class FieldHandlingException extends Exception {
    private static final long serialVersionUID = 1L;

    public FieldHandlingException(String message) {
        super(message);
    }

    public FieldHandlingException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldHandlingException(Throwable cause) {
        super(cause);
    }

}
