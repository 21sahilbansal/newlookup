package com.loconav.lookup;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.loconav.lookup.adapter.ClientAdapter;
import com.loconav.lookup.databinding.ActivityFetchClientBinding;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.sharedetailsfragmants.NewInstallationFragment;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class FetchClientFragment extends BaseTitleFragment {
    private ActivityFetchClientBinding binding;
    private ClientAdapter clientAdapter;
    private final List<Client> clients = new ArrayList<>();
    private final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private PassingReason passingReason;
    private final FragmentController fragmentController=new FragmentController();

    @Override
    public int setViewId() {
        return R.layout.activity_fetch_client;
    }

    @Override
    public void onFragmentCreated() {
        setFetchClientButton();
        setAdapter();
        passingReason= ((LookupSubActivity)getActivity()).getPassingReason();
    }

    @Override
    public void bindView(View view) {
        binding = DataBindingUtil.bind(view);
    }

    @Override
    public void getComponentFactory() {

    }

    private void setFetchClientButton() {
        binding.fetchClient.setOnClickListener(view -> {
            if(CommonFunction.validate(new EditText[]{binding.clientId})) {
                getSetData(binding.clientId.getText().toString());
            }
        });
    }

    private void getSetData(final String clientId) {
        if (AppUtils.isNetworkAvailable()) {
            apiService.getClients(clientId).enqueue(new RetrofitCallback<List<Client>>() {
                @Override
                public void handleSuccess(Call<List<Client>> call, Response<List<Client>> response) {
                    if (response.body() != null && response.body().size() > 0) {
                        binding.layoutClient.setVisibility(View.VISIBLE);
                        Client client=new Client();
                        Log.e("the is","ss"+response.body().get(0).getTransporter_id());
                        clients.clear();
                        clients.addAll(response.body());
                        clientAdapter.notifyDataSetChanged();
                    } else
                        handleFailure(call, new Throwable("Client id doesn't exist"));
                }

                @Override
                public void handleFailure(Call<List<Client>> call, Throwable t) {
                    binding.layoutClient.setVisibility(View.GONE);
                    if(getContext()!=null)
                    Toaster.makeToast(t.getMessage());
                    Log.e("error ", t.getMessage());
                }
            });
        } else
            Toaster.makeToast(getString(R.string.internet_not_available));
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvClients.setLayoutManager(layoutManager);
        clientAdapter = new ClientAdapter(clients, object -> {
            NewInstallationFragment f1 = new NewInstallationFragment();
            passingReason.setClientId((Client)object);
            ((LookupSubActivity)getActivity()).setPassingReason(passingReason);
            fragmentController.loadFragment(f1,getFragmentManager(),R.id.frameLayout,true);
        });
        binding.rvClients.setAdapter(clientAdapter);
    }

    @Override
    public String getTitle() {
        return "Enter Client";
    }
}
