package com.zakariahossain.supervisorsolution.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.models.Supervisor;
import com.zakariahossain.supervisorsolution.models.Topic;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;
import com.zakariahossain.supervisorsolution.utils.MySharedPreference;
import com.zakariahossain.supervisorsolution.utils.PermissionListener;
import com.zakariahossain.supervisorsolution.utils.PlayerConfig;
import com.zakariahossain.supervisorsolution.utils.ShowCasePopUp;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

public class TopicSupervisorDetailActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    private static final int RECOVERY_REQUEST = 123;
    private YouTubePlayerView youTubePlayerView;
    private MySharedPreference preference;
    private PermissionListener permissionListener;

    private ImageView supervisorImageView;
    private AppCompatImageView topicImageView;
    private AppCompatTextView topicDescriptionOne, topicDescriptionTwo, topicSupervisorInitial, topicName;
    private AppCompatTextView supervisorName, supervisorInitial, supervisorDesignation, supervisorPhone, supervisorEmail, supervisorResearchArea, supervisorTrainingExperience, supervisorMembership, supervisorPublicationProject, supervisorProfileLink;

    private Topic topic;
    private Supervisor supervisor;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String intentString = getIntent().getStringExtra(IntentAndBundleKey.KEY_TOPIC_AND_SUPERVISOR);
        preference = new MySharedPreference(this);

        if (intentString.equals("supervisor_intent")) {
            setContentView(R.layout.activity_supervisor_detail);

            setUpSupervisorUi();
            getSupervisorDataFromIntent();
            checkAndUpdateShowCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_SUPERVISOR, R.id.tvSupervisorPhone, "Phone Call and Email", "Click on the phone number to make a phone call or click on the email address to send an email");

        } else if (intentString.equals("topic_intent")) {
            setContentView(R.layout.activity_topic_detail);

            setUpTopicUi();
            getTopicDataFromIntent();
            checkAndUpdateShowCasePreference(IntentAndBundleKey.KEY_SHOW_CASE_TOPIC, R.id.btnFloatingActionOk, "Ok Button", "Click on the button to go back to the topic again");
        }

        permissionListener = new PermissionListener(this);
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
    }

    private void setUpTopicUi() {
        youTubePlayerView = findViewById(R.id.youTubePlayerView);
        collapsingToolbarLayout = findViewById(R.id.collapseToolbarLayout);
        appBarLayout = findViewById(R.id.appBarLayout);
        topicImageView = findViewById(R.id.ivTopicImg);
        topicName = findViewById(R.id.tvTopicName);
        topicDescriptionOne = findViewById(R.id.tvTopicDescriptionOne);
        topicDescriptionTwo = findViewById(R.id.tvTopicDescriptionTwo);
        topicSupervisorInitial = findViewById(R.id.tvSupervisorInitial);
    }

    private void getSupervisorDataFromIntent() {
        supervisor = (Supervisor) getIntent().getSerializableExtra(IntentAndBundleKey.KEY_SUPERVISOR_DATA);

        supervisorName.setText(supervisor.getSupervisorName());
        supervisorInitial.setText(supervisor.getSupervisorInitial());
        supervisorDesignation.setText(supervisor.getDesignation());
        supervisorPhone.setText(supervisor.getPhone());
        supervisorEmail.setText(supervisor.getEmail());
        supervisorResearchArea.setText(supervisor.getResearchArea());
        supervisorTrainingExperience.setText(supervisor.getTrainingExperience());
        supervisorMembership.setText(supervisor.getMembership());
        supervisorPublicationProject.setText(supervisor.getPublicationProject());
        supervisorProfileLink.setText(supervisor.getProfileLink());

        Glide.with(this)
                .load(supervisor.getSupervisorImage())
                .into(supervisorImageView);

        supervisorPhone.setOnClickListener(this);
        supervisorEmail.setOnClickListener(this);
    }

    private void getTopicDataFromIntent() {
        topic = (Topic) getIntent().getSerializableExtra(IntentAndBundleKey.KEY_TOPIC_DATA);

        topicName.setText(topic.getTopicName());
        topicSupervisorInitial.setText(topic.getSupervisorInitial());
        topicDescriptionOne.setText(topic.getTopicDescriptionOne());
        topicDescriptionTwo.setText(topic.getTopicDescriptionTwo());

        Glide.with(TopicSupervisorDetailActivity.this)
                .load(topic.getTopicImage())
                .into(topicImageView);

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
        if (!preference.checkShowCasePreference(preferenceName)) {
            ShowCasePopUp.popupShow(this, view, primaryText, secondaryText);
            preference.updateShowCasePreference(preferenceName);
        }
    }

    public void FloatingActionButtonOnClick(View view) {
        finish();
    }

    public void playVideoClickListener(View view) {
        getYouTubePlayerProvider().initialize(PlayerConfig.YOUTUBE_API_KEY, this);
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        youTubePlayerView.initialize(PlayerConfig.YOUTUBE_API_KEY, this);
        return youTubePlayerView;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
            youTubePlayer.cueVideo(topic.getTopicVideoLink()); //play video
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
            getYouTubePlayerProvider().initialize(PlayerConfig.YOUTUBE_API_KEY, this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSupervisorEmail:
                openEmailDialog();
                break;

            case R.id.tvSupervisorPhone:
                requestPhoneCallPermission();
                break;
        }
    }

    private void openEmailDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_email, null);

        final AppCompatEditText editTextTo = view.findViewById(R.id.etTo);
        final AppCompatEditText editTextSubject = view.findViewById(R.id.etSubject);
        final AppCompatEditText editTextBody = view.findViewById(R.id.etBody);

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setTitle("Send email")
                .setIcon(R.drawable.ic_menu_send)
                .setPositiveButton("Send", null)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create();

        editTextTo.setText(supervisor.getEmail().trim());

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button sendButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                sendButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                cancelButton.setTextColor(getResources().getColor(R.color.colorShadow));

                sendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String to = Objects.requireNonNull(editTextTo.getText()).toString();
                        String subject = Objects.requireNonNull(editTextSubject.getText()).toString();
                        String body = Objects.requireNonNull(editTextBody.getText()).toString();

                        if (!TextUtils.isEmpty(to.trim()) && !TextUtils.isEmpty(subject.trim()) && !TextUtils.isEmpty(body.trim())) {
                            sendEmail(to, subject, body);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(TopicSupervisorDetailActivity.this, "Please, fill up all the fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    private void sendEmail(String to, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + to));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        //intent.setType("message/rfc822");

        try {
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(Intent.createChooser(intent, "Choose an email client: (ex: Gmail app)"));
            } else {
                Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_LONG).show();
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPhoneCallPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(permissionListener)
                .check();
    }

    private void getPhoneCallAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Do you want to make this call right now?")
                .setIcon(R.drawable.ic_phone_call)
                .setPositiveButton("Call", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button callNowButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                callNowButton.setTextColor(getResources().getColor(R.color.colorPrimary));
                cancelButton.setTextColor(getResources().getColor(R.color.colorShadow));

                callNowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + supervisor.getPhone().trim())));
                        dialog.dismiss();
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        dialog.show();
    }

    public void showPermissionGranted(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.CALL_PHONE:
                getPhoneCallAlertDialog();
                break;
        }
    }

    public void showPermissionDenied(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.CALL_PHONE:
                Toast.makeText(this, "Phone call permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void showPermissionRationale(final PermissionToken permissionToken) {
        new AlertDialog.Builder(this).setTitle("Why we need phone call permission?")
                .setMessage("We need phone call permission, because you can not make a call using this app until phone call permission granted.")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionToken.continuePermissionRequest();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionToken.cancelPermissionRequest();
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        permissionToken.cancelPermissionRequest();
                    }
                }).show();
    }

    public void handlePermanentDeniedPermissions(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.CALL_PHONE:
                Toast.makeText(this, "Phone call permission permanently denied", Toast.LENGTH_SHORT).show();
                break;
        }

        new AlertDialog.Builder(this).setTitle("Why we need phone call permission?")
                .setMessage("We need phone call permission, because you can not make a call using this app until phone call permission granted. And please, allow the phone call permission from the app settings")
                .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openAppPermissionSetting();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void openAppPermissionSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }
}
