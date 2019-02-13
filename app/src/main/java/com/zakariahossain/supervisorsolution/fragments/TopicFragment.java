package com.zakariahossain.supervisorsolution.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;
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

import java.util.List;

public class TopicFragment extends Fragment implements OnMyClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<Topic> topicList;
    private RecyclerView topicRecyclerView;
    private TopicAdapter topicAdapter;
    private SwipeRefreshLayout swipeRefreshLayoutTopic;
    private AVLoadingIndicatorView loadingIndicatorViewTopic;
    private LinearLayoutCompat loadingIndicatorViewTopicLL;

    public TopicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        setUpTopicFragmentUi(view);
        loadTopicsFromServer();

        return view;
    }

    private void setUpTopicFragmentUi(View view) {
        topicRecyclerView = view.findViewById(R.id.rvTopic);
        swipeRefreshLayoutTopic = view.findViewById(R.id.swipeRefreshLayoutTopic);
        loadingIndicatorViewTopic = view.findViewById(R.id.avLoadingViewTopic);
        loadingIndicatorViewTopicLL = view.findViewById(R.id.avLoadingViewTopicLinearLayout);

        swipeRefreshLayoutTopic.setOnRefreshListener(this);
        swipeRefreshLayoutTopic.setColorSchemeResources(R.color.colorPrimary);
    }

    private void loadTopicsFromServer() {
        swipeRefreshLayoutTopic.setRefreshing(true);

        MyApiService myApiService = new NetworkCall();
        myApiService.getTopicsFromServer(new ResponseCallback<TopicList>() {
            @Override
            public void onSuccess(TopicList data) {
                if (data.getTopics() != null) {
                    loadingIndicatorViewTopicLL.setVisibility(View.GONE);
                    loadingIndicatorViewTopic.hide();

                    topicList = data.getTopics();

                    topicAdapter = new TopicAdapter(getContext(), topicList);

                    topicRecyclerView.setHasFixedSize(true);
                    topicRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    topicRecyclerView.setAdapter(topicAdapter);
                    topicAdapter.notifyDataSetChanged();

                    swipeRefreshLayoutTopic.setRefreshing(false);
                    topicAdapter.setOnMyClickListener(TopicFragment.this);

                } else {
                    swipeRefreshLayoutTopic.setRefreshing(false);
                    Toast.makeText(getContext(), "No data found. Internet connection problem or something else.", Toast.LENGTH_LONG).show();

                    if (topicList == null || topicList.isEmpty()) {
                        loadingIndicatorViewTopicLL.setVisibility(View.VISIBLE);
                        loadingIndicatorViewTopic.show();
                    }
                }
            }

            @Override
            public void onError(Throwable th) {
                swipeRefreshLayoutTopic.setRefreshing(false);
                Toast.makeText(getContext(), "Error: " + th.getMessage(), Toast.LENGTH_LONG).show();

                if (topicList == null || topicList.isEmpty()) {
                    loadingIndicatorViewTopicLL.setVisibility(View.VISIBLE);
                    loadingIndicatorViewTopic.show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        loadTopicsFromServer();
    }

    @Override
    public void onMyClick(int position) {
        Topic topic = new Topic(topicList.get(position).getId(), topicList.get(position).getTopicName(), topicList.get(position).getImagePath(), topicList.get(position).getSupervisorInitial(), topicList.get(position).getDescriptionOne(), topicList.get(position).getDescriptionTwo(), topicList.get(position).getVideoPath());

        Intent intent = new Intent(getContext(), TopicSupervisorDetailActivity.class);
        intent.putExtra(IntentAndBundleKey.KEY_TOPIC_AND_SUPERVISOR, "topic_intent");
        intent.putExtra(IntentAndBundleKey.KEY_TOPIC_DATA, topic);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
