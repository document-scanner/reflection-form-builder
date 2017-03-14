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
package richtercloud.reflection.form.builder.fieldhandler.factory;

import junit.framework.Assert;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import richtercloud.message.handler.IssueHandler;
import richtercloud.message.handler.LoggerIssueHandler;

/**
 *
 * @author richter
 */
public class MappingFieldHandlerFactoryTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MappingFieldHandlerFactoryTest.class);
    private final IssueHandler issueHandler = new LoggerIssueHandler(LOGGER);

    /**
     * Test of getInstance method, of class MappingFieldHandlerFactory.
     */
    @Test
    public void testGetInstance() {
        try {
            MappingFieldHandlerFactory result = new MappingFieldHandlerFactory(null);
            Assert.fail("IllegalArgumentException expected");
        }catch(IllegalArgumentException ex) {
            //expected
        }
        MappingFieldHandlerFactory result = new MappingFieldHandlerFactory(issueHandler);
        assertNotNull(result);
    }
}
