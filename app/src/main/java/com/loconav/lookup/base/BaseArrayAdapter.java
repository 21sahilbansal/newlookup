package com.loconav.lookup.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by prateek on 10/07/18.
 */

public abstract class BaseArrayAdapter<T> extends ArrayAdapter {
    protected final List<T> list;

    protected BaseArrayAdapter(@NonNull Context context, int viewId, ArrayList<T> list) {
        super(context,0, list);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(getItemViewId(), parent, false);
        }
        setData(convertView, position);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClick(getItem(position), v);
//            }
//        });

        return convertView;
    }

    protected abstract void setData(View view, int position);

    protected abstract int getItemViewId();

 //   public abstract void onItemClick(T t, View v);
}
