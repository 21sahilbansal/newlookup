package com.loconav.lookup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by sejal on 06-07-2018.
 */

public class RecycleGridView extends BaseAdapter {

    List<ImageUri> data;
    private Callback callback;
    // Provide repair suitable constructor (depends on the kind of dataset)
    public RecycleGridView(List<ImageUri> myDataset, Callback callback) {
        data = myDataset;
        this.callback = callback;
    }

    @Override
    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.gridview_layout;
    }

    @Override
    public void onItemClick(Object object) {
        Log.e("item ", "onItemClick: "+ ((Client) object).getName());
        callback.onEventDone(object);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
