package com.loconav.lookup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.adapter.RecycleGridAdapter;
import com.loconav.lookup.adapter.WhatToDoAdapter;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.formData.Validation;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.REASONS_RESPONSE;
import static com.loconav.lookup.Constants.USER_ID;

/**
 * Created by sejal on 10-07-2018.
 */

public class WhatToDo extends BaseFragment {


    @BindView(R.id.rvLay)
    RecyclerView recyclerView;
    WhatToDoAdapter adapter;
    ArrayList<Input> addtional = new ArrayList<>();
    ArrayList<ReasonResponse> jsonLog = new ArrayList<>();

    @Override
    public int setViewId() {
        return R.layout.what_to_do_fragment;
    }

    @Override
    public void onFragmentCreated() {
        String reasonsResponse = SharedPrefHelper.getInstance(getContext()).getStringData(REASONS_RESPONSE);
        Gson gson = new Gson();
        jsonLog = gson.fromJson(reasonsResponse, new TypeToken<List<ReasonResponse>>() {}.getType());
        if(jsonLog!=null) {
            List<ReasonTypeResponse> ll = new ArrayList<>();
            ArrayList<Input> ll1 = new ArrayList<>();
            ReasonResponse reasonResponse = new ReasonResponse(1, "New Install", ll, ll1, "abc");
            jsonLog.add(reasonResponse);
            setPhotoAdapter();
        }else{
            Toast.makeText(getContext(),"something went wrong",Toast.LENGTH_LONG).show();
        }
    }
    private void addOtherFields(String userChoice) {
            Input i1 = new Input("deviceId", "imei", "textView", "Device Id :");
            Input i2 = new Input("remarks", "remarks", "text", "");
            Input i3 = new Input("reasons", "reasons", "spinner", "");
            Input i4 = new Input("addImage", "addImage", "ImagePicker", "");
            addtional.add(i1);
            addtional.add(i2);
            addtional.add(i3);
            addtional.add(i4);
        for(int i=0;i<jsonLog.size();i++) {
            if (jsonLog.get(i).getName().equals(userChoice)) {
                addtional.addAll(jsonLog.get(i).getAdditional_fields());
            }
        }
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this, getView());
    }

    @Override
    public void getComponentFactory() {

    }


    private void setPhotoAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WhatToDoAdapter(jsonLog, new Callback() {
            @Override
            public void onEventDone(Object object) {
                ReasonResponse reasonResponse = (ReasonResponse) object;
                PassingReason passingReason = new PassingReason();
                passingReason.setReasonResponse(reasonResponse);
                passingReason.setUserChoice(reasonResponse.getName());
                addOtherFields(passingReason.getUserChoice());
                reasonResponse.setAdditional_fields(addtional);
                passIntent("str", passingReason);
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    void passIntent(String tag, PassingReason value) {
        Intent intent = new Intent(getContext(), LookupSubActivity.class);
        intent.putExtra(tag, value);
        startActivity(intent);
    }

}