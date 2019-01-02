package com.loconav.lookup.network;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by prateek on 06/05/18.
 */
public abstract class RetrofitCallback<T> implements Callback<T> {

    private final String TAG = getClass().getSimpleName();
    private final String DEFAULT_ERROR_MESSAGE = "Something Went Wrong!!";

    @Override public void onFailure(Call<T> call,Throwable t) {
        Log.e(TAG, "onFailure: " + t.getMessage() );
        handleFailure(call, new Throwable(DEFAULT_ERROR_MESSAGE));
    }

    @Override public void onResponse(Call<T> call, Response<T> response) {
        Log.i(TAG, "response " + response.raw().request().url() + " : " + (new Date()).getTime());
        if (response.isSuccessful()) {
            handleSuccess(call, response);
        } else {
            String error;
            try {
                error = (new JSONObject(response.errorBody().string())).getString("message");
            } catch (IOException | NullPointerException | JSONException e) {
                error = DEFAULT_ERROR_MESSAGE;
                e.printStackTrace();
            }
            handleFailure(call, new Throwable(error));
        }
    }
    public abstract void handleSuccess(Call<T> call, Response<T> response);
    public abstract void handleFailure(Call<T> call, Throwable t);
}
