package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.ServerResponse;
import com.zakariahossain.supervisorsolution.models.Student;
import com.zakariahossain.supervisorsolution.preferences.UserSharedPrefManager;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

public class GroupDetailsFragment extends Fragment implements View.OnClickListener, OnFragmentBackPressedListener {

    private OnMyMessageListener onMyMessageListener;
    private ArrayList<Student> requestedOrAcceptedGroupList;

    private MaterialButton acceptGroupButton, declineGroupButton, sendGroupMailButton;
    private MaterialCardView studentOneCardView, studentTwoCardView, studentThreeCardView;

    private AppCompatTextView studentOneName, studentOneEmail, studentOneId, studentOnePhone, studentTwoName, studentTwoEmail, studentTwoId, studentTwoPhone, studentThreeName, studentThreeEmail, studentThreeId, studentThreePhone;

    private MyApiService myApiService;
    private AlertDialog alertDialog;
    private OthersUtil othersUtil;
    private String emailRecipients;

    public GroupDetailsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_requested_and_accepted_group_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        myApiService = new NetworkCall();
        othersUtil = new OthersUtil(getContext());
        setUpGroupDetailsUI(view);
    }

    @Override
    public void onStart() {
        super.onStart();
        getGroupDetailsBundleData();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setDataToGroupDetailsUI();
    }

    private void setUpGroupDetailsUI(View view) {
        studentOneName = view.findViewById(R.id.tvStudentOneName);
        studentOneEmail = view.findViewById(R.id.tvStudentOneEmail);
        studentOneId = view.findViewById(R.id.tvStudentOneId);
        studentOnePhone = view.findViewById(R.id.tvStudentOnePhone);
        studentTwoName = view.findViewById(R.id.tvStudentTwoName);
        studentTwoEmail = view.findViewById(R.id.tvStudentTwoEmail);
        studentTwoId = view.findViewById(R.id.tvStudentTwoId);
        studentTwoPhone = view.findViewById(R.id.tvStudentTwoPhone);
        studentThreeName = view.findViewById(R.id.tvStudentThreeName);
        studentThreeEmail = view.findViewById(R.id.tvStudentThreeEmail);
        studentThreeId = view.findViewById(R.id.tvStudentThreeId);
        studentThreePhone = view.findViewById(R.id.tvStudentThreePhone);

        studentOneCardView = view.findViewById(R.id.mcvStudentOne);
        studentTwoCardView = view.findViewById(R.id.mcvStudentTwo);
        studentThreeCardView = view.findViewById(R.id.mcvStudentThree);

        acceptGroupButton = view.findViewById(R.id.btnAcceptGroup);
        declineGroupButton = view.findViewById(R.id.btnDeclineGroup);
        sendGroupMailButton = view.findViewById(R.id.btnSendGroupMail);

        studentOnePhone.setPaintFlags(studentOnePhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        studentTwoPhone.setPaintFlags(studentTwoPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        studentThreePhone.setPaintFlags(studentThreePhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        acceptGroupButton.setOnClickListener(this);
        declineGroupButton.setOnClickListener(this);
        studentOnePhone.setOnClickListener(this);
        studentTwoPhone.setOnClickListener(this);
        studentThreePhone.setOnClickListener(this);
        sendGroupMailButton.setOnClickListener(this);
    }

    private void getGroupDetailsBundleData() {
        if (getArguments() != null) {
            String requestOrAccept = getArguments().getString(IntentAndBundleKey.KEY_REQUEST_OR_ACCEPT_GROUP_DETAILS);

            if (requestOrAccept != null) {
                switch (requestOrAccept) {
                    case IntentAndBundleKey.KEY_REQUEST:
                        sendGroupMailButton.setVisibility(View.GONE);

                        requestedOrAcceptedGroupList = (ArrayList<Student>) getArguments().getSerializable(IntentAndBundleKey.KEY_REQUEST_GROUP_DETAILS);
                        break;

                    case IntentAndBundleKey.KEY_ACCEPT:
                        acceptGroupButton.setVisibility(View.GONE);
                        declineGroupButton.setVisibility(View.GONE);

                        requestedOrAcceptedGroupList = (ArrayList<Student>) getArguments().getSerializable(IntentAndBundleKey.KEY_ACCEPT_GROUP_DETAILS);
                        break;
                }
            }
        }
    }

    private void setDataToGroupDetailsUI() {
        switch (requestedOrAcceptedGroupList.size()) {
            case 1:
                studentOneCardView.setVisibility(View.VISIBLE);
                studentOneName.setText(requestedOrAcceptedGroupList.get(0).getName());
                studentOneEmail.setText(requestedOrAcceptedGroupList.get(0).getEmail());
                studentOneId.setText(String.valueOf(requestedOrAcceptedGroupList.get(0).getStudentId()));
                studentOnePhone.setText(requestedOrAcceptedGroupList.get(0).getPhone());
                emailRecipients = requestedOrAcceptedGroupList.get(0).getEmail();
                break;

            case 2:
                studentOneCardView.setVisibility(View.VISIBLE);
                studentTwoCardView.setVisibility(View.VISIBLE);
                setDataToGroupDetailsUIForTwoStudent();
                break;

            case 3:
                studentOneCardView.setVisibility(View.VISIBLE);
                studentTwoCardView.setVisibility(View.VISIBLE);
                studentThreeCardView.setVisibility(View.VISIBLE);
                setDataToGroupDetailsUIForThreeStudent();
                break;
        }
    }

    private void setDataToGroupDetailsUIForThreeStudent() {
        studentOneName.setText(requestedOrAcceptedGroupList.get(0).getName());
        studentOneEmail.setText(requestedOrAcceptedGroupList.get(0).getEmail());
        studentOneId.setText(String.valueOf(requestedOrAcceptedGroupList.get(0).getStudentId()));
        studentOnePhone.setText(requestedOrAcceptedGroupList.get(0).getPhone());

        studentTwoName.setText(requestedOrAcceptedGroupList.get(1).getName());
        studentTwoEmail.setText(requestedOrAcceptedGroupList.get(1).getEmail());
        studentTwoId.setText(String.valueOf(requestedOrAcceptedGroupList.get(1).getStudentId()));
        studentTwoPhone.setText(requestedOrAcceptedGroupList.get(1).getPhone());

        studentThreeName.setText(requestedOrAcceptedGroupList.get(2).getName());
        studentThreeEmail.setText(requestedOrAcceptedGroupList.get(2).getEmail());
        studentThreeId.setText(String.valueOf(requestedOrAcceptedGroupList.get(1).getStudentId()));
        studentThreePhone.setText(requestedOrAcceptedGroupList.get(2).getPhone());

        emailRecipients = requestedOrAcceptedGroupList.get(0).getEmail() + "," + requestedOrAcceptedGroupList.get(1).getEmail() + "," + requestedOrAcceptedGroupList.get(2).getEmail();
    }

    private void setDataToGroupDetailsUIForTwoStudent() {
        studentOneName.setText(requestedOrAcceptedGroupList.get(0).getName());
        studentOneEmail.setText(requestedOrAcceptedGroupList.get(0).getEmail());
        studentOneId.setText(String.valueOf(requestedOrAcceptedGroupList.get(0).getStudentId()));
        studentOnePhone.setText(requestedOrAcceptedGroupList.get(0).getPhone());

        studentTwoName.setText(requestedOrAcceptedGroupList.get(1).getName());
        studentTwoEmail.setText(requestedOrAcceptedGroupList.get(1).getEmail());
        studentTwoId.setText(String.valueOf(requestedOrAcceptedGroupList.get(1).getStudentId()));
        studentTwoPhone.setText(requestedOrAcceptedGroupList.get(1).getPhone());

        emailRecipients = requestedOrAcceptedGroupList.get(0).getEmail() + "," + requestedOrAcceptedGroupList.get(1).getEmail();
    }

    private void acceptOrDeclineGroup(int acceptOrDecline) {
        myApiService.groupAcceptOrDecline(new UserSharedPrefManager(Objects.requireNonNull(getContext())).getUser().getEmail(), requestedOrAcceptedGroupList.get(0).getEmail(), acceptOrDecline, new ResponseCallback<ServerResponse>() {
            @Override
            public void onSuccess(ServerResponse data) {
                alertDialog.dismiss();

                if (data != null) {
                    if (data.getError().equals(false)) {
                        Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
                        onMyMessageListener.onMyFragment(new ProfileFragment());

                    } else if (data.getError().equals(true)) {
                        Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong! Try again later", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError(Throwable th) {
                alertDialog.dismiss();
                Toast.makeText(getContext(), th.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAcceptGroup:
                alertDialog = othersUtil.setCircularProgressBar();
                acceptOrDeclineGroup(1);
                break;

            case R.id.btnDeclineGroup:
                alertDialog = othersUtil.setCircularProgressBar();
                acceptOrDeclineGroup(-1);
                break;

            case R.id.btnSendGroupMail:
                othersUtil.openEmailDialog(emailRecipients, "Send Email");
                break;

            case R.id.tvStudentOnePhone:
                othersUtil.requestPhoneCallAndPermission(getActivity(), requestedOrAcceptedGroupList.get(0).getPhone());
                break;

            case R.id.tvStudentTwoPhone:
                othersUtil.requestPhoneCallAndPermission(getActivity(), requestedOrAcceptedGroupList.get(1).getPhone());
                break;

            case R.id.tvStudentThreePhone:
                othersUtil.requestPhoneCallAndPermission(getActivity(), requestedOrAcceptedGroupList.get(2).getPhone());
                break;

        }
    }

    @Override
    public boolean onFragmentBackPressed() {
        onMyMessageListener.onMyFragment(new ProfileFragment());
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            onMyMessageListener = (OnMyMessageListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implements OnMyMessageSendListener methods.");
        }
    }
}
