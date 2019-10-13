package com.loconav.lookup.network.rest;

import com.loconav.lookup.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sejal on 29-06-2018.
 */

public class LoginApiClient {
    private static final String BASE_URL = "https://android-stage.loconav.com";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
       loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();


            httpClient.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Linehaul-V2-Secret", BuildConfig.HAUL_SECRET)
                        .build();
                return chain.proceed(request);
            }).addInterceptor(loggingInterceptor);


            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL).client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
