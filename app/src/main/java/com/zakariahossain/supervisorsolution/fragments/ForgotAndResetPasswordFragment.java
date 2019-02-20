package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ForgotAndResetPasswordFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private OthersUtil progressBar;
    private AlertDialog alertDialog;
    private OnMyMessageSendListener onMyMessageSendListener;
    private MyApiService myApiService;

    private TextInputLayout textInputLayoutEnterEmail, textInputLayoutVerificationCode, textInputLayoutResetPassword, textInputLayoutResetConfirmPassword;

    private MaterialButton enterEmailBackButton, enterEmailNextButton, resetPasswordBackButton, resetPasswordButton;

    private String email, verificationCode, newPassword, confirmPassword, bundleKey;

    public ForgotAndResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        context = container.getContext();

        if (getArguments() != null) {
            bundleKey = getArguments().getString(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD);

            if (bundleKey != null) {
                switch (bundleKey) {
                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL:
                        view = inflater.inflate(R.layout.fragment_forgot_password_enter_email, container, false);
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET:
                        view = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);
                        break;
                }
            }
        }

        if (view != null) {
            return view;
        } else {
            return inflater.inflate(R.layout.fragment_forgot_password_enter_email, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Authentication");
        }

        if (bundleKey != null) {
            switch (bundleKey) {
                case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL:
                    setUpEnterEmailUi(view);
                    break;

                case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET:
                    setUpResetPasswordUi(view);
                    break;
            }
        }

        progressBar = new OthersUtil(context);
    }

    private void setUpEnterEmailUi(View view) {
        textInputLayoutEnterEmail = view.findViewById(R.id.tilEnterEmail);
        enterEmailBackButton = view.findViewById(R.id.btnBackEnterEmail);
        enterEmailNextButton = view.findViewById(R.id.btnNextEnterEmail);

        enterEmailBackButton.setOnClickListener(this);
        enterEmailNextButton.setOnClickListener(this);
    }

    private void setUpResetPasswordUi(View view) {
        textInputLayoutVerificationCode = view.findViewById(R.id.tilVerificationCode);
        textInputLayoutResetPassword = view.findViewById(R.id.tilResetPassword);
        textInputLayoutResetConfirmPassword = view.findViewById(R.id.tilResetConfirmPassword);
        resetPasswordBackButton = view.findViewById(R.id.btnBackResetPassword);
        resetPasswordButton = view.findViewById(R.id.btnResetPassword);

        resetPasswordBackButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
    }

    private boolean getEnterEmailEditTextData() {
        email = Objects.requireNonNull(textInputLayoutEnterEmail.getEditText()).getText().toString().trim();

        if (!TextUtils.isEmpty(email)) {
            return true;
        } else {
            textInputLayoutEnterEmail.setError("Enter a email address");
            return false;
        }
    }

    private boolean getResetPasswordEditTextData() {
        if(getArguments() != null) {
            email = getArguments().getString("email_for_reset_password");
        } else {
            email = "";
        }

        verificationCode = Objects.requireNonNull(textInputLayoutVerificationCode.getEditText()).getText().toString().trim();
        newPassword = Objects.requireNonNull(textInputLayoutResetPassword.getEditText()).getText().toString().trim();
        confirmPassword = Objects.requireNonNull(textInputLayoutResetConfirmPassword.getEditText()).getText().toString().trim();

        if (verificationCode.length() < 6 && newPassword.length() < 8) {
            verificationCode = "";
            newPassword = "";
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(verificationCode.trim()) && !TextUtils.isEmpty(newPassword.trim()) && !TextUtils.isEmpty(confirmPassword.trim())) {
            if (newPassword.equals(confirmPassword)) {
                return true;
            } else {
                textInputLayoutResetConfirmPassword.setError("Password and confirm password not match");
                return false;
            }
        } else {
            if(TextUtils.isEmpty(verificationCode.trim())) {
                textInputLayoutVerificationCode.setError("Verification code must be 6 digits long");
            }
            if(TextUtils.isEmpty(newPassword.trim())) {
                textInputLayoutResetPassword.setError("Password must be at least 8 characters long");
            }
            if(TextUtils.isEmpty(confirmPassword.trim())) {
                textInputLayoutResetConfirmPassword.setError("Please, retype confirm password");
            }
            if(TextUtils.isEmpty(email.trim())) {
                Toast.makeText(context, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
            }
            return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackEnterEmail:
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
                break;

            case R.id.btnNextEnterEmail:
                if (getEnterEmailEditTextData()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    sendVerificationCode(email);
                }
                break;

            case R.id.btnBackResetPassword:
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_EMAIL_VERIFICATION, "");
                break;

            case R.id.btnResetPassword:
                if (getResetPasswordEditTextData()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    resetPassword(email, verificationCode, newPassword);
                }
                break;
        }
    }

    private void resetPassword(String email, String verificationCode, String newPassword) {
        myApiService = new NetworkCall();
        myApiService.resetPassword(email, Integer.parseInt(verificationCode), newPassword, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                            onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong! Internet connection problem or something else. Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable th) {
                alertDialog.dismiss();
                Toast.makeText(context, th.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void sendVerificationCode(final String enteredEmail) {
        myApiService = new NetworkCall();
        myApiService.forgotPassword(enteredEmail, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET, email);
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong! Internet connection problem or something else. Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable th) {
                alertDialog.dismiss();
                Toast.makeText(context, th.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
