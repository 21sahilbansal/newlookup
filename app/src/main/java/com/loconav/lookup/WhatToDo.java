package com.loconav.lookup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.Constants.DEVICE_ID;

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
    String str="";
    @Override
    void onFragmentCreated() {

        newInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str="newInstall";
                passIntent("button",str);
            }
        });
        vehChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str="vehChange";
                passIntent("button",str);
            }
        });
        devChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str="devChange";
                passIntent("button",str);
            }
        });
        simChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str="simChange";
                passIntent("button",str);
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
        Bundle bundle = new Bundle();
        bundle.putString(tag, value);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
