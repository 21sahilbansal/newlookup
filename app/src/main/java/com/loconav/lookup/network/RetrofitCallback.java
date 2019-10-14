package com.loconav.lookup.network;

import android.util.Log;

import com.loconav.lookup.Toaster;
import com.loconav.lookup.UserProfileFragment;
import com.loconav.lookup.model.ApiException;
import com.loconav.lookup.network.rest.ApiClient;
import com.loconav.lookup.network.rest.ApiInterface;
import com.loconav.lookup.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prateek on 06/05/18.
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    private final String TAG = getClass().getSimpleName();
    private final String DEFAULT_ERROR_MESSAGE = "Something Went Wrong!!";

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(TAG, "onFailure: " + t.getMessage());
        throwExceptionToServer("This is app side exception " + t.getMessage(), call.request().url().toString());
        handleFailure(call, new Throwable(DEFAULT_ERROR_MESSAGE));
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        Log.i(TAG, "response " + response.raw().request().url() + " : " + (new Date()).getTime());
        //This is the exception to throw
        String exception;
        if(response.code() == 401) {
            try {
                Toaster.makeToast("Unauthorized access");
                AppUtils.logOut();
            } catch (Exception e) {
                Log.e(TAG, "intercept: " + "login failed...");
            }}
        else if (response.isSuccessful()){
            handleSuccess(call, response);
        } else {
            String error;
            try {
                error = (new JSONObject(response.errorBody().string())).getString("message");
                exception = error;
            } catch (IOException | NullPointerException | JSONException e) {
                error = DEFAULT_ERROR_MESSAGE;
                exception = e.getMessage();
                e.printStackTrace();
            }
            throwExceptionToServer("This is api(server) exception " + exception, call.request().url().toString());
            handleFailure(call, new Throwable(error));
        }
    }

    protected abstract void handleSuccess(Call<T> call, Response<T> response);

    protected abstract void handleFailure(Call<T> call, Throwable t);

    private void throwExceptionToServer(String exception, String api) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        ApiException exceptionThrow = new ApiException();
        exceptionThrow.setCrash_log(exception);
        exceptionThrow.setNote(api);

        apiService.logExceptionToServer(exceptionThrow).enqueue(new RetrofitCallback<ResponseBody>() {
            @Override
            public void handleSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e(TAG, "Exception thrown");
            }

            @Override
            public void handleFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Exception not thrown");
            }
        });
    }

}