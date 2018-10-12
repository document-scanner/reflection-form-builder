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
package de.richtercloud.reflection.form.builder;

import java.awt.Component;
import java.awt.Container;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author richter
 */
public final class ReflectionFormBuilderUtils {

    public final static Comparator<Class<?>> CLASS_COMPARATOR_SUBCLASS_FIRST = (Class<?> o1, Class<?> o2) -> {
        if(o1.equals(o2)) {
            return 0;
        }else if(o1.isAssignableFrom(o2)) {
            return 1;
        }
        return -1;
    };

    public final static Comparator<Class<?>> CLASS_COMPARATOR_SUPERCLASS_FIRST = (Class<?> o1, Class<?> o2) -> {
        if(o1.equals(o2)) {
            return 0;
        }else if(o1.isAssignableFrom(o2)) {
            return -1;
        }
        return 1;
    };

    public static void disableRecursively(Container container,
            boolean enable) {
        Queue<Component> queue = new LinkedList<>(Arrays.asList(container.getComponents()));
        while(!queue.isEmpty()) {
            Component head = queue.poll();
            head.setEnabled(enable);
            if(head instanceof Container) {
                Container headCast = (Container) head;
                queue.addAll(Arrays.asList(headCast.getComponents()));
            }
        }
    }

    private ReflectionFormBuilderUtils() {
    }
}
