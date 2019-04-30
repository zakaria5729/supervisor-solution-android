package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.Student;
import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.TitleDefense;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationThreeFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private OnMyMessageListener onMyMessageSendListener;
    private TitleDefense titleDefense;

    private Context context;
    private AppCompatCheckBox cbAgree;
    private RadioGroup rgAreaOfInterest;
    private List<Student> studentList;
    private List<Supervisor> supervisorList;
    private AlertDialog alertDialog;

    private String areaOfInterest = "Mobile Application (Traditional)";
    private boolean agreeCheckBox;

    private TextInputLayout textInputLayoutOtherAreaOfInterest, tilFirstSupervisorEmail, tilSecondSupervisorEmail, tilThirdSupervisorEmail;
    private AppCompatAutoCompleteTextView autoCompleteTextViewOneEmail, autoCompleteTextViewTwoEmail, autoCompleteTextViewThreeEmail;

    public TitleDefenseRegistrationThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_title_defense_registration_three, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Registration");
        }
        studentList = new ArrayList<>();
        supervisorList = new ArrayList<>();
        setUpPageThreeUi(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        agreeTermsAndCondition();
        addRadioButtonAreaOfInterests();
        addSupervisorAutoCompleteTextView();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        getBundleDataPageThree();
    }

    private void setUpPageThreeUi(View view) {
        autoCompleteTextViewOneEmail = view.findViewById(R.id.acTextViewInitialOne);
        autoCompleteTextViewTwoEmail = view.findViewById(R.id.acTextViewInitialTwo);
        autoCompleteTextViewThreeEmail = view.findViewById(R.id.acTextViewInitialThree);
        rgAreaOfInterest = view.findViewById(R.id.rbAreaOfInterest);
        cbAgree = view.findViewById(R.id.cbAgree);
        tilFirstSupervisorEmail = view.findViewById(R.id.tilFirstSupervisorEmail);
        tilSecondSupervisorEmail = view.findViewById(R.id.tilSecondSupervisorEmail);
        tilThirdSupervisorEmail = view.findViewById(R.id.tilThirdSupervisorEmail);
        textInputLayoutOtherAreaOfInterest = view.findViewById(R.id.textInputLayoutAreaOfInterest);

        view.findViewById(R.id.btnBackPageThree).setOnClickListener(this);
        view.findViewById(R.id.btnSubmit).setOnClickListener(this);
        Objects.requireNonNull(tilThirdSupervisorEmail.getEditText()).setOnEditorActionListener(editorActionListener);
    }

    private void addSupervisorAutoCompleteTextView() {
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewOneEmail.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewTwoEmail.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.supervisors));
        autoCompleteTextViewThreeEmail.setAdapter(adapter3);
    }

    private void getBundleDataPageThree() {
        if (getArguments() != null) {
            titleDefense = (TitleDefense) getArguments().getSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE);
        }

        studentList = new ArrayList<>();

        if (titleDefense != null) {
            switch (titleDefense.getNumberOfStudents()) {
                case 1:
                    studentList.add(new Student(titleDefense.getEditTextIdOne(), titleDefense.getEditTextNameOne(), titleDefense.getEditTextEmailOne(), titleDefense.getEditTextPhoneOne()));
                    break;

                case 2:
                    studentList.add(new Student(titleDefense.getEditTextIdOne(), titleDefense.getEditTextNameOne(), titleDefense.getEditTextEmailOne(), titleDefense.getEditTextPhoneOne()));

                    studentList.add(new Student(titleDefense.getEditTextIdTwo(), titleDefense.getEditTextNameTwo(), titleDefense.getEditTextEmailTwo(), titleDefense.getEditTextPhoneTwo()));
                    break;

                case 3:
                    studentList.add(new Student(titleDefense.getEditTextIdOne(), titleDefense.getEditTextNameOne(), titleDefense.getEditTextEmailOne(), titleDefense.getEditTextPhoneOne()));

                    studentList.add(new Student(titleDefense.getEditTextIdTwo(), titleDefense.getEditTextNameTwo(), titleDefense.getEditTextEmailTwo(), titleDefense.getEditTextPhoneTwo()));

                    studentList.add(new Student(titleDefense.getEditTextIdThree(), titleDefense.getEditTextNameThree(), titleDefense.getEditTextEmailThree(), titleDefense.getEditTextPhoneThree()));
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackPageThree:
                OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationOneFragment(), null);
                break;

            case R.id.btnSubmit:
                if (getSupervisorsEmailAndAreaOfInterest()) {
                    if (agreeCheckBox) {
                        alertDialog = new OthersUtil(context).setCircularProgressBar();
                        nextThree();
                    } else {
                        Toast.makeText(context, "Please tik the right", Toast.LENGTH_SHORT).show();
                        cbAgree.setTextColor(getResources().getColor(R.color.colorAccent));
                    }
                }
                break;
        }
    }

    private void nextThree() {
        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));

        TitleDefense titleDefense1 = new TitleDefense(titleDefense.getProjectInternship(), titleDefense.getProjectInternshipType(), titleDefense.getProjectInternshipTitle(), areaOfInterest, titleDefense.getDayEvening(), studentList, supervisorList);

        MyApiService myApiService = new NetworkCall();
        myApiService.titleDefenseRegistration(titleDefense1, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if(data.getError().equals(false)) {
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        onMyMessageSendListener.onMyFragment(new ProfileFragment());
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, data.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable th) {
                alertDialog.dismiss();
                Toast.makeText(context, "Error" +th.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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
            radioGroup.check(radioGroup.getChildAt(0).getId()); //default checked at 0 position

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (anAreaOfInterest.equals("Other")) {
                        textInputLayoutOtherAreaOfInterest.setVisibility(View.VISIBLE);
                    } else {
                        textInputLayoutOtherAreaOfInterest.setVisibility(View.GONE);
                    }
                    areaOfInterest = anAreaOfInterest;
                }
            });
        }
        rgAreaOfInterest.addView(radioGroup);
    }

    private boolean getSupervisorsEmailAndAreaOfInterest() {
        String firstSupervisorEmail = autoCompleteTextViewOneEmail.getText().toString().trim();
        String secondSupervisorEmail = autoCompleteTextViewTwoEmail.getText().toString().trim();
        String thirdSupervisorEmail = autoCompleteTextViewThreeEmail.getText().toString().trim();

        if (areaOfInterest.equals("Other")) {
            if (!TextUtils.isEmpty(Objects.requireNonNull(textInputLayoutOtherAreaOfInterest.getEditText()).getText().toString().trim())) {
                areaOfInterest = Objects.requireNonNull(textInputLayoutOtherAreaOfInterest.getEditText()).getText().toString();
            } else {
                Toast.makeText(context, "Can not empty area of interest", Toast.LENGTH_SHORT).show();
            }
        }

        if ((!TextUtils.isEmpty(areaOfInterest.trim()) && !areaOfInterest.trim().equals("Other")) && Patterns.EMAIL_ADDRESS.matcher(firstSupervisorEmail).matches() && Patterns.EMAIL_ADDRESS.matcher(secondSupervisorEmail).matches() && Patterns.EMAIL_ADDRESS.matcher(thirdSupervisorEmail).matches()) {

            supervisorList = new ArrayList<>();
            supervisorList.add(new Supervisor(firstSupervisorEmail));
            supervisorList.add(new Supervisor(secondSupervisorEmail));
            supervisorList.add(new Supervisor(thirdSupervisorEmail));
            return true;

        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(firstSupervisorEmail).matches()) {
                tilFirstSupervisorEmail.setError("Please enter first supervisor email");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(secondSupervisorEmail).matches()) {
                tilSecondSupervisorEmail.setError("Please enter second supervisor email");
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(thirdSupervisorEmail).matches()) {
                tilThirdSupervisorEmail.setError("Please enter third supervisor email");
            }

            return false;
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_GO) {
                if (getSupervisorsEmailAndAreaOfInterest()) {
                    if (agreeCheckBox) {
                        alertDialog = new OthersUtil(context).setCircularProgressBar();
                        nextThree();
                    } else {
                        Toast.makeText(context, "Please, tik the agree checkbox", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            return true;
        }
    };

    private void agreeTermsAndCondition() {
        cbAgree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                agreeCheckBox = isChecked;

                if (isChecked) {
                    cbAgree.setTextColor(Color.BLACK);
                } else {
                    cbAgree.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    @Override
    public boolean onFragmentBackPressed() {
        onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationOneFragment(), null);
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onMyMessageSendListener = (OnMyMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements OnMyMessageSendListener methods.");
        }
    }
}
