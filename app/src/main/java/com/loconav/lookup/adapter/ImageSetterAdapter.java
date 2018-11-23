package com.loconav.lookup.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageSetterAdapter extends BaseAdapter{
    public List<String> images;
    private Callback callback;

    public  ImageSetterAdapter(List<String> images,Callback callback)
    {
        this.images=images;
        this.callback=callback;
    }

    @Override
    public Object getDataAtPosition(int position) {
        return images.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.imagesetter;
    }

    @Override
    public void onItemClick(Object object, int position) {
        callback.onEventDone(object);
    }

    @Override
    public void editHeightWidthItem(View view, ViewGroup parent) {

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
