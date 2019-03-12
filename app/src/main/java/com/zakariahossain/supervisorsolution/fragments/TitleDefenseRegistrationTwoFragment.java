package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageSendListener;
import com.zakariahossain.supervisorsolution.models.TitleDefenseRegistration;
import com.zakariahossain.supervisorsolution.preferences.SharedPrefManager;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationTwoFragment extends Fragment implements View.OnClickListener {

    private SharedPrefManager sharedPrefManager;
    private OnMyMessageSendListener onMyMessageSendListener;
    private TitleDefenseRegistration titleDefenseRegistrationTwo;

    private Context context;
    private TextInputEditText textInputEditTextIdOne, textInputEditTextNameOne, textInputEditTextEmailOne, textInputEditTextPhoneOne, textInputEditTextIdTwo, textInputEditTextNameTwo, textInputEditTextEmailTwo, textInputEditTextPhoneTwo, textInputEditTextIdThree, textInputEditTextNameThree, textInputEditTextEmailThree, textInputEditTextPhoneThree;

    private LinearLayoutCompat studentOneEditTextLL, studentTwoEditTextLL, studentThreeEditTextLL;

    private int numberOfStudents;
    private String editTextIdOne, editTextNameOne, editTextEmailOne, editTextPhoneOne, editTextIdTwo, editTextNameTwo, editTextEmailTwo, editTextPhoneTwo, editTextIdThree, editTextNameThree, editTextEmailThree, editTextPhoneThree;

    public TitleDefenseRegistrationTwoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_title_defense_registration_two, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Title Defense Registration");
        }

	sharedPrefManager = new SharedPrefManager(context);
        setUpPageTwoUi(view);
        getBundleDataPageTwo();
    }

    private void setUpPageTwoUi(View view) {
        studentOneEditTextLL = view.findViewById(R.id.llStudentOne);
        studentTwoEditTextLL = view.findViewById(R.id.llStudentTwo);
        studentThreeEditTextLL = view.findViewById(R.id.llStudentThree);

        textInputEditTextIdOne = view.findViewById(R.id.tietIdOne);
        textInputEditTextNameOne = view.findViewById(R.id.tietNameOne);
        textInputEditTextEmailOne = view.findViewById(R.id.tietEmailOne);
        textInputEditTextPhoneOne = view.findViewById(R.id.tietPhoneOne);

        textInputEditTextIdTwo = view.findViewById(R.id.tietIdTwo);
        textInputEditTextNameTwo = view.findViewById(R.id.tietNameTwo);
        textInputEditTextEmailTwo = view.findViewById(R.id.tietEmailTwo);
        textInputEditTextPhoneTwo = view.findViewById(R.id.tietPhoneTwo);

        textInputEditTextIdThree = view.findViewById(R.id.tietIdThree);
        textInputEditTextNameThree = view.findViewById(R.id.tietNameThree);
        textInputEditTextEmailThree = view.findViewById(R.id.tietEmailThree);
        textInputEditTextPhoneThree = view.findViewById(R.id.tietPhoneThree);

        MaterialButton buttonBackPageTwo = view.findViewById(R.id.btnBackPageTwo);
        MaterialButton buttonNextPageTwo = view.findViewById(R.id.btnSignUp);

        buttonBackPageTwo.setOnClickListener(this);
        buttonNextPageTwo.setOnClickListener(this);
    }

    private void getBundleDataPageTwo() {
        if (getArguments() != null) {
            titleDefenseRegistrationTwo = (TitleDefenseRegistration) getArguments().getSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE);
        }

        if (titleDefenseRegistrationTwo != null) {
            numberOfStudents = titleDefenseRegistrationTwo.getNumberOfStudents();
        }

        switch (numberOfStudents) {
            case 1:
                studentOneEditTextLL.setVisibility(View.VISIBLE);
                break;

            case 2:
                studentOneEditTextLL.setVisibility(View.VISIBLE);
                studentTwoEditTextLL.setVisibility(View.VISIBLE);
                break;

            case 3:
                studentOneEditTextLL.setVisibility(View.VISIBLE);
                studentTwoEditTextLL.setVisibility(View.VISIBLE);
                studentThreeEditTextLL.setVisibility(View.VISIBLE);
                break;
        }

	setLoggedInStudentDataToEditText();
    }

     private void setLoggedInStudentDataToEditText() {
	if (sharedPrefManager.isLoggedIn()) {
            textInputEditTextNameOne.setText(sharedPrefManager.getUser().getName());
            textInputEditTextEmailOne.setText(sharedPrefManager.getUser().getEmail());
        }
    }

    private void getEditTextValuePageTwo() {
        editTextIdOne = Objects.requireNonNull(textInputEditTextIdOne.getText()).toString();
        editTextNameOne = Objects.requireNonNull(textInputEditTextNameOne.getText()).toString();
        editTextEmailOne = Objects.requireNonNull(textInputEditTextEmailOne.getText()).toString();
        editTextPhoneOne = Objects.requireNonNull(textInputEditTextPhoneOne.getText()).toString();

        editTextIdTwo = Objects.requireNonNull(textInputEditTextIdTwo.getText()).toString();
        editTextNameTwo = Objects.requireNonNull(textInputEditTextNameTwo.getText()).toString();
        editTextEmailTwo = Objects.requireNonNull(textInputEditTextEmailTwo.getText()).toString();
        editTextPhoneTwo = Objects.requireNonNull(textInputEditTextPhoneTwo.getText()).toString();

        editTextIdThree = Objects.requireNonNull(textInputEditTextIdThree.getText()).toString();
        editTextNameThree = Objects.requireNonNull(textInputEditTextNameThree.getText()).toString();
        editTextEmailThree = Objects.requireNonNull(textInputEditTextEmailThree.getText()).toString();
        editTextPhoneThree = Objects.requireNonNull(textInputEditTextPhoneThree.getText()).toString();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackPageTwo:
                onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationOneFragment(), null);
                break;

            case R.id.btnSignUp:
                nextTwo();
                break;
        }
    }

    private void nextTwo() {
        getEditTextValuePageTwo();
        TitleDefenseRegistration defenseRegistration;

        switch (numberOfStudents) {
            case 1:
                if (!TextUtils.isEmpty(editTextIdOne.trim()) && !TextUtils.isEmpty(editTextNameOne.trim()) && !TextUtils.isEmpty(editTextEmailOne.trim()) && !TextUtils.isEmpty(editTextPhoneOne.trim())) {

                    defenseRegistration = new TitleDefenseRegistration(titleDefenseRegistrationTwo.getNumberOfStudents(), titleDefenseRegistrationTwo.getDayEvening(), titleDefenseRegistrationTwo.getProjectInternship(), titleDefenseRegistrationTwo.getProjectInternshipType(), titleDefenseRegistrationTwo.getProjectInternshipTitle(), editTextIdOne, editTextNameOne, editTextEmailOne, editTextPhoneOne);

                    onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), defenseRegistration);
                } else {
                    Toast.makeText(context, "Please, fill up all the elements", Toast.LENGTH_SHORT).show();
                }
                break;

            case 2:
                if (!TextUtils.isEmpty(editTextIdOne.trim()) && !TextUtils.isEmpty(editTextNameOne.trim()) && !TextUtils.isEmpty(editTextEmailOne.trim()) && !TextUtils.isEmpty(editTextPhoneOne.trim()) && !TextUtils.isEmpty(editTextIdTwo.trim()) && !TextUtils.isEmpty(editTextNameTwo.trim()) && !TextUtils.isEmpty(editTextEmailTwo.trim()) && !TextUtils.isEmpty(editTextPhoneTwo.trim())) {

                    defenseRegistration = new TitleDefenseRegistration(titleDefenseRegistrationTwo.getNumberOfStudents(), titleDefenseRegistrationTwo.getDayEvening(), titleDefenseRegistrationTwo.getProjectInternship(), titleDefenseRegistrationTwo.getProjectInternshipType(), titleDefenseRegistrationTwo.getProjectInternshipTitle(), editTextIdOne, editTextNameOne, editTextEmailOne, editTextPhoneOne, editTextIdTwo, editTextNameTwo, editTextEmailTwo, editTextPhoneTwo);

                    onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), defenseRegistration);
                } else {
                    Toast.makeText(context, "Please, fill up all the elements", Toast.LENGTH_SHORT).show();
                }
                break;

            case 3:
                if (!TextUtils.isEmpty(editTextIdOne.trim()) && !TextUtils.isEmpty(editTextNameOne.trim()) && !TextUtils.isEmpty(editTextEmailOne.trim()) && !TextUtils.isEmpty(editTextPhoneOne.trim()) && !TextUtils.isEmpty(editTextIdTwo.trim()) && !TextUtils.isEmpty(editTextNameTwo.trim()) && !TextUtils.isEmpty(editTextEmailTwo.trim()) && !TextUtils.isEmpty(editTextPhoneTwo.trim()) && !TextUtils.isEmpty(editTextIdThree.trim()) && !TextUtils.isEmpty(editTextNameThree.trim()) && !TextUtils.isEmpty(editTextEmailThree.trim()) && !TextUtils.isEmpty(editTextPhoneThree.trim())) {

                    defenseRegistration = new TitleDefenseRegistration(titleDefenseRegistrationTwo.getNumberOfStudents(), titleDefenseRegistrationTwo.getDayEvening(), titleDefenseRegistrationTwo.getProjectInternship(), titleDefenseRegistrationTwo.getProjectInternshipType(), titleDefenseRegistrationTwo.getProjectInternshipTitle(), editTextIdOne, editTextNameOne, editTextEmailOne, editTextPhoneOne, editTextIdTwo, editTextNameTwo, editTextEmailTwo, editTextPhoneTwo, editTextIdThree, editTextNameThree, editTextEmailThree, editTextPhoneThree);

                    onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), defenseRegistration);
                } else {
                    Toast.makeText(context, "Please, fill up all the elements", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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
