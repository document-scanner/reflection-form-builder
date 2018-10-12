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
package de.richtercloud.reflection.form.builder.storage;

import java.io.Serializable;

/**
 * Instances which ought to be persisted using certain {@link Storage}s somehow
 * need to be identified. Therefore the minimal requirement is to have a way to
 * distinguish entities. Some implementations of {@link Storage} might not need
 * to rely on having persisted instances implement this interface (e.g.
 * JPA-based implementations).
 *
 * It'd also be possible to enforce implementation of {@link #hashCode() }, but
 * that's troublesome and provide a {@link #getId() } isn't too much to ask.
 *
 * Implements {@link Serializable} because there's no case where you'd want to
 * have an {@code Identifiable} which you don't want to serialize.
 *
 * @author richter
 */
public interface Identifiable extends Serializable {

    Long getId();
}
