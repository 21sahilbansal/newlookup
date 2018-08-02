package com.loconav.lookup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loconav.lookup.Callback;
import com.loconav.lookup.CustomImagePicker;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.ImageUri;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by sejal on 06-07-2018.
 */

public class RecycleGridAdapter extends BaseAdapter {

    List<ImageUri> data;
    ImageView context;
    int limit;
    private Callback callback;
    // Provide repair suitable constructor (depends on the kind of dataset)
    public RecycleGridAdapter(List<ImageUri> myDataset, ImageView context,int limit, Callback callback) {
        data = myDataset;
        this.context=context;
        this.callback = callback;
        this.limit=limit;
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
        Log.e("item ", "onItemClick: "+ data.size());
//        CustomImagePicker cs=new CustomImagePicker(context);
//        cs.checkLimit(data);
        checkLimit(data);
        notifyDataSetChanged();
        callback.onEventDone(object);
    }

    @Override
    public void editHeightWidthItem(View view, ViewGroup parent) {

    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public void checkLimit(List<ImageUri> imageUri){
        if(imageUri.size()>=limit){
            context.setVisibility(GONE);
        }else {
            context.setVisibility(VISIBLE);
        }
        notifyDataSetChanged();
    }


}
