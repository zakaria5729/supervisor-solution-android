package com.zakariahossain.supervisorsolution.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.zakariahossain.supervisorsolution.R;
import com.zakariahossain.supervisorsolution.models.User;
import com.zakariahossain.supervisorsolution.utils.IntentAndBundleKey;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "supervisor_shared_preference";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void saveUser(User user) {
        editor.putInt("id", user.getId());
        editor.putString("name", user.getName());
        editor.putString("email", user.getEmail());
        editor.putString("user_role", user.getUserRole());
        editor.putString("created_at", user.getCreatedAt());
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getInt("id", -1) != -1;
    }

    public User getUser() {
        return new User(
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("email", null),
                sharedPreferences.getString("user_role", null),
                sharedPreferences.getString("created_at", null)
        );
    }

    public void clearPreference() {
        editor.clear();
        editor.apply();
    }
}
