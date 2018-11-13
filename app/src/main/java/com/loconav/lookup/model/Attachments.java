package com.loconav.lookup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attachments {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("image")
    @Expose
    private String image;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle() { return this.title; }
    public void setImage(String image){
        this.image = image;
    }
    public String getImage(){
        return this.image;
    }
}
