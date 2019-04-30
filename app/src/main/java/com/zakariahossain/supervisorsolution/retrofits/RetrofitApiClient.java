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

    /*private static final String BASE_URL = "https://diusupervisorsolution.000webhostapp.com/public/";*/
    private static final String BASE_URL = "http://192.168.100.3/supervisor/public/";

    private static RetrofitApiClient instance;
    private Retrofit retrofit;

    private RetrofitApiClient() {
        /*HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);*/

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
                /*.client(httpClient.build())*/
                .build();
    }

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    static RetrofitApiClient getRetrofitInstance() {
        if (instance == null) {
            synchronized (RetrofitApiClient.class) {  //double check lock with thread safety
                if (instance == null) {
                    instance = new RetrofitApiClient();
                }
            }
        }
        return instance;
    }

    ApiInterface getApiInterface() {
        return retrofit.create(ApiInterface.class);
    }
}
