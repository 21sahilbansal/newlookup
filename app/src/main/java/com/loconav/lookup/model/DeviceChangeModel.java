package com.loconav.lookup.model;

/**
 * Created by prateek on 15/06/18.
 */

public class DeviceChangeModel extends DeviceClient {
    private String vehicleName;
    private String oldIMEI;
    private String newSimNo;
    private String oldSimNo;
    private String deviceModel;

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getOldIMEI() {
        return oldIMEI;
    }

    public void setOldIMEI(String oldIMEI) {
        this.oldIMEI = oldIMEI;
    }

    public String getNewSimNo() {
        return newSimNo;
    }

    public void setNewSimNo(String newSimNo) {
        this.newSimNo = newSimNo;
    }

    public String getOldSimNo() {
        return oldSimNo;
    }

    public void setOldSimNo(String oldSimNo) {
        this.oldSimNo = oldSimNo;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}
