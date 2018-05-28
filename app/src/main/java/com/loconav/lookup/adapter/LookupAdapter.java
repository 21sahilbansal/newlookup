package com.loconav.lookup.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.loconav.lookup.R;
import com.loconav.lookup.model.Entity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by prateek on 14/05/18.
 */
public class LookupAdapter extends RecyclerView.Adapter<LookupAdapter.MyViewHolder> {

    private List<Entity> entities;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.value) TextView value;
        @BindView(R.id.iv_status) ImageView ivStatus;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            context = view.getContext();
        }
    }


    public LookupAdapter(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_entity, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Entity entity = entities.get(position);
        if(entity.getStatus() == null) {
            holder.ivStatus.setImageResource(android.R.color.transparent);
        } else {
            if(entity.getStatus())
                holder.ivStatus.setImageResource(R.drawable.greentick);
            else
                holder.ivStatus.setImageResource(R.drawable.red_tick);
        }
        holder.title.setText(entity.getTitle());
        if(entity.getValue() == null) {
            holder.value.setText("");
        } else {
            holder.value.setText(entity.getValue());
        }

    }

    @Override
    public int getItemCount() {
        return entities.size();
    }
}