package com.zakariahossain.supervisorsolution.utils;

import android.app.Activity;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.RectanglePromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class ShowCasePopUp {
    public static void popupShow(Activity activity, int view, String primaryText, String secondaryText) {
        new MaterialTapTargetPrompt.Builder(activity)
            .setTarget(view)
            .setPrimaryText(primaryText)
            .setSecondaryText(secondaryText)
            .setPromptBackground(new RectanglePromptBackground())
            .setPromptFocal(new RectanglePromptFocal())
            .show();
    }
}
