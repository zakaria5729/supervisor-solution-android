package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class ForgotAndResetPasswordFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private OnMyMessageSendListener onMyMessageSendListener;

    private TextInputLayout textInputLayoutEnterEmail, textInputLayoutVerificationCode, textInputLayoutResetPassword;

    private MaterialButton enterEmailBackButton, enterEmailNextButton, verificationBackButton, verificationSendButton, resetPasswordBackButton, resetPasswordButton;

    public ForgotAndResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        context = container.getContext();

        if (getArguments() != null) {
            String key = getArguments().getString(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD);

            if (key != null) {
                switch (key) {
                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL:
                        view = inflater.inflate(R.layout.fragment_forgot_password_enter_email, container, false);

                        setUpEnterEmailUi(view);
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_VERIFICATION:
                        view = inflater.inflate(R.layout.fragment_forgot_password_verification, container, false);

                        setUpVerificationUi(view);
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET:
                        view = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);

                        setUpResetPasswordUi(view);
                        break;
                }
            }
        }

        if (getActivity() != null) {
            getActivity().setTitle("Authentication");
        }

        if (view != null) {
            return view;
        } else {
            return inflater.inflate(R.layout.fragment_forgot_password_enter_email, container, false);
        }
    }

    private void setUpEnterEmailUi(View view) {
        textInputLayoutEnterEmail = view.findViewById(R.id.tilEnterEmail);
        enterEmailBackButton = view.findViewById(R.id.btnBackEnterEmail);
        enterEmailNextButton = view.findViewById(R.id.btnNextEnterEmail);

        enterEmailBackButton.setOnClickListener(this);
        enterEmailNextButton.setOnClickListener(this);
    }

    private void setUpVerificationUi(View view) {
        textInputLayoutVerificationCode = view.findViewById(R.id.tilVerification);
        verificationBackButton = view.findViewById(R.id.btnBackVerification);
        verificationSendButton = view.findViewById(R.id.btnSendVerification);

        verificationBackButton.setOnClickListener(this);
        verificationSendButton.setOnClickListener(this);
    }

    private void setUpResetPasswordUi(View view) {
        textInputLayoutResetPassword = view.findViewById(R.id.tilResetPassword);
        resetPasswordBackButton = view.findViewById(R.id.btnBackResetPassword);
        resetPasswordButton = view.findViewById(R.id.btnResetPassword);

        resetPasswordBackButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackEnterEmail:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnNextEnterEmail:
                onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_VERIFICATION);
                break;

            case R.id.btnBackVerification:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnSendVerification:
                onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET);
                break;

            case R.id.btnBackResetPassword:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnResetPassword:

                break;

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onMyMessageSendListener = (OnMyMessageSendListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements onMyTitleDefenseRegistrationMessage method.");
        }
    }
}
