package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NewInstall  implements Parcelable {
    @SerializedName("attachments")
    private List<Attachments> attachments=new ArrayList<>();

    @SerializedName("client_id")
    private String client_id;

    @SerializedName("notes")
    private Notes notes;

    @SerializedName("imei_number")
    private String imei_number;

    @SerializedName("registration_number")
    private String truck_number;

    @SerializedName("transporter_id")
    private long transporter_id;

    @SerializedName("immobilizer")
    private String immobilizer;

    @SerializedName("SOS")
    private String SOS;

    @SerializedName("tripbutton")
    private String tripbutton;

    public NewInstall()
    {

    }

    private NewInstall(Parcel in) {
        attachments = in.createTypedArrayList(Attachments.CREATOR);
        client_id = in.readString();
        imei_number = in.readString();
        truck_number = in.readString();
        transporter_id = in.readLong();
        immobilizer = in.readString();
        SOS = in.readString();
        tripbutton = in.readString();
    }

    public static final Creator<NewInstall> CREATOR = new Creator<NewInstall>() {
        @Override
        public NewInstall createFromParcel(Parcel in) {
            return new NewInstall(in);
        }

        @Override
        public NewInstall[] newArray(int size) {
            return new NewInstall[size];
        }
    };

    public String getImmobilizer() {
        return immobilizer;
    }

    public void setImmobilizer(String immobilizer) {
        this.immobilizer = immobilizer;
    }

    public String getSOS() {
        return SOS;
    }

    public void setSOS(String SOS) {
        this.SOS = SOS;
    }

    public String getTripbutton() {
        return tripbutton;
    }

    public void setTripbutton(String tripbutton) {
        this.tripbutton = tripbutton;
    }

    public long getTransporter_id() {
        return transporter_id;
    }

    public void setTransporter_id(long transporter_id) {
        this.transporter_id = transporter_id;
    }

    public List<Attachments> getAttachments ()
    {
        return attachments;
    }

    public void setAttachments (List<Attachments> attachments)
    {
        this.attachments = attachments;
    }

    public String getClient_id ()
    {
        return client_id;
    }

    public void setClient_id (String client_id)
    {
        this.client_id = client_id;
    }

    public Notes getNotes ()
    {
        return notes;
    }

    public void setNotes (Notes notes)
    {
        this.notes = notes;
    }

    public String getImei_number()
    {
        return imei_number;
    }

    public void setImei_number (String imei_number)
    {
        this.imei_number = imei_number;
    }

    public String getTruck_number ()
    {
        return truck_number;
    }

    public void setTruck_number (String truck_number)
    {
        this.truck_number = truck_number;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(attachments);
        dest.writeString(client_id);
        dest.writeString(imei_number);
        dest.writeString(truck_number);
        dest.writeLong(transporter_id);
        dest.writeString(immobilizer);
        dest.writeString(SOS);
        dest.writeString(tripbutton);
    }
}
