package com.loconav.lookup.network.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.loconav.lookup.UserPrefs.authenticationToken;

/**
 * Created by sejal on 29-06-2018.
 */

public class LoginApiClient {
    public static final String BASE_URL = "http://staging.loconav.com/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit==null) {
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request().newBuilder()
                            .addHeader("X-Linehaul-V2-Secret", "5ed183673b9709a69e51ed86e6b53b")
                            .build();
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
