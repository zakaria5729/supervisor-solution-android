package com.zakariahossain.supervisorsolution.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zakariahossain.supervisorsolution.R;

public class MySharedPreference {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public MySharedPreference(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.my_shared_preference), Context.MODE_PRIVATE);
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
        }
    }

    public boolean checkShowCasePreference(String showCase) {
        boolean isChecked = false;

        switch (showCase) {
            case IntentAndBundleKey.KEY_SHOW_CASE_TOPIC:
                isChecked = sharedPreferences.getBoolean(context.getString(R.string.show_case_preference_topic), false);  //here false is a default value
                break;

            case IntentAndBundleKey.KEY_SHOW_CASE_SUPERVISOR:
                isChecked = sharedPreferences.getBoolean(context.getString(R.string.show_case_preference_teacher), false);  //here false is a default value
                break;
        }

        return isChecked;
    }
}
