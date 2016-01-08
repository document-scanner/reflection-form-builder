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

import java.lang.reflect.Field;

/**
 *
 * @author richter
 */
public class ReflectionFormPanelUpdateEvent {
    public final static int INSTANCE_DELETED = 1;
    public final static int INSTANCE_SAVED = 2;
    public final static int INSTANCE_UPDATED = 4;
    public final static int INSTANCE_FIELD_CHANGED = 8;
    public final static int INSTANCE_RESET = 16;
    /**
     * the changed field
     */
    private Field field;
    /**
     * The deleted, saved or updated instance
     */
    private Object instance;
    private int type;

    public ReflectionFormPanelUpdateEvent(int type, Field field, Object instance) {
        this.type = type;
        this.field = field;
        this.instance = instance;
    }

    public int getType() {
        return type;
    }

    public Object getInstance() {
        return instance;
    }

    public Field getField() {
        return field;
    }
}
