package com.zakariahossain.supervisorsolution.retrofits;

import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitApiClient {
    private static final String BASIC_AUTH = "Basic " + Base64.encodeToString(("diu_supervisor_solution:admin@diu_supervisor_solution").getBytes(), Base64.NO_WRAP);
    private static final String BASE_URL = "http://192.168.100.4/supervisor/public/";

    private static RetrofitApiClient instance;
    private Retrofit retrofit;

    private RetrofitApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .addHeader("Authorization", BASIC_AUTH)
                                .method(originalRequest.method(), originalRequest.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
    }

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static synchronized RetrofitApiClient getRetrofitInstance() {
        if (instance == null) {
            instance = new RetrofitApiClient();
        }
        return instance;
    }

    RetrofitApiInterface getApiInterface() {
        return retrofit.create(RetrofitApiInterface.class);
    }
}
