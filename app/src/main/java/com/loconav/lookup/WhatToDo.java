package com.loconav.lookup;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sejal on 10-07-2018.
 */

public class WhatToDo extends BaseFragment {

    @BindView(R.id.repair) TextView repair;

    @Override
    int setViewId() {
        return R.layout.what_to_do_fragment;
    }

    @Override
    void onFragmentCreated() {
        ButterKnife.bind(this,getView());
        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),lookupSubActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    void bindView(View view) {

    }

    @Override
    void getComponentFactory() {

    }
}
