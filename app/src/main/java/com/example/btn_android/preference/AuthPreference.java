package com.example.btn_android.preference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * AuthPreference - Quản lý trạng thái đăng nhập bằng SharedPreferences
 * Phụ trách: Dương - Đăng nhập & xác thực
 *
 * Lưu trữ thông tin đăng nhập của user để duy trì phiên làm việc
 */
public class AuthPreference {
    private static final String PREF_NAME = "AuthPreference";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_EMAIL = "email";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public AuthPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Lưu thông tin đăng nhập khi user đăng nhập thành công
     */
    public void saveLoginSession(int userId, String username, String fullName, String email) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_USER_ID, userId);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_FULL_NAME, fullName);
        editor.putString(KEY_EMAIL, email);
        editor.apply();
    }

    /**
     * Xóa thông tin đăng nhập khi user đăng xuất
     */
    public void clearLoginSession() {
        editor.clear();
        editor.apply();
    }

    /**
     * Kiểm tra user đã đăng nhập chưa
     */
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    /**
     * Lấy User ID của user đang đăng nhập
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, -1);
    }

    /**
     * Lấy Username của user đang đăng nhập
     */
    public String getUsername() {
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    /**
     * Lấy Full Name của user đang đăng nhập
     */
    public String getFullName() {
        return sharedPreferences.getString(KEY_FULL_NAME, null);
    }

    /**
     * Lấy Email của user đang đăng nhập
     */
    public String getEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }
}

