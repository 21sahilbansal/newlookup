package com.loconav.lookup.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.view.View;
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
    private final ArrayList<FastagsList>  suggestions;
    private final SearchView.SearchAutoComplete searchAutoComplete;
    public FastagAdapter(@NonNull Context context, ArrayList<FastagsList> fastagsList, SearchView.SearchAutoComplete searchAutoComplete) {
        super(context, 0, fastagsList);
        this.suggestions = new ArrayList<>(fastagsList);
        this.searchAutoComplete=searchAutoComplete;
    }

    @Override
    public void setData(View view, int position) {
        FastagsList vl = getItem(position);
        TextView name = view.findViewById(R.id.vehicle_no);
        if (name != null)
            name.setText(vl.getSerialNumber());
        TextView color = view.findViewById(R.id.fastag_color);
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

    private final Filter myFilter = new Filter() {
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

