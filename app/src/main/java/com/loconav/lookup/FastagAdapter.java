package com.loconav.lookup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.VehiclesList;

import java.util.ArrayList;

/**
 * Created by sejal on 18-07-2018.
 */

public class FastagAdapter extends ArrayAdapter<FastagsList> {
    ArrayList<FastagsList> fastagsList;
    ArrayList<FastagsList> tempCustomer, suggestions;

    public FastagAdapter(@NonNull Context context, ArrayList<FastagsList> fastagsList) {
        super(context,R.layout.activity_get_fastag,fastagsList);
        this.fastagsList = fastagsList;
        this.tempCustomer = new ArrayList<FastagsList>(fastagsList);
        this.suggestions = new ArrayList<FastagsList>(fastagsList);
    }


    @Override
    public int getCount() {
        return fastagsList.size();
    }

    @Override
    public FastagsList getItem(int i) {
        return fastagsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        FastagsList vl = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_get_fastag, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.fastag_no);
        if (name != null)
            name.setText(vl.getSerialNumber());
        TextView color = (TextView) convertView.findViewById(R.id.fastag_color);
        if (color != null)
            color.setText(vl.getColor());
        return convertView;
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
                for (FastagsList people : tempCustomer) {
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
            ArrayList<FastagsList> c = (ArrayList<FastagsList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (FastagsList cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}

