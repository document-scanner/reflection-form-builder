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
package de.richtercloud.reflection.form.builder.fieldhandler.factory;

import de.richtercloud.message.handler.IssueHandler;
import de.richtercloud.message.handler.LoggerIssueHandler;
import static org.junit.Assert.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author richter
 */
public class MappingFieldHandlerFactoryTest {
    private final static Logger LOGGER = LoggerFactory.getLogger(MappingFieldHandlerFactoryTest.class);

    /**
     * Test of getInstance method, of class MappingFieldHandlerFactory.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetInstanceNull() {
        new MappingFieldHandlerFactory(null);
    }

    @Test
    public void testGetInstance() {
        IssueHandler issueHandler = new LoggerIssueHandler(LOGGER);
        MappingFieldHandlerFactory result = new MappingFieldHandlerFactory(issueHandler);
        assertNotNull(result);
    }
}
