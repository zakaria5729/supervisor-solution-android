package com.zakariahossain.supervisorsolution.fragments;

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
import com.zakariahossain.supervisorsolution.preferences.SharedPrefManager;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

public class TabFragment extends Fragment {

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        if (getActivity() != null) {
            getActivity().setTitle("Topic and Supervisor");
        }

        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);

        ViewPageAdapter topicViewPageAdapter = new ViewPageAdapter(getChildFragmentManager());
        topicViewPageAdapter.addFragment(new TopicFragment(), "Topic");
        topicViewPageAdapter.addFragment(new SupervisorFragment(), "Supervisor");

        viewPager.setAdapter(topicViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return true;
    }
}
