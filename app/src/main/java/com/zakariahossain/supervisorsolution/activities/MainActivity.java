package com.zakariahossain.supervisorsolution.activities;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.fragments.AuthenticationFragment;
import com.zakariahossain.supervisorsolution.fragments.ForgotAndResetPasswordFragment;
import com.zakariahossain.supervisorsolution.fragments.HomeFragment;
import com.zakariahossain.supervisorsolution.fragments.ProfileFragment;
import com.zakariahossain.supervisorsolution.fragments.RuleFragment;
import com.zakariahossain.supervisorsolution.fragments.TabFragment;
import com.zakariahossain.supervisorsolution.fragments.TitleDefenseRegistrationOneFragment;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;
import com.zakariahossain.supervisorsolution.preferences.SharedPrefManager;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMyMessageSendListener {

    private OnMyMessageSendListener onMyMessageSendListener;
    private SharedPrefManager sharedPrefManager;
    private DrawerLayout drawer;
    private Menu navMenu;

    private AppCompatTextView headerUserName, headerUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMainActivityUi(savedInstanceState);
        sharedPrefManager = new SharedPrefManager(this);
    }

    private void setUpMainActivityUi(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }

        View headerView = navigationView.getHeaderView(0);
        headerUserName = headerView.findViewById(R.id.tvUserName);
        headerUserEmail = headerView.findViewById(R.id.tvUserEmail);
        //ImageView userImage = headerView.findViewById(R.id.ivSupervisor);

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);

        onMyMessageSendListener = this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPrefManager.isLoggedIn()) {
            headerUserName.setText(sharedPrefManager.getUser().getName());
            headerUserEmail.setText(sharedPrefManager.getUser().getEmail());

            navMenu.findItem(R.id.nav_login).setIcon(R.drawable.ic_profile);
            navMenu.findItem(R.id.nav_login).setTitle("Profile");

            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(true);
        } else {
            headerUserName.setText(getResources().getString(R.string.nav_header_title));
            headerUserEmail.setText(getResources().getString(R.string.nav_header_subtitle));

            navMenu.findItem(R.id.nav_login).setIcon(R.drawable.ic_login);
            navMenu.findItem(R.id.nav_login).setTitle("Login");

            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                replaceFragment(new HomeFragment());
                break;

            case R.id.nav_topic_teacher:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                replaceFragment(new TabFragment());
                break;

            case R.id.nav_rule:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                replaceFragment(new RuleFragment());
                break;

            case R.id.nav_login:
                if (sharedPrefManager.isLoggedIn()) {
                    OthersUtil.closeVisibleSoftKeyBoard(this);
                    replaceFragment(new ProfileFragment());
                } else {
                    OthersUtil.closeVisibleSoftKeyBoard(this);
                    onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");

                    headerUserName.setText(getResources().getString(R.string.nav_header_title));
                    headerUserEmail.setText(getResources().getString(R.string.nav_header_subtitle));
                }
                break;

            case R.id.nav_title_defense_registration:
                if (sharedPrefManager.isLoggedIn() && sharedPrefManager.getUser().getUserRole().equals("Student")) {
                    OthersUtil.closeVisibleSoftKeyBoard(this);
                    replaceFragment(new TitleDefenseRegistrationOneFragment());
                }
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void onMyTitleDefenseRegistrationMessage(Fragment fragment, TitleDefenseRegistration titleDefenseRegistration) {
        if (titleDefenseRegistration != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE, titleDefenseRegistration);
            fragment.setArguments(bundle);
        }

        replaceFragment(fragment);
    }

    @Override
    public void onMyAuthenticationMessage(String messageKey, String email) {
        AuthenticationFragment authenticationFragment = new AuthenticationFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION, messageKey);
        bundle.putString("email_for_verification_code", email);
        authenticationFragment.setArguments(bundle);

        replaceFragment(authenticationFragment);
    }

    @Override
    public void onMyForgotPasswordMessage(String messageKey, String email) {
        ForgotAndResetPasswordFragment passwordFragment = new ForgotAndResetPasswordFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD, messageKey);
        bundle.putString("email_for_reset_password", email);
        passwordFragment.setArguments(bundle);

        replaceFragment(passwordFragment);
    }

    @Override
    public void onMyFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onMyHeaderViewAndNavMenuItem(String name, String email, String menuItemTitle, int icon, boolean isNavTitleDefenceShow) {
        headerUserName.setText(name);
        headerUserEmail.setText(email);

        navMenu.findItem(R.id.nav_login).setTitle(menuItemTitle);
        navMenu.findItem(R.id.nav_login).setIcon(icon);

        if (isNavTitleDefenceShow) {
            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(true);
        } else {
            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(false);
        }
    }

}
