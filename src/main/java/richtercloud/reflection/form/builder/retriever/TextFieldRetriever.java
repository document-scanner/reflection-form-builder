/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder.retriever;

import javax.swing.JTextField;

/**
 *
 * @author richter
 */
public class TextFieldRetriever implements ValueRetriever<String, JTextField>{
    private final static TextFieldRetriever instance = new TextFieldRetriever();

    public static TextFieldRetriever getInstance() {
        return instance;
    }

    protected TextFieldRetriever() {
    }

    @Override
    public String retrieve(JTextField comp) {
        return comp.getText();
    }

}
