package com.zakariahossain.supervisorsolution.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.zakariahossain.supervisorsolution.R;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class OthersUtil {
    private  Context context;
    private AlertDialog alertDialog;
    private AppCompatEditText editTextTo, editTextSubject, editTextBody;
    private TextInputLayout textInputLayoutTo, textInputLayoutSubject, textInputLayoutBody;
    private MaterialButton sendEmailButton, cancelEmailButton;
    private String phoneNumber;

    private String toEmail, subject, body;

    public OthersUtil(Context context) {
        this.context = context;
    }

    public AlertDialog setCircularProgressBar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_loader, null);
            builder.setView(view);
            builder.setCancelable(false);
            alertDialog = builder.create();
            alertDialog.show();
        }

        return alertDialog;
    }

    public static void closeVisibleSoftKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void popUpShow(Activity activity, int view, String primaryText, String secondaryText) {
        new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(view)
                .setPrimaryText(primaryText)
                .setSecondaryText(secondaryText)
                .setPromptBackground(new RectanglePromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .show();
    }

    public static boolean passwordPatternCheck(String password) {
        Pattern pattern = Pattern.compile("[^a-z0-9]");
        Matcher matcher = pattern.matcher(password);
        return matcher.find();
    }

    public void openEmailDialog(final String emailRecipients, String title) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.dialog_email, null);

        editTextTo = view.findViewById(R.id.etTo);
        editTextSubject = view.findViewById(R.id.etSubject);
        editTextBody = view.findViewById(R.id.etBody);
        sendEmailButton = view.findViewById(R.id.btnSendEmail);
        cancelEmailButton = view.findViewById(R.id.btnCancelEmail);

        textInputLayoutTo = view.findViewById(R.id.tilTo);
        textInputLayoutSubject = view.findViewById(R.id.tilSubject);
        textInputLayoutBody = view.findViewById(R.id.tilBody);

        alertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setTitle(title)
                .setMessage("Enter your subject and message body to send this email")
                .setIcon(R.drawable.ic_menu_send)
                .setCancelable(false)
                .create();

        editTextTo.setText(emailRecipients);
        editTextTo.setFocusable(false);
        editTextTo.setFocusableInTouchMode(false);

        editTextBody.setImeOptions(EditorInfo.IME_ACTION_SEND);
        editTextBody.setRawInputType(InputType.TYPE_CLASS_TEXT);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                editTextBody.setOnEditorActionListener(editorActionListener);
                sendEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (getEmailEditTextValues()) {
                            sendEmail();
                            alertDialog.dismiss();
                        }
                    }
                });

                cancelEmailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }

    private void sendEmail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + toEmail));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(Intent.createChooser(intent, "Choose an email client: (ex: Gmail app)"));
            } else {
                Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_LONG).show();
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_LONG).show();
        }
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            switch (actionId) {
                case EditorInfo.IME_ACTION_SEND:
                    if (getEmailEditTextValues()) {
                        closeVisibleSoftKeyBoard((Activity) context);
                        sendEmail();
                        alertDialog.dismiss();
                    }
                    break;
            }
            return true;
        }
    };

    private boolean getEmailEditTextValues() {
        toEmail = Objects.requireNonNull(editTextTo.getText()).toString();
        subject = Objects.requireNonNull(editTextSubject.getText()).toString();
        body = Objects.requireNonNull(editTextBody.getText()).toString();

        if (!TextUtils.isEmpty(toEmail.trim()) && !TextUtils.isEmpty(subject.trim()) && !TextUtils.isEmpty(body.trim())) {
            return true;
        } else {
            if (TextUtils.isEmpty(toEmail.trim())) {
                textInputLayoutTo.setError("Email field can not be empty");
            }
            if (TextUtils.isEmpty(subject.trim())) {
                textInputLayoutSubject.setError("Subject field can not be empty");
            }
            if (TextUtils.isEmpty(body.trim())) {
                textInputLayoutBody.setError("Body field can not be empty");
            }
            return false;
        }
    }

    public void requestPhoneCallAndPermission(Activity activity, String phoneNumber) {
        this.phoneNumber = phoneNumber;

        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(new PermissionListener(this))
                .check();
    }

    private void getPhoneCallAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Call to "+phoneNumber)
                .setMessage("Do you want to make this call to "+phoneNumber+" right now?")
                .setIcon(R.drawable.ic_phone_call)
                .setPositiveButton("Call Now", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button callNowButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button cancelButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                callNowButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));

                callNowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phoneNumber)));
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

    void showPermissionGranted(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.CALL_PHONE:
                getPhoneCallAlertDialog();
                break;
        }
    }

    void showPermissionDenied(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.CALL_PHONE:
                Toast.makeText(context, "Phone call permission denied", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    void showPermissionRationale(final PermissionToken permissionToken) {
        new AlertDialog.Builder(context).setTitle("Why we need phone call permission?")
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

    void handlePermanentDeniedPermissions(String permissionName) {
        switch (permissionName) {
            case Manifest.permission.CALL_PHONE:
                Toast.makeText(context, "Phone call permission permanently denied", Toast.LENGTH_SHORT).show();
                break;
        }

        new AlertDialog.Builder(context).setTitle("Why we need phone call permission?")
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
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }
}
