package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.preferences.SharedPrefManager;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private Context context;
    private OnMyMessageSendListener onMyMessageSendListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_logout).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPrefManager sharedPrefManager = new SharedPrefManager(context);
            sharedPrefManager.clearPreference();

            onMyMessageSendListener.onMyHeaderViewAndNavMenuItem(getResources().getString(R.string.nav_header_title), getResources().getString(R.string.nav_header_subtitle), "Login", R.drawable.ic_login, false);
            onMyMessageSendListener.onMyAuthenticationMessage(IntentAndBundleKey.KEY_FRAGMENT_AUTHENTICATION_LOGIN, "");
        }
        return true;
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
