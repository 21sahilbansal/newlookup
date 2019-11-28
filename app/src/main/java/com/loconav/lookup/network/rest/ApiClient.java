package com.loconav.lookup.network.rest;

/**
 * Created by prateek on 5/3/18.
 */

import com.loconav.lookup.BuildConfig;
import com.loconav.lookup.application.SharedPrefHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.loconav.lookup.UserPrefs.authenticationToken;

public class ApiClient {
    private static final String BASE_URL = "https://android-stage.loconav.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        if (retrofit == null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).writeTimeout(100, TimeUnit.SECONDS);
            httpClient.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader("X-Linehaul-V2-Secret", BuildConfig.HAUL_SECRET)
                        .addHeader("Authorization",SharedPrefHelper.getInstance().getStringData(authenticationToken)).build();
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

