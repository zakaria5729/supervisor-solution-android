package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.AcceptedGroupList;
import com.zakariahossain.supervisorsolution.models.GroupStatusList;
import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.RequestedGroupList;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.models.TitleDefense;
import com.zakariahossain.supervisorsolution.models.TopicList;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall implements MyApiService {

    private ApiInterface apiInterface = RetrofitApiClient.getRetrofitInstance().getApiInterface();

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

    @Override
    public void changePassword(String email, String currentPassword, String newPassword, final ResponseCallback<ServerResponse> responseCallback) {
        Call<ServerResponse> changePasswordCall = apiInterface.changePassword(email, currentPassword, newPassword);

        changePasswordCall.enqueue(new Callback<ServerResponse>() {
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
    public void groupListStatus(String groupEmail, final ResponseCallback<GroupStatusList> responseCallback) {
        Call<GroupStatusList> groupListStatusCall = apiInterface.groupListStatus(groupEmail);

        groupListStatusCall.enqueue(new Callback<GroupStatusList>() {
            @Override
            public void onResponse(@NonNull Call<GroupStatusList> call, @NonNull Response<GroupStatusList> response) {
                if(response.isSuccessful()) {
                    GroupStatusList groupStatusList = response.body();

                    if (groupStatusList != null) {
                        responseCallback.onSuccess(groupStatusList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<GroupStatusList> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void requestedGroupList(String supervisorEmail, final ResponseCallback<RequestedGroupList> responseCallback) {
        Call<RequestedGroupList> requestedGroupListCall = apiInterface.requestedGroupList(supervisorEmail);

        requestedGroupListCall.enqueue(new Callback<RequestedGroupList>() {
            @Override
            public void onResponse(@NonNull Call<RequestedGroupList> call, @NonNull Response<RequestedGroupList> response) {
                if(response.isSuccessful()) {
                    RequestedGroupList requestedGroupList = response.body();

                    if (requestedGroupList != null) {
                        responseCallback.onSuccess(requestedGroupList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RequestedGroupList> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void acceptedGroupList(String supervisorEmail, final ResponseCallback<AcceptedGroupList> responseCallback) {
        Call<AcceptedGroupList> acceptedGroupListCall = apiInterface.acceptedGroupList(supervisorEmail);

        acceptedGroupListCall.enqueue(new Callback<AcceptedGroupList>() {
            @Override
            public void onResponse(@NonNull Call<AcceptedGroupList> call, @NonNull Response<AcceptedGroupList> response) {
                if(response.isSuccessful()) {
                    AcceptedGroupList acceptedGroupList = response.body();

                    if (acceptedGroupList != null) {
                        responseCallback.onSuccess(acceptedGroupList);
                    } else {
                        responseCallback.onError(new Exception(response.message()));
                    }
                } else {
                    responseCallback.onError(new Exception(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<AcceptedGroupList> call, @NonNull Throwable t) {
                responseCallback.onError(new Exception(t.getMessage()));
            }
        });
    }

    @Override
    public void groupAcceptOrDecline(String supervisorEmail, String groupEmail, int acceptOrDecline, final ResponseCallback<ServerResponse> responseCallback) {
        Call<ServerResponse> groupAcceptOrDeclineCall = apiInterface.groupAcceptOrDecline(supervisorEmail, groupEmail, acceptOrDecline);

        groupAcceptOrDeclineCall.enqueue(new Callback<ServerResponse>() {
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

    /*@Override
    public void titleDefenseRegistration(String projectInternship, String projectInternshipType, String projectInternshipTitle, String areaOfInterest, String dayEvening, List<RequestedOrAcceptedGroup> studentList, List<Super> supervisorList, final ResponseCallback<ServerResponse> responseCallback) {

        Call<ServerResponse> titleDefenseRegistrationCall = apiInterface.titleDefenseRegistration(projectInternship, projectInternshipType, projectInternshipTitle, areaOfInterest, dayEvening, studentList, supervisorList);

        *//*Call<ServerResponse> titleDefenseRegistrationCall = apiInterface.titleDefenseRegistration(studentList, supervisorList);*//*

        titleDefenseRegistrationCall.enqueue(new Callback<ServerResponse>() {
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
    }*/

    @Override
    public void titleDefenseRegistration(TitleDefense titleDefense, final ResponseCallback<ServerResponse> responseCallback) {

        Call<ServerResponse> titleDefenseRegistrationCall = apiInterface.titleDefenseRegistration(titleDefense);

        /*Call<ServerResponse> titleDefenseRegistrationCall = apiInterface.titleDefenseRegistration(studentList, supervisorList);*/

        titleDefenseRegistrationCall.enqueue(new Callback<ServerResponse>() {
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
