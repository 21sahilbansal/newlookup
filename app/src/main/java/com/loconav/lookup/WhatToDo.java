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

    @BindView(R.id.simChange) TextView simChange;
    @BindView(R.id.devChange) TextView devChange;
    @BindView(R.id.vehChange) TextView vehChange;
    @BindView(R.id.newInstall) TextView newInstall;

    @Override
    int setViewId() {
        return R.layout.what_to_do_fragment;
    }

    @Override
    void onFragmentCreated() {
        newInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passIntent("buttonN","newInstall");
            }
        });
        vehChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passIntent("buttonV","vehChange");
            }
        });
        devChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passIntent("buttonD","devChange");
            }
        });
        simChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              passIntent("buttonS","sim");
            }
        });
    }

    @Override
    void bindView(View view) {
        ButterKnife.bind(this,getView());
    }

    @Override
    void getComponentFactory() {

    }

    void passIntent(String tag,String value){
        Intent intent=new Intent(getContext(),LookupSubActivity.class);
        intent.putExtra(tag,value);
        startActivity(intent);
    }
}
