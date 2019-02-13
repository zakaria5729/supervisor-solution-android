package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.models.TopicList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitApiInterface {

    @GET("topics")
    Call<TopicList> getTopics();

    @GET("supervisors")
    Call<SupervisorList> getSupervisors();

    @FormUrlEncoded
    @POST("create")
    Call<ServerResponse> signUp(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login_or_signin")
    Call<LoginResponse> loginOrSignIn(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("token") String token,
            @Field("user_role") String userRole,
            @Field("login_type") String loginType
    );

    @FormUrlEncoded
    @POST("verification")
    Call<ServerResponse> verifyEmail(
            @Field("email") String email,
            @Field("verification_code") int verificationCode
    );

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ServerResponse> forgotPassword(@Field("email") String email);

    @FormUrlEncoded
    @PUT("reset_password")
    Call<ServerResponse> resetPassword(
            @Field("email") String email,
            @Field("verification_code") int verificationCode,
            @Field("new_password") String newPassword
    );
}
