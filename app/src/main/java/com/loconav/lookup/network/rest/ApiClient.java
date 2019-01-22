package com.loconav.lookup.network.rest;

/**
 * Created by prateek on 5/3/18.
 */

import com.loconav.lookup.application.SharedPrefHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.loconav.lookup.UserPrefs.authenticationToken;

public class ApiClient {
    private static final String BASE_URL = "http://loconav.com/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS);

            httpClient.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Linehaul-V2-Secret", "5ed183673b9709a69e51ed86e6b53b")
                        .addHeader("Authorization",SharedPrefHelper.getInstance().getStringData(authenticationToken)).build();
                return chain.proceed(request);

            });
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

