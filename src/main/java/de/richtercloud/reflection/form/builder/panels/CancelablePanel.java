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

import de.richtercloud.reflection.form.builder.ReflectionFormBuilderUtils;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A panel which allows blocking the GUI while performing a task in a background
 * thread. The task can be canceled using a button which is displayed in the
 * component block. Both the blockage and the cancelation are optional and are
 * controlled with the parameters of {@link #startTask(boolean, boolean) }.
 *
 * @author richter
 * @param <P> the type of panel to use as main panel
 * @param <T> the type of background task result
 */
@SuppressWarnings("PMD.SingularField")
public abstract class CancelablePanel<P extends CancelablePanelPanel, T> extends JPanel {
    private static final long serialVersionUID = 1L;
    private final static String CANCEL_PANEL_CONSTRAINT = "cancel-panel";
    private final static String MAIN_PANEL_CONSTRAINT = "main-panel";
    private final P mainPanel;
    private final Box.Filler filler1;
    private final Box.Filler filler2;
    private final JButton cancelButton;
    private final JPanel cancelPanel;
    private final LayoutManager layout = new CardLayout();

    public CancelablePanel(P mainPanel) {
        super();
        this.mainPanel = mainPanel;
        mainPanel.addCancelablePanelListener((boolean async, boolean cancelable) -> {
            doTask(async, cancelable);
        });
        setLayout(layout);
        filler2 = new Box.Filler(new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener((event) -> {
            cancelTask();
        });
        filler1 = new Box.Filler(new Dimension(0, 0),
                new Dimension(0, 0),
                new Dimension(32767, 0));
        this.cancelPanel = new JPanel();
        this.cancelPanel.setLayout(new BoxLayout(cancelPanel,
                BoxLayout.LINE_AXIS));
        cancelPanel.add(filler1);
        cancelPanel.add(cancelButton);
        cancelPanel.add(filler2);
        add(cancelPanel,
                CANCEL_PANEL_CONSTRAINT);
        add(mainPanel,
                MAIN_PANEL_CONSTRAINT);
            //There's no way to do that in NetBeans GUI builder
        CardLayout layout = (CardLayout) getLayout();
        layout.show(this,
                MAIN_PANEL_CONSTRAINT);
    }

    public P getMainPanel() {
        return mainPanel;
    }

    /**
     *
     * @param async whether the task ought to be run in a background thread or
     *     on the thread invoking this methods (show be the event dispatch
     *     thread (EDT))
     * @param cancelable whether the background task ought to be cancelable
     */
    public void doTask(boolean async,
            boolean cancelable) {
        ReflectionFormBuilderUtils.disableRecursively(mainPanel,
                false //enable
        );
        if(cancelable) {
            CardLayout layout = (CardLayout) getLayout();
            layout.show(this,
                    CANCEL_PANEL_CONSTRAINT);
        }
        if(!async) {
            T nonGUIResult = doTaskNonGUI();
            if(nonGUIResult == null) {
                    //exception occured in doTaskNonGUI
                return;
            }
            doTaskGUI0(nonGUIResult);
        }else {
            Thread backgroundThread = new Thread(() -> {
                T nonGUIResult = doTaskNonGUI();
                if(nonGUIResult == null) {
                    //exception occured in doTaskNonGUI
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    doTaskGUI0(nonGUIResult);
                });
            },
                    "ocr-fetch-thread");
            backgroundThread.start();
        }
    }

    /**
     * Fetches the OCR result from {@link oCRResultPanelFetcher} and catches
     * eventual exceptions.
     * @return the OCR result or {@code null} iff an exception occured.
     */
    protected abstract T doTaskNonGUI();

    private void doTaskGUI0(T nonGUIResult) {
        ReflectionFormBuilderUtils.disableRecursively(mainPanel,
                true //enable
        );
        CardLayout layout = (CardLayout) getLayout();
        layout.show(this,
                MAIN_PANEL_CONSTRAINT);
        doTaskGUI(nonGUIResult);
    }

    protected abstract void doTaskGUI(T nonGUIResult);

    /**
     * Called when the cancel button is pressed. Needs to get
     * {@link #doTaskNonGUI() } to return a value which indicates cancelation.
     */
    protected abstract void cancelTask();
}
