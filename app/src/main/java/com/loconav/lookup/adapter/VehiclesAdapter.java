package com.loconav.lookup.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.loconav.lookup.R;
import com.loconav.lookup.model.VehiclesList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 30-06-2018.
 */

public class VehiclesAdapter extends ArrayAdapter<VehiclesList> {
    ArrayList<VehiclesList> vehiclesLists;
    ArrayList<VehiclesList> tempCustomer, suggestions;

    public VehiclesAdapter(@NonNull Context context, ArrayList<VehiclesList> vehiclesLists) {
        super(context,android.R.layout.simple_list_item_1,vehiclesLists);
        this.vehiclesLists = vehiclesLists;
        this.tempCustomer = new ArrayList<VehiclesList>(vehiclesLists);
        this.suggestions = new ArrayList<VehiclesList>(vehiclesLists);
    }


    @Override
    public int getCount() {
        return vehiclesLists.size();
    }

    @Override
    public VehiclesList getItem(int i) {
        return vehiclesLists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VehiclesList vl = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_get_vehicles, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.vehicle_no);
        if (name != null)
            name.setText(vl.getNumber());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            VehiclesList customer = (VehiclesList) resultValue;
            return customer.getNumber();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (VehiclesList people : tempCustomer) {
                    if (people.getNumber().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<VehiclesList> c = (ArrayList<VehiclesList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (VehiclesList cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
