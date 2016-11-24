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
package richtercloud.reflection.form.builder.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.swing.GroupLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * An abstract superclass for components which don't have a control to specify
 * the value {@code null} in their model (or have a function for that which is
 * just too complicated to figure out intuitively). Expects a
 * {@code mainComponent} to be passed to constructor which might allow the value
 * {@code null} to be managed in its model.
 *
 * Subclasses need to add action listeners to {@code mainComponent} and notify
 * all {@link NullableComponentUpdateListener}s (using
 * {@link #getUpdateListeners() }) when changes in the UI occur.
 * @author richter
 * @param <T> the type of value which is managed by {@code mainComponent}
 * @param <C> the type of {@code mainComponent}
 */
public abstract class NullableComponent<T, C extends JComponent> extends JPanel {
    private static final long serialVersionUID = 1L;
    private final JCheckBox checkBox = new JCheckBox("null");
    private final Set<NullableComponentUpdateListener<NullableComponentUpdateEvent<T>>> updateListeners = new HashSet<>();
    private final T initialValue;
    private final C mainComponent;

    /**
     * Creates a new {@code NullableComponent}.
     * @param initialValue
     * @param mainComponent
     */
    /*
    internal implementation notes:
    - mainComponent is passed in constructor in order to make it usable in
    constructor (method in subclass isn't available)
    */
    public NullableComponent(T initialValue,
            final C mainComponent) {
        this.initialValue = initialValue;
        this.mainComponent = mainComponent;
        GroupLayout groupLayout = new GroupLayout(this);
        setLayout(groupLayout);
        groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
                .addComponent(mainComponent,
                        0,
                        GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(checkBox));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(mainComponent,
                        0,
                        GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)
                .addComponent(checkBox));
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainComponent.setEnabled(!checkBox.isSelected());
                //if checkbox is deactived a default value != null has to be
                //set, otherwise the model/value and GUI are in a divergent
                //state -> since mainComponent.getValue0 can return null use
                //setValue0 because otherwise the checkbox is checked and
                //mainComponent gets disabled again
                setValue0(getValue0());
                for(NullableComponentUpdateListener<NullableComponentUpdateEvent<T>> updateListener : getUpdateListeners()) {
                    updateListener.onUpdate(new NullableComponentUpdateEvent<>(getValue()));
                }
            }
        });
        reset0();
    }

    public C getMainComponent() {
        return mainComponent;
    }

    public void addUpdateListener(NullableComponentUpdateListener<NullableComponentUpdateEvent<T>> updateListener) {
        this.updateListeners.add(updateListener);
    }

    public void removeUpdateListener(NullableComponentUpdateListener<NullableComponentUpdateEvent<T>> updateListener) {
        this.updateListeners.remove(updateListener);
    }

    protected Set<NullableComponentUpdateListener<NullableComponentUpdateEvent<T>>> getUpdateListeners() {
        return Collections.unmodifiableSet(updateListeners);
    }

    public T getValue() {
        if(checkBox.isSelected()) {
            return null;
        }
        return getValue0();
    }

    /**
     * Sets {@code value} on {@code mainComponent} and notifies
     * {@link NullableComponentUpdateListener}s.
     * @param value the value to be set
     */
    public void setValue(T value) {
        //checkBox is always enabled
        this.checkBox.setSelected(value == null);
        mainComponent.setEnabled(value != null);
        if(value != null) {
            setValue0(value);
        }
        for(NullableComponentUpdateListener<NullableComponentUpdateEvent<T>> updateListener : updateListeners) {
            updateListener.onUpdate(new NullableComponentUpdateEvent<>(value));
        }
    }

    /**
     * Sets {@code value} on {@code mainComponent}. Doesn't notify
     * {@link NullableComponentUpdateListener}s.
     * @param value the value (might be {@code null} if the component supports
     * it
     */
    protected abstract void setValue0(T value);

    /**
     * Gets the value of {@code mainComponent} regardless of whether
     * {@code checkBox} is checked or not.
     * @return the value (might be {@code null}, see class comment for details)
     */
    protected abstract T getValue0();

    private void reset0() {
        setValue(this.initialValue);
    }

    public void reset() {
        reset0();
    }
}
