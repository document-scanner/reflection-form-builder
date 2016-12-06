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
package richtercloud.reflection.form.builder.storage;

/**
 *
 * @author richter
 * @param <T>
 */
public interface Storage<T, C extends StorageConf> {

    void delete(Object object) throws StorageException;

    void store(Object object) throws StorageException;

    /**
     * Retrieves the instance with ID {@code id} of type {@code clazz} from
     * storage.
     * @param id
     * @param clazz
     * @return
     * @throws StorageException
     */
    /*
    internal implementation notes:
    - id should be a of type Object in order be able to support composite id
    keys in JPA implementations
    */
    T retrieve(Object id, Class<? extends T> clazz) throws StorageException;

    void update(Object object) throws StorageException;

    /**
     * Frees eventually aquired resources.
     */
    void shutdown();

    C getStorageConf();
}
