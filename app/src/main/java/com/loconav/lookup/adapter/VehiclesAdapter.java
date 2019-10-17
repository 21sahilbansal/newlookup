package com.loconav.lookup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseArrayAdapter;
import com.loconav.lookup.model.VehiclesList;

import java.util.ArrayList;

/**
 * Created by sejal on 30-06-2018.
 */

public class VehiclesAdapter extends BaseArrayAdapter<VehiclesList> {
    private final ArrayList<VehiclesList>  suggestions;
    private final SearchView.SearchAutoComplete searchAutoComplete;
    public VehiclesAdapter(@NonNull Context context, ArrayList<VehiclesList> vehiclesLists, SearchView.SearchAutoComplete searchAutoComplete) {
        super(context, 0, vehiclesLists);
        this.suggestions = new ArrayList<>(vehiclesLists);
        this.searchAutoComplete=searchAutoComplete;
    }

    @Override
    public void setData(View view, int position) {
        VehiclesList vl = getItem(position);
        TextView name = view.findViewById(R.id.vehicle_no);
        if (name != null)
            name.setText(vl.getNumber());
    }

    @Override
    public int getItemViewId() {
        return R.layout.activity_get_vehicles;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private final Filter myFilter = new Filter() {
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