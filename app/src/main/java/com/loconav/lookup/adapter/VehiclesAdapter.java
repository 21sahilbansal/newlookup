package com.loconav.lookup.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.loconav.lookup.Callback;
import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseArrayAdapter;
import com.loconav.lookup.model.VehiclesList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sejal on 30-06-2018.
 */

public class VehiclesAdapter extends BaseArrayAdapter<VehiclesList> {
    ArrayList<VehiclesList>  suggestions;
    public VehiclesAdapter(@NonNull Context context, ArrayList<VehiclesList> vehiclesLists) {
        super(context, 0, vehiclesLists);
        this.suggestions = new ArrayList<VehiclesList>(vehiclesLists);
    }

    @Override
    public void setData(View view, int position) {
        VehiclesList vl = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.vehicle_no);
        if (name != null)
            name.setText(vl.getNumber());
    }

    @Override
    public int getItemViewId() {
        return R.layout.activity_get_vehicles;
    }

    @Override
    public void onItemClick(VehiclesList vehiclesList) {
//        TODO : work here for any of the items clicked
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
                for (VehiclesList people : list) {
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
