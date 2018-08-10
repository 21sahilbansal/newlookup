package com.loconav.lookup.base;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.loconav.lookup.R;
import com.squareup.picasso.Picasso;

/**
 * Created by prateek on 08/08/18.
 */
public class ImageBindingAdapters {

    @BindingAdapter("url")
    public void setImageUrl(ImageView imageView, String url) {
        Picasso.get().load(url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
    }

    @BindingAdapter("half_url")
    public void setImageHalfUrl(ImageView imageView, String url) {
        Picasso.get().load(url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
    }

    @BindingAdapter({"android:src"})
    public void loadImage(ImageView view, int id) {
        Picasso.get().load(id).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(view);
    }

    @BindingAdapter({"android:src"})
    public void loadImage(ImageView view, Uri id) {
        Picasso.get().load(id).into(view);
    }


}