package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.Topic;

import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall implements MyApiService {

    private RetrofitApiInterface apiInterface = RetrofitApiClient.getRetrofitInstance().create(RetrofitApiInterface.class);

    @Override
    public void getTopicsFromServer(final ResponseCallback<List<Topic>> responseCallback) {
        Call<List<Topic>> callTopic = apiInterface.getTopics();

        callTopic.enqueue(new Callback<List<Topic>>() {
            @Override
            public void onResponse(@NonNull Call<List<Topic>> call, @NonNull Response<List<Topic>> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    List<Topic> topicList = response.body();

                    if (topicList != null && !topicList.isEmpty()) {
                        responseCallback.onSuccess(topicList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Topic>> call, @NonNull Throwable t) {
                responseCallback.onError(t);
            }
        });
    }

    @Override
    public void getSupervisorsFromServer(final ResponseCallback<List<Supervisor>> responseCallback) {
        Call<List<Supervisor>> callSupervisor = apiInterface.getSupervisors();

        callSupervisor.enqueue(new Callback<List<Supervisor>>() {
            @Override
            public void onResponse(@NonNull Call<List<Supervisor>> call, @NonNull Response<List<Supervisor>> response) {

                if (response.isSuccessful() && response.code() == 200) {
                    List<Supervisor> supervisorList = response.body();

                    if (supervisorList != null && !supervisorList.isEmpty()) {
                        responseCallback.onSuccess(supervisorList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Supervisor>> call, @NonNull Throwable t) {
                responseCallback.onError(t);
            }
        });
    }
}
