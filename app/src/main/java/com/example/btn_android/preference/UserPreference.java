package com.example.btn_android.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreference {
    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public UserPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveLoginStatus(int userId, String username) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.apply();
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, "");
    }

    public void clearLoginStatus() {
        editor.clear();
        editor.apply();
    }
}
