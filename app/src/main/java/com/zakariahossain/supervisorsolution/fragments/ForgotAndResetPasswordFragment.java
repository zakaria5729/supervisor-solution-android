package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
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

public class ForgotAndResetPasswordFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private Context context;
    private View view;

    private OthersUtil progressBar;
    private AlertDialog alertDialog;
    private OnMyMessageListener onMyMessageSendListener;
    private MyApiService myApiService;

    private TextInputLayout textInputLayoutEnterEmail, textInputLayoutVerificationCode, textInputLayoutResetPassword, textInputLayoutResetConfirmPassword;

    private String email;
    private String verificationCode;
    private String newPassword;
    private String bundleKey;

    public ForgotAndResetPasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();

        if (getArguments() != null) {
            bundleKey = getArguments().getString(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD);

            if (bundleKey != null) {
                switch (bundleKey) {
                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL:
                        view = inflater.inflate(R.layout.fragment_forgot_password_enter_email, container, false);

                        if (getActivity() != null) {
                            getActivity().setTitle("Email Verification");
                        }
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET:
                        view = inflater.inflate(R.layout.fragment_forgot_password_reset, container, false);

                        if (getActivity() != null) {
                            getActivity().setTitle("Reset Password");
                        }
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
        MaterialButton enterEmailBackButton = view.findViewById(R.id.btnBackEnterEmail);
        MaterialButton enterEmailNextButton = view.findViewById(R.id.btnNextEnterEmail);
        TextInputEditText editTextEnterEmail = view.findViewById(R.id.etEnterEmail);

        textInputLayoutEnterEmail.requestFocus();

        enterEmailBackButton.setOnClickListener(this);
        enterEmailNextButton.setOnClickListener(this);
        editTextEnterEmail.setOnEditorActionListener(editorActionListener);
    }

    private void setUpResetPasswordUi(View view) {
        textInputLayoutVerificationCode = view.findViewById(R.id.tilVerificationCode);
        textInputLayoutResetPassword = view.findViewById(R.id.tilResetPassword);
        textInputLayoutResetConfirmPassword = view.findViewById(R.id.tilResetConfirmPassword);
        MaterialButton resetPasswordBackButton = view.findViewById(R.id.btnBackResetPassword);
        MaterialButton resetPasswordButton = view.findViewById(R.id.btnResetPassword);
        TextInputEditText editTextResetConfirmPassword = view.findViewById(R.id.etResetConfirmPassword);

        textInputLayoutVerificationCode.requestFocus();

        resetPasswordBackButton.setOnClickListener(this);
        resetPasswordButton.setOnClickListener(this);
        editTextResetConfirmPassword.setOnEditorActionListener(editorActionListener);
    }

    private boolean getEnterEmailEditTextData() {
        email = Objects.requireNonNull(textInputLayoutEnterEmail.getEditText()).getText().toString().trim();

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            textInputLayoutEnterEmail.setError("Please enter a valid email address");
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private boolean getResetPasswordEditTextData() {
        if(getArguments() != null) {
            email = getArguments().getString(IntentAndBundleKey.KEY_EMAIL_FOR_RESET_PASSWORD);
        } else {
            email = "";
        }

        verificationCode = Objects.requireNonNull(textInputLayoutVerificationCode.getEditText()).getText().toString().trim();
        newPassword = Objects.requireNonNull(textInputLayoutResetPassword.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(textInputLayoutResetConfirmPassword.getEditText()).getText().toString().trim();

        if (verificationCode.length() < 6) {
            verificationCode = "";
        }

        if (newPassword.length() < 8 || newPassword.contains(" ")) {
            newPassword = "";
        }

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(verificationCode.trim()) && OthersUtil.passwordPatternCheck(newPassword) && !TextUtils.isEmpty(confirmPassword.trim())) {
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
            if (!OthersUtil.passwordPatternCheck(newPassword)) {
                textInputLayoutResetPassword.setError("Password must be combined as alphabets, numbers(a-z, 0-9)");
            }
            if (TextUtils.isEmpty(newPassword.trim())) {
                textInputLayoutResetPassword.setError("Password must be at least 8 characters long without a whitespace");
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
                OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                onMyMessageSendListener.onMyFragment(new LoginFragment());
                break;

            case R.id.btnNextEnterEmail:
                if (getEnterEmailEditTextData()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    sendVerificationCode(email);
                }
                break;

            case R.id.btnBackResetPassword:
                OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL, email);
                break;

            case R.id.btnResetPassword:
                if (getResetPasswordEditTextData()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    resetPassword(email, verificationCode, newPassword);
                }
                break;
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    if (getEnterEmailEditTextData()) {
                        alertDialog = progressBar.setCircularProgressBar();
                        sendVerificationCode(email);
                    }
                    break;

                case EditorInfo.IME_ACTION_GO:
                    if (getResetPasswordEditTextData()) {
                        alertDialog = progressBar.setCircularProgressBar();
                        resetPassword(email, verificationCode, newPassword);
                    }
                    break;
            }
            return true;
        }
    };

    private void resetPassword(final String email, String verificationCode, String newPassword) {
        myApiService = new NetworkCall();
        myApiService.resetPassword(email, Integer.parseInt(verificationCode), newPassword, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                            onMyMessageSendListener.onMyFragmentAndEmail(new LoginFragment(), email);
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
            onMyMessageSendListener = (OnMyMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements OnMyMessageSendListener methods.");
        }
    }

    @Override
    public boolean onFragmentBackPressed() {
        if (bundleKey != null) {
            switch (bundleKey) {
                case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL:
                    onMyMessageSendListener.onMyFragment(new LoginFragment());
                    break;

                case IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_RESET:
                    onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL, email);
                    break;
            }
        }

        return true;
    }
}
