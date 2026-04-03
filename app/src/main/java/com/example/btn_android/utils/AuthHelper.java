package com.example.btn_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.btn_android.preference.AuthPreference;
import com.example.btn_android.ui.auth.LoginActivity;

/**
 * AuthHelper - Tiện ích kiểm tra và xử lý xác thực
 * Phụ trách: Dương - Đăng nhập & xác thực
 *
 * Sử dụng: Các màn hình khác (Product Detail, Checkout) sẽ dùng class này
 * để kiểm tra đăng nhập trước khi thực hiện hành động
 */
public class AuthHelper {

    /**
     * Kiểm tra user đã đăng nhập chưa
     * Trả về true nếu đã đăng nhập, false nếu chưa
     */
    public static boolean isUserLoggedIn(Context context) {
        AuthPreference authPreference = new AuthPreference(context);
        return authPreference.isLoggedIn();
    }

    /**
     * Lấy User ID của user đang đăng nhập
     * Trả về -1 nếu chưa đăng nhập
     */
    public static int getLoggedInUserId(Context context) {
        AuthPreference authPreference = new AuthPreference(context);
        return authPreference.getUserId();
    }

    /**
     * Kiểm tra đăng nhập và điều hướng đến Login nếu chưa đăng nhập
     *
     * @param activity Activity hiện tại
     * @return true nếu đã đăng nhập, false nếu chưa (và đã điều hướng đến Login)
     *
     * Ví dụ sử dụng trong ProductDetailActivity:
     * ```
     * btnAddToCart.setOnClickListener(v -> {
     *     if (!AuthHelper.requireLogin(this)) {
     *         return; // Chưa đăng nhập, đã chuyển sang Login
     *     }
     *     // Đã đăng nhập, thực hiện thêm vào giỏ hàng
     *     addToCart();
     * });
     * ```
     */
    public static boolean requireLogin(Activity activity) {
        if (!isUserLoggedIn(activity)) {
            redirectToLogin(activity);
            return false;
        }
        return true;
    }

    /**
     * Kiểm tra đăng nhập và điều hướng đến Login với thông tin redirect
     *
     * @param activity Activity hiện tại
     * @param redirectTo Nơi cần quay lại sau khi đăng nhập thành công
     * @param productId Product ID (nếu có)
     * @return true nếu đã đăng nhập, false nếu chưa
     *
     * Ví dụ sử dụng trong ProductDetailActivity:
     * ```
     * btnBuyNow.setOnClickListener(v -> {
     *     if (!AuthHelper.requireLoginWithRedirect(this,
     *             LoginActivity.REDIRECT_PRODUCT_DETAIL, productId)) {
     *         return;
     *     }
     *     // Đã đăng nhập, chuyển sang checkout
     *     checkout();
     * });
     * ```
     */
    public static boolean requireLoginWithRedirect(Activity activity, String redirectTo, int productId) {
        if (!isUserLoggedIn(activity)) {
            redirectToLogin(activity, redirectTo, productId);
            return false;
        }
        return true;
    }

    /**
     * Chuyển đến màn hình Login
     */
    public static void redirectToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * Chuyển đến m��n hình Login với thông tin redirect
     */
    public static void redirectToLogin(Context context, String redirectTo, int extraId) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(LoginActivity.EXTRA_REDIRECT_TO, redirectTo);
        if (redirectTo.equals(LoginActivity.REDIRECT_PRODUCT_DETAIL)) {
            intent.putExtra(LoginActivity.EXTRA_PRODUCT_ID, extraId);
        }
        context.startActivity(intent);
    }

    /**
     * Đăng xuất user
     */
    public static void logout(Context context) {
        AuthPreference authPreference = new AuthPreference(context);
        authPreference.clearLoginSession();
    }

    /**
     * Lấy tên user đang đăng nhập
     */
    public static String getLoggedInUsername(Context context) {
        AuthPreference authPreference = new AuthPreference(context);
        return authPreference.getUsername();
    }

    /**
     * Lấy họ tên user đang đăng nhập
     */
    public static String getLoggedInFullName(Context context) {
        AuthPreference authPreference = new AuthPreference(context);
        return authPreference.getFullName();
    }
}

