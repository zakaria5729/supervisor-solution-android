package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.models.TopicList;

import java.util.List;

public interface MyApiService {
    void getTopicsFromServer(ResponseCallback<TopicList> responseCallback);
    void getSupervisorsFromServer(ResponseCallback<SupervisorList> responseCallback);

    void signUp(String name, String email, String password, ResponseCallback<ServerResponse> responseCallback);
    void loginOrSignIn(String name, String email, String password, String token, String userRole, String loginType, ResponseCallback<LoginResponse> responseCallback);

    void verifyEmail(String email, int verificationCode, ResponseCallback<ServerResponse> responseCallback);

    void forgotPassword(String email, ResponseCallback<ServerResponse> responseCallback);
    void resetPassword(String email, int verificationCode, String newPassword, ResponseCallback<ServerResponse> responseCallback);
}
