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

    public Input(String name, String key, String field_type) {
        this.name = name;
        this.key = key;
        this.field_type = field_type;
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

    public String getFeild_type() {
        return field_type;
    }

    public void setFeild_type(String feild_type) {
        this.field_type = feild_type;
    }
}
