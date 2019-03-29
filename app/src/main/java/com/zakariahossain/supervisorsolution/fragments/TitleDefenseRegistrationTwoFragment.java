package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.TitleDefense;
import com.zakariahossain.supervisorsolution.preferences.UserSharedPrefManager;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

public class TitleDefenseRegistrationTwoFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private UserSharedPrefManager sharedPrefManager;
    private OnMyMessageListener onMyMessageSendListener;
    private TitleDefense titleDefense;

    private Context context;
    private TextInputLayout textInputLayoutIdOne, textInputLayoutNameOne, textInputLayoutEmailOne, textInputLayoutPhoneOne, textInputLayoutIdTwo, textInputLayoutNameTwo, textInputLayoutEmailTwo, textInputLayoutPhoneTwo, textInputLayoutIdThree, textInputLayoutNameThree, textInputLayoutEmailThree, textInputLayoutPhoneThree;

    private LinearLayoutCompat studentOneEditTextLL, studentTwoEditTextLL, studentThreeEditTextLL;

    private int numberOfStudents;

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

        sharedPrefManager = new UserSharedPrefManager(context);
        setUpPageTwoUi(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        getBundleDataPageTwo();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private void setUpPageTwoUi(View view) {
        studentOneEditTextLL = view.findViewById(R.id.llStudentOne);
        studentTwoEditTextLL = view.findViewById(R.id.llStudentTwo);
        studentThreeEditTextLL = view.findViewById(R.id.llStudentThree);

        textInputLayoutIdOne = view.findViewById(R.id.tilIdOne);
        textInputLayoutNameOne = view.findViewById(R.id.tilNameOne);
        textInputLayoutEmailOne = view.findViewById(R.id.tilEmailOne);
        textInputLayoutPhoneOne = view.findViewById(R.id.tilPhoneOne);

        textInputLayoutIdTwo = view.findViewById(R.id.tilIdTwo);
        textInputLayoutNameTwo = view.findViewById(R.id.tilNameTwo);
        textInputLayoutEmailTwo = view.findViewById(R.id.tilEmailTwo);
        textInputLayoutPhoneTwo = view.findViewById(R.id.tilPhoneTwo);

        textInputLayoutIdThree = view.findViewById(R.id.tilIdThree);
        textInputLayoutNameThree = view.findViewById(R.id.tilNameThree);
        textInputLayoutEmailThree = view.findViewById(R.id.tilEmailThree);
        textInputLayoutPhoneThree = view.findViewById(R.id.tilPhoneThree);

        MaterialButton buttonBackPageTwo = view.findViewById(R.id.btnBackPageTwo);
        MaterialButton buttonNextPageTwo = view.findViewById(R.id.btnSignUp);

        buttonBackPageTwo.setOnClickListener(this);
        buttonNextPageTwo.setOnClickListener(this);
    }

    private void getBundleDataPageTwo() {
        if (getArguments() != null) {
            titleDefense = (TitleDefense) getArguments().getSerializable(IntentAndBundleKey.KEY_FRAGMENT_TITLE_DEFENSE);
        }

        if (titleDefense != null) {
            numberOfStudents = titleDefense.getNumberOfStudents();
        }

        switch (numberOfStudents) {
            case 1:
                studentOneEditTextLL.setVisibility(View.VISIBLE);
                Objects.requireNonNull(textInputLayoutPhoneOne.getEditText()).setOnEditorActionListener(editorActionListener);
                break;

            case 2:
                studentOneEditTextLL.setVisibility(View.VISIBLE);
                studentTwoEditTextLL.setVisibility(View.VISIBLE);
                Objects.requireNonNull(textInputLayoutPhoneOne.getEditText()).setImeOptions(EditorInfo.IME_ACTION_NEXT);
                Objects.requireNonNull(textInputLayoutPhoneTwo.getEditText()).setOnEditorActionListener(editorActionListener);
                break;

            case 3:
                studentOneEditTextLL.setVisibility(View.VISIBLE);
                studentTwoEditTextLL.setVisibility(View.VISIBLE);
                studentThreeEditTextLL.setVisibility(View.VISIBLE);
                Objects.requireNonNull(textInputLayoutPhoneOne.getEditText()).setImeOptions(EditorInfo.IME_ACTION_NEXT);
                Objects.requireNonNull(textInputLayoutPhoneTwo.getEditText()).setImeOptions(EditorInfo.IME_ACTION_NEXT);
                Objects.requireNonNull(textInputLayoutPhoneThree.getEditText()).setOnEditorActionListener(editorActionListener);
                break;
        }

        //setLoggedInStudentDataToEditText
        if (sharedPrefManager.isLoggedIn()) {
            Objects.requireNonNull(textInputLayoutNameOne.getEditText()).setText(sharedPrefManager.getUser().getName());
            Objects.requireNonNull(textInputLayoutEmailOne.getEditText()).setText(sharedPrefManager.getUser().getEmail());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBackPageTwo:
                OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationOneFragment(), null);
                break;

            case R.id.btnSignUp:
                nextTwo();
                break;
        }
    }

    private void nextTwo() {
        TitleDefense defense;

        switch (numberOfStudents) {
            case 1:
                if (checkTextInputLayoutEditText(textInputLayoutIdOne) && checkTextInputLayoutEditText(textInputLayoutNameOne) && checkTextInputLayoutEditText(textInputLayoutEmailOne) && checkTextInputLayoutEditText(textInputLayoutPhoneOne)) {

                    defense = new TitleDefense(titleDefense.getNumberOfStudents(), titleDefense.getDayEvening(), titleDefense.getProjectInternship(), titleDefense.getProjectInternshipType(), titleDefense.getProjectInternshipTitle(), Objects.requireNonNull(textInputLayoutIdOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutNameOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutEmailOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutPhoneOne.getEditText()).getText().toString());

                    OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                    onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), defense);
                } else {
                    checkTextInputLayoutEditText(textInputLayoutIdOne);
                    checkTextInputLayoutEditText(textInputLayoutNameOne);
                    checkTextInputLayoutEditText(textInputLayoutEmailOne);
                    checkTextInputLayoutEditText(textInputLayoutPhoneOne);
                }
                break;

            case 2:
                    if (checkTextInputLayoutEditText(textInputLayoutIdOne) && checkTextInputLayoutEditText(textInputLayoutNameOne) && checkTextInputLayoutEditText(textInputLayoutEmailOne) && checkTextInputLayoutEditText(textInputLayoutPhoneOne) && checkTextInputLayoutEditText(textInputLayoutIdTwo) && checkTextInputLayoutEditText(textInputLayoutNameTwo) && checkTextInputLayoutEditText(textInputLayoutEmailTwo) && checkTextInputLayoutEditText(textInputLayoutPhoneTwo)) {

                    defense = new TitleDefense(titleDefense.getNumberOfStudents(), titleDefense.getDayEvening(), titleDefense.getProjectInternship(), titleDefense.getProjectInternshipType(), titleDefense.getProjectInternshipTitle(), Objects.requireNonNull(textInputLayoutIdOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutNameOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutEmailOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutPhoneOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutIdTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutNameTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutEmailTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutPhoneTwo.getEditText()).getText().toString());

                    OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                    onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), defense);
                } else {
                        checkTextInputLayoutEditText(textInputLayoutIdOne);
                        checkTextInputLayoutEditText(textInputLayoutNameOne);
                        checkTextInputLayoutEditText(textInputLayoutEmailOne);
                        checkTextInputLayoutEditText(textInputLayoutPhoneOne);
                        checkTextInputLayoutEditText(textInputLayoutIdTwo);
                        checkTextInputLayoutEditText(textInputLayoutNameTwo);
                        checkTextInputLayoutEditText(textInputLayoutEmailTwo);
                        checkTextInputLayoutEditText(textInputLayoutPhoneTwo);
                }
                break;

            case 3:
                if (checkTextInputLayoutEditText(textInputLayoutIdOne) && checkTextInputLayoutEditText(textInputLayoutNameOne) && checkTextInputLayoutEditText(textInputLayoutEmailOne) && checkTextInputLayoutEditText(textInputLayoutPhoneOne) && checkTextInputLayoutEditText(textInputLayoutIdTwo) && checkTextInputLayoutEditText(textInputLayoutNameTwo) && checkTextInputLayoutEditText(textInputLayoutEmailTwo) && checkTextInputLayoutEditText(textInputLayoutPhoneTwo) && checkTextInputLayoutEditText(textInputLayoutIdThree) && checkTextInputLayoutEditText(textInputLayoutNameThree) && checkTextInputLayoutEditText(textInputLayoutEmailThree) && checkTextInputLayoutEditText(textInputLayoutPhoneThree)) {


                    defense = new TitleDefense(titleDefense.getNumberOfStudents(), titleDefense.getDayEvening(), titleDefense.getProjectInternship(), titleDefense.getProjectInternshipType(), titleDefense.getProjectInternshipTitle(), Objects.requireNonNull(textInputLayoutIdOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutNameOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutEmailOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutPhoneOne.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutIdTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutNameTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutEmailTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutPhoneTwo.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutIdThree.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutNameThree.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutEmailThree.getEditText()).getText().toString(), Objects.requireNonNull(textInputLayoutPhoneThree.getEditText()).getText().toString());

                    OthersUtil.closeVisibleSoftKeyBoard(Objects.requireNonNull(getActivity()));
                    onMyMessageSendListener.onMyTitleDefenseRegistrationMessage(new TitleDefenseRegistrationThreeFragment(), defense);
                } else {
                    checkTextInputLayoutEditText(textInputLayoutIdOne);
                    checkTextInputLayoutEditText(textInputLayoutNameOne);
                    checkTextInputLayoutEditText(textInputLayoutEmailOne);
                    checkTextInputLayoutEditText(textInputLayoutPhoneOne);
                    checkTextInputLayoutEditText(textInputLayoutIdTwo);
                    checkTextInputLayoutEditText(textInputLayoutNameTwo);
                    checkTextInputLayoutEditText(textInputLayoutEmailTwo);
                    checkTextInputLayoutEditText(textInputLayoutPhoneTwo);
                    checkTextInputLayoutEditText(textInputLayoutIdThree);
                    checkTextInputLayoutEditText(textInputLayoutNameThree);
                    checkTextInputLayoutEditText(textInputLayoutEmailThree);
                    checkTextInputLayoutEditText(textInputLayoutPhoneThree);
                }
                break;
        }
    }

    private boolean checkTextInputLayoutEditText(TextInputLayout textInputLayout) {
        String textInputLayoutValue = Objects.requireNonNull(textInputLayout.getEditText()).getText().toString();

        if (TextUtils.isEmpty(textInputLayoutValue)) {
            textInputLayout.setError("Field can not be empty");
            return false;
        } else {
            return true;
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_GO:
                    nextTwo();
                    break;
            }
            return true;
        }
    };

    @Override
    public boolean onFragmentBackPressed() {
        onMyMessageSendListener.onMyFragment(new TitleDefenseRegistrationOneFragment());
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
