package com.loconav.lookup.base;

import androidx.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.loconav.lookup.R;
import com.loconav.lookup.application.LookUpApplication;
import com.loconav.lookup.customcamera.ImageUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by prateek on 08/08/18.
 */
public class ImageBindingAdapters {

    @BindingAdapter("imagesetter:imageurl")
    public void setImageUrl(ImageView imageView, String imageurl) {
        Log.e("fsdfs","hey f"+imageurl);
        Picasso.get().load(imageurl).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
    }

    @BindingAdapter("half_url")
    public void setImageHalfUrl(ImageView imageView, String url) {
        Picasso.get().load(url).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(imageView);
    }

    @BindingAdapter({"android:src"})
    public void loadImage(ImageView view, int id) {
        Picasso.get().load(id).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(view);
    }

    @BindingAdapter({"android:uri"})
    public void loadImage(ImageView view, Uri id) {
        Picasso.get().load(id).into(view);
    }

    @BindingAdapter({"android:drawable"})
    public void loadImageNoPlacehoalder(ImageView view, int id) {
        Picasso.get().load(id).into(view);
    }

    @BindingAdapter({"imagesetter:load_thumbnail"})
    public void loadThumbNail(ImageView view,Uri uri) {
        try {
            Bitmap thumbnail= ImageUtils.getThumbnailImage(uri, LookUpApplication.getInstance());
            view.setImageBitmap(thumbnail);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter({"imagesetter:html_beautify"})
    public void beautifyHtmlText(TextView textView,String text) {
        if(text!=null)
            textView.setText(""+Html.fromHtml(text));
    }

}