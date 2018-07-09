package com.loconav.lookup.adapter;

import android.util.Log;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.Client;

import java.util.List;

/**
 * Created by prateek on 28/05/18.
 */

public class ClientAdapter extends BaseAdapter {

    List<Client> data;
    private Callback callback;
    // Provide a suitable constructor (depends on the kind of dataset)
    public ClientAdapter(List<Client> myDataset, Callback callback) {
        data = myDataset;
        this.callback = callback;
    }

    @Override
    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.item_client;
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
