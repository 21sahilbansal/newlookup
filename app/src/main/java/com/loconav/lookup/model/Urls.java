package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Urls implements Serializable {
    @SerializedName("original")
    private String original;

    @SerializedName("small")
    private String small;

    @SerializedName("thumb")
    private String thumb;

    @SerializedName("large")
    private String large;

    @SerializedName("medium")
    private String medium;

    public String getOriginal ()
    {
        return original;
    }

    public void setOriginal (String original)
    {
        this.original = original;
    }

    public String getSmall ()
    {
        return small;
    }

    public void setSmall (String small)
    {
        this.small = small;
    }

    public String getThumb ()
    {
        return thumb;
    }

    public void setThumb (String thumb)
    {
        this.thumb = thumb;
    }

    public String getLarge ()
    {
        return large;
    }

    public void setLarge (String large)
    {
        this.large = large;
    }

    public String getMedium ()
    {
        return medium;
    }

    public void setMedium (String medium)
    {
        this.medium = medium;
    }
}
