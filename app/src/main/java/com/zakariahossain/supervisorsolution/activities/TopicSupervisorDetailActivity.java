package com.zakariahossain.supervisorsolution.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.card.MaterialCardView;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.adapters.SupervisorInitialAdapter;
import com.zakariahossain.supervisorsolution.models.SupervisorList;
import com.zakariahossain.supervisorsolution.models.TopicList;
import com.zakariahossain.supervisorsolution.preferences.ShowCaseAndTabSelectionPreference;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.OthersUtil;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TopicSupervisorDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    //account in mail: zakariaallinone@gmail.com
    private static final String YOUTUBE_API_KEY = "AIzaSyCgM8RIaByCtXNKlGCM8v2RcAGb7mPsfIk";

    private static final int RECOVERY_REQUEST = 123;
    private YouTubePlayerView youTubePlayerView;
    private ShowCaseAndTabSelectionPreference showCasePreference;

    private RecyclerView recyclerViewInitial;
    private ImageView supervisorImageView;
    private AppCompatImageView topicImageView;
    private AppCompatTextView topicDescriptionOne;
    private AppCompatTextView topicDescriptionTwo;
    private AppCompatTextView topicName;
    private AppCompatTextView supervisorName, supervisorInitial, supervisorDesignation, supervisorPhone, supervisorEmail, supervisorResearchArea, supervisorTrainingExperience, supervisorMembership, supervisorPublicationProject, supervisorProfileLink, supervisorResearchAreaTag, supervisorTrainingExperienceTag, supervisorMembershipTag, supervisorPublicationProjectTag, supervisorProfileLinkTag;

    private String intentKey;
    private TopicList.Topic topic;
    private SupervisorList.Supervisor supervisor;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentKey = getIntent().getStringExtra(IntentAndBundleKey.KEY_TOPIC_AND_SUPERVISOR);

        if (intentKey != null) {
            if (intentKey.equals("supervisor_intent")) {
                setContentView(R.layout.activity_supervisor_detail);
                setUpSupervisorUi();
            } else if (intentKey.equals("topic_intent")) {
                setContentView(R.layout.activity_topic_detail);
                setUpTopicUi();
            }
        }

        showCasePreference = new ShowCaseAndTabSelectionPreference(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (intentKey != null) {
            if (intentKey.equals("supervisor_intent")) {
                getAndSetSupervisorDataFromIntent();
                supervisorImageView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));

                checkAndUpdateShowCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_SUPERVISOR, R.id.tvSupervisorPhone, "Phone Call and Email", "Click on the phone number to make a phone call or click on the email address to send an email");

            } else if (intentKey.equals("topic_intent")) {
                getAndSetTopicDataFromIntent();
                topicImageView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.zoom_in));

                checkAndUpdateShowCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_TOPIC, R.id.rcvSupervisorInitial, "Supervisors Initial on this topic", "Scrolling left to right to show the more supervisors initial. And click on the initial to copy initial to clip board");
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (intentKey.equals("topic_intent")) {
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this); //Initialize youtube player with api
        }
    }

    private void setUpSupervisorUi() {
        supervisorName = findViewById(R.id.tvSupervisorName);
        supervisorInitial = findViewById(R.id.tvSupervisorInitial);
        supervisorDesignation = findViewById(R.id.tvSupervisorDesignation);
        supervisorImageView = findViewById(R.id.ivSupervisor);
        supervisorPhone = findViewById(R.id.tvSupervisorPhone);
        supervisorEmail = findViewById(R.id.tvSupervisorEmail);
        supervisorResearchArea = findViewById(R.id.tvSupervisorResearchArea);
        supervisorTrainingExperience = findViewById(R.id.tvSupervisorTrainingExperience);
        supervisorMembership = findViewById(R.id.tvSupervisorMembership);
        supervisorPublicationProject = findViewById(R.id.tvSupervisorPublicationProject);
        supervisorProfileLink = findViewById(R.id.tvSupervisorProfileLink);

        supervisorResearchAreaTag = findViewById(R.id.tvResearchAreaTag);
        supervisorTrainingExperienceTag = findViewById(R.id.tvTrainingExperienceTag);
        supervisorMembershipTag = findViewById(R.id.tvMembershipTag);
        supervisorPublicationProjectTag = findViewById(R.id.tvPublicationProjectTag);
        supervisorProfileLinkTag = findViewById(R.id.tvProfileLinkTag);

        supervisorProfileLink.setOnClickListener(this);
    }

    private void setUpTopicUi() {
        youTubePlayerView = findViewById(R.id.youTubePlayerView);
        collapsingToolbarLayout = findViewById(R.id.collapseToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        topicImageView = findViewById(R.id.ivTopicImg);
        topicName = findViewById(R.id.tvTopicName);
        topicDescriptionOne = findViewById(R.id.tvTopicDescriptionOne);
        topicDescriptionTwo = findViewById(R.id.tvTopicDescriptionTwo);
        recyclerViewInitial = findViewById(R.id.rcvSupervisorInitial);
    }

    private void getAndSetSupervisorDataFromIntent() {
        supervisor = (SupervisorList.Supervisor) getIntent().getSerializableExtra(IntentAndBundleKey.KEY_SUPERVISOR_DATA);

        if (supervisor != null) {
            if (!TextUtils.isEmpty(supervisor.getSupervisorImage())) {
                Glide.with(this)
                        .load(supervisor.getSupervisorImage())
                        .into(supervisorImageView);
            }

            String initial = supervisor.getSupervisorInitial() + ",";
            String designation = supervisor.getDesignation() + ",";

            supervisorName.setText(supervisor.getSupervisorName());
            supervisorInitial.setText(initial);
            supervisorDesignation.setText(designation);
            supervisorPhone.setText(supervisor.getPhone());
            supervisorEmail.setText(supervisor.getEmail());

            supervisorPhone.setPaintFlags(supervisorProfileLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            supervisorEmail.setPaintFlags(supervisorProfileLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            if (!TextUtils.isEmpty(supervisor.getResearchArea())) {
                supervisorResearchAreaTag.setVisibility(View.VISIBLE);
                supervisorResearchArea.setVisibility(View.VISIBLE);
                supervisorResearchArea.setText(supervisor.getResearchArea());
            }
            if (!TextUtils.isEmpty(supervisor.getTrainingExperience())) {
                supervisorTrainingExperienceTag.setVisibility(View.VISIBLE);
                supervisorTrainingExperience.setVisibility(View.VISIBLE);
                supervisorTrainingExperience.setText(supervisor.getTrainingExperience());
            }
            if (!TextUtils.isEmpty(supervisor.getMembership())) {
                supervisorMembershipTag.setVisibility(View.VISIBLE);
                supervisorMembership.setVisibility(View.VISIBLE);
                supervisorMembership.setText(supervisor.getMembership());
            }
            if (!TextUtils.isEmpty(supervisor.getPublicationProject())) {
                supervisorPublicationProjectTag.setVisibility(View.VISIBLE);
                supervisorPublicationProject.setVisibility(View.VISIBLE);
                supervisorPublicationProject.setText(supervisor.getPublicationProject());
            }
            if (!TextUtils.isEmpty(supervisor.getProfileLink())) {
                supervisorProfileLinkTag.setVisibility(View.VISIBLE);
                supervisorProfileLink.setVisibility(View.VISIBLE);
                supervisorProfileLink.setPaintFlags(supervisorProfileLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                supervisorProfileLink.setText(supervisor.getProfileLink());
            }
        }

        supervisorPhone.setOnClickListener(this);
        supervisorEmail.setOnClickListener(this);
    }

    private void getAndSetTopicDataFromIntent() {
        topic = (TopicList.Topic) getIntent().getSerializableExtra(IntentAndBundleKey.KEY_TOPIC_DATA);

        if (topic != null) {
            if (!TextUtils.isEmpty(topic.getImagePath())) {
                Glide.with(TopicSupervisorDetailActivity.this)
                        .load(topic.getImagePath())
                        .into(topicImageView);
            }

            topicName.setText(topic.getTopicName());
            topicDescriptionOne.setText(topic.getDescriptionOne());
            topicDescriptionTwo.setText(topic.getDescriptionTwo());

            String[] supervisorInitialList = topic.getSupervisorInitial().split(", ");

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerViewInitial.setLayoutManager(layoutManager);
            recyclerViewInitial.setHasFixedSize(true);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            SupervisorInitialAdapter initialAdapter = new SupervisorInitialAdapter(this, supervisorInitialList);
            recyclerViewInitial.setAdapter(initialAdapter);
        }

        appBarOffsetChangedListener();
    }

    private void appBarOffsetChangedListener() {
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) { //Collapse
                    collapsingToolbarLayout.setTitle(topic.getTopicName());
                } else { //Else
                    collapsingToolbarLayout.setTitle("");
                }
            }
        });
    }

    private void checkAndUpdateShowCasePreference(String preferenceName, int view, String primaryText, String secondaryText) {
        if (showCasePreference.isNotShownCasePreference(preferenceName)) {
            OthersUtil.popUpShow(this, view, primaryText, secondaryText);
            showCasePreference.updateShowCasePreference(preferenceName);
        }
    }

    public void FloatingActionButtonOnClick(View view) {
        onBackPressed();
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, this);
        return youTubePlayerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youTubePlayer.cueVideo(topic.getVideoPath()); //play video
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST && resultCode == RESULT_OK) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSupervisorEmail:
                OthersUtil othersUtil = new OthersUtil(this);
                othersUtil.openEmailDialog(supervisor.getEmail().trim(), "Send Email");
                break;

            case R.id.tvSupervisorPhone:
                new OthersUtil(this).requestPhoneCallAndPermission(this, supervisor.getPhone());
                break;

            case R.id.tvSupervisorProfileLink:
                if (supervisor.getProfileLink() != null) {
                    Intent intent = new Intent(this, WebViewAndGroupListDetailsActivity.class);
                    intent.putExtra(IntentAndBundleKey.KEY_PROFILE_LINK, supervisor.getProfileLink());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
                break;
        }
    }
}
