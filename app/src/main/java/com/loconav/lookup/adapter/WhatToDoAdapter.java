package com.loconav.lookup.adapter;

import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.ReasonResponse;

import java.util.List;

/**
 * Created by sejal on 27-07-2018.
 */

public class WhatToDoAdapter extends BaseAdapter {

    List<ReasonResponse> data;
    private Callback callback;
    // Provide repair suitable constructor (depends on the kind of dataset)
    public WhatToDoAdapter(List<ReasonResponse> myDataset, Callback callback) {
        data = myDataset;
        this.callback = callback;
    }

    @Override
    public Object getDataAtPosition(int position) {
        return data.get(position);
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.item_what_todo;
    }

    @Override
    public void onItemClick(Object object, int position) {
        callback.onEventDone(object);
    }

    @Override
    public void editHeightWidthItem(View view,ViewGroup parent) {
        view.getLayoutParams().height = parent.getMeasuredHeight() / 2;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

