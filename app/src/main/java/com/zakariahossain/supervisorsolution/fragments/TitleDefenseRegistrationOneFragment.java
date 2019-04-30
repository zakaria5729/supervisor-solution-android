package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.TitleDefense;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationOneFragment extends Fragment implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, AdapterView.OnItemSelectedListener, OnFragmentBackPressedListener {

    private Context context;
    private OnMyMessageListener onMyMessageListener;
    private AppCompatSpinner spProjectInternship;
    private TextInputEditText editTextProjectInternshipTitle;
    private TextInputLayout tilProjectInternshipTitle;

    private int numberOfStudents;
    private RadioButton rbOneNumOfStudents, rbDay, rbProject;
    private String dayEvening, projectInternship, projectInternshipType, projectInternshipTitle;

    public TitleDefenseRegistrationOneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_title_defense_registration_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpPageOneUi(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Registration");
        }
        addProjectInternshipTypeSpinner();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private void setUpPageOneUi(View view) {
        MaterialButton buttonNextPageOne = view.findViewById(R.id.btnNextPageOne);
        spProjectInternship = view.findViewById(R.id.spProjectInternship);
        rbDay = view.findViewById(R.id.rbDay);
        rbProject = view.findViewById(R.id.rbProject);
        rbOneNumOfStudents = view.findViewById(R.id.rbStudentNumberOne);
        editTextProjectInternshipTitle = view.findViewById(R.id.etProjectInternshipTitle);
        tilProjectInternshipTitle = view.findViewById(R.id.tilProjectInternshipTitle);

        RadioGroup radioGroupNumberOfStudents = view.findViewById(R.id.rgNumberOfStudents);
        RadioGroup radioGroupDayEvening = view.findViewById(R.id.rgDayEvening);
        RadioGroup radioGroupProjectInternship = view.findViewById(R.id.rgProjectInternship);

        view.findViewById(R.id.btnNextPageOne).setOnClickListener(this);
        radioGroupNumberOfStudents.setOnCheckedChangeListener(this);
        radioGroupDayEvening.setOnCheckedChangeListener(this);
        radioGroupProjectInternship.setOnCheckedChangeListener(this);

        spProjectInternship.setOnItemSelectedListener(this);
        editTextProjectInternshipTitle.setOnEditorActionListener(editorActionListener);
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
        if (view.getId() == R.id.btnNextPageOne) {
            if (getAllData()) {
                TitleDefense titleDefense = new TitleDefense(numberOfStudents, dayEvening, projectInternship, projectInternshipType, projectInternshipTitle);

                OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                onMyMessageListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationTwoFragment(), titleDefense);
            }
        }
    }

    private boolean getAllData() {
        projectInternshipTitle = Objects.requireNonNull(editTextProjectInternshipTitle.getText()).toString();

        if (numberOfStudents > 0 && !TextUtils.isEmpty(dayEvening) && !TextUtils.isEmpty(projectInternship) && !projectInternshipType.equals("Choose a type") && !TextUtils.isEmpty(projectInternshipTitle.trim())) {
            return true;

        } else {
            if (projectInternshipType.equals("Choose a type")) {
                TextView projectInternshipType = (TextView) spProjectInternship.getSelectedView();
                projectInternshipType.setTextColor(Color.RED);
            }
            if (TextUtils.isEmpty(projectInternshipTitle.trim())) {
                tilProjectInternshipTitle.setError("Project or internship title can not be empty");
            }
            if (numberOfStudents <= 0) {
                Toast.makeText(context, "Please select number of students", Toast.LENGTH_SHORT).show();
                rbOneNumOfStudents.setTextColor(Color.RED);
            }
            if (TextUtils.isEmpty(dayEvening)) {
                Toast.makeText(context, "Please select day or evening", Toast.LENGTH_SHORT).show();
                rbDay.setTextColor(Color.RED);
            }
            if (TextUtils.isEmpty(projectInternship)) {
                Toast.makeText(context, "Please select project or internship", Toast.LENGTH_SHORT).show();
                rbProject.setTextColor(Color.RED);
            }

            return false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        projectInternshipType = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        projectInternshipType = "Choose a type";
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (getAllData()) {
                    TitleDefense titleDefense = new TitleDefense(numberOfStudents, dayEvening, projectInternship, projectInternshipType, projectInternshipTitle);

                    onMyMessageListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationTwoFragment(), titleDefense);
                }
            }
            return true;
        }
    };

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
    public boolean onFragmentBackPressed() {
        onMyMessageListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
        return true;
    }
}
