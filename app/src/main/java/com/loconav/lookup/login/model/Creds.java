package com.loconav.lookup.login.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.loconav.lookup.BR;

/**
 * Created by prateek on 12/06/18.
 */

public class Creds extends BaseObservable{

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("password")
    private String password;

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
}