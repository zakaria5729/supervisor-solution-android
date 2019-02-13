package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.models.TopicList;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall implements MyApiService {

    private RetrofitApiInterface apiInterface = RetrofitApiClient.getRetrofitInstance().create(RetrofitApiInterface.class);

    @Override
    public void getTopicsFromServer(final ResponseCallback<TopicList> responseCallback) {
        Call<TopicList> topicListCall = apiInterface.getTopics();

        topicListCall.enqueue(new Callback<TopicList>() {
            @Override
            public void onResponse(@NonNull Call<TopicList> call, @NonNull Response<TopicList> response) {

                if (response.isSuccessful()) {
                    TopicList topicList = response.body();

                    if (topicList != null) {
                        responseCallback.onSuccess(topicList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<TopicList> call, @NonNull Throwable t) {
                responseCallback.onError(t);
            }
        });
    }

    @Override
    public void getSupervisorsFromServer(final ResponseCallback<SupervisorList> responseCallback) {
        Call<SupervisorList> supervisorListCall = apiInterface.getSupervisors();

        supervisorListCall.enqueue(new Callback<SupervisorList>() {
            @Override
            public void onResponse(@NonNull Call<SupervisorList> call, @NonNull Response<SupervisorList> response) {

                if (response.isSuccessful()) {
                    SupervisorList supervisorList = response.body();

                    if (supervisorList != null) {
                        responseCallback.onSuccess(supervisorList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<SupervisorList> call, @NonNull Throwable t) {
                responseCallback.onError(t);
            }
        });
    }

    @Override
    public void signUp(String name, String email, String password, final ResponseCallback<ServerResponse> responseCallback) {
        Call<ServerResponse> signUpCall = apiInterface.signUp(name, email, password);

        signUpCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();

                    if (serverResponse != null) {
                        responseCallback.onSuccess(serverResponse);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void loginOrSignIn(String name, String email, String password, String token, String userRole, String loginType, final ResponseCallback<LoginResponse> responseCallback) {
        Call<LoginResponse> loginCall = apiInterface.loginOrSignIn(name, email, password, token, userRole, loginType);

        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if(response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse != null) {
                        responseCallback.onSuccess(loginResponse);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void verifyEmail(String email, int verificationCode, final ResponseCallback<ServerResponse> responseCallback) {
        Call<ServerResponse> verifyEmailCall = apiInterface.verifyEmail(email, verificationCode);

        verifyEmailCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();

                    if (serverResponse != null) {
                        responseCallback.onSuccess(serverResponse);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void forgotPassword(String email, final ResponseCallback<ServerResponse> responseCallback) {
        Call<ServerResponse> forgotPasswordCall = apiInterface.forgotPassword(email);

        forgotPasswordCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();

                    if (serverResponse != null) {
                        responseCallback.onSuccess(serverResponse);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void resetPassword(String email, int verificationCode, String newPassword, final ResponseCallback<ServerResponse> responseCallback) {
        Call<ServerResponse> resetPasswordCall = apiInterface.resetPassword(email, verificationCode, newPassword);

        resetPasswordCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                if(response.isSuccessful()) {
                    ServerResponse serverResponse = response.body();

                    if (serverResponse != null) {
                        responseCallback.onSuccess(serverResponse);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }
}
