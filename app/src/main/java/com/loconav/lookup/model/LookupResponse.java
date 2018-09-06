package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by prateek on 12/05/18.
 */

public class LookupResponse implements Serializable {

    @SerializedName("data")
    private List<Entity> data ;
    @SerializedName("passed")
    private Boolean passed;

    public List<Entity> getData() {
        return data;
    }

    public void setData(List<Entity> data) {
        this.data = data;
    }

    public Boolean getPassed() { return passed; }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

}
