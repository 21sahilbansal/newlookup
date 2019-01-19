package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 25-07-2018.
 */

public class ReasonTypeResponse implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("icon_url")
    @Expose
    private String  iconUrl;

    @SerializedName("name")
    @Expose
    private String name;

    public ReasonTypeResponse()
    {

    }

    public ReasonTypeResponse(int id, String name, String iconUrl) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
    }

    private ReasonTypeResponse(Parcel in) {
        id = in.readInt();
        iconUrl = in.readString();
        name = in.readString();
    }

    public static final Creator<ReasonTypeResponse> CREATOR = new Creator<ReasonTypeResponse>() {
        @Override
        public ReasonTypeResponse createFromParcel(Parcel in) {
            return new ReasonTypeResponse(in);
        }

        @Override
        public ReasonTypeResponse[] newArray(int size) {
            return new ReasonTypeResponse[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(iconUrl);
        dest.writeString(name);
    }
}
