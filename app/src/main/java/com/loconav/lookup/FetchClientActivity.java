package com.loconav.lookup;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loconav.lookup.adapter.ClientAdapter;
import com.loconav.lookup.databinding.ActivityFetchClientBinding;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class FetchClientActivity extends AppCompatActivity {
    private ActivityFetchClientBinding binding;
    private ClientAdapter clientAdapter;
    private List<Client> clients = new ArrayList<>();
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    PassingReason passingReason;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fetch_client);
        setFetchClientButton();
        setAdapter();
        Bundle receivedBundle = getIntent().getExtras();
        passingReason=(PassingReason) receivedBundle.getSerializable("str");
    }

    private void setFetchClientButton() {
        binding.fetchClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CommonFunction.validate(new EditText[]{binding.clientId})) {
                    getSetData(binding.clientId.getText().toString());
                }
            }
        });
    }

    private void getSetData(final String clientId) {
        apiService.getClients(clientId).enqueue(new RetrofitCallback<List<Client>>() {
            @Override
            public void handleSuccess(Call<List<Client>> call, Response<List<Client>> response) {
                if(response.body()!=null && response.body().size()>0) {
                    binding.layoutClient.setVisibility(View.VISIBLE);
                    clients.clear();
                    clients.addAll(response.body());
                    clientAdapter.notifyDataSetChanged();
                } else
                    handleFailure(call, new Throwable("Client id doesn't exist"));
            }

            @Override
            public void handleFailure(Call<List<Client>> call, Throwable t) {
                binding.layoutClient.setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("error ", t.getMessage());
            }
        });
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvClients.setLayoutManager(layoutManager);
        clientAdapter = new ClientAdapter(clients, new Callback() {
            @Override
            public void onEventDone(Object object) {
                Intent intent = new Intent(FetchClientActivity.this, EnterDetails.class);
                passingReason.setClientId((Client)object);
                Bundle bundle = new Bundle();
                bundle.putSerializable("str", passingReason);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        binding.rvClients.setAdapter(clientAdapter);
    }

}
