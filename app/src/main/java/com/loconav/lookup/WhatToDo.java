package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.adapter.WhatToDoAdapter;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.WhatToDoFragmentBinding;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.loconav.lookup.Constants.REASONS_RESPONSE;

/**
 * Created by sejal on 10-07-2018.
 */

public class WhatToDo extends BaseFragment {

    private WhatToDoFragmentBinding binding;
    private PassingReason passingReason = new PassingReason();
    WhatToDoAdapter adapter;
    ReasonResponse reasonResponse;
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
            List<ReasonTypeResponse> reasons = new ArrayList<>();
            ArrayList<Input> additionalFields = new ArrayList<>();
            ReasonResponse reasonResponse = new ReasonResponse(1, "New Install", reasons, additionalFields, "abc");
            jsonLog.add(reasonResponse);
            setPhotoAdapter();
        }else{
            Toast.makeText(getContext(),"something went wrong",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() { }


    private void setPhotoAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        binding.rvTasks.setLayoutManager(layoutManager);
        adapter = new WhatToDoAdapter(jsonLog, new Callback() {
            @Override
            public void onEventDone(Object object) {
                reasonResponse= (ReasonResponse) object;
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        binding.rvTasks.setAdapter(adapter);
        binding.rvTasks.setNestedScrollingEnabled(false);
    }

    private void passIntent() {
        Intent intent = new Intent(getContext(), LookupSubActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PassingReason",passingReason);
        bundle.putSerializable("reasonResponse",reasonResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }


}