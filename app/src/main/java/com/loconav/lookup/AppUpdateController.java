package com.loconav.lookup;

import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.loconav.lookup.application.SharedPrefHelper;
import com.loconav.lookup.dialog.AppUpdateDialog;
import com.loconav.lookup.model.VersionResponse;
import com.loconav.lookup.network.RetrofitCallback;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;

import retrofit2.Call;
import retrofit2.Response;

public class AppUpdateController {
    public static final String LATER = "later";
    private ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

    public AppUpdateController(FragmentManager fragmentManager, int versionCode) {
        apiService.getVersion(versionCode).enqueue(new RetrofitCallback<VersionResponse>() {
            @Override
            public void handleSuccess(Call<VersionResponse> call, Response<VersionResponse> response) {
                int savedLater = SharedPrefHelper.getInstance().getIntData(LATER);
                VersionResponse versionResponse = response.body();
                if(savedLater < versionResponse.getNextVersion() && versionCode < versionResponse.getNextVersion()) {
                    AppUpdateDialog appUpdateDialog = AppUpdateDialog.newInstance(versionResponse.getForceUpdate(),
                            versionResponse.getNextVersion(), versionResponse.getAppLink());
                    appUpdateDialog.show(fragmentManager, getClass().getSimpleName());
                }
            }
            @Override
            public void handleFailure(Call<VersionResponse> call, Throwable t) {
                Log.e("ss", "handleFailure: ");
            }
        });

    }
}