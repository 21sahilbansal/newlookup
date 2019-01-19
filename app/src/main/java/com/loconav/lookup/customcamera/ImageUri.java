package com.loconav.lookup.customcamera;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;

import java.io.Serializable;

/**
 * Created by sejal on 07-07-2018.
 */

public class ImageUri extends BaseObservable implements Serializable {

    private Uri uri;

    @Bindable
    public Uri getUri(){
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


}
