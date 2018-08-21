package com.loconav.lookup;

import com.loconav.lookup.adapter.RecycleGridAdapter;
import com.loconav.lookup.base.BaseFragment;
import com.loconav.lookup.databinding.FragmentFastagBinding;
import com.loconav.lookup.model.FastagsList;
import com.loconav.lookup.model.ImageUri;
import com.loconav.lookup.model.InstallationRequirements;
import com.loconav.lookup.model.InstallationResponse;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.adapter.VehiclesAdapter;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import com.loconav.lookup.model.VehiclesList;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.StagingApiClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by sejal on 28-06-2018.
 */

public class FastTagFragment extends BaseFragment {
    FragmentFastagBinding binding;
    private ApiInterface apiService = StagingApiClient.getClient().create(ApiInterface.class);
    SearchView.SearchAutoComplete searchAutoComplete;
    SearchView.SearchAutoComplete searchAutoCompleteFastag;
    VehiclesList query;
    FastagsList queryFastags;
    InstallationRequirements installerCreds = new InstallationRequirements();
    ArrayList<VehiclesList> vehiclesLists=new ArrayList<>();
    ArrayList<FastagsList> fastagsLists=new ArrayList<>();
    VehiclesAdapter vehiclesAdapter;
    FastagAdapter fastagAdapter;

    @Override
    public int setViewId() {
        return R.layout.fragment_fastag;
    }


    @Override
    public void onFragmentCreated() {
        searchAutoComplete = (SearchView.SearchAutoComplete)binding.searchTruck.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        binding.searchTruck.setQueryHint("Select Vehicle");
        searchAutoCompleteFastag = (SearchView.SearchAutoComplete)binding.searchFastId.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        binding.searchFastId.setQueryHint("Select Fastag");
                vehiclesLists=getSetData(vehiclesLists);
        binding.truckSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSubmitClicked();
        }
    });
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
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("error ", t.getMessage());
            }
        });

        return vehiclesLists;
    }

    @SuppressLint("RestrictedApi")
    public void setSearchView(){
        vehiclesAdapter= new VehiclesAdapter(getContext(),vehiclesLists,searchAutoComplete);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(vehiclesAdapter);
        binding.searchTruck.setActivated(true);
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //JsonUtil.hideKeyboard(getView().findFocus(), getContext());
                Log.e("fijriv",""+query);
                query= (VehiclesList) parent.getItemAtPosition(position);
                searchAutoComplete.setText(query.getNumber());
                searchAutoComplete.setSelection(query.getNumber().length());
                installerCreds.setTruck_id(query.getId());
                fastagsLists=getFastags(fastagsLists);
            }
        });
    }


    @SuppressLint("RestrictedApi")
    private void setSearchViewFastags() {
        fastagAdapter= new FastagAdapter(getContext(),fastagsLists);
        searchAutoCompleteFastag.setThreshold(1);
        searchAutoCompleteFastag.setAdapter(fastagAdapter);
       // binding.searchFastId.setActivated(true);
        binding.searchFastId.setIconified(false);
        searchAutoCompleteFastag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //JsonUtil.hideKeyboard(getView().findFocus(), getContext());
                queryFastags= (FastagsList) parent.getItemAtPosition(position);
                searchAutoCompleteFastag.setText(queryFastags.getSerialNumber()+" "+queryFastags.getColor());
                searchAutoCompleteFastag.setSelection(queryFastags.getSerialNumber().length()+queryFastags.getColor().length());
                installerCreds.setFastag_id(queryFastags.getId());
            }
        });
    }


    private void onSubmitClicked() {
        apiService.createInstallation(installerCreds).enqueue(new RetrofitCallback<InstallationResponse>() {
            @Override
            public void handleSuccess(Call<InstallationResponse> call, Response<InstallationResponse> response) {
                Toast.makeText(getContext(),response.message(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFailure(Call<InstallationResponse> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }
}
