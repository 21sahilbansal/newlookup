package com.loconav.lookup.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loconav.lookup.BaseCameraActivity;
import com.loconav.lookup.LandingActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.utils.AppUtils;
import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.loconav.lookup.Constants.IS_LOGGED_IN;
import static com.loconav.lookup.Constants.LOG_IN_TIME;
import static com.loconav.lookup.Constants.REASONS_RESPONSE;

public class SplashActivity extends BaseCameraActivity {

    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    private SharedPrefHelper sharedPrefHelper = SharedPrefHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void onAllPermissionsGranted() {

        if (SharedPrefHelper.getInstance().getBooleanData(IS_LOGGED_IN)) {
            Long currentTime = System.currentTimeMillis();
            Long loginTime = SharedPrefHelper.getInstance().getLongData(LOG_IN_TIME);
            if (currentTime - loginTime > TimeUnit.HOURS.toMillis(1)) {
                fetchAndSetData();
                //currentTime - login > TimeUnit.DAYS.toMillis(1)
            } else {
                Intent intent = new Intent(this, LandingActivity.class);
                startActivity(intent);
                finish();
            }
            Log.e(TAG, "onAllPermissionsGranted: ");
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
            }
    }

    /**
     * This function fetch the reason for repars(like sim_change,device_change etc.) and save it in shared preferences.
     */
    void fetchAndSetData() {
        if (AppUtils.isNetworkAvailable()) {
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
                    Intent intent = new Intent(getBaseContext(), LandingActivity.class);
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
