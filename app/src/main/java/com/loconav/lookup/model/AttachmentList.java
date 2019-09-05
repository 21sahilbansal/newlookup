package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class AttachmentList {

    @SerializedName("attachments")
    List<Attachments> attachmentsArrayList;


    public void setAttachmentsArrayList(List<Attachments> attachmentsArrayList) {
        this.attachmentsArrayList = attachmentsArrayList;
    }
}
