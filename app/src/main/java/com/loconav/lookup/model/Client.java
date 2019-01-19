package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prateek on 28/05/18.
 */
public class Client implements Parcelable {
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

    public Client()
    {

    }
    Client(Parcel in) {
        clientId = in.readString();
        contactNumber = in.readString();
        name = in.readString();
        contactEmail = in.readString();
        transporter_id = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(clientId);
        dest.writeString(contactNumber);
        dest.writeString(name);
        dest.writeString(contactEmail);
        dest.writeString(transporter_id);
    }
}