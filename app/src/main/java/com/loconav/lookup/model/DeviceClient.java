package com.loconav.lookup.model;
import android.databinding.BaseObservable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prateek on 15/06/18.
 */

public class DeviceClient extends BaseObservable implements Parcelable {

    private String clinetName;
    private String clientId;
    private String deviceId;

    protected DeviceClient(Parcel in) {
        clinetName = in.readString();
        clientId = in.readString();
        deviceId = in.readString();
    }

    public static final Creator<DeviceClient> CREATOR = new Creator<DeviceClient>() {
        @Override
        public DeviceClient createFromParcel(Parcel in) {
            return new DeviceClient(in);
        }

        @Override
        public DeviceClient[] newArray(int size) {
            return new DeviceClient[size];
        }
    };

    public String getClinetName() {
        return clinetName;
    }

    public void setClinetName(String clinetName) {
        this.clinetName = clinetName;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clinetName);
        dest.writeString(clientId);
        dest.writeString(deviceId);
    }
}
