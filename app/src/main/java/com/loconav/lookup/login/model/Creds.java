package com.loconav.lookup.login.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.loconav.lookup.BR;

/**
 * Created by prateek on 12/06/18.
 */

public class Creds extends BaseObservable implements Parcelable {

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("password")
    private String password;

    private Creds(Parcel in) {
        phoneNumber = in.readString();
        password = in.readString();
    }

    public Creds()
    {

    }

    public static final Creator<Creds> CREATOR = new Creator<Creds>() {
        @Override
        public Creds createFromParcel(Parcel in) {
            return new Creds(in);
        }

        @Override
        public Creds[] newArray(int size) {
            return new Creds[size];
        }
    };

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeString(password);
    }
}