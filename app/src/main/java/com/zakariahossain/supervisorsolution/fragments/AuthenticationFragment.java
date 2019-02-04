package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class AuthenticationFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private static final int RC_SIGN_IN = 123;
    private OnMyMessageSendListener onMyMessageSendListener;

    private SignInButton googleSignInButton;
    private MaterialButton loginButton, signUpButton;
    private AppCompatSpinner userRoleSpinner;
    private AppCompatTextView signUpTextView, loginTextView, forgotPasswordTextView;
    private TextInputLayout editTextLoginEmail, editTextLoginPassword, editTextSignUpEmail, editTextSignUpPassword;

    private String userLoginRole;
    private GoogleSignInClient googleSignInClient;

    public AuthenticationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        context = container.getContext();

        if (getArguments() != null) {
            String key = getArguments().getString(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION);

            if (key != null) {
                switch (key) {
                    case IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN:
                        view = inflater.inflate(R.layout.fragment_authentication_login, container, false);

                        setUpLoginUi(view);
                        setGooglePlusButtonText(googleSignInButton);
                        seUserRoleSpinner();
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_SIGN_UP:
                        view = inflater.inflate(R.layout.fragment_authentication_signup, container, false);

                        setUpSignUpUi(view);
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
            return inflater.inflate(R.layout.fragment_authentication_login, container, false);
        }
    }

    private void seUserRoleSpinner() {
        final ArrayAdapter<String> userRoleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.user_role_to_login));
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRoleSpinner.setAdapter(userRoleAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        updateUI(account);
    }

    private void setUpLoginUi(View view) {
        editTextLoginEmail = view.findViewById(R.id.tilLoginEmail);
        editTextLoginPassword = view.findViewById(R.id.tilLoginPassword);
        signUpTextView = view.findViewById(R.id.tvSignUp);
        forgotPasswordTextView = view.findViewById(R.id.tvForgotPassword);
        loginButton = view.findViewById(R.id.btnLogin);
        userRoleSpinner = view.findViewById(R.id.spUserRole);
        googleSignInButton = view.findViewById(R.id.btnGoogleSignIn);

        signUpTextView.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
        userRoleSpinner.setOnItemSelectedListener(this);
    }

    private void setUpSignUpUi(View view) {
        editTextSignUpEmail = view.findViewById(R.id.tilSignUpEmail);
        editTextSignUpPassword = view.findViewById(R.id.tilSignUpPassword);
        loginTextView = view.findViewById(R.id.tvLogin);
        signUpButton = view.findViewById(R.id.btnSignUp);

        loginTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void setGooglePlusButtonText(SignInButton signInButton) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign in with Google");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextAppearance(android.R.style.TextAppearance_Widget_Button);
                } else {
                    tv.setTextAppearance(context, android.R.style.TextAppearance_Widget_Button);
                }
                return;
            }
        }
    }

    private boolean getValueFromLoginTextInputLayout() {
        String loginEmail = Objects.requireNonNull(editTextLoginEmail.getEditText()).getText().toString();
        String loginPassword = Objects.requireNonNull(editTextLoginPassword.getEditText()).getText().toString();

        if (!TextUtils.isEmpty(loginEmail.trim()) && !TextUtils.isEmpty(loginPassword.trim())) {
            editTextLoginEmail.setBoxStrokeColor(getResources().getColor(R.color.colorPrimary));
            return true;
        } else {
            if (TextUtils.isEmpty(loginEmail.trim())) {
                editTextLoginEmail.setError("Please enter an address. (Ex: zakaria15-5729@diu.edu.bd)");
            }
            if (TextUtils.isEmpty(loginPassword.trim())) {
                editTextLoginPassword.setError("Please enter a password.");
            }
            return false;
        }
    }

    private boolean getValueFromSignUpTextInputLayout() {
        String signUpEmail = Objects.requireNonNull(editTextSignUpEmail.getEditText()).getText().toString();
        String signUpPassword = Objects.requireNonNull(editTextSignUpPassword.getEditText()).getText().toString();

        if (!TextUtils.isEmpty(signUpEmail.trim()) && !TextUtils.isEmpty(signUpPassword.trim())) {
            return true;
        } else {
            if (TextUtils.isEmpty(signUpEmail.trim())) {
                editTextSignUpEmail.setError("Please enter an address.");
                editTextSignUpEmail.setBoxStrokeColor(getResources().getColor(R.color.colorPrimary));

            }
            if (TextUtils.isEmpty(signUpPassword.trim())) {
                editTextSignUpPassword.setError("Please enter a password.");
                editTextSignUpPassword.setBoxStrokeColor(getResources().getColor(R.color.colorPrimary));

            }
            return false;
        }
    }

    private void signInWithGoogleAccount() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOutWithGoogleAccount() {
       googleSignInClient.signOut()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()) {
                           Toast.makeText(context, "logout success", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(context, "logout failed", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                String email = account.getEmail();
                String name = account.getDisplayName();
                Uri imageUri = account.getPhotoUrl();

                if (email != null) {
                    String[] split = email.split("@");
                    String emailExtension = split[1];

                    Toast.makeText(context, ""+userLoginRole, Toast.LENGTH_SHORT).show();

                    if (emailExtension.equals("diu.edu.bd") && !userLoginRole.equals("Choose a role to login")) {
                        updateUI(account);
                    } else {
                        Toast.makeText(context, "Please, choose your role first and select a DIU email", Toast.LENGTH_LONG).show();
                        signOutWithGoogleAccount();
                    }
                }
            } else {
                Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
            }
        } catch (ApiException e) {
            Toast.makeText(context, "Please, choose your role first and select a DIU email", Toast.LENGTH_SHORT).show();
            updateUI(null);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Toast.makeText(context, "have account", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "not have account", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                Toast.makeText(context, ""+userLoginRole, Toast.LENGTH_SHORT).show();

                if (getValueFromLoginTextInputLayout() && !userLoginRole.equals("Choose a role to login")) {
                    Toast.makeText(context, "login", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "login empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnSignUp:
                if (getValueFromSignUpTextInputLayout()) {
                    Toast.makeText(context, "login", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "login empty", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvSignUp:
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_SIGN_UP);
                break;

            case R.id.tvLogin:
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN);
                break;

            case R.id.btnGoogleSignIn:
                signInWithGoogleAccount();
                break;

            case R.id.tvForgotPassword:
                onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userLoginRole = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        userLoginRole = "Choose a role to login";
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
