package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.LoginResponse;
import com.zakariahossain.supervisorsolution.preferences.UserSharedPrefManager;
import com.zakariahossain.supervisorsolution.preferences.ShowCaseAndTabSelectionPreference;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, GoogleApiClient.OnConnectionFailedListener, OnFragmentBackPressedListener {

    private Context context;
    private static final int RC_SIGN_IN = 123;
    private OnMyMessageListener onMyMessageSendListener;
    private UserSharedPrefManager sharedPrefManager;
    private ShowCaseAndTabSelectionPreference showCasePreference;

    private SignInButton googleSignInButton;
    private AppCompatSpinner userRoleSpinner;
    private AppCompatTextView userRoleErrorTexVew;
    private TextInputLayout textInputLoginEmail, textInputLoginPassword;

    private TextInputEditText editTextLoginEmail;

    private String userLoginRole, loginEmail, loginPassword, emailExtension;
    private GoogleSignInClient googleSignInClient;
    private MyApiService myApiService;
    private OthersUtil progressBar;
    private AlertDialog alertDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Login");
        }

        sharedPrefManager = new UserSharedPrefManager(context);
        showCasePreference = new ShowCaseAndTabSelectionPreference(context);

        progressBar = new OthersUtil(context);
        myApiService = new NetworkCall();

        setUpLoginUi(view);
        setGooglePlusButtonText(googleSignInButton);
        seUserRoleSpinner();
    }

    @Override
    public void onStart() {
        super.onStart();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(context, gso);

        if(getArguments() != null) {
            editTextLoginEmail.setText(getArguments().getString("key_email"));
        }

        if (showCasePreference.isNotShownCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_GOOGLE_SIGN_IN)) {
            OthersUtil.popUpShow(getActivity(), R.id.btnGoogleSignIn, "Google Sign In", "This button only works to sign in for Student account. Click on the button to sign in using google account.");
            showCasePreference.updateShowCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_GOOGLE_SIGN_IN);
        }
    }

    private void setUpLoginUi(View view) {
        textInputLoginEmail = view.findViewById(R.id.tilLoginEmail);
        textInputLoginPassword = view.findViewById(R.id.tilLoginPassword);
        AppCompatTextView signUpTextView = view.findViewById(R.id.tvSignUp);
        AppCompatTextView forgotPasswordTextView = view.findViewById(R.id.tvForgotPassword);
        userRoleErrorTexVew = view.findViewById(R.id.tvErrorUserRole);
        MaterialButton loginButton = view.findViewById(R.id.btnLogin);
        userRoleSpinner = view.findViewById(R.id.spUserRole);
        googleSignInButton = view.findViewById(R.id.btnGoogleSignIn);
        TextInputEditText editTextLoginPassword = view.findViewById(R.id.etLoginPassword);
        editTextLoginEmail = view.findViewById(R.id.etLoginEmail);

        textInputLoginEmail.requestFocus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            textInputLoginEmail.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
        }

        signUpTextView.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        googleSignInButton.setOnClickListener(this);
        userRoleSpinner.setOnItemSelectedListener(this);
        editTextLoginPassword.setOnEditorActionListener(editorActionListener);

        googleSignInButton.setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
        view.findViewById(R.id.imageView2).setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
    }

    private void setGooglePlusButtonText(SignInButton signInButton) {
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);

            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText("Sign in with Google Account");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    tv.setTextAppearance(android.R.style.TextAppearance_Widget_Button);
                } else {
                    tv.setTextAppearance(context, android.R.style.TextAppearance_Widget_Button);
                }
                return;
            }
        }
    }

    private void seUserRoleSpinner() {
        final ArrayAdapter<String> userRoleAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.user_role_to_login));
        userRoleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userRoleSpinner.setAdapter(userRoleAdapter);
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

            case R.id.tvSignUp:
                onMyMessageSendListener.onMyFragment(new SignUpFragment());
                break;

            case R.id.btnGoogleSignIn:
                signInWithGoogleAccount();
                break;

            case R.id.tvForgotPassword:
                onMyMessageSendListener.onMyForgotPasswordMessage(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD_ENTER_EMAIL, "");
                break;
        }
    }

    private boolean getValueFromLoginTextInputLayout() {
        loginEmail = Objects.requireNonNull(textInputLoginEmail.getEditText()).getText().toString();
        loginPassword = Objects.requireNonNull(textInputLoginPassword.getEditText()).getText().toString();

        if ((!TextUtils.isEmpty(loginEmail.trim()) && Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches()) && !TextUtils.isEmpty(loginPassword.trim()) && !userLoginRole.equals("Choose a role to login")) {
            userRoleErrorTexVew.setVisibility(View.GONE);
            return true;
        } else {

            if ((TextUtils.isEmpty(loginEmail.trim()) || !Patterns.EMAIL_ADDRESS.matcher(loginEmail).matches())) {
                textInputLoginEmail.setError("Please enter a valid email address");
            }
            if (TextUtils.isEmpty(loginPassword.trim())) {
                textInputLoginPassword.setError("Please enter a password.");
            }
            if (userLoginRole.equals("Choose a role to login")) {
                userRoleErrorTexVew.setText("Please choose your role first");
                userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorPink));
                userRoleErrorTexVew.setVisibility(View.VISIBLE);
            }
            return false;
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

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:
                    if (getValueFromLoginTextInputLayout()) {
                        alertDialog = progressBar.setCircularProgressBar();
                        loginOrSignInUser(".", loginEmail, loginPassword, ".", userLoginRole, "login_email");
                    }
                    break;
            }
            return true;
        }
    };

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
                            userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorPink));
                            userRoleErrorTexVew.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "Please choose your role as a Student and select a DIU email", Toast.LENGTH_LONG).show();
                        } else {
                            if (!userLoginRole.equals("Student")) {
                                userRoleErrorTexVew.setText("Please choose your role as a Student");
                                userRoleErrorTexVew.setTextColor(getResources().getColor(R.color.colorPink));
                                userRoleErrorTexVew.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Please choose your role as a Student", Toast.LENGTH_LONG).show();
                            }

                            if (!emailExtension.equals("diu.edu.bd")) {
                                Toast.makeText(context, "Please select a DIU email (Ex: example15-1234@diu.edu.bd)", Toast.LENGTH_LONG).show();
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

    private void loginOrSignInUser(String name, String email, String password, String token, String userRole, final String loginType) {

        myApiService.loginOrSignIn(name, email, password, token, userRole, loginType, new ResponseCallback<LoginResponse>() {
            @Override
            public void onSuccess(LoginResponse data) {
                if(loginType.equals("login_email")) {
                    alertDialog.dismiss();
                }

                if (data != null) {
                    if(data.getError().equals(false)) {
                        LoginResponse.User user = data.getUser();
                        sharedPrefManager.saveUser(user);

                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        onMyMessageSendListener.onMyFragment(new ProfileFragment());
                        onMyMessageSendListener.onMyHeaderViewAndNavMenuItem(user.getName(), user.getEmail(), "Profile", R.drawable.ic_profile);

                    } else if (data.getError().equals(true)){
                        if (data.getMessage().equals("Please check your inbox and verify your email first")) {
                            OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                            onMyMessageSendListener.onMyFragmentAndEmail(new EmailVerificationFragment(), loginEmail);

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
        onMyMessageSendListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
        return true;
    }
}