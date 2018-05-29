package com.loconav.lookup;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.LinearLayout;

import com.loconav.lookup.adapter.ClientAdapter;
import com.loconav.lookup.databinding.ActivityFetchClientBinding;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FetchClientActivity extends AppCompatActivity {
    private ActivityFetchClientBinding binding;
    private ClientAdapter clientAdapter;
    private List<Client> clients = new ArrayList<>();
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_fetch_client);
        setAdapter();
        getSetData();
    }

    private void getSetData() {
        clients.clear();
        Client client = new Client();
        client.setClientId("2000");
        client.setContactEmail("prateek@loconav.com");
        client.setContactNumber("8287752684");
        client.setName("Prateek");
        clients.add(client);
        clients.add(client);
        clientAdapter.notifyDataSetChanged();

//        apiService.getClients("2000").enqueue(new RetrofitCallback<List<Client>>() {
//            @Override
//            public void handleSuccess(Call<List<Client>> call, Response<List<Client>> response) {
//                clients.addAll(response.body());
//                clientAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void handleFailure(Call<List<Client>> call, Throwable t) {
//                Log.e("error ", t.getMessage());
//            }
//        });
    }

    private void setAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rvClients.setLayoutManager(layoutManager);
        clientAdapter = new ClientAdapter(clients, new Callback() {
            @Override
            public void onEventDone(Object object) {
                Intent intent = new Intent(FetchClientActivity.this, ShareDetails.class);
                intent.putExtra("client", (Client)object);
                startActivity(intent);
            }
        });
        binding.rvClients.setAdapter(clientAdapter);
    }


}
