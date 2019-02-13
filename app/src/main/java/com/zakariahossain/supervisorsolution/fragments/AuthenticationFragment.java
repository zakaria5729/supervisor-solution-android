package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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
import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.User;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.CircularProgressBar;
import com.zakariahossain.supervisorsolution.utils.HandlerUtil;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class AuthenticationFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private static final int RC_SIGN_IN = 123;
    private OnMyMessageSendListener onMyMessageSendListener;

    private SignInButton googleSignInButton;
    private MaterialButton loginButton, signUpButton, verificationBackButton, verificationNextButton;
    private AppCompatSpinner userRoleSpinner;
    private AppCompatTextView signUpTextView, loginTextView, forgotPasswordTextView, userRoleErrorTexVew;
    private TextInputLayout editTextLoginEmail, editTextLoginPassword, editTextSignUpName, editTextSignUpEmail, editTextSignUpPassword, editTextSignUpConfirmPassword, editTextVerificationCode;

    private String userLoginRole, signUpName, signUpEmail, signUpPassword, signUpConfirmPassword, loginEmail, loginPassword, emailExtension, verificationCode;
    private GoogleSignInClient googleSignInClient;
    private MyApiService myApiService;
    private CircularProgressBar progressBar;
    private AlertDialog alertDialog = null;

    public AuthenticationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        context = container.getContext();

        if (getArguments() != null) {
            String bundleKey = getArguments().getString(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION);

            if (bundleKey != null) {
                switch (bundleKey) {
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

                    case IntentAndBundleKey.KEY_FRAGMENT_EMAIL_VERIFICATION:
                        view = inflater.inflate(R.layout.fragment_email_verification, container, false);
                        setUpEmailVerificationUi(view);
                        break;
                }
            }
        }

        progressBar = new CircularProgressBar(context);
        myApiService = new NetworkCall();

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
        userRoleErrorTexVew = view.findViewById(R.id.tvErrorUserRole);
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
        editTextSignUpName = view.findViewById(R.id.tilSignUpName);
        editTextSignUpEmail = view.findViewById(R.id.tilSignUpEmail);
        editTextSignUpPassword = view.findViewById(R.id.tilSignUpPassword);
        editTextSignUpConfirmPassword = view.findViewById(R.id.tilSignUpConfirmPassword);
        loginTextView = view.findViewById(R.id.tvLogin);
        signUpButton = view.findViewById(R.id.btnSignUp);

        loginTextView.setOnClickListener(this);
        signUpButton.setOnClickListener(this);
    }

    private void setUpEmailVerificationUi(View view) {
        editTextVerificationCode = view.findViewById(R.id.tilVerificationCode);
        verificationBackButton = view.findViewById(R.id.btnBackVerification);
        verificationNextButton = view.findViewById(R.id.btnNextVerification);

        verificationBackButton.setOnClickListener(this);
        verificationNextButton.setOnClickListener(this);
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
        loginEmail = Objects.requireNonNull(editTextLoginEmail.getEditText()).getText().toString();
        loginPassword = Objects.requireNonNull(editTextLoginPassword.getEditText()).getText().toString();

        if (Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) {
            String[] split = loginEmail.split("@");
            emailExtension = split[1];
        } else {
            emailExtension = "";
        }

            if ((!TextUtils.isEmpty(loginEmail.trim()) && emailExtension.equals("diu.edu.bd")) && !TextUtils.isEmpty(loginPassword.trim()) && !userLoginRole.equals("Choose a role to login")) {
                userRoleErrorTexVew.setVisibility(View.GONE);
                return true;
            } else {

                if ((TextUtils.isEmpty(loginEmail.trim()) || !emailExtension.equals("diu.edu.bd"))) {
                    editTextLoginEmail.setError("Please enter a DIU Email (Ex: example15-1234@diu.edu.bd)");
                }
                if (TextUtils.isEmpty(loginPassword.trim())) {
                    editTextLoginPassword.setError("Please enter a password.");
                }
                if (userLoginRole.equals("Choose a role to login")) {
                    userRoleErrorTexVew.setText("Please choose your role first");
                    userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorRed));
                    userRoleErrorTexVew.setVisibility(View.VISIBLE);
                }
                return false;
            }
    }

    private boolean getValueFromSignUpTextInputLayout() {
        signUpName = Objects.requireNonNull(editTextSignUpName.getEditText()).getText().toString();
        signUpEmail = Objects.requireNonNull(editTextSignUpEmail.getEditText()).getText().toString();
        signUpPassword = Objects.requireNonNull(editTextSignUpPassword.getEditText()).getText().toString();
        signUpConfirmPassword = Objects.requireNonNull(editTextSignUpConfirmPassword.getEditText()).getText().toString();

        if (Patterns.EMAIL_ADDRESS.matcher(signUpEmail).matches()) {
            String[] split = signUpEmail.split("@");
            emailExtension = split[1];
        } else {
            emailExtension = "";
        }

        if(signUpPassword.length() < 8) {
            signUpPassword = "";
        }

        if (!TextUtils.isEmpty(signUpName.trim()) && (!TextUtils.isEmpty(signUpEmail.trim()) && emailExtension.equals("diu.edu.bd")) && !TextUtils.isEmpty(signUpPassword.trim()) && !TextUtils.isEmpty(signUpConfirmPassword.trim())) {
            if(signUpPassword.equals(signUpConfirmPassword)) {
                return true;
            } else {
                editTextSignUpConfirmPassword.setError("Password and confirm password not match");
                return false;
            }
        } else {
            if (TextUtils.isEmpty(signUpName.trim())) {
                editTextSignUpName.setError("Please enter your name");
            }
            if (TextUtils.isEmpty(signUpEmail.trim()) || !emailExtension.equals("diu.edu.bd")) {
                editTextSignUpEmail.setError("Please enter a DIU email (Ex: example15-1234@diu.edu.bd)");
            }
            if (TextUtils.isEmpty(signUpPassword.trim())) {
                editTextSignUpPassword.setError("Password must be at least 8 characters long");
            }
            if (TextUtils.isEmpty(signUpConfirmPassword.trim())) {
                editTextSignUpConfirmPassword.setError("Please retype confirm password");
            }
            return false;
        }
    }

    private boolean getValueFromVerificationTextInputLayout() {
        if (getArguments() != null) {
            loginEmail = getArguments().getString("email_for_verification_code");
        } else {
            loginEmail = "";
        }

        verificationCode = Objects.requireNonNull(editTextVerificationCode.getEditText()).getText().toString();

        if (verificationCode.length() < 6) {
            verificationCode = "";
        }

        if (!TextUtils.isEmpty(verificationCode) && !TextUtils.isEmpty(loginEmail.trim())) {
            return true;
        } else {
            if(TextUtils.isEmpty(loginEmail.trim())) {
                Toast.makeText(context, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
            }
            if (TextUtils.isEmpty(verificationCode)) {
                editTextVerificationCode.setError("Verification code must be 6 digit");
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
                        if (!task.isSuccessful()) {
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
                if (account.getEmail() != null) {
                    String[] split = account.getEmail().split("@");
                    String emailExtension = split[1];

                    if (emailExtension.equals("diu.edu.bd") && !userLoginRole.equals("Choose a role to login")) {
                        loginOrSignInUser(account.getDisplayName(), account.getEmail(), "1", account.getId(), userLoginRole, "google_sign_in");
                        updateUI(account);
                    } else {
                        if (!emailExtension.equals("diu.edu.bd") && userLoginRole.equals("Choose a role to login")) {
                            userRoleErrorTexVew.setText("Please choose your role first");
                            userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorRed));
                            userRoleErrorTexVew.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Please choose your role first and select a DIU email", Toast.LENGTH_LONG).show();
                        } else {
                            if(userLoginRole.equals("Choose a role to login") && !emailExtension.equals("diu.edu.bd")) {
                                userRoleErrorTexVew.setText("Please choose your role first");
                                userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorRed));
                                userRoleErrorTexVew.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Please choose your role first and select a DIU email", Toast.LENGTH_LONG).show();
                            } else {
                                if (userLoginRole.equals("Choose a role to login")) {
                                    userRoleErrorTexVew.setText("Please choose your role first");
                                    userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorRed));
                                    userRoleErrorTexVew.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "Please choose your role first", Toast.LENGTH_LONG).show();
                                }
                                if (!emailExtension.equals("diu.edu.bd")) {
                                    Toast.makeText(context, "Please select a DIU email", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                        signOutWithGoogleAccount();
                    }
                }
            } else {
                Toast.makeText(context, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            Toast.makeText(context, "Please choose your role first and select a DIU email", Toast.LENGTH_LONG).show();
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
                if (getValueFromLoginTextInputLayout()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    loginOrSignInUser("", loginEmail, loginPassword, "", userLoginRole, "login_email");
                }
                break;

            case R.id.btnSignUp:
                if (getValueFromSignUpTextInputLayout()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    signUpUser();
                }
                break;

            case R.id.tvSignUp:
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_SIGN_UP, "");
                break;

            case R.id.tvLogin:
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
                break;

            case R.id.btnGoogleSignIn:
                signInWithGoogleAccount();
                break;

            case R.id.tvForgotPassword:
                onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL, "");
                break;

            case R.id.btnBackVerification:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnNextVerification:
                if (getValueFromVerificationTextInputLayout()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    verificationForUserLogin();
                }
                break;
        }
    }

    private void verificationForUserLogin() {
        myApiService.verifyEmail(loginEmail, Integer.parseInt(verificationCode), new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        HandlerUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        HandlerUtil.removePreviousFragmentsFromBackStack(getChildFragmentManager());
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

    private void signUpUser() {
        myApiService.signUp(signUpName, signUpEmail, signUpPassword, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        HandlerUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        HandlerUtil.removePreviousFragmentsFromBackStack(getChildFragmentManager());
                        onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
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

    private void loginOrSignInUser(String name, String email, String password, String token, String userRole, final String loginType) {
        myApiService.loginOrSignIn(name, email, password, token, userRole, loginType, new ResponseCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse data) {
                if(loginType.equals("login_email")) {
                    alertDialog.dismiss();
                }

                if (data != null) {
                    if(data.getError().equals(false)) {
                        User user = data.getUser();
                        Toast.makeText(context, ""+user.getName(), Toast.LENGTH_SHORT).show();

                    } else if (data.getError().equals(true)){
                        if (data.getMessage().equals("Please, check your inbox and verify your email first")) {
                            onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_EMAIL_VERIFICATION, loginEmail);
                            Toast.makeText(context, "verify", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                        }
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
