package com.loconav.lookup.customcamera;

import android.net.Uri;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.io.Serializable;

/**
 * Created by sejal on 07-07-2018.
 */

public class ImageUri extends BaseObservable implements Serializable {

    private Uri uri;
    private Long epochTime;

    @Bindable
    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setImageEpochTime(Long time) {
        this.epochTime = time;
    }

    public Long getImageEpochTime() {
        return epochTime;
    }

}
