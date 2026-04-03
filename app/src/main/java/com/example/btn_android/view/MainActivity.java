package com.example.btn_android.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btn_android.R;
import com.example.btn_android.data.seed.UserSeed;
import com.example.btn_android.ui.auth.LoginActivity;
import com.example.btn_android.utils.AuthHelper;

public class MainActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private Button btnLogin, btnLogout, btnTestLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Seed dữ liệu mẫu cho Users (chỉ chạy lần đầu)
        UserSeed.seedUsers(this);

        initViews();
        updateUI();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tv_welcome);
        btnLogin = findViewById(R.id.btn_login);
        btnLogout = findViewById(R.id.btn_logout);
        btnTestLogin = findViewById(R.id.btn_test_login);

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            AuthHelper.logout(this);
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
            updateUI();
        });

        btnTestLogin.setOnClickListener(v -> {
            // Test chức năng kiểm tra đăng nhập
            if (AuthHelper.requireLogin(this)) {
                Toast.makeText(this, "Bạn đã đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (AuthHelper.isUserLoggedIn(this)) {
            String fullName = AuthHelper.getLoggedInFullName(this);
            tvWelcome.setText("Xin chào, " + fullName + "!");
            btnLogin.setEnabled(false);
            btnLogout.setEnabled(true);
        } else {
            tvWelcome.setText("Bạn chưa đăng nhập");
            btnLogin.setEnabled(true);
            btnLogout.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI(); // Cập nhật UI khi quay lại t�� LoginActivity
    }
}