package com.loconav.lookup.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sejal on 28-07-2018.
 */

public class PassingReason implements Serializable {

    private String Deviceid;

    private String UserChoice;

    private Client ClientId;

    public ArrayList<String> imagesList;

    private ReasonResponse ReasonResponse;

    private int imagesPreRepair;

    private int imagesInRepair;

    private int imagesPostRepair;


    public PassingReason(String deviceid, Client clientId,  ReasonResponse ReasonResponse) {
        Deviceid = deviceid;
        ClientId = clientId;
        this.ReasonResponse = ReasonResponse;
    }

    public PassingReason(){

    }
    public PassingReason(ReasonResponse reasons,String UserChoice) {
        this.ReasonResponse = reasons;
        this.UserChoice = UserChoice;
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
        return UserChoice;
    }

    public void setUserChoice(String userChoice) {
        UserChoice = userChoice;
    }

    public String getDeviceid() {
        return Deviceid;
    }

    public void setDeviceid(String deviceid) {
        Deviceid = deviceid;
    }

    public Client getClientId() {
        return ClientId;
    }

    public void setClientId(Client clientId) {
        ClientId = clientId;
    }

    public  ReasonResponse getReasonResponse() {
        return ReasonResponse;
    }


    public void setReasonResponse( ReasonResponse reasons) {
        this.ReasonResponse = reasons;
    }
}
