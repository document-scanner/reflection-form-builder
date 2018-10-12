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
 *
 * @author richter
 * @param <E> the type of the event
 */
@FunctionalInterface
public interface FieldUpdateListener<E extends FieldUpdateEvent<?>> {

    /**
     * Called if a component is updated. They type of the update is determined
     * by the {@code type} property of the used {@link FieldUpdateEvent}.
     * @param event the event to handle
     */
    void onUpdate(E event);
}
