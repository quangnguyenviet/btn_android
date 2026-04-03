package com.example.btn_android.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btn_android.R;
import com.example.btn_android.adapter.OrderHistoryAdapter;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Order;
import com.example.btn_android.data.local.entity.User;
import com.example.btn_android.ui.auth.LoginActivity;
import com.example.btn_android.ui.home.HomeActivity;
import com.example.btn_android.ui.order.OrderDetailActivity;
import com.example.btn_android.utils.AuthHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * ProfileActivity - Màn hình profile người dùng với order history và logout
 */
public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsername;
    private TextView tvFullName;
    private TextView tvEmail;
    private TextView tvPhone;
    private RecyclerView rvOrderHistory;
    private Button btnLogout;
    private Button btnBackToHome;
    private TextView tvEmptyOrders;
    
    private AppDatabase database;
    private OrderHistoryAdapter orderHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Check if logged in
        if (!AuthHelper.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        initViews();
        database = AppDatabase.getDatabase(this);
        loadUserProfile();
        loadOrderHistory();
        setupClickListeners();
    }

    private void initViews() {
        tvUsername = findViewById(R.id.tvProfileUsername);
        tvFullName = findViewById(R.id.tvProfileFullName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPhone = findViewById(R.id.tvProfilePhone);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        btnLogout = findViewById(R.id.btnLogout);
        btnBackToHome = findViewById(R.id.btnBackToHome);
        tvEmptyOrders = findViewById(R.id.tvEmptyOrders);
    }

    private void loadUserProfile() {
        int userId = AuthHelper.getCurrentUserId(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            User user = database.userDao().getUserById(userId);
            runOnUiThread(() -> {
                if (user != null) {
                    tvUsername.setText("@" + user.getUsername());
                    tvFullName.setText(user.getFullName());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                }
            });
        });
    }

    private void loadOrderHistory() {
        int userId = AuthHelper.getCurrentUserId(this);
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Order> orders = database.orderDao().getOrdersByUser(userId);
            // Only show paid orders
            List<Order> paidOrders = new ArrayList<>();
            for (Order order : orders) {
                if ("Paid".equals(order.getStatus())) {
                    paidOrders.add(order);
                }
            }
            
            runOnUiThread(() -> {
                if (paidOrders.isEmpty()) {
                    tvEmptyOrders.setVisibility(TextView.VISIBLE);
                    rvOrderHistory.setVisibility(RecyclerView.GONE);
                } else {
                    tvEmptyOrders.setVisibility(TextView.GONE);
                    rvOrderHistory.setVisibility(RecyclerView.VISIBLE);
                    setupOrderHistoryRecycler(paidOrders);
                }
            });
        });
    }

    private void setupOrderHistoryRecycler(List<Order> orders) {
        orderHistoryAdapter = new OrderHistoryAdapter(orders, this::viewOrderDetail);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        rvOrderHistory.setAdapter(orderHistoryAdapter);
    }

    private void viewOrderDetail(Order order) {
        Intent intent = new Intent(this, OrderDetailActivity.class);
        intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, order.getId());
        startActivity(intent);
    }

    private void setupClickListeners() {
        btnLogout.setOnClickListener(v -> showLogoutDialog());
        btnBackToHome.setOnClickListener(v -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đăng xuất")
            .setMessage("Bạn có chắc muốn đăng xuất?")
            .setPositiveButton("Đăng xuất", (dialog, which) -> {
                AuthHelper.logout(this);
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }
}

