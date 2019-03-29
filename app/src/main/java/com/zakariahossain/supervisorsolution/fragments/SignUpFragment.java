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
import android.view.animation.AnimationUtils;
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

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class SignUpFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private Context context;
    private OnMyMessageListener onMyMessageSendListener;

    private TextInputLayout textInputSignUpName, textInputSignUpEmail, textInputSignUpPassword, textInputSignUpConfirmPassword;

    private String signUpName;
    private String signUpEmail;
    private String signUpPassword;
    private MyApiService myApiService;
    private OthersUtil progressBar;
    private AlertDialog alertDialog;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_signup, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Sign up");
        }

        setUpSignUpUi(view);
        progressBar = new OthersUtil(context);
        myApiService = new NetworkCall();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private void setUpSignUpUi(View view) {
        textInputSignUpName = view.findViewById(R.id.tilSignUpName);
        textInputSignUpEmail = view.findViewById(R.id.tilSignUpEmail);
        textInputSignUpPassword = view.findViewById(R.id.tilSignUpPassword);
        textInputSignUpConfirmPassword = view.findViewById(R.id.tilSignUpConfirmPassword);
        AppCompatTextView loginTextView = view.findViewById(R.id.tvLogin);
        MaterialButton signUpButton = view.findViewById(R.id.btnSignUp);
        TextInputEditText editTextSignUpConfirmPassword = view.findViewById(R.id.etSignUpConfirmPassword);

        textInputSignUpName.requestFocus();

        loginTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
        editTextSignUpConfirmPassword.setOnEditorActionListener(editorActionListener);

        view.findViewById(R.id.imageView).setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:
                if (getValueFromSignUpTextInputLayout()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    signUpUser();
                }
                break;

            case R.id.tvLogin:
                onMyMessageSendListener.onMyFragment(new LoginFragment());
                break;
        }
    }

    private void signUpUser() {
        myApiService.signUp(signUpName, signUpEmail, signUpPassword, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        onMyMessageSendListener.onMyFragmentAndEmail(new LoginFragment(), signUpEmail);
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable th) {
                alertDialog.dismiss();
                Toast.makeText(context, th.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean getValueFromSignUpTextInputLayout() {
        signUpName = Objects.requireNonNull(textInputSignUpName.getEditText()).getText().toString();
        signUpEmail = Objects.requireNonNull(textInputSignUpEmail.getEditText()).getText().toString();
        signUpPassword = Objects.requireNonNull(textInputSignUpPassword.getEditText()).getText().toString();
        String signUpConfirmPassword = Objects.requireNonNull(textInputSignUpConfirmPassword.getEditText()).getText().toString();

        String emailExtension;
        if (Patterns.EMAIL_ADDRESS.matcher(signUpEmail).matches()) {
            String[] split = signUpEmail.split("@");
            emailExtension = split[1];
        } else {
            emailExtension = "";
        }

        if(signUpPassword.length() < 8 || signUpPassword.contains(" ")) {
            signUpPassword = "";
        }

        if (!TextUtils.isEmpty(signUpName.trim()) && (!TextUtils.isEmpty(signUpEmail.trim()) && emailExtension.equals("diu.edu.bd")) && OthersUtil.passwordPatternCheck(signUpPassword) && !TextUtils.isEmpty(signUpConfirmPassword.trim())) {
            if(signUpPassword.equals(signUpConfirmPassword)) {
                return true;
            } else {
                textInputSignUpConfirmPassword.setError("Password and confirm password not match");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(signUpName.trim())) {
                textInputSignUpName.setError("Please enter your name");
            }
            if (TextUtils.isEmpty(signUpEmail.trim()) || !emailExtension.equals("diu.edu.bd")) {
                textInputSignUpEmail.setError("Please enter a DIU email (Ex: example15-1234@diu.edu.bd)");
            }
            if (!OthersUtil.passwordPatternCheck(signUpPassword)) {
                textInputSignUpPassword.setError("Password must be combined as alphabets, numbers and symbols");
            }
            if (TextUtils.isEmpty(signUpPassword.trim())) {
                textInputSignUpPassword.setError("Password must be at least 8 characters long without a whitespace");
            }
            if (TextUtils.isEmpty(signUpConfirmPassword.trim())) {
                textInputSignUpConfirmPassword.setError("Please retype confirm password");
            }
            return false;
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:
                    if (getValueFromSignUpTextInputLayout()) {
                        alertDialog = progressBar.setCircularProgressBar();
                        signUpUser();
                    }
                    break;
            }
            return true;
        }
    };

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
        onMyMessageSendListener.onMyFragment(new LoginFragment());
        return true;
    }
}