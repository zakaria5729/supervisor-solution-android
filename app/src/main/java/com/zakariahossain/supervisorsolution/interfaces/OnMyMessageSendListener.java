package com.zakariahossain.supervisorsolution.interfaces;

import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;

import androidx.fragment.app.Fragment;

public interface OnMyMessageSendListener {
    void onMyTitleDefenseRegistrationMessage(Fragment fragment, TitleDefenseRegistration titleDefenseRegistration);
    void onMyAuthenticationMessage(String messageKey);
    void onMyForgotPasswordMessage(String messageKey);
}
