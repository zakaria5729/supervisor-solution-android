package com.zakariahossain.supervisorsolution.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zakariahossain.supervisorsolution.R;

import androidx.appcompat.app.AlertDialog;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class OthersUtil {
    private Context context;
    private AlertDialog alertDialog;

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
}
