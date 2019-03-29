package com.zakariahossain.supervisorsolution.interfaces;

import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;

import androidx.fragment.app.Fragment;

public interface OnMyMessageListener {
    void onMyTitleDefenseRegistrationMessage(Fragment fragment, TitleDefenseRegistration titleDefenseRegistration);
    void onMyFragmentAndEmail(Fragment fragment, String email);
    void onMyForgotPasswordMessage(String messageKey, String email);

    void onMyFragment(Fragment fragment);
    void onMyHomeOrRule(String messageKey);
    void onMyHeaderViewAndNavMenuItem(String name, String email, String menuItemTitle, int icon);
}
