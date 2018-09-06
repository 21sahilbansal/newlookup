package com.loconav.lookup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.loconav.lookup.R;
import com.loconav.lookup.base.BaseArrayAdapter;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.VehiclesList;

import java.util.ArrayList;

/**
 * Created by sejal on 18-07-2018.
 */

public class FastagAdapter extends BaseArrayAdapter<FastagsList> {
    ArrayList<FastagsList>  suggestions;
    SearchView.SearchAutoComplete searchAutoComplete;
    public FastagAdapter(@NonNull Context context, ArrayList<FastagsList> fastagsList, SearchView.SearchAutoComplete searchAutoComplete) {
        super(context, 0, fastagsList);
        this.suggestions = new ArrayList<FastagsList>(fastagsList);
        this.searchAutoComplete=searchAutoComplete;
    }

    @Override
    public void setData(View view, int position) {
        FastagsList vl = getItem(position);
        TextView name = (TextView) view.findViewById(R.id.vehicle_no);
        if (name != null)
            name.setText(vl.getSerialNumber());
        TextView color = (TextView) view.findViewById(R.id.fastag_color);
        if (color != null)
            color.setText(vl.getColor());
    }

    @Override
    public int getItemViewId() {
        return R.layout.activity_get_fastag;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            FastagsList customer = (FastagsList) resultValue;
            return customer.getSerialNumber();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (FastagsList people : list) {
                    if (people.getSerialNumber().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
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

