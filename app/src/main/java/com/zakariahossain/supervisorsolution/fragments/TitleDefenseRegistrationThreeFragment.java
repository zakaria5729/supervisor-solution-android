package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationThreeFragment extends Fragment implements View.OnClickListener {

    private TitleDefenseRegistration titleDefenseRegistrationThree;

    private Context context;
    private List<String> checkList;
    private LinearLayoutCompat cbLinearLayout;
    private AppCompatAutoCompleteTextView autoCompleteTextViewOneInitial, autoCompleteTextViewTwoInitial, autoCompleteTextViewThreeInitial;
    private TextInputLayout textInputLayoutOtherAreaOfInterest;

    private String firstSupervisorInitial, secondSupervisorInitial, thirdSupervisorInitial, agreeCheckBox;

    public TitleDefenseRegistrationThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_defense_registration_three, container, false);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Registration");
        }
        context = container.getContext();

        setUpPageThreeUi(view);
        addCheckBoxAreaOfInterests();
        addSupervisorAutoCompleteTextView();
        getBundleDataPageThree();

        return view;
    }

    private void setUpPageThreeUi(View view) {
        checkList = new ArrayList<>();

        MaterialButton buttonBackPageThree = view.findViewById(R.id.btnBackPageThree);
        MaterialButton buttonSubmit = view.findViewById(R.id.btnSubmit);
        autoCompleteTextViewOneInitial = view.findViewById(R.id.acTextViewInitialOne);
        autoCompleteTextViewTwoInitial = view.findViewById(R.id.acTextViewInitialTwo);
        autoCompleteTextViewThreeInitial = view.findViewById(R.id.acTextViewInitialThree);
        cbLinearLayout = view.findViewById(R.id.cbLinearLayout);
        textInputLayoutOtherAreaOfInterest = view.findViewById(R.id.textInputLayoutAreaOfInterest);

        buttonBackPageThree.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    private void addSupervisorAutoCompleteTextView() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewOneInitial.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewTwoInitial.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewThreeInitial.setAdapter(adapter3);
    }

    private void addCheckBoxAreaOfInterests() {
        for (String aStr : getResources().getStringArray(R.array.area_of_interests)) {
            AppCompatCheckBox checkBox = new AppCompatCheckBox(context);
            checkBox.setText(aStr);
            cbLinearLayout.addView(checkBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkList.add(buttonView.getText().toString());

                        if (checkList.size() > 2) {
                            Toast.makeText(context, "Sorry! You can't select more than two topic!", Toast.LENGTH_SHORT).show();
                        }

                        if (checkList.get(checkList.size() - 1).equals("Other")) {
                            textInputLayoutOtherAreaOfInterest.setVisibility(View.VISIBLE);
                            Toast.makeText(context, "" + checkList, Toast.LENGTH_SHORT).show();
                        } else {
                            textInputLayoutOtherAreaOfInterest.setVisibility(View.GONE);
                        }

                        Toast.makeText(context, "" + checkList, Toast.LENGTH_SHORT).show();
                    } else {
                        checkList.remove(buttonView.getText().toString());
                    }
                }
            });
        }
    }

    private void getBundleDataPageThree() {
        if (getArguments() != null) {
            titleDefenseRegistrationThree = (TitleDefenseRegistration) getArguments().getSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE);
        }

        if (titleDefenseRegistrationThree != null) {
            switch (titleDefenseRegistrationThree.getNumberOfStudents()) {
                case 1:

                    break;

                case 2:

                    break;

                case 3:

                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackPageThree:
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStackImmediate();
                break;

            case R.id.btnSubmit:
                Toast.makeText(getContext(), "submit", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            OnMyMessageSendListener onMyMessageSendListener = (OnMyMessageSendListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements onMyTitleDefenseRegistrationMessage method.");
        }
    }
}
