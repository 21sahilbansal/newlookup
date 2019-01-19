package com.loconav.lookup.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 09-08-2018.
 */

public class VersionResponse implements Parcelable {
    @SerializedName("update_available")
    private Boolean updateAvailable;
    @SerializedName("next_version")
    private int nextVersion;
    @SerializedName("force_update")
    private Boolean forceUpdate;
    @SerializedName("app_link")
    private String appLink;
    
    public VersionResponse()
    {

    }

    protected VersionResponse(Parcel in) {
        byte tmpUpdateAvailable = in.readByte();
        updateAvailable = tmpUpdateAvailable == 0 ? null : tmpUpdateAvailable == 1;
        nextVersion = in.readInt();
        byte tmpForceUpdate = in.readByte();
        forceUpdate = tmpForceUpdate == 0 ? null : tmpForceUpdate == 1;
        appLink = in.readString();
    }

    public static final Creator<VersionResponse> CREATOR = new Creator<VersionResponse>() {
        @Override
        public VersionResponse createFromParcel(Parcel in) {
            return new VersionResponse(in);
        }

        @Override
        public VersionResponse[] newArray(int size) {
            return new VersionResponse[size];
        }
    };

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public Boolean getUpdateAvailable() {
        return updateAvailable;
    }

    public void setUpdateAvailable(Boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
    }

    public int getNextVersion() {
        return nextVersion;
    }

    public void setNextVersion(int nextVersion) {
        this.nextVersion = nextVersion;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(Boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (updateAvailable == null ? 0 : updateAvailable ? 1 : 2));
        dest.writeInt(nextVersion);
        dest.writeByte((byte) (forceUpdate == null ? 0 : forceUpdate ? 1 : 2));
        dest.writeString(appLink);
    }
}
