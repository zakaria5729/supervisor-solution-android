package com.zakariahossain.supervisorsolution.retrofits;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitApiClient {
    private static final String BASE_URL = "http://192.168.100.11/supervisor/public/";
    private static Retrofit retrofit = null;

    private RetrofitApiClient() {}

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            synchronized (RetrofitApiClient.class) {

                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .build();
                }
            }
        }

        return retrofit;
    }
}
