package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Status_message implements Parcelable {
    @SerializedName("received_at")
    private String received_at;

    @SerializedName("io_state")
    private String io_state;

    public Status_message()
    {

    }

    protected Status_message(Parcel in) {
        received_at = in.readString();
        io_state = in.readString();
    }

    public static final Creator<Status_message> CREATOR = new Creator<Status_message>() {
        @Override
        public Status_message createFromParcel(Parcel in) {
            return new Status_message(in);
        }

        @Override
        public Status_message[] newArray(int size) {
            return new Status_message[size];
        }
    };

    public String getReceived_at ()
    {
        return received_at;
    }

    public void setReceived_at (String received_at)
    {
        this.received_at = received_at;
    }

    public String getIo_state ()
    {
        return io_state;
    }

    public void setIo_state (String io_state)
    {
        this.io_state = io_state;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(received_at);
        dest.writeString(io_state);
    }
}
