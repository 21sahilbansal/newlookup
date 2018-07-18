package com.loconav.lookup.adapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.loconav.lookup.BR;

import java.util.List;

/**
 * Created by prateek on 28/05/18.
 */


public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.MyViewHolder> {

    // Provide repair reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for repair data item in repair view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just repair string in this case
        private final ViewDataBinding binding;

        public MyViewHolder(final ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(getDataAtPosition(getAdapterPosition()));
                }
            });
        }

        public void bind(Object obj) {
            binding.setVariable(BR.obj, obj);
            binding.executePendingBindings();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create repair new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, getLayoutIdForType(viewType), parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new MyViewHolder(binding);
    }

    // Replace the contents of repair view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(getDataAtPosition(position));
    }

    public abstract Object getDataAtPosition(int position);

    public abstract int getLayoutIdForType(int viewType);

    public abstract void onItemClick(Object object);

}