package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.Student;
import com.zakariahossain.supervisorsolution.models.Super;
import com.zakariahossain.supervisorsolution.models.TitleDefense;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;
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
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationThreeFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private OnMyMessageListener onMyMessageSendListener;
    private TitleDefenseRegistration titleDefenseRegistrationThree;

    private Context context;
    private AppCompatCheckBox cbAgree;
    private RadioGroup rgAreaOfInterest;
    private List<Student> studentList;
    private List<Super> supervisorList;

    private TextInputLayout textInputLayoutOtherAreaOfInterest;
    private AppCompatAutoCompleteTextView autoCompleteTextViewOneEmail, autoCompleteTextViewTwoEmail, autoCompleteTextViewThreeEmail;

    private String areaOfInterest;
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

        studentList = new ArrayList<>();
        supervisorList = new ArrayList<>();

        setUpPageThreeUi(view);
        agreeTermsAndCondition();
    }

    @Override
    public void onStart() {
        super.onStart();

        addRadioButtonAreaOfInterests();
        addSupervisorAutoCompleteTextView();
        getBundleDataPageThree();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private void setUpPageThreeUi(View view) {
        List<String> checkList = new ArrayList<>();

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

    private void getBundleDataPageThree() {
        if (getArguments() != null) {
            titleDefenseRegistrationThree = (TitleDefenseRegistration) getArguments().getSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE);
        }

        studentList = new ArrayList<>();

        if (titleDefenseRegistrationThree != null) {
            switch (titleDefenseRegistrationThree.getNumberOfStudents()) {
                case 1:
                    /*studentList.add(new RequestedOrAcceptedGroup(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdOne()), titleDefenseRegistrationThree.getEditTextNameOne(), titleDefenseRegistrationThree.getEditTextEmailOne(), titleDefenseRegistrationThree.getEditTextPhoneOne()));*/

                    studentList.add(new Student(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdOne()), titleDefenseRegistrationThree.getEditTextNameOne(), titleDefenseRegistrationThree.getEditTextEmailOne(), titleDefenseRegistrationThree.getEditTextPhoneOne()));
                    break;

                case 2:
                    studentList.add(new Student(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdOne()), titleDefenseRegistrationThree.getEditTextNameOne(), titleDefenseRegistrationThree.getEditTextEmailOne(), titleDefenseRegistrationThree.getEditTextPhoneOne()));

                    studentList.add(new Student(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdTwo()), titleDefenseRegistrationThree.getEditTextNameTwo(), titleDefenseRegistrationThree.getEditTextEmailTwo(), titleDefenseRegistrationThree.getEditTextPhoneTwo()));

                    /*studentList.add(new RequestedOrAcceptedGroup(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdOne()), titleDefenseRegistrationThree.getEditTextNameOne(), titleDefenseRegistrationThree.getEditTextEmailOne(), titleDefenseRegistrationThree.getEditTextPhoneOne()));

                    studentList.add(new RequestedOrAcceptedGroup(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdTwo()), titleDefenseRegistrationThree.getEditTextNameTwo(), titleDefenseRegistrationThree.getEditTextEmailTwo(), titleDefenseRegistrationThree.getEditTextPhoneTwo()));*/
                    break;

                case 3:
                    studentList.add(new Student(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdOne()), titleDefenseRegistrationThree.getEditTextNameOne(), titleDefenseRegistrationThree.getEditTextEmailOne(), titleDefenseRegistrationThree.getEditTextPhoneOne()));

                    studentList.add(new Student(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdTwo()), titleDefenseRegistrationThree.getEditTextNameTwo(), titleDefenseRegistrationThree.getEditTextEmailTwo(), titleDefenseRegistrationThree.getEditTextPhoneTwo()));

                    studentList.add(new Student(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdThree()), titleDefenseRegistrationThree.getEditTextNameThree(), titleDefenseRegistrationThree.getEditTextEmailThree(), titleDefenseRegistrationThree.getEditTextPhoneThree()));

                    /*studentList.add(new RequestedOrAcceptedGroup(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdOne()), titleDefenseRegistrationThree.getEditTextNameOne(), titleDefenseRegistrationThree.getEditTextEmailOne(), titleDefenseRegistrationThree.getEditTextPhoneOne()));

                    studentList.add(new RequestedOrAcceptedGroup(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdTwo()), titleDefenseRegistrationThree.getEditTextNameTwo(), titleDefenseRegistrationThree.getEditTextEmailTwo(), titleDefenseRegistrationThree.getEditTextPhoneTwo()));

                    studentList.add(new RequestedOrAcceptedGroup(Integer.parseInt(titleDefenseRegistrationThree.getEditTextIdThree()), titleDefenseRegistrationThree.getEditTextNameThree(), titleDefenseRegistrationThree.getEditTextEmailThree(), titleDefenseRegistrationThree.getEditTextPhoneThree()));*/
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
                        OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                        MyApiService myApiService = new NetworkCall();

                        TitleDefense titleDefense = new TitleDefense(titleDefenseRegistrationThree.getProjectInternship(), titleDefenseRegistrationThree.getProjectInternshipType(), titleDefenseRegistrationThree.getProjectInternshipTitle(), areaOfInterest, titleDefenseRegistrationThree.getDayEvening(), studentList, supervisorList);

                        myApiService.titleDefenseRegistration(titleDefense, new ResponseCallback<ServerResponse>() {
                            @Override
                            public void onSuccess(ServerResponse data) {
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
                                Toast.makeText(context, "Error" +th.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(context, "Please, tik the agree checkbox", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Please fill up all the fields", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private boolean getSupervisorsEmailAndAreaOfInterest() {
        /*if (areaOfInterest.equals("Other")) {
            areaOfInterest = Objects.requireNonNull(textInputLayoutOtherAreaOfInterest.getEditText()).getText().toString();
        }*/

        Log.e("inter", areaOfInterest);

        String firstSupervisorEmail = autoCompleteTextViewOneEmail.getText().toString().trim();
        String secondSupervisorEmail = autoCompleteTextViewTwoEmail.getText().toString().trim();
        String thirdSupervisorEmail = autoCompleteTextViewThreeEmail.getText().toString().trim();

        if (!TextUtils.isEmpty(areaOfInterest.trim()) && Patterns.EMAIL_ADDRESS.matcher(firstSupervisorEmail).matches() && Patterns.EMAIL_ADDRESS.matcher(secondSupervisorEmail).matches() && Patterns.EMAIL_ADDRESS.matcher(thirdSupervisorEmail).matches()) {
            supervisorList = new ArrayList<>();

            supervisorList.add(new Super(firstSupervisorEmail));
            supervisorList.add(new Super(secondSupervisorEmail));
            supervisorList.add(new Super(thirdSupervisorEmail));

            return true;
        } else {
            return false;
        }
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
