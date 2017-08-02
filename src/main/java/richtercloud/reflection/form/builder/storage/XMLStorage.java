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

import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

/**
 *
 * @author richter
 * @param <T> allows enforcing subtypes of {@link Identifiable}
 */
public class XMLStorage<T extends Identifiable> implements Storage<T, XMLStorageConf> {
    private final static String NOT_SUPPORTED = "Not supported yet.";
    private File file;
    private final XMLStorageConf storageConf;

    public XMLStorage(XMLStorageConf storageConf) throws FileNotFoundException {
        this.file = storageConf.getFile();
        this.storageConf = storageConf;
    }

    @Override
    public void store(T object) throws StorageException {
        XStream xStream = new XStream();
        List<Object> existingObjects;
        try {
            existingObjects = (List<Object>) xStream.fromXML(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw new StorageException(ex);
        }
        existingObjects.add(object);
        xStream = new XStream();
        try {
            xStream.toXML(existingObjects, new FileOutputStream(file));
        } catch (FileNotFoundException ex) {
            throw new StorageException(ex);
        }
    }

    @Override
    public <U extends T> U retrieve(Object id, Class<U> clazz) throws StorageException {
        //@TODO: this is most certainly more efficient when implemented with an XStream ObjectInputStream
        XStream xStream = new XStream();
        List<U> existingObjects;
        try {
            existingObjects = (List<U>) xStream.fromXML(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw new StorageException(ex);
        }
        for(U existingObject : existingObjects) {
            if(existingObject.getId().equals(id)) {
                return existingObject;
            }
        }
        return null;
    }

    @Override
    public void update(T object) throws StorageException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void delete(T object) throws StorageException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void refresh(Object object) throws StorageException {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void start() throws StorageCreationException {
        //nothing to do
    }

    @Override
    public void shutdown() {
        //nothing to do
    }

    @Override
    public XMLStorageConf getStorageConf() {
        return storageConf;
    }

    @Override
    public void registerPostStoreCallback(T object, StorageCallback callback) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }

    @Override
    public void registerPreStoreCallback(T object, StorageCallback callback) {
        throw new UnsupportedOperationException(NOT_SUPPORTED);
    }
}
