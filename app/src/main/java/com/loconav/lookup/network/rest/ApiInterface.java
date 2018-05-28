package com.loconav.lookup.network.rest;

import com.loconav.lookup.model.Client;
import com.loconav.lookup.model.LookupResponse;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
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
    @Headers("X-Linehaul-V2-Secret: 5ed183673b9709a69e51ed86e6b53b")
    @GET("device_lookup")
    Call<LookupResponse> getDeviceLookup(@Query("device_id") String deviceId);

    @Headers("X-Linehaul-V2-Secret: 5ed183673b9709a69e51ed86e6b53b")
    @GET("device_lookup")
    Call<List<Client>> getClients(@Query("client_lookup") String clientId);
}