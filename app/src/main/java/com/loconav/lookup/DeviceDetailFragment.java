package com.loconav.lookup;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loconav.lookup.adapter.LookupAdapter;
import com.loconav.lookup.databinding.FragmentDeviceDetailBinding;
import com.loconav.lookup.ignitontest.view.IgnitionTestFragment;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.LOOKUP_RESPONSE;
import static com.loconav.lookup.Constants.NEW_INSTALL;
import static com.loconav.lookup.Constants.SLUG_ID_FOR_DEVICE_REMOVED;


/**
 * Created by sejal on 11-08-2018.
 */

public class DeviceDetailFragment extends BaseTitleFragment implements SwipeRefreshLayout.OnRefreshListener {

    private FragmentDeviceDetailBinding binding;
    private LookupAdapter lookupAdapter;
    private final List<Entity> entities = new ArrayList<>();
    private String deviceID;
    private PassingReason passingReason;
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private final FragmentController fragmentController = new FragmentController();

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
        binding.continueDetails.setOnClickListener(view -> {
            if (passingReason.getUserChoice().equals(NEW_INSTALL)) {
                FetchClientFragment f1 = new FetchClientFragment();
                ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                fragmentController.loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
            } else {
                ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
                //CommonRepairFragment f1 = new CommonRepairFragment();
                IgnitionTestFragment ignitionTestFragment = new IgnitionTestFragment();
                Bundle bundle = new Bundle();
                bundle.putString(String.valueOf(R.string.deviceDetail_deviceid),deviceID);
                ignitionTestFragment.setArguments(bundle);
                fragmentController.loadFragment(ignitionTestFragment,getFragmentManager(),R.id.frameLayout,true);
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
        binding.refresh.setOnClickListener(view -> onRefresh());
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
                    try {
                        setData(Objects.requireNonNull(response.body()));
                    }
                    catch (Exception ex){
                        Toaster.makeToast(getString(R.string.something_went_wrong));
                    }

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
        LookupResponse lookupResponse = receivedBundle.getParcelable(LOOKUP_RESPONSE);
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
        setData(lookupResponse);
    }

    private void setData(LookupResponse lookupResponse) {
        entities.clear();
        setDeviceId(lookupResponse.getData());
        entities.addAll(lookupResponse.getData());
        //If the reason is "Remove Device" then we don't care if the lookup is passing or not.
        if(passingReason.getReasonResponse().getSlug().equals(SLUG_ID_FOR_DEVICE_REMOVED))
        {
            if (lookupResponse.getPassed()) {
                binding.passed.setImageResource(R.drawable.passed);
            } else {
                binding.passed.setImageResource(android.R.color.transparent);
            }
            binding.continueDetails.setVisibility(View.VISIBLE);
        }
        else {
            if (lookupResponse.getPassed()) {
                binding.passed.setImageResource(R.drawable.passed);
                binding.continueDetails.setVisibility(View.VISIBLE);
            } else {
                binding.passed.setImageResource(android.R.color.transparent);
                binding.continueDetails.setVisibility(View.GONE);
            }
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