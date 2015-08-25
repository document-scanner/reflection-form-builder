/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder.retriever;

import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 *
 * @author richter
 */
public class CheckBoxRetriever implements ValueRetriever<Boolean, JCheckBox>{
    private final static CheckBoxRetriever instance = new CheckBoxRetriever();

    public static CheckBoxRetriever getInstance() {
        return instance;
    }

    protected CheckBoxRetriever() {
    }

    @Override
    public Boolean retrieve(JCheckBox comp) {
        Boolean retValue = comp.isSelected();
        return retValue;
    }
    
}
