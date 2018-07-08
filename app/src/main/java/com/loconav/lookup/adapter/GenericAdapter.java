package com.loconav.lookup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loconav.lookup.R;
import com.loconav.lookup.model.VehiclesList;

import java.util.ArrayList;

/**
 * Created by sejal on 06-07-2018.
 */

public abstract class  GenericAdapter<T> extends ArrayAdapter {
    ArrayList<T> arrayList;
    Context context;

    public GenericAdapter(@NonNull Context context, int resource, ArrayList arrayList) {
        super(context, resource,arrayList);
        this.context=context;
        this.arrayList=arrayList;

    }
    public abstract int getCount() ;

    public abstract T getItem(int i);

    public abstract long getItemId(int i) ;

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // VehiclesList vl = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_get_vehicles, parent, false);
        }
//        TextView name = (TextView) convertView.findViewById(R.id.vehicle_no);
//        if (name != null) {
//            assert vl != null;
//            name.setText(vl.getNumber());
//        }
        return convertView;
    }
}
