package com.loconav.lookup.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.loconav.lookup.customcamera.ImageUri;

import java.util.ArrayList;

/**
 * Created by sejal on 28-07-2018.
 */

public class PassingReason implements Parcelable {

    private String deviceId;

    private String userChoice;

    private Client clientId;

    private ArrayList<String> imagesList;

    private ArrayList<ImageUri> imageUriList;

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

    private PassingReason(Parcel in) {
        deviceId = in.readString();
        userChoice = in.readString();
        clientId = in.readParcelable(Client.class.getClassLoader());
        imagesList = in.createStringArrayList();
        reasonResponse = in.readParcelable(ReasonResponse.class.getClassLoader());
        imagesPreRepair = in.readInt();
        imagesInRepair = in.readInt();
        imagesPostRepair = in.readInt();
    }

    public static final Creator<PassingReason> CREATOR = new Creator<PassingReason>() {
        @Override
        public PassingReason createFromParcel(Parcel in) {
            return new PassingReason(in);
        }

        @Override
        public PassingReason[] newArray(int size) {
            return new PassingReason[size];
        }
    };

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
    public void setImagesUriList(ArrayList<ImageUri> imagesUriList) {
        this.imageUriList = imagesUriList;
    }
    public ArrayList<ImageUri> getImagesUriList() {
        return imageUriList;
    }


    public void setReasonResponse( ReasonResponse reasons) {
        this.reasonResponse = reasons;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceId);
        dest.writeString(userChoice);
        dest.writeParcelable(clientId, flags);
        dest.writeStringList(imagesList);
        dest.writeSerializable(imageUriList);
        dest.writeParcelable(reasonResponse, flags);
        dest.writeInt(imagesPreRepair);
        dest.writeInt(imagesInRepair);
        dest.writeInt(imagesPostRepair);
    }
}
