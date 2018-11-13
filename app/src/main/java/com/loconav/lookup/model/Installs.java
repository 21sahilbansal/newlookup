package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Installs {
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


}
