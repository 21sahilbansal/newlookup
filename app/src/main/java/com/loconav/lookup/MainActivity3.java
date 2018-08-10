package com.loconav.lookup;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.loconav.lookup.adapter.LookupAdapter;
import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.Entity;
import com.loconav.lookup.model.LookupResponse;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.DEVICE_ID;
import static com.loconav.lookup.Constants.USER_CHOICE;

public class MainActivity3 extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity3);
        ButterKnife.bind(this);
        setSwipeRefresh();
        setRecyclerView();
        setShareDetails();
        getSetIntentData();
        Log.e("save ", "onCreate: ");
    }

    @Override
    public boolean showBackButton() {
        return true;
    }

    private void setShareDetails() {
        shareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passingReason.getUserChoice().equals("New Install")) {
                    Intent intent = new Intent(MainActivity3.this, FetchClientActivity.class);
                    Bundle bundle = new Bundle();
                    //bundle.putString("Image", String.valueOf(uri));
                    bundle.putSerializable("str", passingReason);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(MainActivity3.this, EnterDetails.class);
                    Bundle bundle = new Bundle();
                    //bundle.putString("Image", String.valueOf(uri));
                    bundle.putSerializable("str", passingReason);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    private void setRecyclerView() {
        lookupAdapter = new LookupAdapter(entities);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
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
    protected void onResume() {
        super.onResume();
        if(deviceID!=null)
            onRefresh();
    }

    private void getSetFreshData(String deviceID) {
        apiService.getDeviceLookup(deviceID).enqueue(new RetrofitCallback<LookupResponse>() {
            @Override
            public void handleSuccess(Call<LookupResponse> call, Response<LookupResponse> response) {
                Log.e("handle ", response.code() +"");
                setData(response.body());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void handleFailure(Call<LookupResponse> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getSetIntentData() {
        Log.e("save ", "getSetData: ");
	    receivedBundle = getIntent().getExtras();
        //uri= Uri.parse(receivedBundle.getString("Image"));
	    LookupResponse lookupResponse = (LookupResponse) receivedBundle.getSerializable("lookup_response");
        passingReason=(PassingReason) receivedBundle.getSerializable("str");
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
}
