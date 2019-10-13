package com.loconav.lookup.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.Entity;

import java.util.List;

/**
 * Created by prateek on 14/05/18.
 */
public class LookupAdapter extends BaseAdapter {

    private final List<Entity> data;

    public LookupAdapter(List<Entity> myDataset) {
        data = myDataset;
    }

    @Override
    public Object getDataAtPosition(int position) {
        if(position>=0)
            return data.get(position);
        else
            return null;
    }

    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.item_entity;
    }

    @Override
    public void onItemClick(Object object, int position) {

    }

    @Override
    public void editHeightWidthItem(View view, ViewGroup parent) {}

    @Override
    public int getItemCount() {
        return data.size();
    }
}