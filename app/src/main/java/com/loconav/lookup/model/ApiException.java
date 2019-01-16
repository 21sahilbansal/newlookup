package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiException implements Serializable {
    @SerializedName("crash_log")
    String crash_log;

    @SerializedName("note")
    String note;

    public String getCrash_log() {
        return crash_log;
    }

    public void setCrash_log(String crash_log) {
        this.crash_log = crash_log;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
