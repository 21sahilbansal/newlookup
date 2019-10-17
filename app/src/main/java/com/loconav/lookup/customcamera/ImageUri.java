package com.loconav.lookup.customcamera;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
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
