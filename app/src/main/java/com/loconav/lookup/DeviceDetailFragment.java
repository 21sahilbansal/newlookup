package com.loconav.lookup;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.loconav.lookup.adapter.LookupAdapter;
import com.loconav.lookup.databinding.FragmentDeviceDetailBinding;
import com.loconav.lookup.model.Entity;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.sharedetailsfragmants.CommonRepairFragment;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.LOOKUP_RESPONSE;
import static com.loconav.lookup.Constants.NEW_INSTALL;


/**
 * Created by sejal on 11-08-2018.
 */

public class DeviceDetailFragment extends BaseTitleFragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentDeviceDetailBinding binding;
    private LookupAdapter lookupAdapter;
    private List<Entity> entities = new ArrayList<>();
    private String deviceID;
    private PassingReason passingReason;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    FragmentController fragmentController = new FragmentController();

    @Override
    public int setViewId() {
        return R.layout.fragment_device_detail;
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
                if (passingReason.getUserChoice().equals(NEW_INSTALL)) {
                    FetchClientFragment f1 = new FetchClientFragment();
                    ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                    fragmentController.loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
                } else {
                    ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                    CommonRepairFragment f1 = new CommonRepairFragment();
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
        if(AppUtils.isNetworkAvailable()) {
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
                    if(getContext()!=null)
                    Toaster.makeToast(t.getMessage());
                }
            });
        }else
            Toaster.makeToast(getString(R.string.internet_not_available));
    }

    private void getSetIntentData() {
        Log.e("save ", "getSetData: ");
        Bundle receivedBundle;
        receivedBundle = getArguments();
        LookupResponse lookupResponse = (LookupResponse) receivedBundle.getParcelable(LOOKUP_RESPONSE);
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
            if(entity.getTitle().equals("Device IMEI"))
                deviceID = entity.getValue();
        }
    }

    @Override
    public String getTitle() {
        return "Device Details";
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.unbind();
    }
}