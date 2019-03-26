package com.zakariahossain.supervisorsolution.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
import com.wang.avi.Indicator;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.activities.TopicSupervisorDetailActivity;
import com.zakariahossain.supervisorsolution.adapters.TopicAdapter;
import com.zakariahossain.supervisorsolution.interfaces.OnMyClickListener;
import com.zakariahossain.supervisorsolution.models.Topic;
import com.zakariahossain.supervisorsolution.models.TopicList;
import com.zakariahossain.supervisorsolution.retrofits.MyApiService;
import com.zakariahossain.supervisorsolution.retrofits.NetworkCall;
import com.zakariahossain.supervisorsolution.retrofits.ResponseCallback;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends Fragment implements OnMyClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private List<Topic> topicList;
    private RecyclerView topicRecyclerView;
    private TopicAdapter topicAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutTopic;
    private AVLoadingIndicatorView loadingIndicatorViewTopic;
    private AppCompatTextView loadingIndicatorTextViewTopic;
    private LinearLayoutCompat loadingIndicatorViewTopicLL;

    public TopicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        return inflater.inflate(R.layout.fragment_topic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpTopicFragmentUi(view);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() != null) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        loadingIndicatorViewTopic.show();
        loadTopicsFromServer();
    }

    private void setUpTopicFragmentUi(View view) {
        topicRecyclerView = view.findViewById(R.id.rvTopic);
        swipeRefreshLayoutTopic = view.findViewById(R.id.swipeRefreshLayoutTopic);
        loadingIndicatorViewTopic = view.findViewById(R.id.avLoadingViewTopic);
        loadingIndicatorTextViewTopic = view.findViewById(R.id.avLoadingTextViewTopic);
        loadingIndicatorViewTopicLL = view.findViewById(R.id.avLoadingViewTopicLinearLayout);

        swipeRefreshLayoutTopic.setOnRefreshListener(this);
        swipeRefreshLayoutTopic.setColorSchemeResources(R.color.colorPrimaryDark);
    }

   /* @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            screenOrientation = 0;
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            screenOrientation = 1;
        }
    }*/

    private void loadTopicsFromServer() {
        MyApiService myApiService = new NetworkCall();

        myApiService.getTopicsFromServer(new ResponseCallback<TopicList>() {
            @Override
            public void onSuccess(TopicList data) {
                if (data != null && !data.getTopics().isEmpty()) {
                    loadingIndicatorViewTopic.hide();
                    loadingIndicatorViewTopicLL.setVisibility(View.GONE);
                    swipeRefreshLayoutTopic.setRefreshing(false);

                    topicList = data.getTopics();
                    //topicAdapter = new TopicAdapter(context, topicList, screenOrientation);
                    topicAdapter = new TopicAdapter(context, topicList);

                    topicRecyclerView.setHasFixedSize(true);
                    topicRecyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                    topicRecyclerView.setAdapter(topicAdapter);
                    topicAdapter.notifyDataSetChanged();
                    topicAdapter.setOnMyClickListener(TopicFragment.this);

                } else {
                    swipeRefreshLayoutTopic.setRefreshing(false);
                    Toast.makeText(context, "No data found.", Toast.LENGTH_SHORT).show();

                    if (topicList == null || topicList.isEmpty()) {
                        loadingIndicatorViewTopic.hide();
                        loadingIndicatorTextViewTopic.setText("No data found.");
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                swipeRefreshLayoutTopic.setRefreshing(false);
                Toast.makeText(context, "Error: " + th.getMessage(), Toast.LENGTH_SHORT).show();

                if (topicList == null || topicList.isEmpty()) {
                    loadingIndicatorTextViewTopic.setText(context.getResources().getString(R.string.check_your_internet_connection_and_pull_to_refresh));
                    loadingIndicatorTextViewTopic.setTextColor(context.getResources().getColor(R.color.colorAccent));
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayoutTopic.setRefreshing(true);
        loadTopicsFromServer();
    }

    @Override
    public void onMyClick(int position) {
        Topic topic = new Topic(topicList.get(position).getId(), topicList.get(position).getTopicName(), topicList.get(position).getImagePath(), topicList.get(position).getSupervisorInitial(), topicList.get(position).getDescriptionOne(), topicList.get(position).getDescriptionTwo(), topicList.get(position).getVideoPath());

        Intent intent = new Intent(context, TopicSupervisorDetailActivity.class);
        intent.putExtra(IntentAndBundleKey.KEY_TOPIC_AND_SUPERVISOR, "topic_intent");
        intent.putExtra(IntentAndBundleKey.KEY_TOPIC_DATA, topic);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }

    @Override
    public void onMyClickRequestOrAcceptListener(int position, int requestedOrAccepted) {}
}
