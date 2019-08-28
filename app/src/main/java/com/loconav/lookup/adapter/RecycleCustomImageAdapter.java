package com.loconav.lookup.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.loconav.lookup.customcamera.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseAdapter;
import com.loconav.lookup.customcamera.ImagePickerEvent;
import com.loconav.lookup.customcamera.ImageUri;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by sejal on 06-07-2018.
 */
public class RecycleCustomImageAdapter extends BaseAdapter {

    private final List<ImageUri> data;
    private final Callback callback;
    private final Object object;

    // Provide repair suitable constructor (depends on the kind of dataset)
    public RecycleCustomImageAdapter(List<ImageUri> myDataset, Callback callback, Object object) {
        data = myDataset;
        this.callback = callback;
        this.object = object;
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
        callback.onEventDone(object);
    }

    @Override
    public void editHeightWidthItem(View view, ViewGroup parent) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        super.onBindViewHolder(holder, position);
        View view = holder.itemView;
        ImageView imageView = view.findViewById(R.id.remove);
        imageView.setOnClickListener(view1 -> {
            data.remove(data.get(position));
            EventBus.getDefault().post(new ImagePickerEvent(ImagePickerEvent.IMAGE_REMOVED_AFTER_CAPTURING, object));
            notifyDataSetChanged();
        });
    }
}
