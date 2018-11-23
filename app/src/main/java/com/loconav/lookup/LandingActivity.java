package com.loconav.lookup;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loconav.lookup.adapter.WhatToDoAdapter;
import com.loconav.lookup.application.SharedPrefHelper;


import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.databinding.ActivityLookupEntryBinding;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.Coordinates;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.REASONS_RESPONSE;

public class LandingActivity extends BaseActivity implements Callback {
    private static final Location TODO = null;

    private ActivityLookupEntryBinding lookupEntryBinding;

    private PassingReason passingReason = new PassingReason();

    WhatToDoAdapter adapter;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    ReasonResponse reasonResponse;
    ArrayList<ReasonResponse> jsonLog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocationGetter locationGetter=new LocationGetter(this,getBaseContext());
        locationGetter.getLocation();
        lookupEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_lookup_entry);
        getSupportActionBar().setElevation(0);
        //This is to make visisble the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_lookup_app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        String reasonsResponse = SharedPrefHelper.getInstance().getStringData(REASONS_RESPONSE);
        Gson gson = new Gson();
        jsonLog = gson.fromJson(reasonsResponse, new TypeToken<List<ReasonResponse>>() {
        }.getType());
        if (jsonLog != null) {
            setPhotoAdapter();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show();
        }
        showAppUpdateDialog();
    }

    private void showAppUpdateDialog() {
        new AppUpdateController(getSupportFragmentManager(), AppUtils.getVersionCode(getBaseContext()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean showBackButton() {
        return false;
    }

    private void setPhotoAdapter() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        lookupEntryBinding.rvTasks.setLayoutManager(layoutManager);
        adapter = new WhatToDoAdapter(jsonLog, new Callback() {
            @Override
            public void onEventDone(Object object) {
                reasonResponse = (ReasonResponse) object;
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        lookupEntryBinding.rvTasks.setAdapter(adapter);
        lookupEntryBinding.rvTasks.setNestedScrollingEnabled(false);
    }

    public void newInstall(View view) {
        List<ReasonTypeResponse> reasons = new ArrayList<>();
        ArrayList<Input> additionalFields = new ArrayList<>();
        ReasonResponse reasonResponse = new ReasonResponse(1, "New Install", reasons, additionalFields, "abc");
        this.reasonResponse = reasonResponse;
        passingReason.setUserChoice(reasonResponse.getName());
        passIntent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_user:
                Intent intent = new Intent(getBaseContext(), UserActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void passIntent() {
        Intent intent = new Intent(this, LookupSubActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("PassingReason", passingReason);
        bundle.putSerializable("reasonResponse", reasonResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lookupEntryBinding.unbind();
    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onEventDone(Object object) {
        Location location=(Location) object;
        long time=System.currentTimeMillis()/1000;
        String latitude=String.valueOf(location.getLatitude());
        String longitude=String.valueOf(location.getLongitude());
        CoordinateRequest coordinateRequest = new CoordinateRequest();
        List<Coordinates> coordinatesList = new ArrayList<>();
        Coordinates coordinates = new Coordinates();
        coordinates.setLatitude(latitude);
        coordinates.setLongitude(longitude);
        coordinates.setRecordedAt(time);
        coordinatesList.add(coordinates);
        coordinateRequest.setCoordinates(coordinatesList);
        sendCoordinates(coordinateRequest);
        Log.e("location", "onLocationChanged: " + location);
    }

    public void sendCoordinates(CoordinateRequest coordinateRequest)
    {
        apiService.addCoordinates(coordinateRequest).enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void handleSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("coordinates","posted");
            }

            @Override
            public void handleFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Not posted","not sent"+t.toString());
            }
        });
    }



}
