package com.zakariahossain.supervisorsolution.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

public class ShowCaseAndTabSelectionPreference {
    private static final String SHARED_PREF_NAME = "show_case_shared_preference";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public ShowCaseAndTabSelectionPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void updateShowCasePreference(String permission) {
        switch (permission) {
            case IntentAndBundleKey.KEY_SHOW_CASE_TOPIC:
                editor.putBoolean(context.getString(R.string.show_case_preference_topic), true);
                editor.apply();
                break;

            case IntentAndBundleKey.KEY_SHOW_CASE_SUPERVISOR:
                editor.putBoolean(context.getString(R.string.show_case_preference_teacher), true);
                editor.apply();
                break;

            case IntentAndBundleKey.KEY_SHOW_CASE_GOOGLE_SIGN_IN:
                editor.putBoolean("show_case_preference_google_sign_in", true);
                editor.apply();
                break;
        }
    }

    public boolean isNotShownCasePreference(String showCase) {
        switch (showCase) {
            case IntentAndBundleKey.KEY_SHOW_CASE_TOPIC:
                return !sharedPreferences.getBoolean(context.getString(R.string.show_case_preference_topic), false);  //here false is a default value

            case IntentAndBundleKey.KEY_SHOW_CASE_SUPERVISOR:
                return !sharedPreferences.getBoolean(context.getString(R.string.show_case_preference_teacher), false);  //here false is a default value

            case IntentAndBundleKey.KEY_SHOW_CASE_GOOGLE_SIGN_IN:
                return !sharedPreferences.getBoolean("show_case_preference_google_sign_in", false);

            default:
                return false;
        }
    }

    public void updateSelectedTabPosition(int selectedPosition) {
        editor.putInt(context.getString(R.string.selected_tab_position), selectedPosition);
        editor.apply();
    }

    public int getPreviouslyTabSelected() {
        return sharedPreferences.getInt(context.getString(R.string.selected_tab_position), 0);
    }
}
