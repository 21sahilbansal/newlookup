package com.loconav.lookup.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.loconav.lookup.BR;


/**
 * Created by prateek on 28/05/18.
 */


public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.MyViewHolder> {


    public  class MyViewHolder extends RecyclerView.ViewHolder {
        private final ViewDataBinding binding;

        MyViewHolder(final ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(view -> onItemClick(getDataAtPosition(getAdapterPosition()),getAdapterPosition()));
        }

        void bind(Object obj) {
            binding.setVariable(BR.obj, obj);
            binding.executePendingBindings();
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create repair new view
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater,getLayoutIdForType(viewType), parent, false);
        // set the view's size, margins, paddings and layout parameters
        editHeightWidthItem(binding.getRoot(),parent);
        return new MyViewHolder(binding);
    }

    // Replace the contents of repair view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(getDataAtPosition(position));
    }

    protected abstract Object getDataAtPosition(int position);

    protected abstract int getLayoutIdForType(int viewType);

    protected abstract void onItemClick(Object object, int position);

    protected abstract void editHeightWidthItem(View view, ViewGroup parent);

}