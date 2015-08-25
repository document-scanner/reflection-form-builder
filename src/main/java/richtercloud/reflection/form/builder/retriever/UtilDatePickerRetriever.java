/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder.retriever;

import java.util.Date;
import richtercloud.reflection.form.builder.components.UtilDatePicker;

/**
 *
 * @author richter
 */
public class UtilDatePickerRetriever implements ValueRetriever<Date, UtilDatePicker> {
    private final static UtilDatePickerRetriever instance = new UtilDatePickerRetriever();

    public static UtilDatePickerRetriever getInstance() {
        return instance;
    }

    protected UtilDatePickerRetriever() {
    }

    @Override
    public Date retrieve(UtilDatePicker comp) {
        Date retValue = comp.getModel().getValue();
        return retValue;
    }
    
}
