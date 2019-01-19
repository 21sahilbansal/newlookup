package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Installs implements Parcelable {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("installable_serial_number")
    @Expose
    private String installable_serial_number;
    @SerializedName("attachments")
    @Expose
    private AttachmentsDetails[] attachments;
    @SerializedName("installation_date")
    @Expose
    private String installation_date;
    @SerializedName("truck_number")
    @Expose
    private String truck_number;
    @SerializedName("audit_status")
    private String auditStatus;

    public Installs()
    {

    }
    protected Installs(Parcel in) {
        id = in.readString();
        installable_serial_number = in.readString();
        attachments = in.createTypedArray(AttachmentsDetails.CREATOR);
        installation_date = in.readString();
        truck_number = in.readString();
        auditStatus = in.readString();
    }

    public static final Creator<Installs> CREATOR = new Creator<Installs>() {
        @Override
        public Installs createFromParcel(Parcel in) {
            return new Installs(in);
        }

        @Override
        public Installs[] newArray(int size) {
            return new Installs[size];
        }
    };

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getInstallable_serial_number ()
    {
        return installable_serial_number;
    }

    public void setInstallable_serial_number (String installable_serial_number)
    {
        this.installable_serial_number = installable_serial_number;
    }

    public AttachmentsDetails[] getAttachments ()
    {
        return attachments;
    }

    public void setAttachments (AttachmentsDetails[] attachments)
    {
        this.attachments = attachments;
    }

    public String getInstallation_date ()
    {
        return installation_date;
    }

    public void setInstallation_date (String installation_date)
    {
        this.installation_date = installation_date;
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
        dest.writeString(id);
        dest.writeString(installable_serial_number);
        dest.writeTypedArray(attachments, flags);
        dest.writeString(installation_date);
        dest.writeString(truck_number);
        dest.writeString(auditStatus);
    }
}
