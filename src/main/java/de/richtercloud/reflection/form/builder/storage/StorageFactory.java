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

/**
 *
 * @author richter
 * @param <S> allows to enforce a type of storage
 * @param <C> allows to enforce a corresponding type of storage configuration
 */
public interface StorageFactory<S extends Storage, C extends StorageConf> {

    /**
     * Creates a {@link Storage} from {@code storageConf} if {@code storageConf}
     * is supported by the implementation.
     * @param storageConf the storage configuration to use
     * @return the created {@link Storage}
     * @throws StorageCreationException if an exception occured during the
     *     creation
     * @throws IllegalArgumentException if the type of {@code storageConf} isn't
     *     supported
     */
    /*
    internal implementation notes:
    - unsupported StorageConf types can be expressed either by returning null or
    throwing an exception -> exception is more restrictive
    */
    S create(C storageConf) throws StorageCreationException;
}
