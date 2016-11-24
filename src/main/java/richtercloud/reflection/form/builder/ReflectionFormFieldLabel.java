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
package richtercloud.reflection.form.builder;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Group;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * A small component encapsulating the name and description of a field. The
 * design is: a label for the name and a tooltip text and a button labeled "?"
 * opening a dialog for the description.
 *
 * Default value for the description is controled by the default value of
 * {@link FieldInfo#description() }.
 *
 * @author richter
 */
public class ReflectionFormFieldLabel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int MAX_WIDTH_DEFAULT = 50;

    public ReflectionFormFieldLabel(String fieldName,
            final String fieldDescription,
            final String fieldDescriptionDialogTitle) {
        this(fieldName, fieldDescription, fieldDescriptionDialogTitle, MAX_WIDTH_DEFAULT);
    }

    /**
     *
     * @param fieldName
     * @param fieldDescription if empty, the ? button won't be displayed
     * @param fieldDescriptionDialogTitle
     * @param labelMaxWidth the maximal width of the label part
     */
    /*
    internal implementation notes:
    - rather than indicating that the field description isn't provided with null
    use "" for that because it doesn't have any use anyway
    */
    public ReflectionFormFieldLabel(String fieldName,
            final String fieldDescription,
            final String fieldDescriptionDialogTitle,
            int labelMaxWidth) {
        if(fieldName == null) {
            throw new IllegalArgumentException("fieldName mustn't be null");
        }
        if(fieldName.isEmpty()) {
            throw new IllegalArgumentException("fieldName mustn't be empty");
        }
        if(fieldDescription == null) {
            throw new IllegalArgumentException("fieldDescription mustn't be null");
        }
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
//        this.setMaximumSize(new Dimension(maxWidth, -1));
        JLabel fieldNameLabel = new JLabel(String.format("<html>%s</html>", fieldName));
        Group layoutHorizontalGroup = layout.createSequentialGroup();
        Group layoutVerticalGroup = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
        layoutHorizontalGroup.addComponent(fieldNameLabel,
                GroupLayout.DEFAULT_SIZE,
                labelMaxWidth,
                GroupLayout.PREFERRED_SIZE);
        layoutVerticalGroup.addComponent(fieldNameLabel);
        if(!fieldDescription.isEmpty()) {
            fieldNameLabel.setToolTipText(fieldDescription);
            JButton fieldDescriptionButton = new JButton("?");
            fieldDescriptionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showConfirmDialog(ReflectionFormFieldLabel.this, //parent
                            fieldDescription, //message
                            fieldDescriptionDialogTitle, //title
                            JOptionPane.DEFAULT_OPTION, //optionType
                            JOptionPane.INFORMATION_MESSAGE //messageType
                    );
                }
            });
            layoutHorizontalGroup.addGap(8);
            layoutHorizontalGroup.addComponent(fieldDescriptionButton);
            layoutVerticalGroup.addComponent(fieldDescriptionButton);
        }
        layout.setHorizontalGroup(layoutHorizontalGroup);
        layout.setVerticalGroup(layoutVerticalGroup);
    }
}
