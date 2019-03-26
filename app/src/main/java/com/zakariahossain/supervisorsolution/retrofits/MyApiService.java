package com.zakariahossain.supervisorsolution.retrofits;

import com.zakariahossain.supervisorsolution.models.AcceptedGroupList;
import com.zakariahossain.supervisorsolution.models.GroupStatusList;
import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.RequestedGroupList;
import com.zakariahossain.supervisorsolution.models.RequestedOrAcceptedGroup;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.models.TopicList;

import java.util.ArrayList;
import java.util.List;

public interface MyApiService {
    void getTopicsFromServer(ResponseCallback<TopicList> responseCallback);

    void getSupervisorsFromServer(ResponseCallback<SupervisorList> responseCallback);

    void signUp(String name, String email, String password, ResponseCallback<ServerResponse> responseCallback);

    void loginOrSignIn(String name, String email, String password, String token, String userRole, String loginType, ResponseCallback<LoginResponse> responseCallback);

    void verifyEmail(String email, int verificationCode, ResponseCallback<ServerResponse> responseCallback);

    void forgotPassword(String email, ResponseCallback<ServerResponse> responseCallback);

    void resetPassword(String email, int verificationCode, String newPassword, ResponseCallback<ServerResponse> responseCallback);

    void changePassword(String email, String currentPassword, String newPassword, ResponseCallback<ServerResponse> responseCallback);

    void groupListStatus(String groupEmail, ResponseCallback<GroupStatusList> responseCallback);

    void requestedGroupList(String supervisorEmail, ResponseCallback<RequestedGroupList> responseCallback);

    void acceptedGroupList(String supervisorEmail, ResponseCallback<AcceptedGroupList> responseCallback);

    void groupAcceptOrDecline(String supervisorEmail, String groupEmail, int acceptOrDecline, ResponseCallback<ServerResponse> responseCallback);

    void titleDefenseRegistration(String projectInternship, String projectInternshipType, String projectInternshipTitle, String areaOfInterest, String dayEvening, List<RequestedOrAcceptedGroup> studentList, List<String> supervisorList, ResponseCallback<ServerResponse> responseCallback);
}
