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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author richter
 * @param <T> see {@link Storage}
 * @param <C> see {@link Storage}
 */
public abstract class AbstractStorage<T, C extends StorageConf> implements Storage<T, C> {
    private final Map<Object, List<StorageCallback>> postStoreCallbackMap = new HashMap<>();
    private final Map<Object, List<StorageCallback>> preStoreCallbackMap = new HashMap<>();
    /**
     * A flag to improve feedback in case {@link #start() } hasn't been called.
     */
    private boolean started = false;

    @Override
    public void start() throws StorageCreationException {
        this.started = true;
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    public void registerPostStoreCallback(T object, StorageCallback callback) throws StorageException {
        List<StorageCallback> callbackList = postStoreCallbackMap.get(object);
        if(callbackList == null) {
            //calling store.update(object) doesn't assign ID (if @GeneratedValue
            //is used) and most likely causes a duplicate key exception when
            //calling storage.store for object
            callbackList = new LinkedList<>();
            postStoreCallbackMap.put(object,
                    callbackList);
        }
        callbackList.add(callback);
    }

    protected List<StorageCallback> getPostStoreCallbacks(T object) {
        List<StorageCallback> retValue = this.postStoreCallbackMap.get(object);
        return retValue;
    }

    @Override
    public void registerPreStoreCallback(T object, StorageCallback callback) throws StorageException {
        List<StorageCallback> callbackList = preStoreCallbackMap.get(object);
        if(callbackList == null) {
            //calling store.update(object) doesn't assign ID (if @GeneratedValue
            //is used) and most likely causes a duplicate key exception when
            //calling storage.store for object
            callbackList = new LinkedList<>();
            preStoreCallbackMap.put(object,
                    callbackList);
        }
        callbackList.add(callback);
    }

    protected List<StorageCallback> getPreStoreCallbacks(T object) {
        List<StorageCallback> retValue = this.preStoreCallbackMap.get(object);
        return retValue;
    }
}
