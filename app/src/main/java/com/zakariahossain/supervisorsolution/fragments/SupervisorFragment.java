package com.zakariahossain.supervisorsolution.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

    private List<Supervisor> supervisorList;
    private RecyclerView supervisorRecyclerView;
    private SupervisorAdapter supervisorAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutSupervisor;
    private AVLoadingIndicatorView loadingIndicatorViewSupervisor;
    private LinearLayoutCompat loadingIndicatorViewSupervisorLL;

    public SupervisorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_supervisor, container, false);

        setUpSupervisorFragmentUi(view);
        loadSupervisorsFromServer();

        return view;
    }

    private void setUpSupervisorFragmentUi(View view) {
        supervisorRecyclerView = view.findViewById(R.id.rvTeacher);
        swipeRefreshLayoutSupervisor = view.findViewById(R.id.swipeRefreshLayoutTeacher);
        loadingIndicatorViewSupervisor = view.findViewById(R.id.avLoadingViewTeacher);
        loadingIndicatorViewSupervisorLL = view.findViewById(R.id.avLoadingViewTeacherLinearLayout);

        swipeRefreshLayoutSupervisor.setOnRefreshListener(this);
        swipeRefreshLayoutSupervisor.setColorSchemeResources(R.color.colorPrimary);
    }

    private void loadSupervisorsFromServer() {
        swipeRefreshLayoutSupervisor.setRefreshing(true);

        MyApiService myApiService = new NetworkCall();
        myApiService.getSupervisorsFromServer(new ResponseCallback<SupervisorList>() {
            @Override
            public void onSuccess(SupervisorList data) {
                if (data != null && !data.getSupervisors().isEmpty()) {
                    loadingIndicatorViewSupervisorLL.setVisibility(View.GONE);
                    loadingIndicatorViewSupervisor.hide();

                    supervisorList = data.getSupervisors();
                    supervisorAdapter = new SupervisorAdapter(getContext(), supervisorList);

                    supervisorRecyclerView.setHasFixedSize(true);
                    supervisorRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    supervisorRecyclerView.setAdapter(supervisorAdapter);
                    supervisorAdapter.notifyDataSetChanged();

                    swipeRefreshLayoutSupervisor.setRefreshing(false);
                    supervisorAdapter.setOnMyClickListener(SupervisorFragment.this);

                } else {
                    swipeRefreshLayoutSupervisor.setRefreshing(false);
                    Toast.makeText(getContext(), "No data found. Internet connection problem or something else.", Toast.LENGTH_LONG).show();

                    if (supervisorList == null || supervisorList.isEmpty()) {
                        loadingIndicatorViewSupervisorLL.setVisibility(View.VISIBLE);
                        loadingIndicatorViewSupervisor.show();
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                swipeRefreshLayoutSupervisor.setRefreshing(false);
                Toast.makeText(getContext(), "Error: " + th.getMessage(), Toast.LENGTH_LONG).show();

                if (supervisorList == null || supervisorList.isEmpty()) {
                    loadingIndicatorViewSupervisorLL.setVisibility(View.VISIBLE);
                    loadingIndicatorViewSupervisor.show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadSupervisorsFromServer();
    }

    @Override
    public void onMyClick(int position) {
        Supervisor supervisor = new Supervisor(supervisorList.get(position).getId(), supervisorList.get(position).getSupervisorName(), supervisorList.get(position).getSupervisorInitial(), supervisorList.get(position).getDesignation(), supervisorList.get(position).getSupervisorImage(), supervisorList.get(position).getPhone(), supervisorList.get(position).getEmail(), supervisorList.get(position).getResearchArea(), supervisorList.get(position).getTrainingExperience(), supervisorList.get(position).getMembership(), supervisorList.get(position).getPublicationProject(), supervisorList.get(position).getProfileLink());

        Intent intent = new Intent(getContext(), TopicSupervisorDetailActivity.class);
        intent.putExtra(IntentAndBundleKey.KEY_TOPIC_AND_SUPERVISOR, "supervisor_intent");
        intent.putExtra(IntentAndBundleKey.KEY_SUPERVISOR_DATA, supervisor);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
