package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 27-07-2018.
 */

public class RepairResponse implements Parcelable {
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public RepairResponse()
    {

    }

    protected RepairResponse(Parcel in) {
        byte tmpSuccess = in.readByte();
        success = tmpSuccess == 0 ? null : tmpSuccess == 1;
        message = in.readString();
    }

    public static final Creator<RepairResponse> CREATOR = new Creator<RepairResponse>() {
        @Override
        public RepairResponse createFromParcel(Parcel in) {
            return new RepairResponse(in);
        }

        @Override
        public RepairResponse[] newArray(int size) {
            return new RepairResponse[size];
        }
    };

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

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
        dest.writeByte((byte) (success == null ? 0 : success ? 1 : 2));
        dest.writeString(message);
    }
}
