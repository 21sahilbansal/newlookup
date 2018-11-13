package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewInstall  {
    @SerializedName("attachments")
    private List<Attachments> attachments=new ArrayList<>();

    @SerializedName("client_id")
    private String client_id;

    @SerializedName("notes")
    private Notes notes;

    @SerializedName("imei_number")
    private String imei_number;

    @SerializedName("registration_number")
    private String registration_number;

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

    public String getRegistration_number ()
    {
        return registration_number;
    }

    public void setRegistration_number (String registration_number)
    {
        this.registration_number = registration_number;
    }


}
