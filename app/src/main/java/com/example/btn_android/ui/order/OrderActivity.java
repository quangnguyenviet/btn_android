package com.example.btn_android.ui.order;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btn_android.R;
import com.example.btn_android.adapter.OrderItemAdapter;
import com.example.btn_android.data.local.entity.Order;
import com.example.btn_android.data.local.entity.OrderDetail;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.repository.OrderRepository;
import com.example.btn_android.utils.AuthHelper;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * OrderActivity - Màn hình giỏ hàng và thanh toán
 * Được phát triển bởi Việt Anh
 */
public class OrderActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "extra_order_id";

    private TextView tvOrderId;
    private TextView tvOrderDate;
    private TextView tvTotalAmount;
    private RecyclerView rvOrderItems;
    private Button btnCheckout;
    private Button btnContinueShopping;

    private OrderRepository orderRepository;
    private Order currentOrder;
    private List<OrderDetail> orderDetails;
    private AppDatabase database;
    private OrderItemAdapter adapter;  // Reuse adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tvOrderId = findViewById(R.id.tvOrderId);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);

        orderRepository = new OrderRepository(this);
        database = AppDatabase.getDatabase(this);
        orderDetails = new ArrayList<>();

        // Lấy OrderID từ intent hoặc từ user hiện tại
        int orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        if (orderId != -1) {
            loadOrder(orderId);
        } else {
            getCurrentUserOrder();
        }

        setupClickListeners();
    }

    /**
     * Lấy Order hiện tại của user
     */
    private void getCurrentUserOrder() {
        int userId = AuthHelper.getCurrentUserId(this);
        if (userId == -1) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        orderRepository.getCurrentOrder(userId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    currentOrder = (Order) result;
                    loadOrderDetails(currentOrder.getId());
                    displayOrderInfo();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    /**
     * Tải Order từ ID
     */
    private void loadOrder(int orderId) {
        orderRepository.getOrder(orderId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    currentOrder = (Order) result;
                    loadOrderDetails(orderId);
                    displayOrderInfo();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    /**
     * Tải chi tiết Order
     */
    private void loadOrderDetails(int orderId) {
        orderRepository.getOrderDetails(orderId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    orderDetails = (List<OrderDetail>) result;
                    displayOrderItems();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Reload toàn bộ Order data (sau khi update/delete)
     */
    private void reloadOrderData() {
        if (currentOrder == null) return;

        int orderId = currentOrder.getId();

        // Reload Order để có totalAmount mới
        orderRepository.getOrder(orderId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    currentOrder = (Order) result;
                    displayOrderInfo();

                    // Reload OrderDetails
                    orderRepository.getOrderDetails(orderId, new OrderRepository.OrderCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            runOnUiThread(() -> {
                                orderDetails.clear();
                                orderDetails.addAll((List<OrderDetail>) result);

                                if (orderDetails.isEmpty()) {
                                    Toast.makeText(OrderActivity.this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
                                    finish();
                                    return;
                                }

                                // Update adapter nếu đã tồn tại
                                if (adapter != null) {
                                    adapter.notifyDataSetChanged();
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Hiển thị thông tin Order
     */
    private void displayOrderInfo() {
        if (currentOrder == null) return;

        tvOrderId.setText("Đơn hàng #" + currentOrder.getId());
        tvOrderDate.setText("Ngày: " + currentOrder.getOrderDate());

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        tvTotalAmount.setText(nf.format(currentOrder.getTotalAmount()));
    }

    /**
     * Hiển thị danh sách sản phẩm trong Order
     */
    private void displayOrderItems() {
        if (orderDetails.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Chỉ tạo adapter một lần, sau đó reuse
        if (adapter == null) {
            adapter = new OrderItemAdapter(orderDetails, new OrderItemAdapter.OnItemActionListener() {
                @Override
                public void onRemove(int orderId, int productId) {
                    orderRepository.removeProductFromCart(orderId, productId, new OrderRepository.OrderCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            runOnUiThread(() -> {
                                Toast.makeText(OrderActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                reloadOrderData();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }

                @Override
                public void onQuantityChanged(int orderId, int productId, int newQuantity) {
                    orderRepository.updateProductQuantity(orderId, productId, newQuantity, new OrderRepository.OrderCallback() {
                        @Override
                        public void onSuccess(Object result) {
                            runOnUiThread(() -> {
                                reloadOrderData();
                            });
                        }

                        @Override
                        public void onError(String error) {
                            runOnUiThread(() -> {
                                Toast.makeText(OrderActivity.this, error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                }
            });

            rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
            rvOrderItems.setAdapter(adapter);
        } else {
            // Adapter đã tồn tại, chỉ notify change
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Setup các nút bấm
     */
    private void setupClickListeners() {
        btnCheckout.setOnClickListener(v -> checkout());
        btnContinueShopping.setOnClickListener(v -> finish());
    }

    /**
     * Thanh toán đơn hàng
     */
    private void checkout() {
        if (currentOrder == null || orderDetails.isEmpty()) {
            Toast.makeText(this, "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
            return;
        }

        orderRepository.checkoutOrder(currentOrder.getId(), new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển đến màn hình hóa đơn chi tiết
                    Intent intent = new Intent(OrderActivity.this, OrderDetailActivity.class);
                    intent.putExtra(OrderDetailActivity.EXTRA_ORDER_ID, currentOrder.getId());
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderActivity.this, "Lỗi thanh toán: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
