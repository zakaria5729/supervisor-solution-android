package com.zakariahossain.supervisorsolution.activities;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.fragments.AuthenticationFragment;
import com.zakariahossain.supervisorsolution.fragments.ForgotAndResetPasswordFragment;
import com.zakariahossain.supervisorsolution.fragments.HomeFragment;
import com.zakariahossain.supervisorsolution.fragments.RuleFragment;
import com.zakariahossain.supervisorsolution.fragments.TabFragment;
import com.zakariahossain.supervisorsolution.fragments.TitleDefenseRegistrationOneFragment;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMyMessageSendListener {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMainActivityUi(savedInstanceState);
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
        AppCompatTextView userName = headerView.findViewById(R.id.tvUserName);
        AppCompatTextView userEmail = headerView.findViewById(R.id.tvUserEmail);
        ImageView userImage = headerView.findViewById(R.id.ivSupervisor);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                closeVisibleSoftKeyBoard();
                removePreviousFragmentsFromBackStack();
                replaceFragment(new HomeFragment());
                break;

            case R.id.nav_topic_teacher:
                closeVisibleSoftKeyBoard();
                removePreviousFragmentsFromBackStack();
                replaceFragment(new TabFragment());
                break;

            case R.id.nav_rule:
                closeVisibleSoftKeyBoard();
                removePreviousFragmentsFromBackStack();
                replaceFragment(new RuleFragment());
                break;

            case R.id.nav_login:
                closeVisibleSoftKeyBoard();
                removePreviousFragmentsFromBackStack();
                OnMyMessageSendListener onMyMessageSendListener = this;
                onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN);
                break;

            case R.id.nav_title_efense_registration:
                closeVisibleSoftKeyBoard();
                removePreviousFragmentsFromBackStack();
                replaceFragmentWithBackStack(new TitleDefenseRegistrationOneFragment());
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    private void replaceFragmentWithBackStack(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragmentContainer, fragment).commit();
    }

    private void removePreviousFragmentsFromBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    @Override
    public void onMyTitleDefenseRegistrationMessage(Fragment fragment, TitleDefenseRegistration titleDefenseRegistration) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE, titleDefenseRegistration);
        fragment.setArguments(bundle);

        replaceFragmentWithBackStack(fragment);
    }

    @Override
    public void onMyAuthenticationMessage(String messageKey) {
        AuthenticationFragment authenticationFragment = new AuthenticationFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION, messageKey);
        authenticationFragment.setArguments(bundle);

        replaceFragmentWithBackStack(authenticationFragment);
    }

    @Override
    public void onMyForgotPasswordMessage(String messageKey) {
        ForgotAndResetPasswordFragment passwordFragment = new ForgotAndResetPasswordFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD, messageKey);
        passwordFragment.setArguments(bundle);

        replaceFragmentWithBackStack(passwordFragment);
    }

    private void closeVisibleSoftKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
