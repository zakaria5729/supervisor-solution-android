package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.wang.avi.AVLoadingIndicatorView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.adapters.AcceptedGroupAdapter;
import com.zakariahossain.supervisorsolution.adapters.GroupStatusAdapter;
import com.zakariahossain.supervisorsolution.adapters.RequestedGroupAdapter;
import com.zakariahossain.supervisorsolution.interfaces.OnFragmentBackPressedListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.interfaces.OnMyMessageListener;
import com.zakariahossain.supervisorsolution.models.AcceptedGroupList;
import com.zakariahossain.supervisorsolution.models.GroupStatusList;
import com.zakariahossain.supervisorsolution.models.RequestedGroupList;
import com.zakariahossain.supervisorsolution.models.Student;
import com.zakariahossain.supervisorsolution.preferences.UserSharedPrefManager;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileFragment extends Fragment implements OnMyClickListener, OnFragmentBackPressedListener, View.OnClickListener {

    private Context context;
    private OnMyMessageListener onMyMessageSendListener;
    private UserSharedPrefManager sharedPrefManager;
    private MyApiService myApiService;
    private RecyclerView requestedGroupListRecyclerView, acceptedGroupListRecyclerView, groupListStatusRecyclerView;
    private RequestedGroupAdapter requestedGroupAdapter;
    private AcceptedGroupAdapter acceptedGroupAdapter;
    private GroupStatusAdapter groupListStatusAdapter;
    private List<GroupStatusList.GroupStatus> groupStatusList;
    private List<List<Student>> requestedGroupList, acceptedGroupList;

    private MaterialButton retryButton;
    private MaterialCardView requestedGroupListCardView, acceptedGroupListCardView, groupListStatusCardView;
    private AppCompatTextView userName, userEmail, userRoleAndCreatedDate, loadingIndicatorTextViewProfile;

    private AVLoadingIndicatorView loadingIndicatorViewProfile;
    private LinearLayoutCompat loadingIndicatorViewProfileLL;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() != null) {
            getActivity().setTitle("Profile");
        }

        userName = view.findViewById(R.id.tvUserName);
        userEmail = view.findViewById(R.id.tvUserEmail);
        userRoleAndCreatedDate = view.findViewById(R.id.tvRoleAndCreatedDate);

        requestedGroupListCardView = view.findViewById(R.id.rglCardView);
        acceptedGroupListCardView = view.findViewById(R.id.aglCardView);
        groupListStatusCardView = view.findViewById(R.id.glsCardView);
        retryButton = view.findViewById(R.id.btnRetry);
        requestedGroupListRecyclerView = view.findViewById(R.id.rglRecyclerView);
        acceptedGroupListRecyclerView = view.findViewById(R.id.aglRecyclerView);
        groupListStatusRecyclerView = view.findViewById(R.id.glsRecyclerView);

        loadingIndicatorViewProfile = view.findViewById(R.id.avLoadingViewProfile);
        loadingIndicatorTextViewProfile = view.findViewById(R.id.avLoadingTextViewProfile);
        loadingIndicatorViewProfileLL = view.findViewById(R.id.avLoadingViewProfileLinearLayout);

        view.findViewById(R.id.btnSendMailToAllAcceptedGroups).setOnClickListener(ProfileFragment.this);
        retryButton.setOnClickListener(ProfileFragment.this);
        view.findViewById(R.id.llProfile).setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sharedPrefManager = new UserSharedPrefManager(context);
        myApiService = new NetworkCall();

        String roleAndCreatedDate = sharedPrefManager.getUser().getUserRole()+", "+sharedPrefManager.getUser().getCreatedAt();

        userName.setText(sharedPrefManager.getUser().getName());
        userEmail.setText(sharedPrefManager.getUser().getEmail());
        userRoleAndCreatedDate.setText(roleAndCreatedDate);

        loadingIndicatorViewProfile.show();
        if (sharedPrefManager.getUser().getUserRole().equals("Supervisor")) {
            loadRequestedGroupListFromServer();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        if (sharedPrefManager.getUser().getUserRole().equals("Student")) {
            loadGroupListStatusFromServer();
        } else if (sharedPrefManager.getUser().getUserRole().equals("Supervisor")) {
            loadAcceptedGroupListFromServer();
        }
    }

    private void loadGroupListStatusFromServer() {
        myApiService.groupListStatus(sharedPrefManager.getUser().getEmail(), new ResponseCallback<GroupStatusList>() {
            @Override
            public void onSuccess(GroupStatusList data) {
                if (data != null) {
                    if(data.getError().equals(false)) {
                        loadingIndicatorViewProfile.hide();
                        loadingIndicatorViewProfileLL.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        groupListStatusCardView.setVisibility(View.VISIBLE);

                        groupStatusList = data.getGroupStatusList();

                        if (groupStatusList != null) {
                            groupListStatusAdapter = new GroupStatusAdapter(context, groupStatusList);

                            groupListStatusRecyclerView.setHasFixedSize(true);
                            groupListStatusRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            groupListStatusRecyclerView.setAdapter(groupListStatusAdapter);
                            groupListStatusAdapter.notifyDataSetChanged();
                            groupListStatusAdapter.setOnMyClickListener(ProfileFragment.this);
                        }
                    } else {
                        loadingIndicatorViewProfile.hide();
                        loadingIndicatorTextViewProfile.setText("No register group list found for title defense");
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                Toast.makeText(context, "Error: "+th.getMessage(), Toast.LENGTH_LONG).show();
                loadingIndicatorViewProfile.hide();
                loadingIndicatorTextViewProfile.setText("Check your internet connection");
                loadingIndicatorTextViewProfile.setTextColor(context.getResources().getColor(R.color.colorAccent));
                retryButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadRequestedGroupListFromServer() {
        myApiService.requestedGroupList(sharedPrefManager.getUser().getEmail(), new ResponseCallback<RequestedGroupList>() {
            @Override
            public void onSuccess(RequestedGroupList data) {
                if (data != null) {
                    if(data.getError().equals(false)) {
                        loadingIndicatorViewProfile.hide();
                        loadingIndicatorViewProfileLL.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        requestedGroupListCardView.setVisibility(View.VISIBLE);

                        requestedGroupList = data.getRequestedGroupList();

                        if (requestedGroupList != null) {
                            requestedGroupAdapter = new RequestedGroupAdapter(context, requestedGroupList);

                            requestedGroupListRecyclerView.setHasFixedSize(true);
                            requestedGroupListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            requestedGroupListRecyclerView.setAdapter(requestedGroupAdapter);
                            requestedGroupAdapter.notifyDataSetChanged();
                            requestedGroupAdapter.setOnMyClickListener(ProfileFragment.this);
                        }
                    } else {
                        loadingIndicatorViewProfile.hide();
                        loadingIndicatorTextViewProfile.setText("No requested or accepted group list found");
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                Toast.makeText(context, "Error: "+th.getMessage(), Toast.LENGTH_SHORT).show();
                loadingIndicatorViewProfile.hide();
                loadingIndicatorTextViewProfile.setText("Check your internet connection");
                loadingIndicatorTextViewProfile.setTextColor(context.getResources().getColor(R.color.colorAccent));
                retryButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadAcceptedGroupListFromServer() {
        myApiService.acceptedGroupList(sharedPrefManager.getUser().getEmail(), new ResponseCallback<AcceptedGroupList>() {
            @Override
            public void onSuccess(AcceptedGroupList data) {
                if (data != null) {
                    if(data.getError().equals(false)) {
                        loadingIndicatorViewProfile.hide();
                        loadingIndicatorViewProfileLL.setVisibility(View.GONE);
                        retryButton.setVisibility(View.GONE);
                        acceptedGroupListCardView.setVisibility(View.VISIBLE);

                        acceptedGroupList = data.getAcceptedGroupList();

                        if (acceptedGroupList != null) {
                            acceptedGroupAdapter = new AcceptedGroupAdapter(context, acceptedGroupList);

                            acceptedGroupListRecyclerView.setHasFixedSize(true);
                            acceptedGroupListRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            acceptedGroupListRecyclerView.setAdapter(acceptedGroupAdapter);
                            acceptedGroupAdapter.notifyDataSetChanged();
                            acceptedGroupAdapter.setOnMyClickListener(ProfileFragment.this);
                        }
                    } else {
                        loadingIndicatorViewProfile.hide();
                        loadingIndicatorTextViewProfile.setText("No requested or accepted group list found");
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                Toast.makeText(context, "Error: "+th.getMessage(), Toast.LENGTH_SHORT).show();
                loadingIndicatorViewProfile.hide();
                loadingIndicatorTextViewProfile.setText("Check your internet connection");
                loadingIndicatorTextViewProfile.setTextColor(context.getResources().getColor(R.color.colorAccent));
                retryButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        menu.findItem(R.id.action_logout).setVisible(true);
        menu.findItem(R.id.action_change_password).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                sharedPrefManager.clearPreference();

                onMyMessageSendListener.onMyFragment(new LoginFragment());
                onMyMessageSendListener.onMyHeaderViewAndNavMenuItem(getResources().getString(R.string.nav_header_title), getResources().getString(R.string.nav_header_subtitle), "Login", R.drawable.ic_login);
                return true;

            case R.id.action_change_password:
                onMyMessageSendListener.onMyFragment(new ChangePasswordFragment());
                return true;

            default:
                return false;
        }
    }

    @Override
    public void onMyClick(int position) {
        if (groupStatusList.get(position).getIsAccepted() == 1) {
            new OthersUtil(context).openEmailDialog(groupStatusList.get(position).getSupervisorEmail(), "Send Email");
        }
    }

    @Override
    public void onMyClickRequestOrAcceptListener(int position, int requestedOrAccepted) {
        GroupDetailsFragment groupDetailsFragment = new GroupDetailsFragment();
        Bundle bundle = new Bundle();

        if (requestedOrAccepted == 0) {
            bundle.putString(IntentAndBundleKey.KEY_REQUEST_OR_ACCEPT_GROUP_DETAILS, IntentAndBundleKey.KEY_REQUEST);
            bundle.putSerializable(IntentAndBundleKey.KEY_REQUEST_GROUP_DETAILS, new ArrayList<>(requestedGroupList.get(position)));

        } else if (requestedOrAccepted == 1) {
            bundle.putString(IntentAndBundleKey.KEY_REQUEST_OR_ACCEPT_GROUP_DETAILS, IntentAndBundleKey.KEY_ACCEPT);
            bundle.putSerializable(IntentAndBundleKey.KEY_ACCEPT_GROUP_DETAILS, new ArrayList<>(acceptedGroupList.get(position)));
        }

        groupDetailsFragment.setArguments(bundle);
        onMyMessageSendListener.onMyFragment(groupDetailsFragment);
    }

    @Override
    public boolean onFragmentBackPressed() {
        onMyMessageSendListener.onMyHomeOrRule(IntentAndBundleKey.KEY_FRAGMENT_HOME);
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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSendMailToAllAcceptedGroups) {
            StringBuilder emailRecipients = new StringBuilder();

            for (List<Student> req: acceptedGroupList) {
                for (Student student: req) {
                    emailRecipients.append(student.getEmail()).append(",");
                }
            }

            if (!TextUtils.isEmpty(emailRecipients.toString())) {
                new OthersUtil(context).openEmailDialog(emailRecipients.toString(), "Send Email");
            }
        } else if(view.getId() == R.id.btnRetry) {
            loadingIndicatorViewProfile.show();

            if (sharedPrefManager.getUser().getUserRole().equals("Student")) {
                loadGroupListStatusFromServer();
            } else if (sharedPrefManager.getUser().getUserRole().equals("Supervisor")) {
                loadRequestedGroupListFromServer();
                loadAcceptedGroupListFromServer();
            }
        }
    }
}
