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
package richtercloud.reflection.form.builder.message;

/**
 *
 * @author richter
 */
public class DialogMessage extends Message {
    private final String title;

    public DialogMessage(String title, String text, int type) {
        super(text, type);
        this.title = title;
    }

    public DialogMessage(String title, Throwable throwable, int type) {
        super(throwable, type);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
