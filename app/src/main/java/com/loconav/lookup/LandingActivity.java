package com.loconav.lookup;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.loconav.lookup.model.PassingReason;
import com.loconav.lookup.model.ReasonResponse;
import com.loconav.lookup.model.ReasonTypeResponse;
import com.loconav.lookup.model.VersionResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.loconav.lookup.Constants.REASONS_RESPONSE;

public class LookupEntry2 extends BaseActivity {
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
        jsonLog = gson.fromJson(reasonsResponse, new TypeToken<List<ReasonResponse>>() {}.getType());
        if(jsonLog!=null) {
            setPhotoAdapter();
        }else{
            Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show();
        }
        checkVersion();
    }
    private void checkVersion() {
        String version = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            Log.e("version  ",version.toString());

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (Utility.isNetworkAvailable(this)) {
            apiService.getVersion((int) Float.parseFloat(version)).enqueue(new RetrofitCallback<VersionResponse>() {
                @Override
                public void handleSuccess(Call<VersionResponse> call, Response<VersionResponse> response) {
                    Log.e("version 2 is ",response.toString());
                    if (response.body().getForce_update()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LookupEntry2.this, R.style.DialogTheme);
                        builder.setMessage("Update App")
                                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Uri uri = Uri.parse("http://play.loconav.com");
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
                reasonResponse= (ReasonResponse) object;
                passingReason.setUserChoice(reasonResponse.getName());
                passIntent();
            }
        });
        lookupEntry2Binding.rvTasks.setAdapter(adapter);
        lookupEntry2Binding.rvTasks.setNestedScrollingEnabled(false);
    }
    public void newInstall(View view)
    {
            List<ReasonTypeResponse> reasons = new ArrayList<>();
            ArrayList<Input> additionalFields = new ArrayList<>();
            ReasonResponse reasonResponse = new ReasonResponse(1, "New Install", reasons, additionalFields, "abc");
            this.reasonResponse=reasonResponse;
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
        bundle.putSerializable("PassingReason",passingReason);
        bundle.putSerializable("reasonResponse",reasonResponse);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            Intent service=new Intent(this,LocationService.class);
            startForegroundService(service);
        } else {
            if(!isMyServiceRunning(LocationService.class)) {
                Log.e(getClass().getSimpleName(), "onReceive: " + "service restarted");
                startService(new Intent(getApplicationContext(), LocationService.class));
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lookupEntry2Binding.unbind();
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
