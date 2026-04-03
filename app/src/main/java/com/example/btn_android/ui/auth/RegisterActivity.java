package com.example.btn_android.ui.auth;

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

import java.util.concurrent.ExecutionException;

/**
 * RegisterActivity - Màn hình đăng ký
 * Phụ trách: Dương - Đăng nhập & xác thực
 *
 * Chức năng:
 * - Đăng ký tài khoản mới
 * - Validate thông tin đăng ký
 * - Thêm user mới vào database
 */
public class RegisterActivity extends AppCompatActivity {

    // UI Components
    private EditText etUsername, etPassword, etConfirmPassword, etFullName, etEmail, etPhone, etAddress;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;

    // Data
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initData();
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etFullName = findViewById(R.id.et_full_name);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initData() {
        userRepository = new UserRepository(this);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> handleRegister());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String fullName = etFullName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validate
        if (!validateInput(username, password, confirmPassword, fullName, email)) {
            return;
        }

        showLoading(true);

        // Kiểm tra username đã tồn tại chưa
        new Thread(() -> {
            try {
                boolean usernameExists = userRepository.isUsernameExists(username).get();
                boolean emailExists = userRepository.isEmailExists(email).get();

                runOnUiThread(() -> {
                    if (usernameExists) {
                        showLoading(false);
                        etUsername.setError("Tên đăng nhập đã tồn tại");
                        etUsername.requestFocus();
                    } else if (emailExists) {
                        showLoading(false);
                        etEmail.setError("Email đã được sử dụng");
                        etEmail.requestFocus();
                    } else {
                        // Tạo user mới
                        registerNewUser(username, password, fullName, email, phone, address);
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(RegisterActivity.this,
                            "Lỗi hệ thống: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void registerNewUser(String username, String password, String fullName,
                                  String email, String phone, String address) {
        new Thread(() -> {
            try {
                User newUser = new User(username, password, fullName, email, phone, address);
                long userId = userRepository.register(newUser).get();

                runOnUiThread(() -> {
                    showLoading(false);
                    if (userId > 0) {
                        Toast.makeText(RegisterActivity.this,
                                "Đăng ký thành công! Vui lòng đăng nhập",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Đăng ký thất bại",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(RegisterActivity.this,
                            "Lỗi hệ thống: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private boolean validateInput(String username, String password, String confirmPassword,
                                   String fullName, String email) {
        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Vui lòng nhập tên đăng nhập");
            etUsername.requestFocus();
            return false;
        }

        if (username.length() < 4) {
            etUsername.setError("Tên đăng nhập phải có ít nhất 4 ký tự");
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

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            etConfirmPassword.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Vui lòng nhập họ tên");
            etFullName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Vui lòng nhập email");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Email không hợp lệ");
            etEmail.requestFocus();
            return false;
        }

        return true;
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            btnRegister.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnRegister.setEnabled(true);
        }
    }
}

