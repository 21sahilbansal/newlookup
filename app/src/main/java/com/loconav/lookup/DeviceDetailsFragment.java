package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loconav.lookup.adapter.LookupAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.model.Entity;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.sharedetailsfragmants.SimChangeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.FragmentController.loadFragment;

/**
 * Created by sejal on 11-08-2018.
 */

public class DeviceDetailsFragment extends BaseTitleFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_info) RecyclerView rvInfo;
    @BindView(R.id.passed) ImageView ivPassed;
    @BindView(R.id.share_details) Button shareDetails;
    @BindView(R.id.refresh) Button refresh;
    Uri uri;
    private LookupAdapter lookupAdapter;
    private List<Entity> entities = new ArrayList<>();
    private Bundle receivedBundle;
    private String deviceID;
    PassingReason passingReason;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);


    @Override
    public int setViewId() {
        return R.layout.main_activity3;
    }

    @Override
    public void onFragmentCreated() {
        setSwipeRefresh();
        setRecyclerView();
        setShareDetails();
        getSetIntentData();
        Log.e("save ", "onCreate: ");
    }

    @Override
    public void bindView(View view) {
        ButterKnife.bind(this,getView());
    }

    @Override
    public void getComponentFactory() {

    }
    private void setShareDetails() {
        shareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passingReason.getUserChoice().equals("New Install")) {
                    FetchClientFragment f1 = new FetchClientFragment();
                    ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                    loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
                } else {
                    ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                    SimChangeFragment f1 = new SimChangeFragment();
                    loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
                }
            }
        });
    }

    private void setRecyclerView() {
        lookupAdapter = new LookupAdapter(entities);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvInfo.setLayoutManager(mLayoutManager);
        rvInfo.setItemAnimator(new DefaultItemAnimator());
        rvInfo.setAdapter(lookupAdapter);
    }

    private void setSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        getSetFreshData(deviceID);
    }


    @Override
    public void onResume() {
        super.onResume();
        if(deviceID!=null)
            onRefresh();
    }

    private void getSetFreshData(String deviceID) {
        if(Utility.isNetworkAvailable(getActivity())) {
            apiService.getDeviceLookup(deviceID).enqueue(new RetrofitCallback<LookupResponse>() {
                @Override
                public void handleSuccess(Call<LookupResponse> call, Response<LookupResponse> response) {
                    Log.e("handle ", response.code() + "");
                    setData(response.body());
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void handleFailure(Call<LookupResponse> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }else
            Toast.makeText(getContext(), "Internet not available", Toast.LENGTH_SHORT).show();
    }

    private void getSetIntentData() {
        Log.e("save ", "getSetData: ");
        receivedBundle = getArguments();
        LookupResponse lookupResponse = (LookupResponse) receivedBundle.getSerializable("lookup_response");
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        setData(lookupResponse);
    }

    private void setData(LookupResponse lookupResponse) {
        entities.clear();
        setDeviceId(lookupResponse.getData());
        entities.addAll(lookupResponse.getData());
        if(lookupResponse.getPassed()) {
            ivPassed.setImageResource(R.drawable.passed);
            shareDetails.setVisibility(View.VISIBLE);
        }
        else {
            ivPassed.setImageResource(android.R.color.transparent);
            shareDetails.setVisibility(View.GONE);
        }
        lookupAdapter.notifyDataSetChanged();
    }

    private void setDeviceId(List<Entity> data) {
        for(Entity entity : data) {
            if(entity.getTitle().equals("Device IMEI")) {
                deviceID = entity.getValue();
            }
        }
    }

    @Override
    public String getTitle() {
        return "Device Details";
    }
}