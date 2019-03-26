package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.activities.TopicSupervisorDetailActivity;
import com.zakariahossain.supervisorsolution.adapters.SupervisorAdapter;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.util.List;

public class SupervisorFragment extends Fragment implements OnMyClickListener, SwipeRefreshLayout.OnRefreshListener{

    private Context context;
    private List<Supervisor> supervisorList;
    private RecyclerView supervisorRecyclerView;
    private SupervisorAdapter supervisorAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutSupervisor;
    private AVLoadingIndicatorView loadingIndicatorViewSupervisor;
    private AppCompatTextView loadingIndicatorTextViewSupervisor;
    private LinearLayoutCompat loadingIndicatorViewSupervisorLL;

    public SupervisorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_supervisor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpSupervisorFragmentUi(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        loadingIndicatorViewSupervisor.show();
        loadSupervisorsFromServer();
    }

    private void setUpSupervisorFragmentUi(View view) {
        supervisorRecyclerView = view.findViewById(R.id.rvTeacher);
        swipeRefreshLayoutSupervisor = view.findViewById(R.id.swipeRefreshLayoutSupervisor);
        loadingIndicatorViewSupervisor = view.findViewById(R.id.avLoadingViewSupervisor);
        loadingIndicatorTextViewSupervisor = view.findViewById(R.id.avLoadingTextViewSupervisor);
        loadingIndicatorViewSupervisorLL = view.findViewById(R.id.avLoadingViewSupervisorLinearLayout);

        swipeRefreshLayoutSupervisor.setOnRefreshListener(this);
        swipeRefreshLayoutSupervisor.setColorSchemeResources(R.color.colorPrimaryDark);
    }

    private void loadSupervisorsFromServer() {
        MyApiService myApiService = new NetworkCall();

        myApiService.getSupervisorsFromServer(new ResponseCallback<SupervisorList>() {
            @Override
            public void onSuccess(SupervisorList data) {
                if (data != null && !data.getSupervisors().isEmpty()) {
                    loadingIndicatorViewSupervisor.hide();
                    loadingIndicatorViewSupervisorLL.setVisibility(View.GONE);
                    swipeRefreshLayoutSupervisor.setRefreshing(false);

                    supervisorList = data.getSupervisors();
                    supervisorAdapter = new SupervisorAdapter(getContext(), supervisorList);

                    supervisorRecyclerView.setHasFixedSize(true);
                    supervisorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    supervisorRecyclerView.setAdapter(supervisorAdapter);
                    supervisorAdapter.notifyDataSetChanged();
                    supervisorAdapter.setOnMyClickListener(SupervisorFragment.this);

                } else {
                    swipeRefreshLayoutSupervisor.setRefreshing(false);
                    Toast.makeText(getActivity(), "No data found.", Toast.LENGTH_SHORT).show();

                    if (supervisorList == null || supervisorList.isEmpty()) {
                        loadingIndicatorViewSupervisor.hide();
                        loadingIndicatorTextViewSupervisor.setText("No data found.");
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                swipeRefreshLayoutSupervisor.setRefreshing(false);
                Toast.makeText(context, "Error: " + th.getMessage(), Toast.LENGTH_SHORT).show();

                if (supervisorList == null || supervisorList.isEmpty()) {
                    loadingIndicatorTextViewSupervisor.setText(context.getResources().getString(R.string.check_your_internet_connection_and_pull_to_refresh));
                    loadingIndicatorTextViewSupervisor.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayoutSupervisor.setRefreshing(true);
        loadSupervisorsFromServer();
    }

    @Override
    public void onMyClick(int position) {
        Supervisor supervisor = new Supervisor(supervisorList.get(position).getId(), supervisorList.get(position).getSupervisorName(), supervisorList.get(position).getSupervisorInitial(), supervisorList.get(position).getDesignation(), supervisorList.get(position).getSupervisorImage(), supervisorList.get(position).getPhone(), supervisorList.get(position).getEmail(), supervisorList.get(position).getResearchArea(), supervisorList.get(position).getTrainingExperience(), supervisorList.get(position).getMembership(), supervisorList.get(position).getPublicationProject(), supervisorList.get(position).getProfileLink());

        Intent intent = new Intent(context, TopicSupervisorDetailActivity.class);
        intent.putExtra(IntentAndBundleKey.KEY_TOPIC_AND_SUPERVISOR, "supervisor_intent");
        intent.putExtra(IntentAndBundleKey.KEY_SUPERVISOR_DATA, supervisor);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    @Override
    public void onMyClickRequestOrAcceptListener(int position, int requestedOrAccepted) {}
}
