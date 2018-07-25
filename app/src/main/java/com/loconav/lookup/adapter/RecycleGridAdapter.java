package com.loconav.lookup.adapter;

import android.util.Log;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;

import java.util.List;

import static com.loconav.lookup.CustomImagePicker.checkLimit;

/**
 * Created by sejal on 06-07-2018.
 */

public class RecycleGridAdapter extends BaseAdapter {

    List<ImageUri> data;
    int limit;
    private Callback callback;
    // Provide repair suitable constructor (depends on the kind of dataset)
    public RecycleGridAdapter(List<ImageUri> myDataset,int limit ,Callback callback) {
        data = myDataset;
        this.limit=limit;
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
    public void onItemClick(Object object, int position) {
        Log.e("item ", "onItemClick: "+ ((ImageUri) object).getUri());
        data.remove(data.get(position));
        checkLimit(data);
        notifyDataSetChanged();
        callback.onEventDone(object);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
