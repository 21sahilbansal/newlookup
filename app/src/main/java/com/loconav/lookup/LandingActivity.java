package com.loconav.lookup;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import com.loconav.lookup.databinding.*;


import com.loconav.lookup.base.BaseActivity;
import com.loconav.lookup.model.CoordinateRequest;
import com.loconav.lookup.model.Coordinates;
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;
import com.loconav.lookup.model.VersionResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.REASONS_RESPONSE;

public class LandingActivity extends BaseActivity implements Callback {
    private static final Location TODO = null;
    ActivityLookupEntry2Binding lookupEntry2Binding;
    private PassingReason passingReason = new PassingReason();
    WhatToDoAdapter adapter;
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    ReasonResponse reasonResponse;
    ArrayList<ReasonResponse> jsonLog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lookupEntry2Binding = DataBindingUtil.setContentView(this, R.layout.activity_lookup_entry2);
        getSupportActionBar().setElevation(0);
        //This is to make visisble the icon
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_lookup_app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        String reasonsResponse = SharedPrefHelper.getInstance(this).getStringData(REASONS_RESPONSE);
        Gson gson = new Gson();
        jsonLog = gson.fromJson(reasonsResponse, new TypeToken<List<ReasonResponse>>() {
        }.getType());
        if (jsonLog != null) {
            setPhotoAdapter();
        } else {
            Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show();
        }
        checkVersion();
    }

    private void checkVersion() {
        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            Log.e("version  ", version.toString());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Utility.isNetworkAvailable(this)) {
            apiService.getVersion((int) Float.parseFloat(version)).enqueue(new RetrofitCallback<VersionResponse>() {
                @Override
                public void handleSuccess(Call<VersionResponse> call, Response<VersionResponse> response) {
                    Log.e("version 2 is ", response.toString());
                    if(response.body().getUpdate_available())
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity.this, R.style.DialogTheme);
                        builder.setMessage("Update App")
                                .setCancelable(false)
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse(response.body().getApp_link());
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                    if (response.body().getForce_update()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LandingActivity.this, R.style.DialogTheme);
                        builder.setMessage("Update App")
                                .setCancelable(false)
                                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse(response.body().getApp_link());
                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                        startActivity(intent);
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }

                @Override
                public void handleFailure(Call<VersionResponse> call, Throwable t) {
                    Log.e("ss", "handleFailure: ");
                }
            });
        } else
            Toast.makeText(getBaseContext(), "Internet not available", Toast.LENGTH_SHORT).show();
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
        lookupEntry2Binding.rvTasks.setLayoutManager(layoutManager);
        adapter = new WhatToDoAdapter(jsonLog, new Callback() {
            @Override
            public void onEventDone(Object object) {
                reasonResponse = (ReasonResponse) object;
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        lookupEntry2Binding.rvTasks.setAdapter(adapter);
        lookupEntry2Binding.rvTasks.setNestedScrollingEnabled(false);
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
    protected void onResume() {
        super.onResume();
        LocationGetter locationGetter=new LocationGetter(this,getBaseContext());
        locationGetter.getLocation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lookupEntry2Binding.unbind();
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
        coordinates.setRecordedat(time);
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
