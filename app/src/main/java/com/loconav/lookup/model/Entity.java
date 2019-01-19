package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by prateek on 12/05/18.
 */

public class Entity implements Parcelable {
    @SerializedName("title")
    private String title;
    @SerializedName("value")
    private String value;
    @SerializedName("status")
    private Boolean status;
    @SerializedName("key")
    private String key;

    public Entity()
    {

    }

    protected Entity(Parcel in) {
        title = in.readString();
        value = in.readString();
        byte tmpStatus = in.readByte();
        status = tmpStatus == 0 ? null : tmpStatus == 1;
        key = in.readString();
    }

    public static final Creator<Entity> CREATOR = new Creator<Entity>() {
        @Override
        public Entity createFromParcel(Parcel in) {
            return new Entity(in);
        }

        @Override
        public Entity[] newArray(int size) {
            return new Entity[size];
        }
    };

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(value);
        dest.writeByte((byte) (status == null ? 0 : status ? 1 : 2));
        dest.writeString(key);
    }
}
