package com.loconav.lookup.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.customcamera.Callback;

import java.util.List;

public class ImageSetterAdapter extends BaseAdapter{
    private final List<String> images;
    private final Callback callback;

    public  ImageSetterAdapter(List<String> images,Callback callback)
    {
        this.images=images;
        this.callback=callback;
    }

    @Override
    public Object getDataAtPosition(int position) {
        if(position>=0)
            return images.get(position);
        else
            return null;
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
