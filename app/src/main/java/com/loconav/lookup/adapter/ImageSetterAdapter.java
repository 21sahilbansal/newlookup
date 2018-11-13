package com.loconav.lookup.adapter;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ImageSetterAdapter extends BaseAdapter{
    List<String> images;
    Context context;

    public  ImageSetterAdapter(Context context, List<String> images)
    {
        this.images=images;
        this.context=context;
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
    }

    @Override
    public void editHeightWidthItem(View view, ViewGroup parent) {

    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
