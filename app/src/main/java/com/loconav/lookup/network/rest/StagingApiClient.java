package com.loconav.lookup.network.rest;

import com.loconav.lookup.application.LookUpApplication;
import com.loconav.lookup.application.SharedPrefHelper;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.loconav.lookup.UserPrefs.authenticationToken;

/**
 * Created by sejal on 04-07-2018.
 */

public class StagingApiClient {
    public static final String BASE_URL = "http://staging.loconav.com/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-Linehaul-V2-Secret", "1f0ec3aafb662b71b0dcee84cef5615ea78bd")
                            .addHeader("Authorization",SharedPrefHelper.getInstance(LookUpApplication.getInstance().getBaseContext()).getStringData(authenticationToken)).build();
                    return chain.proceed(request);

                }
            });


            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL).client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
