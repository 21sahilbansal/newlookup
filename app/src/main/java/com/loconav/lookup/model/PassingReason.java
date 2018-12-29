package com.loconav.lookup.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejal on 28-07-2018.
 */

public class PassingReason implements Serializable {

    private String deviceId;

    private String userChoice;

    private Client clientId;

    public ArrayList<String> imagesList;

    private ReasonResponse reasonResponse;

    private int imagesPreRepair;

    private int imagesInRepair;

    private int imagesPostRepair;


    public PassingReason(String deviceid, Client clientId,  ReasonResponse ReasonResponse) {
        deviceId = deviceid;
        this.clientId = clientId;
        this.reasonResponse = ReasonResponse;
    }

    public PassingReason(){

    }
    public PassingReason(ReasonResponse reasons,String UserChoice) {
        this.reasonResponse = reasons;
        this.userChoice = UserChoice;
    }

    public int getImagesPreRepair() {
        return imagesPreRepair;
    }

    public void setImagesPreRepair(int imagesPreRepair) {
        this.imagesPreRepair = imagesPreRepair;
    }

    public int getImagesInRepair() {
        return imagesInRepair;
    }

    public void setImagesInRepair(int imagesInRepair) {
        this.imagesInRepair = imagesInRepair;
    }

    public int getImagesPostRepair() {
        return imagesPostRepair;
    }

    public void setImagesPostRepair(int imagesPostRepair) {
        this.imagesPostRepair = imagesPostRepair;
    }

    public ArrayList<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(ArrayList<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getUserChoice() {
        return userChoice;
    }

    public void setUserChoice(String userChoice) {
        this.userChoice = userChoice;
    }

    public String getDeviceid() {
        return deviceId;
    }

    public void setDeviceid(String deviceid) {
        deviceId = deviceid;
    }

    public Client getClientId() {
        return clientId;
    }

    public void setClientId(Client clientId) {
        this.clientId = clientId;
    }

    public  ReasonResponse getReasonResponse() {
        return reasonResponse;
    }


    public void setReasonResponse( ReasonResponse reasons) {
        this.reasonResponse = reasons;
    }
}
