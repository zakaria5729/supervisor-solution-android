package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationOneFragment extends Fragment  implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {

    private Context context;
    private OnMyMessageSendListener onMyMessageSendListener;
    private AppCompatSpinner spProjectInternship;
    private TextInputEditText textInputEditTextProjectInternshipTitle;

    private int numberOfStudents;
    private String dayEvening;
    private String projectInternship;
    private String projectInternshipType = "Choose a type";

    public TitleDefenseRegistrationOneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_defense_registration_one, container, false);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Registration");
        }
        context = container.getContext();

        setUpPageOneUi(view);
        addProjectInternshipTypeSpinner();

        return view;
    }

    private void setUpPageOneUi(View view) {
        MaterialButton buttonNextPageOne = view.findViewById(R.id.btnNextPageOne);
        spProjectInternship = view.findViewById(R.id.spProjectInternship);
        RadioGroup radioGroupNumberOfStudents = view.findViewById(R.id.rgNumberOfStudents);
        RadioGroup radioGroupDayEvening = view.findViewById(R.id.rgDayEvening);
        RadioGroup radioGroupProjectInternship = view.findViewById(R.id.rgProjectInternship);
        textInputEditTextProjectInternshipTitle = view.findViewById(R.id.tietProjectInternshipTitle);

        buttonNextPageOne.setOnClickListener(this);
        radioGroupNumberOfStudents.setOnCheckedChangeListener(this);
        radioGroupDayEvening.setOnCheckedChangeListener(this);
        radioGroupProjectInternship.setOnCheckedChangeListener(this);
        spProjectInternship.setOnItemSelectedListener(this);
    }

    private void addProjectInternshipTypeSpinner() {
        ArrayAdapter<String> projectInternshipAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.project_internship_type));
        projectInternshipAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProjectInternship.setAdapter(projectInternshipAdapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbStudentNumberOne:
                numberOfStudents = 1;
                break;

            case R.id.rbStudentNumberTwo:
                numberOfStudents = 2;
                break;

            case R.id.rbStudentNumberThree:
                numberOfStudents = 3;
                break;

            case R.id.rbDay:
                dayEvening = "day";
                break;

            case R.id.rbEvening:
                dayEvening = "evening";
                break;

            case R.id.rbProject:
                projectInternship = "project";
                break;

            case R.id.rbInternship:
                projectInternship = "internship";
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNextPageOne:
                nextOne();
                break;
        }
    }

    private void nextOne() {
        String projectInternshipTitle = Objects.requireNonNull(textInputEditTextProjectInternshipTitle.getText()).toString();

        if (numberOfStudents > 0 && !TextUtils.isEmpty(dayEvening) && !TextUtils.isEmpty(projectInternship) && !TextUtils.isEmpty(projectInternshipType) && !TextUtils.isEmpty(projectInternshipTitle.trim())) {

            TitleDefenseRegistration defenseRegistration = new TitleDefenseRegistration(numberOfStudents,dayEvening, projectInternship, projectInternshipType, projectInternshipTitle);

            onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationTwoFragment(), defenseRegistration);
        } else {
            Toast.makeText(context, "Please, fill up all the elements", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).toString().equals("Choose a type")) {
            Toast.makeText(context, "Please, choose a type", Toast.LENGTH_SHORT).show();
        } else {
            projectInternshipType = parent.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

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
