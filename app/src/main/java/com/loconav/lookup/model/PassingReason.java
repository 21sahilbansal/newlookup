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

    private ArrayList<ReasonResponse> reasons;

    private int imagesPreRepair;

    private int imagesInRepair;

    private int imagesPostRepair;


    public PassingReason(String deviceid, Client clientId,  ArrayList<ReasonResponse> reasons) {
        Deviceid = deviceid;
        ClientId = clientId;
        this.reasons = reasons;
    }

    public PassingReason(ArrayList<ReasonResponse> reasons,String UserChoice) {
        this.reasons = reasons;
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

    public  ArrayList<ReasonResponse> getReasons() {
        return reasons;
    }

    public void setReasons( ArrayList<ReasonResponse> reasons) {
        this.reasons = reasons;
    }
}
