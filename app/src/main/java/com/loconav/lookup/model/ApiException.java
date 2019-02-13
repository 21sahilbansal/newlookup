package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ApiException implements Parcelable {
    @SerializedName("crash_log")
    private
    String crash_log;

    @SerializedName("note")
    private
    String note;

    public ApiException()
    {

    }

    private ApiException(Parcel in) {
        crash_log = in.readString();
        note = in.readString();
    }

    public static final Creator<ApiException> CREATOR = new Creator<ApiException>() {
        @Override
        public ApiException createFromParcel(Parcel in) {
            return new ApiException(in);
        }

        @Override
        public ApiException[] newArray(int size) {
            return new ApiException[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(crash_log);
        dest.writeString(note);
    }
}
