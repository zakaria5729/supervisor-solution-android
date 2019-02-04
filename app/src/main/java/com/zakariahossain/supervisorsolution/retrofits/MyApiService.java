package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.Topic;

import java.util.List;

public interface MyApiService {
    void getTopicsFromServer(ResponseCallback<List<Topic>> responseCallback);
    void getSupervisorsFromServer(ResponseCallback<List<Supervisor>> responseCallback);
}
