package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationThreeFragment extends Fragment implements View.OnClickListener {

    private OnMyMessageSendListener onMyMessageSendListener;
    private TitleDefenseRegistration titleDefenseRegistrationThree;

    private Context context;
    private List<String> checkList;
    private AppCompatCheckBox cbAgree;
    private RadioGroup rgAreaOfInterest;

    private TextInputLayout textInputLayoutOtherAreaOfInterest;
    private AppCompatAutoCompleteTextView autoCompleteTextViewOneEmail, autoCompleteTextViewTwoEmail, autoCompleteTextViewThreeEmail;

    private String firstSupervisorEmail, secondSupervisorEmail, thirdSupervisorEmail, areaOfInterest;
    private boolean agreeCheckBox;

    public TitleDefenseRegistrationThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_defense_registration_three, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Registration");
        }

        setUpPageThreeUi(view);
        addRadioButtonAreaOfInterests();
        addSupervisorAutoCompleteTextView();
        agreeTermsAndCondition();
        getBundleDataPageThree();
    }

    private void setUpPageThreeUi(View view) {
        checkList = new ArrayList<>();

        MaterialButton buttonBackPageThree = view.findViewById(R.id.btnBackPageThree);
        MaterialButton buttonSubmit = view.findViewById(R.id.btnSubmit);
        autoCompleteTextViewOneEmail = view.findViewById(R.id.acTextViewInitialOne);
        autoCompleteTextViewTwoEmail = view.findViewById(R.id.acTextViewInitialTwo);
        autoCompleteTextViewThreeEmail = view.findViewById(R.id.acTextViewInitialThree);
        rgAreaOfInterest = view.findViewById(R.id.rbAreaOfInterest);
        cbAgree = view.findViewById(R.id.cbAgree);
        textInputLayoutOtherAreaOfInterest = view.findViewById(R.id.textInputLayoutAreaOfInterest);

        buttonBackPageThree.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    private void addSupervisorAutoCompleteTextView() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewOneEmail.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewTwoEmail.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewThreeEmail.setAdapter(adapter3);
    }

    private void addRadioButtonAreaOfInterests() {
        RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        RadioGroup.LayoutParams layoutParams;

        for (final String anAreaOfInterest : getResources().getStringArray(R.array.area_of_interests)) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setText(anAreaOfInterest);
            layoutParams = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
            radioGroup.addView(radioButton, layoutParams);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    areaOfInterest = anAreaOfInterest;
                }
            });
        }
        rgAreaOfInterest.addView(radioGroup);
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
                onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), null);
                break;

            case R.id.btnSubmit:
                if (getSupervisorsEmail()) {
                    if (agreeCheckBox) {
                        onMyMessageSendListener.onMyFragment(new ProfileFragment());
                    } else {

                    }
                } else {

                }

                Toast.makeText(getContext(), "submit "+areaOfInterest, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean getSupervisorsEmail() {
        firstSupervisorEmail = autoCompleteTextViewOneEmail.getText().toString().trim();
        secondSupervisorEmail = autoCompleteTextViewTwoEmail.getText().toString().trim();
        thirdSupervisorEmail = autoCompleteTextViewThreeEmail.getText().toString().trim();

        return !TextUtils.isEmpty(firstSupervisorEmail) && !TextUtils.isEmpty(secondSupervisorEmail) && !TextUtils.isEmpty(thirdSupervisorEmail);
    }

    private void agreeTermsAndCondition() {
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agreeCheckBox = isChecked;
            }
        });
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
