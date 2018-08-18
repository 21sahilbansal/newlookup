package com.loconav.lookup;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejal on 30-07-2018.
 */

public class Input implements Serializable {

    private String name;
    private String key;
    private String field_type;
    private String validations;
    private String hint;
    private ArrayList<String> data;

    public Input(String name, String key, String field_type,String validations, String hint) {
        this.name = name;
        this.key = key;
        this.hint=hint;
        this.field_type = field_type;
        this.validations = validations;
    }

    public Input(String name, String key, String field_type, String validations, String hint, ArrayList<String> data) {
        this.name = name;
        this.key = key;
        this.field_type = field_type;
        this.validations = validations;
        this.hint = hint;
        this.data = data;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getField_type() {
        return field_type;
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

    public  ArrayList<String> getData() {
        return data;
    }

    public void setData( ArrayList<String> data) {
        this.data = data;
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
}
