package com.loconav.lookup.adapter;


import android.view.View;
import android.view.ViewGroup;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.Installs;

import java.util.List;

public class InstallLogAdapter  extends BaseAdapter {
    public List<Installs> installsList;
    private Callback callback;
    public InstallLogAdapter(List<Installs> installsList,Callback callback)
    {
        this.installsList=installsList;
        this.callback=callback;
    }
    @Override
    public Object getDataAtPosition(int position) {
        return installsList.get(position);
    }
    @Override
    public int getLayoutIdForType(int viewType) {
        return R.layout.installitems;
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
        return installsList.size();
    }
}
