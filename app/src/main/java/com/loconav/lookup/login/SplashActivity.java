package com.loconav.lookup.login;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.loconav.lookup.BaseCameraActivity;
import com.loconav.lookup.LocationService;
import com.loconav.lookup.LookUpEntry;
import com.loconav.lookup.LookupEntry2;
import com.loconav.lookup.R;
import com.loconav.lookup.Utility;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.LOG_IN_TIME;
import static com.loconav.lookup.Constants.REASONS_RESPONSE;

public class SplashActivity extends BaseCameraActivity {

    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private SharedPrefHelper sharedPrefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Fabric.with(this, new Crashlytics());
        sharedPrefHelper = SharedPrefHelper.getInstance(getBaseContext());
        Log.e(TAG, "onCreate: ");
    }


    @Override
    public void onAllPermissionsGranted() {
        Log.e(TAG, "onAllPermissionsGranted: ");
        if(SharedPrefHelper.getInstance(getBaseContext()).getBooleanData(IS_LOGGED_IN)) {
            Long currentTime=System.currentTimeMillis();

            Long login=SharedPrefHelper.getInstance(getBaseContext()).getLongData(LOG_IN_TIME);
            if(currentTime - login > TimeUnit.HOURS.toMillis(1)){
                fetchData();
                //currentTime - login > TimeUnit.DAYS.toMillis(1)
            }else{
                Intent intent = new Intent(getBaseContext(), LookupEntry2.class);
                startActivity(intent);
                finish();
            }
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onStart() {
        super.onStart();
            if(checkAndRequestPermissions(getBaseContext())) {
                onAllPermissionsGranted();
                Log.e(TAG, "onStart: ");
            }
    }

    void fetchData() {
        if (Utility.isNetworkAvailable(this)) {
            apiService.getReasons().enqueue(new Callback<ResponseBody>() {
                String str;

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        str = response.body().string();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sharedPrefHelper.setStringData(REASONS_RESPONSE, str);
                    sharedPrefHelper.setLongData(LOG_IN_TIME, System.currentTimeMillis());
                    Intent intent = new Intent(getBaseContext(), LookupEntry2.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getBaseContext(), t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    Log.e("res ", "onResponse: " + t.getMessage());
                }
            });
        } else
            Toast.makeText(getBaseContext(), "Internet not available", Toast.LENGTH_SHORT).show();
    }

}
