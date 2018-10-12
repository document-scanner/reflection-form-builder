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
 */
public class DelegatingStorageFactory implements StorageFactory<Storage, StorageConf>{
    private final XMLStorageFactory xMLStorageFactory;

    public DelegatingStorageFactory() {
        this.xMLStorageFactory = new XMLStorageFactory();
    }

    public DelegatingStorageFactory(XMLStorageFactory xMLStorageFactory) {
        this.xMLStorageFactory = xMLStorageFactory;
    }

    @Override
    public Storage create(StorageConf storageConf) throws StorageCreationException {
        Storage<?,?> retValue;
        if(storageConf instanceof XMLStorageConf) {
            retValue = xMLStorageFactory.create((XMLStorageConf) storageConf);
        }else {
            throw new IllegalArgumentException(String.format("Storage configurations of type '%s' aren't supported by this storage factory",
                    storageConf.getClass()));
        }
        return retValue;
    }
}
