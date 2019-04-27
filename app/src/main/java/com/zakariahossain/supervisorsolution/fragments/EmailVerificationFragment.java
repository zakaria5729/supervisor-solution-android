package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class EmailVerificationFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private Context context;
    private OnMyMessageListener onMyMessageSendListener;

    private TextInputLayout textInputVerificationCode;

    private String verificationEmail,verificationCode;
    private MyApiService myApiService;
    private OthersUtil progressBar;
    private AlertDialog alertDialog;

    public EmailVerificationFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_email_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Verification");
        }
        setUpEmailVerificationUi(view);

        progressBar = new OthersUtil(context);
        myApiService = new NetworkCall();
    }

    private void setUpEmailVerificationUi(View view) {
        textInputVerificationCode = view.findViewById(R.id.tilVerificationCode);
        TextInputEditText editTextVerificationCode = view.findViewById(R.id.etVerificationCode);

        textInputVerificationCode.requestFocus();

        view.findViewById(R.id.btnBackVerification).setOnClickListener(this);
        view.findViewById(R.id.btnSendVerification).setOnClickListener(this);
        editTextVerificationCode.setOnEditorActionListener(editorActionListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnBackVerification:
                OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                onMyMessageSendListener.onMyFragment(new LoginFragment());
                break;

            case R.id.btnSendVerification:
                if (getValueFromVerificationTextInputLayout()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    verificationForUserLogin();
                }
                break;
        }
    }

    private void verificationForUserLogin() {
        myApiService.verifyEmail(verificationEmail, Integer.parseInt(verificationCode), new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        onMyMessageSendListener.onMyFragmentAndEmail(new LoginFragment(), verificationEmail);
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
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

    private boolean getValueFromVerificationTextInputLayout() {
        if (getArguments() != null) {
            verificationEmail = getArguments().getString("key_email");
        } else {
            verificationEmail = "";
        }

        verificationCode = Objects.requireNonNull(textInputVerificationCode.getEditText()).getText().toString();

        if (verificationCode.length() < 6) {
            verificationCode = "";
        }

        if (!TextUtils.isEmpty(verificationCode) && !TextUtils.isEmpty(verificationEmail.trim())) {
            return true;
        } else {
            if(TextUtils.isEmpty(verificationEmail.trim())) {
                Toast.makeText(context, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
            }
            if (TextUtils.isEmpty(verificationCode)) {
                textInputVerificationCode.setError("Verification code must be 6 digit");
            }
            return false;
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    if (getValueFromVerificationTextInputLayout()) {
                        alertDialog = progressBar.setCircularProgressBar();
                        verificationForUserLogin();
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
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
        onMyMessageSendListener.onMyFragment(new LoginFragment());
        return true;
    }
}
