package com.example.btn_android.utils;

import android.content.Context;
import android.content.Intent;
import com.example.btn_android.preference.UserPreference;
import com.example.btn_android.ui.auth.LoginActivity;

/**
 * Utility class to check login status and redirect to login if needed
 * Used by activities that require authentication before proceeding
 */
public class AuthHelper {

    /**
     * Check if user is logged in
     * @param context Application context
     * @return true if user is logged in, false otherwise
     */
    public static boolean isLoggedIn(Context context) {
        UserPreference userPreference = new UserPreference(context);
        return userPreference.isLoggedIn();
    }

    /**
     * Get current logged in user ID
     * @param context Application context
     * @return User ID or -1 if not logged in
     */
    public static int getCurrentUserId(Context context) {
        UserPreference userPreference = new UserPreference(context);
        return userPreference.getUserId();
    }

    /**
     * Get current logged in username
     * @param context Application context
     * @return Username or empty string if not logged in
     */
    public static String getCurrentUsername(Context context) {
        UserPreference userPreference = new UserPreference(context);
        return userPreference.getUsername();
    }

    /**
     * Redirect to login screen with return information
     * @param context Current activity context
     * @param returnToActivityClass The activity class to return to after login
     */
    public static void redirectToLogin(Context context, Class<?> returnToActivityClass) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_RETURN_TO_ACTIVITY, returnToActivityClass.getName());
        context.startActivity(intent);
    }

    /**
     * Redirect to login screen with return information and extra data
     * @param context Current activity context
     * @param returnToActivityClass The activity class to return to after login
     * @param productId Product ID to pass back (for product detail flows)
     */
    public static void redirectToLogin(Context context, Class<?> returnToActivityClass, int productId) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_RETURN_TO_ACTIVITY, returnToActivityClass.getName());
        intent.putExtra(LoginActivity.EXTRA_PRODUCT_ID, productId);
        context.startActivity(intent);
    }

    /**
     * Check login and redirect if not logged in
     * @param context Current activity context
     * @param returnToActivityClass Activity to return to after login
     * @return true if user is logged in, false if redirected to login
     */
    public static boolean requireLogin(Context context, Class<?> returnToActivityClass) {
        if (!isLoggedIn(context)) {
            redirectToLogin(context, returnToActivityClass);
            return false;
        }
        return true;
    }

    /**
     * Logout current user
     * @param context Application context
     */
    public static void logout(Context context) {
        UserPreference userPreference = new UserPreference(context);
        userPreference.clearLoginStatus();
    }
}

