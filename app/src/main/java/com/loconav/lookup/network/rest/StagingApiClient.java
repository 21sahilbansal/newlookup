package com.loconav.lookup.network.rest;

import com.loconav.lookup.application.SharedPrefHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.loconav.lookup.UserPrefs.authenticationToken;

/**
 * Created by sejal on 04-07-2018.
 */

public class StagingApiClient {
    private static final String BASE_URL = "http://staging.loconav.com/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
       loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS);

            httpClient.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Linehaul-V2-Secret", "1f0ec3aafb662b71b0dcee84cef5615ea78bd")
                        .addHeader("Authorization",SharedPrefHelper.getInstance().getStringData(authenticationToken)).build();
                return chain.proceed(request);

            }).addInterceptor(loggingInterceptor);


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
