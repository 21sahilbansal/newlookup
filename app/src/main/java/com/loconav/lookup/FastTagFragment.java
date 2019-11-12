package com.loconav.lookup;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import com.loconav.lookup.adapter.FastagAdapter;
import com.loconav.lookup.adapter.VehiclesAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentFastagBinding;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.InstallationRequirements;
import com.loconav.lookup.model.InstallationResponse;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.network.rest.StagingApiClient;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by sejal on 28-06-2018.
 */

public class FastTagFragment extends BaseFragment {
    private FragmentFastagBinding binding;
    private final ApiInterface apiService = StagingApiClient.getClient().create(ApiInterface.class);
    private SearchView.SearchAutoComplete searchAutoComplete;
    private SearchView.SearchAutoComplete searchAutoCompleteFastag;
    private VehiclesList query;
    private FastagsList queryFastags;
    private final InstallationRequirements installerCreds = new InstallationRequirements();
    private ArrayList<VehiclesList> vehiclesLists=new ArrayList<>();
    private ArrayList<FastagsList> fastagsLists=new ArrayList<>();
    private VehiclesAdapter vehiclesAdapter;
    private FastagAdapter fastagAdapter;
    @Override
    public int setViewId() {
        return R.layout.fragment_fastag;
    }
    @Override
    public void onFragmentCreated() {
        searchAutoComplete = binding.searchTruck.findViewById(R.id.search_src_text);
        binding.searchTruck.setQueryHint("Select Vehicle");
        searchAutoCompleteFastag = binding.searchFastId.findViewById(R.id.search_src_text);
        binding.searchFastId.setQueryHint("Select Fastag");
                vehiclesLists=getSetData(vehiclesLists);
        binding.truckSubmit.setOnClickListener(view -> onSubmitClicked());
    }


    private ArrayList<FastagsList> getFastags(final ArrayList<FastagsList> fastagsLists) {
        apiService.getFastags(query.getId()).enqueue(new RetrofitCallback<List<FastagsList>>() {
            @Override
            public void handleSuccess(Call<List<FastagsList>> call, Response<List<FastagsList>> response) {
                if(response.body()!=null && response.body().size()>0) {
                    fastagsLists.clear();
                    fastagsLists.addAll(response.body());
                    setSearchViewFastags();
                } else
                    handleFailure(call, new Throwable("no lists available"));
            }

            @Override
            public void handleFailure(Call<List<FastagsList>> call, Throwable t) {
                Toaster.makeToast(t.getMessage());
                Log.e("error ", t.getMessage());
            }
        });
        return fastagsLists;
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {}

    private ArrayList<VehiclesList> getSetData(final ArrayList<VehiclesList> vehiclesLists) {
        apiService.getVehicles().enqueue(new RetrofitCallback<List<VehiclesList>>() {
            @Override
            public void handleSuccess(Call<List<VehiclesList>> call, Response<List<VehiclesList>> response) {
                if(response.body()!=null && response.body().size()>0) {
                    vehiclesLists.clear();
                    vehiclesLists.addAll(response.body());
                    setSearchView();
                    Log.e("error ", ""+vehiclesLists.size());
                } else
                    handleFailure(call, new Throwable("no lists available"));
            }

            @Override
            public void handleFailure(Call<List<VehiclesList>> call, Throwable t) {
            Toaster.makeToast(t.getMessage());
            Log.e("error ", t.getMessage());
            }
        });

        return vehiclesLists;
    }

    @SuppressLint("RestrictedApi")
    private void setSearchView(){
        vehiclesAdapter= new VehiclesAdapter(getContext(),vehiclesLists,searchAutoComplete);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(vehiclesAdapter);
        binding.searchTruck.setActivated(true);
        searchAutoComplete.setOnItemClickListener((parent, view, position, id) -> {
            if(getActivity()!=null)
                AppUtils.hideKeyboard(getActivity());
            query= (VehiclesList) parent.getItemAtPosition(position);
            searchAutoComplete.setText(query.getNumber());
            searchAutoComplete.setSelection(query.getNumber().length());
            installerCreds.setTruck_id(query.getId());
            fastagsLists=getFastags(fastagsLists);
        });
    }


    @SuppressLint("RestrictedApi")
    private void setSearchViewFastags() {
        fastagAdapter= new FastagAdapter(getContext(),fastagsLists,searchAutoCompleteFastag);
        searchAutoCompleteFastag.setThreshold(1);
        searchAutoCompleteFastag.setAdapter(fastagAdapter);
        binding.searchFastId.setActivated(true);
        searchAutoCompleteFastag.setOnItemClickListener((parent, view, position, id) -> {
            if(getActivity()!=null)
                AppUtils.hideKeyboard(getActivity());
            queryFastags= (FastagsList) parent.getItemAtPosition(position);
            searchAutoCompleteFastag.setText(queryFastags.getSerialNumber()+" "+queryFastags.getColor());
            searchAutoCompleteFastag.setSelection(queryFastags.getSerialNumber().length()+queryFastags.getColor().length());
            installerCreds.setFastag_id(queryFastags.getId());
        });
    }


    private void onSubmitClicked() {
        apiService.createInstallation(installerCreds).enqueue(new RetrofitCallback<InstallationResponse>() {
            @Override
            public void handleSuccess(Call<InstallationResponse> call, Response<InstallationResponse> response) {
                Toaster.makeToast(response.message());
            }

            @Override
            public void handleFailure(Call<InstallationResponse> call, Throwable t) {
                Toaster.makeToast(t.getMessage());
                Log.e("error ", t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
