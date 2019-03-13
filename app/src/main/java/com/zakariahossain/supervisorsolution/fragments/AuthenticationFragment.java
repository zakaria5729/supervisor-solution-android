package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.User;
import com.zakariahossain.supervisorsolution.preferences.SharedPrefManager;
import com.zakariahossain.supervisorsolution.preferences.ShowCasePreference;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;
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
    private SharedPrefManager sharedPrefManager;
    private ShowCasePreference showCasePreference;

    private SignInButton googleSignInButton;
    private MaterialButton loginButton, signUpButton, verificationBackButton, verificationNextButton;
    private AppCompatSpinner userRoleSpinner;
    private AppCompatTextView signUpTextView, loginTextView, forgotPasswordTextView, userRoleErrorTexVew;
    private TextInputLayout editTextLoginEmail, editTextLoginPassword, editTextSignUpName, editTextSignUpEmail, editTextSignUpPassword, editTextSignUpConfirmPassword, editTextVerificationCode;

    private String userLoginRole, signUpName, signUpEmail, signUpPassword, signUpConfirmPassword, loginEmail, loginPassword, emailExtension, verificationCode, bundleKey;
    private GoogleSignInClient googleSignInClient;
    private MyApiService myApiService;
    private OthersUtil progressBar;
    private AlertDialog alertDialog = null;

    public AuthenticationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        context = container.getContext();

        if (getArguments() != null) {
            bundleKey = getArguments().getString(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION);

            if (bundleKey != null) {
                switch (bundleKey) {
                    case IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN:
                        view = inflater.inflate(R.layout.fragment_authentication_login, container, false);
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_SIGN_UP:
                        view = inflater.inflate(R.layout.fragment_authentication_signup, container, false);
                        break;

                    case IntentAndBundleKey.KEY_FRAGMENT_EMAIL_VERIFICATION:
                        view = inflater.inflate(R.layout.fragment_email_verification, container, false);
                        break;
                }
            }
        }

        if (view != null) {
            return view;
        } else {
            return inflater.inflate(R.layout.fragment_authentication_login, container, false);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Authentication");
        }

        sharedPrefManager = new SharedPrefManager(context);
        showCasePreference = new ShowCasePreference(context);

        progressBar = new OthersUtil(context);
        myApiService = new NetworkCall();

        if (bundleKey != null) {
            switch (bundleKey) {
                case IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN:
                    setUpLoginUi(view);
                    setGooglePlusButtonText(googleSignInButton);
                    seUserRoleSpinner();

                    if (showCasePreference.isNotShownCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_GOOGLE_SIGN_IN)) {
                        OthersUtil.popUpShow(getActivity(), R.id.btnGoogleSignIn, "Google Sign In", "This button only works to sign in for Student account. Click on the button to sign in using google account.");
                        showCasePreference.updateShowCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_GOOGLE_SIGN_IN);
                    }
                    break;

                case IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_SIGN_UP:
                    setUpSignUpUi(view);
                    break;

                case IntentAndBundleKey.KEY_FRAGMENT_EMAIL_VERIFICATION:
                    setUpEmailVerificationUi(view);
                    break;
            }
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    private void seUserRoleSpinner() {
        final ArrayAdapter<String> userRoleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.user_role_to_login));
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRoleSpinner.setAdapter(userRoleAdapter);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleGoogleSignInResult(task);
        }
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null) {
                if (account.getEmail() != null) {
                    String[] split = account.getEmail().split("@");
                    String emailExtension = split[1];

                    if (emailExtension.equals("diu.edu.bd") && userLoginRole.equals("Student")) {
                        loginOrSignInUser(account.getDisplayName(), account.getEmail(), ".", account.getId(), userLoginRole, "google_sign_in");
                    } else {
                        if (!emailExtension.equals("diu.edu.bd") && !userLoginRole.equals("Student")) {
                            userRoleErrorTexVew.setText("Please choose your role as a Student");
                            userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorRed));
                            userRoleErrorTexVew.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Please choose your role as a Student and select a DIU email", Toast.LENGTH_LONG).show();
                        } else {
                                if (!userLoginRole.equals("Student")) {
                                    userRoleErrorTexVew.setText("Please choose your role as a Student");
                                    userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorRed));
                                    userRoleErrorTexVew.setVisibility(View.VISIBLE);
                                    Toast.makeText(context, "Please choose your role as a Student", Toast.LENGTH_LONG).show();
                                }

                                if (!emailExtension.equals("diu.edu.bd")) {
                                    Toast.makeText(context, "Please select a DIU email", Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                        googleSignInClient.signOut();
                    }
            } else {
                Toast.makeText(context, "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
            }
        } catch (ApiException e) {
            Toast.makeText(context, "Error: "+e.getMessage()+" Try again", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, connectionResult.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                if (getValueFromLoginTextInputLayout()) {
                    alertDialog = progressBar.setCircularProgressBar();
                    loginOrSignInUser(".", loginEmail, loginPassword, ".", userLoginRole, "login_email");
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
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
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
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                        onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
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
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
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
                        sharedPrefManager.saveUser(user);

                        onMyMessageSendListener.onMyFragment(new ProfileFragment());
                        onMyMessageSendListener.onMyHeaderViewAndNavMenuItem(user.getName(), user.getEmail(), "Profile", R.drawable.ic_profile, true);

                    } else if (data.getError().equals(true)){
                        if (data.getMessage().equals("Please check your inbox and verify your email first")) {
                            onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_EMAIL_VERIFICATION, loginEmail);

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
                if(loginType.equals("login_email")) {
                    alertDialog.dismiss();
                }
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
