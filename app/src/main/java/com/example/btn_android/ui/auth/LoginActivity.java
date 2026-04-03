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
import com.example.btn_android.data.local.entity.User;
import com.example.btn_android.data.repository.UserRepository;
import com.example.btn_android.preference.UserPreference;
import com.example.btn_android.ui.home.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvErrorMessage;
    private UserRepository userRepository;
    private UserPreference userPreference;
    private Handler mainHandler;

    // Constants for returning to previous activity
    public static final String EXTRA_RETURN_TO_ACTIVITY = "return_to_activity";
    public static final String EXTRA_PRODUCT_ID = "product_id";

    private String returnToActivity;
    private int productId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        initData();
        setupListeners();
        handleIntent();
        setupBackPressHandler();
    }

    private void initViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
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
            returnToActivity = intent.getStringExtra(EXTRA_RETURN_TO_ACTIVITY);
            productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1);
        }
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> handleLogin());
        btnRegister.setOnClickListener(v -> openRegisterScreen());
    }

    private void handleLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";

        // Hide previous error
        tvErrorMessage.setVisibility(View.GONE);

        // Disable button to prevent multiple clicks
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang đăng nhập...");

        userRepository.login(username, password, new UserRepository.LoginCallback() {
            @Override
            public void onSuccess(User user) {
                mainHandler.post(() -> {
                    // Save login status
                    userPreference.saveLoginStatus(user.getId(), user.getUsername());

                    // Show success message
                    showError("Đăng nhập thành công!");

                    // Navigate to appropriate screen
                    navigateAfterLogin();
                });
            }

            @Override
            public void onError(String message) {
                mainHandler.post(() -> {
                    showError(message);
                    btnLogin.setEnabled(true);
                    btnLogin.setText("Đăng nhập");
                });
            }
        });
    }

    private void navigateAfterLogin() {
        // Delay to show success message
        mainHandler.postDelayed(() -> {
            if (returnToActivity != null && !returnToActivity.isEmpty()) {
                // Return to the activity that requested login
                try {
                    Class<?> activityClass = Class.forName(returnToActivity);
                    Intent intent = new Intent(LoginActivity.this, activityClass);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    // Pass back product ID if available
                    if (productId != -1) {
                        intent.putExtra(EXTRA_PRODUCT_ID, productId);
                    }

                    startActivity(intent);
                    finish();
                } catch (ClassNotFoundException e) {
                    // If class not found, go to home
                    navigateToHome();
                }
            } else {
                // Default: go to home
                navigateToHome();
            }
        }, 1000);
    }

    private void navigateToHome() {
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openRegisterScreen() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        // Pass the return info to register screen as well
        if (returnToActivity != null) {
            intent.putExtra(EXTRA_RETURN_TO_ACTIVITY, returnToActivity);
            intent.putExtra(EXTRA_PRODUCT_ID, productId);
        }
        startActivity(intent);
    }

    private void showError(String message) {
        tvErrorMessage.setText(message);
        tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void setupBackPressHandler() {
        // Use OnBackPressedDispatcher for Android 13+ compatibility
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // If user came from another activity, go back to home instead
                if (returnToActivity != null && !returnToActivity.isEmpty()) {
                    navigateToHome();
                } else {
                    finish();
                }
            }
        });
    }
}



