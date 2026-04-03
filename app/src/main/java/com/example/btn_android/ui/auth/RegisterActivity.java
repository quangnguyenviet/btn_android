package com.example.btn_android.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.btn_android.R;
import com.example.btn_android.data.repository.UserRepository;
import com.example.btn_android.preference.UserPreference;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etPassword, etConfirmPassword;
    private TextInputEditText etFullName, etEmail, etPhone;
    private Button btnRegister, btnBackToLogin;
    private TextView tvErrorMessage;
    private UserRepository userRepository;
    private UserPreference userPreference;
    private Handler mainHandler;

    private String returnToActivity;
    private int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initData();
        setupListeners();
        handleIntent();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);
        tvErrorMessage = findViewById(R.id.tvErrorMessage);
    }

    private void initData() {
        userRepository = new UserRepository(this);
        userPreference = new UserPreference(this);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            returnToActivity = intent.getStringExtra(LoginActivity.EXTRA_RETURN_TO_ACTIVITY);
            productId = intent.getIntExtra(LoginActivity.EXTRA_PRODUCT_ID, -1);
        }
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> handleRegister());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String username = etUsername.getText() != null ? etUsername.getText().toString() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString() : "";
        String fullName = etFullName.getText() != null ? etFullName.getText().toString() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString() : "";
        String phone = etPhone.getText() != null ? etPhone.getText().toString() : "";

        // Hide previous error
        tvErrorMessage.setVisibility(View.GONE);

        // Validate confirm password
        if (!password.equals(confirmPassword)) {
            showError("Mật khẩu xác nhận không khớp");
            return;
        }

        // Disable button to prevent multiple clicks
        btnRegister.setEnabled(false);
        btnRegister.setText("Đang đăng ký...");

        userRepository.register(username, password, fullName, email, phone,
            new UserRepository.RegisterCallback() {
                @Override
                public void onSuccess(long userId) {
                    mainHandler.post(() -> {
                        // Auto login after successful registration
                        userRepository.login(username, password, new UserRepository.LoginCallback() {
                            @Override
                            public void onSuccess(com.example.btn_android.data.local.entity.User user) {
                                mainHandler.post(() -> {
                                    userPreference.saveLoginStatus(user.getId(), user.getUsername());
                                    showError("Đăng ký thành công!");

                                    // Navigate after successful registration
                                    navigateAfterRegister();
                                });
                            }

                            @Override
                            public void onError(String message) {
                                mainHandler.post(() -> {
                                    // Registration succeeded but auto-login failed
                                    // Just go back to login
                                    showError("Đăng ký thành công! Đang chuyển đến đăng nhập...");
                                    mainHandler.postDelayed(() -> finish(), 1500);
                                });
                            }
                        });
                    });
                }

                @Override
                public void onError(String message) {
                    mainHandler.post(() -> {
                        showError(message);
                        btnRegister.setEnabled(true);
                        btnRegister.setText("Đăng ký");
                    });
                }
            });
    }

    private void navigateAfterRegister() {
        mainHandler.postDelayed(() -> {
            if (returnToActivity != null && !returnToActivity.isEmpty()) {
                try {
                    Class<?> activityClass = Class.forName(returnToActivity);
                    Intent intent = new Intent(RegisterActivity.this, activityClass);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    if (productId != -1) {
                        intent.putExtra(LoginActivity.EXTRA_PRODUCT_ID, productId);
                    }

                    startActivity(intent);
                    finish();
                } catch (ClassNotFoundException e) {
                    finish();
                }
            } else {
                finish();
            }
        }, 1500);
    }

    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }
}

