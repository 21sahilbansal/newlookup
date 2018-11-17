package com.loconav.lookup.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sejal on 09-08-2018.
 */

public class VersionResponse {
    @SerializedName("update_available")
    private Boolean updateAvailable;
    @SerializedName("next_version")
    private int nextVersion;
    @SerializedName("force_update")
    private Boolean forceUpdate;
    @SerializedName("app_link")
    private String appLink;

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
}
