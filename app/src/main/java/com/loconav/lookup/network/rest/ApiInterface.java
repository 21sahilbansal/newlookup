package com.loconav.lookup.network.rest;

import com.loconav.lookup.login.model.Creds;
import com.loconav.lookup.login.model.LoginResponse;
import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.LookupResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by prateek on 5/3/18.
 */

public interface ApiInterface {

    @GET("api/v2/device_lookup")
    Call<LookupResponse> getDeviceLookup(@Query("device_id") String deviceId);

    @GET("api/v2/client_lookup")
    Call<List<Client>> getClients(@Query("client_id") String clientId);

    @POST("api/installers/login")
    Call<LoginResponse> validateUser(@Body Creds creds);


}