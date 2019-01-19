package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loconav.lookup.login.model.User;

/**
 * Created by sejal on 18-07-2018.
 */

public class InstallationResponse implements Parcelable {

    @SerializedName("message")
    @Expose
    private String message;

    public InstallationResponse()
    {

    }

    protected InstallationResponse(Parcel in) {
        message = in.readString();
    }

    public static final Creator<InstallationResponse> CREATOR = new Creator<InstallationResponse>() {
        @Override
        public InstallationResponse createFromParcel(Parcel in) {
            return new InstallationResponse(in);
        }

        @Override
        public InstallationResponse[] newArray(int size) {
            return new InstallationResponse[size];
        }
    };

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
    }
}
