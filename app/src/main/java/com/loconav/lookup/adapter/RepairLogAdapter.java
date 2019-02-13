package com.loconav.lookup.adapter;


import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.customcamera.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.Repairs;
import java.util.List;



public class RepairLogAdapter extends BaseAdapter {
    private final List<Repairs> repairsdata;
    private final Callback callback;
    public RepairLogAdapter(List<Repairs> repairsList,Callback callback)
    {
        this.repairsdata=repairsList;
        this.callback=callback;
    }
    @Override
    public Object getDataAtPosition(int position) {
        return repairsdata.get(position);
    }
    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.repair_items;
    }
    @Override
    public void onItemClick(Object object, int position) {
        callback.onEventDone(object);
    }
    @Override
    public void editHeightWidthItem(View view,ViewGroup parent) {
    }
    @Override
    public int getItemCount() {
        return repairsdata.size();
    }
}
