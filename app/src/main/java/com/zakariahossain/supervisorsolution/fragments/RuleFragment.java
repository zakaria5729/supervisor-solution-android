package com.zakariahossain.supervisorsolution.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zakariahossain.supervisorsolution.R;

public class RuleFragment extends Fragment {

    public RuleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rule, container, false);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Rules");
        }

        return view;
    }
}
