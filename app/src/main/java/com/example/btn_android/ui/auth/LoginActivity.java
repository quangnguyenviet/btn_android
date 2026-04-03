package com.example.btn_android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btn_android.R;
import com.example.btn_android.data.local.entity.User;
import com.example.btn_android.data.repository.UserRepository;
import com.example.btn_android.preference.AuthPreference;

import java.util.concurrent.ExecutionException;

/**
 * LoginActivity - Màn hình đăng nhập
 * Phụ trách: Dương - Đăng nhập & xác thực
 * 
 * Chức năng:
 * - Nhập username và password để đăng nhập
 * - Kiểm tra thông tin đăng nhập với database
 * - Lưu trạng thái đăng nhập vào SharedPreferences
 * - Điều hướng người dùng quay lại luồng mua hàng sau khi đăng nhập thành công
 */
public class LoginActivity extends AppCompatActivity {

    // UI Components
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister, tvForgotPassword;
    private ProgressBar progressBar;

    // Data
    private UserRepository userRepository;
    private AuthPreference authPreference;

    // Constants cho Intent
    public static final String EXTRA_REDIRECT_TO = "redirect_to";
    public static final String REDIRECT_PRODUCT_DETAIL = "product_detail";
    public static final String REDIRECT_CHECKOUT = "checkout";
    public static final String EXTRA_PRODUCT_ID = "product_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize
        initViews();
        initData();
        setupListeners();

        // Kiểm tra nếu user đã đăng nhập thì không cần login lại
        if (authPreference.isLoggedIn()) {
            redirectAfterLogin();
        }
    }

    /**
     * Khởi tạo các View components
     */
    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        progressBar = findViewById(R.id.progress_bar);
    }

    /**
     * Khởi tạo dữ liệu
     */
    private void initData() {
        userRepository = new UserRepository(this);
        authPreference = new AuthPreference(this);
    }

    /**
     * Setup các event listeners
     */
    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegister.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình đăng ký
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            // TODO: Chuyển đến màn hình quên mật khẩu
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Xử lý đăng nhập
     */
    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate input
        if (!validateInput(username, password)) {
            return;
        }

        // Show loading
        showLoading(true);

        // Kiểm tra đăng nhập trong background thread
        new Thread(() -> {
            try {
                User user = userRepository.login(username, password).get();

                runOnUiThread(() -> {
                    showLoading(false);

                    if (user != null) {
                        // Đăng nhập thành công
                        onLoginSuccess(user);
                    } else {
                        // Đăng nhập thất bại
                        onLoginFailed();
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(LoginActivity.this, 
                            "Lỗi hệ thống: " + e.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    /**
     * Validate thông tin đăng nhập
     */
    private boolean validateInput(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Vui lòng nhập mật khẩu");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Xử lý khi đăng nhập thành công
     */
    private void onLoginSuccess(User user) {
        // Lưu thông tin đăng nhập vào SharedPreferences
        authPreference.saveLoginSession(
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail()
        );

        Toast.makeText(this, 
                "Đăng nhập thành công! Xin chào " + user.getFullName(), 
                Toast.LENGTH_SHORT).show();

        // Điều hướng người dùng quay lại luồng mua hàng
        redirectAfterLogin();
    }

    /**
     * Xử lý khi đăng nhập thất bại
     */
    private void onLoginFailed() {
        Toast.makeText(this, 
                "Tên đăng nhập hoặc mật khẩu không đúng", 
                Toast.LENGTH_SHORT).show();
        etPassword.setText("");
        etPassword.requestFocus();
    }

    /**
     * Điều hướng người dùng quay lại luồng mua hàng sau khi đăng nhập thành công
     * Dựa vào Intent extras để biết user muốn quay về đâu
     */
    private void redirectAfterLogin() {
        Intent intent = getIntent();
        String redirectTo = intent.getStringExtra(EXTRA_REDIRECT_TO);

        if (REDIRECT_PRODUCT_DETAIL.equals(redirectTo)) {
            // Quay lại màn hình chi tiết sản phẩm
            int productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1);
            // TODO: Chuyển đến ProductDetailActivity với productId
            Toast.makeText(this, "Chuyển đến sản phẩm #" + productId, Toast.LENGTH_SHORT).show();
        } else if (REDIRECT_CHECKOUT.equals(redirectTo)) {
            // Quay lại màn hình thanh toán
            // TODO: Chuyển đến CheckoutActivity
            Toast.makeText(this, "Chuyển đến thanh toán", Toast.LENGTH_SHORT).show();
        } else {
            // Mặc định quay về MainActivity
            Intent homeIntent = new Intent(LoginActivity.this, 
                    com.example.btn_android.view.MainActivity.class);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(homeIntent);
        }

        finish();
    }

    /**
     * Hiển thị/ẩn loading
     */
    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setEnabled(true);
        }
    }

    /**
     * Xử lý khi nhấn nút Back
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Nếu cần đăng nhập mới được tiếp tục, có thể chặn back
        // hoặc chuyển về màn hình Home
    }
}

