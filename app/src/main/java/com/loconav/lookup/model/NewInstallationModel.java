package com.loconav.lookup.model;

/**
 * Created by prateek on 15/06/18.
 */

public class NewInstallationModel extends DeviceClient{
    private String dealerName;
    private String location;
    private String registrationNo;
    private String chassisNo;
    private String manufacure;
    private String model;
    private String typeOfGoods;
    private String odometerReading;
    private String simNo;
    private String deviceModel;

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getChassisNo() {
        return chassisNo;
    }

    public void setChassisNo(String chassisNo) {
        this.chassisNo = chassisNo;
    }

    public String getManufacure() {
        return manufacure;
    }

    public void setManufacure(String manufacure) {
        this.manufacure = manufacure;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTypeOfGoods() {
        return typeOfGoods;
    }

    public void setTypeOfGoods(String typeOfGoods) {
        this.typeOfGoods = typeOfGoods;
    }

    public String getOdometerReading() {
        return odometerReading;
    }

    public void setOdometerReading(String odometerReading) {
        this.odometerReading = odometerReading;
    }

    public String getSimNo() {
        return simNo;
    }

    public void setSimNo(String simNo) {
        this.simNo = simNo;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}
