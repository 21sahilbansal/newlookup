package com.loconav.lookup.model;
import android.databinding.BaseObservable;

/**
 * Created by prateek on 15/06/18.
 */

public class DeviceClient extends BaseObservable {

    private String clinetName;
    private String clientId;
    private String deviceId;

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
}
