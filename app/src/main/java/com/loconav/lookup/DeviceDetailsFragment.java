package com.loconav.lookup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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

import com.loconav.lookup.adapter.ClientAdapter;
import com.loconav.lookup.adapter.LookupAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentDeviceIdBinding;
import com.loconav.lookup.databinding.MainActivity3Binding;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.Entity;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.sharedetailsfragmants.NewInstallation;
import com.loconav.lookup.sharedetailsfragmants.SimChangeFragment;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by sejal on 11-08-2018.
 */

public class DeviceDetailsFragment extends BaseTitleFragment implements SwipeRefreshLayout.OnRefreshListener {

    private MainActivity3Binding binding;
    private LookupAdapter lookupAdapter;
    private List<Entity> entities = new ArrayList<>();
    private Bundle receivedBundle;
    private String deviceID;
    private PassingReason passingReason;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    FragmentController fragmentController=new FragmentController();

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
        binding= DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }
    private void setShareDetails() {
        binding.shareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passingReason.getUserChoice().equals("New Install")) {
                    FetchClientFragment f1 = new FetchClientFragment();
                    ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                    fragmentController.loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
                } else {
                    ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                    SimChangeFragment f1 = new SimChangeFragment();
                    fragmentController.loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
                }
            }
        });
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvInfo.setLayoutManager(layoutManager);
        lookupAdapter = new LookupAdapter(entities);
        binding.rvInfo.setAdapter(lookupAdapter);
    }

    private void setSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(this);
        binding.refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(true);
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
                    binding.swipeRefresh.setRefreshing(false);
                }

                @Override
                public void handleFailure(Call<LookupResponse> call, Throwable t) {
                    binding.swipeRefresh.setRefreshing(false);
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
            binding.passed.setImageResource(R.drawable.passed);
            binding.shareDetails.setVisibility(View.VISIBLE);
        }
        else {
            binding.passed.setImageResource(android.R.color.transparent);
            binding.shareDetails.setVisibility(View.GONE);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
    }
}