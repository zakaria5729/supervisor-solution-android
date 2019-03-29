package com.zakariahossain.supervisorsolution.activities;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.fragments.ForgotAndResetPasswordFragment;
import com.zakariahossain.supervisorsolution.fragments.HomeAndRuleFragment;
import com.zakariahossain.supervisorsolution.fragments.LoginFragment;
import com.zakariahossain.supervisorsolution.fragments.ProfileFragment;
import com.zakariahossain.supervisorsolution.fragments.TabFragment;
import com.zakariahossain.supervisorsolution.fragments.TitleDefenseRegistrationOneFragment;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.TitleDefense;
import com.zakariahossain.supervisorsolution.preferences.ShowCaseAndTabSelectionPreference;
import com.zakariahossain.supervisorsolution.preferences.UserSharedPrefManager;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMyMessageListener {

    private OnMyMessageListener onMyMessageListener;
    private UserSharedPrefManager sharedPrefManager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Menu navMenu;
    private AppCompatTextView headerUserName, headerUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpMainActivityUi(savedInstanceState);
        sharedPrefManager = new UserSharedPrefManager(this);
    }

    private void setUpMainActivityUi(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        onMyMessageListener = this;

        if (savedInstanceState == null) {
            onMyMessageListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
            navigationView.setCheckedItem(R.id.nav_home);
        }

        View headerView = navigationView.getHeaderView(0);
        headerUserName = headerView.findViewById(R.id.tvUserName);
        headerUserEmail = headerView.findViewById(R.id.tvUserEmail);
        //ImageView userImage = headerView.findViewById(R.id.ivSupervisor);

        navMenu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (sharedPrefManager.isLoggedIn()) {
            headerUserName.setText(sharedPrefManager.getUser().getName());
            headerUserEmail.setText(sharedPrefManager.getUser().getEmail());

            navMenu.findItem(R.id.nav_login).setIcon(R.drawable.ic_profile);
            navMenu.findItem(R.id.nav_login).setTitle("Profile");

            if (sharedPrefManager.getUser().getUserRole().equals("Student")) {
                navMenu.findItem(R.id.nav_title_defense_registration).setVisible(true);
            } else if (sharedPrefManager.getUser().getUserRole().equals("Supervisor")){
                navMenu.findItem(R.id.nav_title_defense_registration).setVisible(false);
            }
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
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
            if (!(fragment instanceof OnFragmentBackPressedListener) || !((OnFragmentBackPressedListener) fragment).onFragmentBackPressed()) {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                onMyMessageListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
                break;

            case R.id.nav_topic_teacher:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                replaceFragment(new TabFragment());
                break;

            case R.id.nav_rule:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                onMyMessageListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_RULE);
                break;

            case R.id.nav_login:
                if (sharedPrefManager.isLoggedIn()) {
                    OthersUtil.closeVisibleSoftKeyBoard(this);
                    replaceFragment(new ProfileFragment());
                } else {
                    OthersUtil.closeVisibleSoftKeyBoard(this);
                    replaceFragment(new LoginFragment());

                    headerUserName.setText(getResources().getString(R.string.nav_header_title));
                    headerUserEmail.setText(getResources().getString(R.string.nav_header_subtitle));
                }
                break;

            case R.id.nav_title_defense_registration:
                OthersUtil.closeVisibleSoftKeyBoard(this);
                levelAndTermCompletionAlert();
                break;

            case R.id.nav_feedback_email:
                new OthersUtil(this).openEmailDialog("zakaria15-5729@diu.edu.bd", "Send us feedback");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void levelAndTermCompletionAlert() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Course Completion")
                .setMessage(R.string.level_term)
                .setIcon(R.drawable.ic_pending)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .setCancelable(false)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button yesButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button noButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                yesButton.setTextColor(getResources().getColor(R.color.colorGreen));

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        replaceFragment(new TitleDefenseRegistrationOneFragment());
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        navigationView.setCheckedItem(R.id.nav_home);
                        onMyMessageListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
                    }
                });
            }
        });
        dialog.show();
    }

    private void replaceFragment(Fragment fragment) {
        new ShowCaseAndTabSelectionPreference(this).updateSelectedTabPosition(0); //set selected tab position 0 when replace any fragment

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public void onMyTitleDefenseRegistrationMessage(Fragment fragment, TitleDefense titleDefense) {
        if (titleDefense != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE, titleDefense);
            fragment.setArguments(bundle);
        }

        replaceFragment(fragment);
    }

    @Override
    public void onMyFragmentAndEmail(Fragment fragment, String email) {
        Bundle bundle = new Bundle();
        bundle.putString("key_email", email);
        fragment.setArguments(bundle);

        replaceFragment(fragment);
    }

    @Override
    public void onMyForgotPasswordMessage(String messageKey, String email) {
        ForgotAndResetPasswordFragment passwordFragment = new ForgotAndResetPasswordFragment();

        Bundle bundle = new Bundle();
        bundle.putString(IntentAndBundleKey.KEY_FRAGMENT_FORGOT_PASSWORD, messageKey);
        bundle.putString(IntentAndBundleKey.KEY_EMAIL_FOR_RESET_PASSWORD, email);
        passwordFragment.setArguments(bundle);

        replaceFragment(passwordFragment);
    }

    @Override
    public void onMyFragment(Fragment fragment) {
        replaceFragment(fragment);
    }

    @Override
    public void onMyHomeOrRule(String messageKey) {
        HomeAndRuleFragment homeAndRuleFragment = new HomeAndRuleFragment();
        Bundle bundle = new Bundle();

        if (messageKey.equals(IntentAndBundleKey.KEY_FRAGMENT_HOME)) {
            navigationView.setCheckedItem(R.id.nav_home);
        }

        bundle.putString(IntentAndBundleKey.KEY_FRAGMENT_HOME_OR_RULE, messageKey);
        homeAndRuleFragment.setArguments(bundle);
        replaceFragment(homeAndRuleFragment);
    }

    @Override
    public void onMyHeaderViewAndNavMenuItem(String name, String email, String menuItemTitle, int icon) {
        headerUserName.setText(name);
        headerUserEmail.setText(email);

        navMenu.findItem(R.id.nav_login).setTitle(menuItemTitle);
        navMenu.findItem(R.id.nav_login).setIcon(icon);

        if (sharedPrefManager.isLoggedIn() && sharedPrefManager.getUser().getUserRole().equals("Student")) {
            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(true);
        } else if (sharedPrefManager.isLoggedIn() && sharedPrefManager.getUser().getUserRole().equals("Supervisor")){
            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(false);
        } else {
            navMenu.findItem(R.id.nav_title_defense_registration).setVisible(false);
        }
    }
}
