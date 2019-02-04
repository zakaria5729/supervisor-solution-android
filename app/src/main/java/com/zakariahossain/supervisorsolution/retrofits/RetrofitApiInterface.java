package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.Topic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitApiInterface {

    @GET("topic_and_supervisor/topic.php")
    Call<List<Topic>> getTopics();

    @GET("topic_and_supervisor/supervisor.php")
    Call<List<Supervisor>> getSupervisors();
}
