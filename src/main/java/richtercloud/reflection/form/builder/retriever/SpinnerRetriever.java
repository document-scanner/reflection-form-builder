/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder.retriever;

import javax.swing.JSpinner;
import richtercloud.reflection.form.builder.ValueRetriever;

/**
 *
 * @author richter
 */
public class SpinnerRetriever implements ValueRetriever<Number, JSpinner>{
    private final static SpinnerRetriever instance = new SpinnerRetriever();

    public static SpinnerRetriever getInstance() {
        return instance;
    }

    protected SpinnerRetriever() {
    }

    @Override
    public Number retrieve(JSpinner comp) {
        return (Number) comp.getValue();
    }

}
