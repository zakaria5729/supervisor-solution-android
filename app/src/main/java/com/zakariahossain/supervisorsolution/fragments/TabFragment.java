package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.adapters.ViewPageAdapter;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.preferences.ShowCaseAndTabSelectionPreference;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.Objects;

public class TabFragment extends Fragment implements OnFragmentBackPressedListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private OnMyMessageListener onMyMessageListener;
    private ShowCaseAndTabSelectionPreference tabSelectionPreference;

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Topic and Supervisor");
        }
        tabSelectionPreference = new ShowCaseAndTabSelectionPreference(Objects.requireNonNull(getContext()));

        ViewPageAdapter topicViewPageAdapter = new ViewPageAdapter(getChildFragmentManager());
        topicViewPageAdapter.addFragment(new TopicFragment(), "Topic");
        topicViewPageAdapter.addFragment(new SupervisorFragment(), "Supervisor");

        viewPager.setAdapter(topicViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabSelectionPreference.updateSelectedTabPosition(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Objects.requireNonNull(tabLayout.getTabAt(tabSelectionPreference.getPreviouslyTabSelected())).select();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_search).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onMyMessageListener = (OnMyMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements OnMyMessageSendListener methods.");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }

    @Override
    public boolean onFragmentBackPressed() {
        onMyMessageListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
        return true;
    }
}
