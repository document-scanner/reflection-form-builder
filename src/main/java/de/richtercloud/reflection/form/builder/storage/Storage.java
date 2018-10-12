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
 * @param <T> the type the storage ought to be restricted
 * @param <C> the type of configuration this type of storage needs
 */
public interface Storage<T, C extends StorageConf> {

    void delete(T object) throws StorageException;

    void store(T object) throws StorageException;

    /**
     * Retrieves the instance with ID {@code id} of type {@code clazz} from
     * storage.
     * @param id
     * @param clazz
     * @param <U> allows enforcing a subtype of the type of the storage via the
     * {@code clazz} parameter
     * @return the retrieved object
     * @throws StorageException wraps any exception occuring during storage
     */
    /*
    internal implementation notes:
    - id should be a of type Object in order be able to support composite id
    keys in JPA implementations
    */
    <U extends T> U retrieve(Object id, Class<U> clazz) throws StorageException;

    void update(T object) throws StorageException;

    void refresh(Object object) throws StorageException;

    /**
     * Starts referenced resources. Needs to be called after creating an
     * instance of any subclass (except if the subclass especially states that
     * it's not necessary).
     * @throws de.richtercloud.reflection.form.builder.storage.StorageCreationException
     * wraps any exception which occurs during the creation of this storage
     */
    /*
    internal implementation notes:
    - This is necessary in order to keep process handling properties for MySQL
    and PostgreSQL server process control output of AbstractPersistenceStorage
    and keep a basic sanity which is lost easily when using overridable methods
    in constructor of AbstractPersistenceStorage (and it's very bad style anyway
    and for a reason). It's perfectly fine to ask to initialize resource after
    object creation, especially if a shutdown method is provided.
    - not sure, but this might be an interceptor pattern and it might be right
    or wrong (there might be something easier) to use it here
    */
    void start() throws StorageCreationException;

    boolean isStarted();

    /**
     * Frees eventually aquired resources.
     */
    void shutdown();

    C getStorageConf();

    /**
     * Registers a callback which is executed right after {@code object} is
     * stored with {@link #store(java.lang.Object) }.
     *
     * Note that entities are supposed to be equals in all persistent entity
     * states transient, attached, detached and removed<ref>https://vladmihalcea.com/2014/07/30/a-beginners-guide-to-jpahibernate-entity-state-transitions/</ref>
     * and if they're not this method will explicitly has undefined behaviour.
     *
     * @param object the object the callback ought to be registered for
     * @param callback the callback to register
     * @throws StorageException wraps any exception occuring during storage
     */
    /*
    internal implementation notes:
    - if registery for update, deletion, etc. is added, it might be necessary to
    make StorageCallback executable repeatedly
    */
    void registerPostStoreCallback(T object,
            StorageCallback callback) throws StorageException;

    /**
     * Registers a callback which is executed right before {@code object} is
     * stored with {@link #store(java.lang.Object) }.
     *
     * Note that entities are supposed to be equals in all persistent entity
     * states transient, attached, detached and removed<ref>https://vladmihalcea.com/2014/07/30/a-beginners-guide-to-jpahibernate-entity-state-transitions/</ref>
     * and if they're not this method will explicitly has undefined behaviour.
     *
     * @param object the object the callback ought to be registered for
     * @param callback the callback to register
     * @throws StorageException wraps any exception occuring during storage
     */
    void registerPreStoreCallback(T object,
            StorageCallback callback) throws StorageException;
}
