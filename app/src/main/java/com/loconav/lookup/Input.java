package com.loconav.lookup;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejal on 30-07-2018.
 */

public class Input implements Serializable {

    String name;
    String key;
    String field_type;
    String validations;
    String[] inputData;

    public Input(String name, String key, String field_type) {
        this.name = name;
        this.key = key;
        this.field_type = field_type;
    }


    public void setField_type(String field_type) {
        this.field_type = field_type;
    }

    public String getValidations() {
        return validations;
    }

    public void setValidations(String validations) {
        this.validations = validations;
    }

    public String[] getInputData() {
        return inputData;
    }

    public void setInputData(String[] inputData) {
        this.inputData = inputData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getField_type() {
        return field_type;
    }

    public void setFeild_type(String feild_type) {
        this.field_type = feild_type;
    }
}
