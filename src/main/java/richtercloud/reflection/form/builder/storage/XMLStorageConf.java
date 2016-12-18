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
import java.util.List;
import java.util.Objects;

/**
 *
 * @author richter
 */
public class XMLStorageConf implements StorageConf {
    private File file;

    protected XMLStorageConf() {
    }

    public XMLStorageConf(File file) throws FileNotFoundException {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public void validate() throws StorageConfInitializationException {
        XStream xStream = new XStream();
        try {
            List<Object> existingObjects = (List<Object>) xStream.fromXML(new FileInputStream(file));
        } catch (FileNotFoundException ex) {
            throw new StorageConfInitializationException(ex);
        }
    }

    @Override
    public String getShortDescription() {
        return "XML file storage";
    }

    @Override
    public String getLongDescription() {
        return "Stores data in an XML file (quite inefficient)";
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.file);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XMLStorageConf other = (XMLStorageConf) obj;
        if (!Objects.equals(this.file, other.file)) {
            return false;
        }
        return true;
    }
}
