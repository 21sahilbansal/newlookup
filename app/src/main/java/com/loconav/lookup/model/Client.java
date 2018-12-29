package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by prateek on 28/05/18.
 */
public class Client implements Serializable{
    @SerializedName("client_id")
    private String clientId;
    @SerializedName("contact_number")
    private String contactNumber;
    @SerializedName("name")
    private String name;
    @SerializedName("contact_email")
    private String contactEmail;
    @SerializedName("transporter_id")
    private String transporter_id;

    public String getTransporter_id() {
        return transporter_id;
    }

    public void setTransporter_id(String transporter_id) {
        this.transporter_id = transporter_id;
    }



    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}