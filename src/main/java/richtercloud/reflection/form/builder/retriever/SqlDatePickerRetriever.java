/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package richtercloud.reflection.form.builder.retriever;

import java.util.Date;
import richtercloud.reflection.form.builder.components.SqlDatePicker;

/**
 *
 * @author richter
 */
public class SqlDatePickerRetriever implements ValueRetriever<Date, SqlDatePicker> {
    private final static SqlDatePickerRetriever instance = new SqlDatePickerRetriever();

    public static SqlDatePickerRetriever getInstance() {
        return instance;
    }

    protected SqlDatePickerRetriever() {
    }

    @Override
    public Date retrieve(SqlDatePicker comp) {
        Date retValue = comp.getModel().getValue();
        return retValue;
    }
    
}
