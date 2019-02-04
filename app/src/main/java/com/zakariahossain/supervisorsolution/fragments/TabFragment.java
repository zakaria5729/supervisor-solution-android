package com.zakariahossain.supervisorsolution.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.adapters.TopicViewPageAdapter;

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

        TopicViewPageAdapter topicViewPageAdapter = new TopicViewPageAdapter(getChildFragmentManager());
        topicViewPageAdapter.addFragment(new TopicFragment(), "Topic");
        topicViewPageAdapter.addFragment(new SupervisorFragment(), "Supervisor");

        viewPager.setAdapter(topicViewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
