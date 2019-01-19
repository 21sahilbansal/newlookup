package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

public class InstallationDetails implements Parcelable {
        @SerializedName("installable_id")
        private String installable_id;

        @SerializedName("uninstalled_at")
        private long uninstalled_at;

        @SerializedName("status_message")
        private Status_message status_message;

        @SerializedName("installable_type")
        private String installable_type;

        @SerializedName("installation_date")
        private long installation_date;

        @SerializedName("truck_number")
        private String truck_number;

        @SerializedName("temperature")
        private String temperature;

        @SerializedName("id")
        private String id;

        @SerializedName("installable_serial_number")
        private String installable_serial_number;

        @SerializedName("transporter_name")
        private String transporter_name;

        @SerializedName("client_id")
        private String client_id;

        @SerializedName("uses_immobilization")
        private String uses_immobilization;

        @SerializedName("active")
        private String active;

        @SerializedName("attachments")
        private List<AttachmentsDetails> attachments;

        @SerializedName("notes")
        private String notes;

        @SerializedName("device_subscription_expires_at")
        private String device_subscription_expires_at;

        @SerializedName("device_phone_number")
        private String device_phone_number;
        @SerializedName("audit_status")
        private String auditStatus;
        @SerializedName("audit_notes")
        private String auditNotes;

        private String chassis;

        public InstallationDetails()
        {

        }

    protected InstallationDetails(Parcel in) {
        installable_id = in.readString();
        uninstalled_at = in.readLong();
        status_message = in.readParcelable(Status_message.class.getClassLoader());
        installable_type = in.readString();
        installation_date = in.readLong();
        truck_number = in.readString();
        temperature = in.readString();
        id = in.readString();
        installable_serial_number = in.readString();
        transporter_name = in.readString();
        client_id = in.readString();
        uses_immobilization = in.readString();
        active = in.readString();
        attachments = in.createTypedArrayList(AttachmentsDetails.CREATOR);
        notes = in.readString();
        device_subscription_expires_at = in.readString();
        device_phone_number = in.readString();
        auditStatus = in.readString();
        auditNotes = in.readString();
        chassis = in.readString();
    }

    public static final Creator<InstallationDetails> CREATOR = new Creator<InstallationDetails>() {
        @Override
        public InstallationDetails createFromParcel(Parcel in) {
            return new InstallationDetails(in);
        }

        @Override
        public InstallationDetails[] newArray(int size) {
            return new InstallationDetails[size];
        }
    };

    public String getChassis() {
        return chassis;
    }

    public void setChassis(String chassis) {
        this.chassis = chassis;
    }

    public String getAuditStatus() {
            return auditStatus; }

        public void setAuditStatus(String auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getAuditNotes() {
            return auditNotes;
        }

        public void setAuditNotes(String auditNotes) {
            this.auditNotes = auditNotes;
        }

        public String getInstallable_id ()
        {
            return installable_id;
        }

        public void setInstallable_id (String installable_id)
        {
            this.installable_id = installable_id;
        }

        public long getUninstalled_at ()
        {
            return uninstalled_at;
        }

        public void setUninstalled_at (long uninstalled_at)
        {
            this.uninstalled_at = uninstalled_at;
        }

        public Status_message getStatus_message ()
        {
            return status_message;
        }

        public void setStatus_message (Status_message status_message)
        {
            this.status_message = status_message;
        }

        public String getInstallable_type ()
        {
            return installable_type;
        }

        public void setInstallable_type (String installable_type)
        {
            this.installable_type = installable_type;
        }

        public long getInstallation_date ()
        {
            return installation_date;
        }

        public void setInstallation_date (long installation_date)
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

        public String getTemperature ()
        {
            return temperature;
        }

        public void setTemperature (String temperature)
        {
            this.temperature = temperature;
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

        public String getTransporter_name ()
        {
            return transporter_name;
        }

        public void setTransporter_name (String transporter_name)
        {
            this.transporter_name = transporter_name;
        }

        public String getClient_id ()
        {
            return client_id;
        }

        public void setClient_id (String client_id)
        {
            this.client_id = client_id;
        }

        public String getUses_immobilization ()
        {
            return uses_immobilization;
        }

        public void setUses_immobilization (String uses_immobilization)
        {
            this.uses_immobilization = uses_immobilization;
        }

        public String getActive ()
        {
            return active;
        }

        public void setActive (String active)
        {
            this.active = active;
        }

        public List<AttachmentsDetails> getAttachments ()
        {
            return attachments;
        }

        public void setAttachments (List<AttachmentsDetails> attachments)
        {
            this.attachments = attachments;
        }

        public String getNotes ()
        {
            return notes;
        }

        public void setNotes (String notes)
        {
            this.notes = notes;
        }

        public String getDevice_subscription_expires_at ()
        {
            return device_subscription_expires_at;
        }

        public void setDevice_subscription_expires_at (String device_subscription_expires_at)
        {
            this.device_subscription_expires_at = device_subscription_expires_at;
        }

        public String getDevice_phone_number ()
        {
            return device_phone_number;
        }

        public void setDevice_phone_number (String device_phone_number)
        {
            this.device_phone_number = device_phone_number;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(installable_id);
        dest.writeLong(uninstalled_at);
        dest.writeParcelable(status_message, flags);
        dest.writeString(installable_type);
        dest.writeLong(installation_date);
        dest.writeString(truck_number);
        dest.writeString(temperature);
        dest.writeString(id);
        dest.writeString(installable_serial_number);
        dest.writeString(transporter_name);
        dest.writeString(client_id);
        dest.writeString(uses_immobilization);
        dest.writeString(active);
        dest.writeTypedList(attachments);
        dest.writeString(notes);
        dest.writeString(device_subscription_expires_at);
        dest.writeString(device_phone_number);
        dest.writeString(auditStatus);
        dest.writeString(auditNotes);
        dest.writeString(chassis);
    }
}
