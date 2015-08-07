/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder;

import javax.swing.JComponent;

/**
 * Due to the fact that the {@code JComponent} inheritance hierarchy can't be
 * changed in order to provide a unique method to access value. That's why this
 * interface is introduced.
 * @author richter
 */
public interface ValueRetriever<T, C extends JComponent> {

    T retrieve(C comp);
}
