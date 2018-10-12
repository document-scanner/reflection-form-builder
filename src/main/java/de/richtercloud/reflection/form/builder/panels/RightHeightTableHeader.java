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
package de.richtercloud.reflection.form.builder.panels;

import java.awt.Dimension;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author richter
 */
public class RightHeightTableHeader extends JTableHeader {

    private static final long serialVersionUID = 1L;
    private final int height;

    public RightHeightTableHeader(int height) {
        super();
        this.height = height;
    }

    public RightHeightTableHeader(TableColumnModel tableColumnModel, int height) {
        super(tableColumnModel);
        this.height = height;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(super.getPreferredSize().width, height);
    }

}
