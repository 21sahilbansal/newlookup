package com.loconav.lookup.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

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

    @BindingAdapter({"android:src"})
    public static void loadImage(ImageView view, String imageUrl) {
//        Picasso.with(view.getContext())
//                .load(imageUrl)
//                .placeholder(R.drawable.placeholder)
//                .into(view);

        Glide.with(view.getContext())
                .load(imageUrl)
                //.apply(RequestOptions.circleCropTransform())
                .into(view);
    }


}
