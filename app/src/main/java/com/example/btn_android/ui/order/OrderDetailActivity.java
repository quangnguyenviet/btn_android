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
import com.example.btn_android.adapter.InvoiceItemAdapter;
import com.example.btn_android.data.local.entity.Order;
import com.example.btn_android.data.local.entity.OrderDetail;
import com.example.btn_android.data.local.entity.Product;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.repository.OrderRepository;
import com.example.btn_android.ui.home.HomeActivity;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * OrderDetailActivity - Màn hình hiển thị hóa đơn sau khi thanh toán
 * Được phát triển bởi Việt Anh
 */
public class OrderDetailActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "extra_order_id";

    private TextView tvInvoiceNumber;
    private TextView tvOrderDate;
    private TextView tvStatus;
    private RecyclerView rvInvoiceItems;
    private TextView tvSubtotal;
    private TextView tvTax;
    private TextView tvTotal;
    private Button btnContinueShopping;
    private Button btnPrintInvoice;

    private OrderRepository orderRepository;
    private Order order;
    private List<OrderDetail> orderDetails;
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        tvInvoiceNumber = findViewById(R.id.tvInvoiceNumber);
        tvOrderDate = findViewById(R.id.tvOrderDate);
        tvStatus = findViewById(R.id.tvOrderStatus);
        rvInvoiceItems = findViewById(R.id.rvInvoiceItems);
        tvSubtotal = findViewById(R.id.tvInvoiceSubtotal);
        tvTax = findViewById(R.id.tvInvoiceTax);
        tvTotal = findViewById(R.id.tvInvoiceTotal);
        btnContinueShopping = findViewById(R.id.btnContinueShopping);
        btnPrintInvoice = findViewById(R.id.btnPrintInvoice);

        orderRepository = new OrderRepository(this);
        database = AppDatabase.getDatabase(this);
        orderDetails = new ArrayList<>();

        int orderId = getIntent().getIntExtra(EXTRA_ORDER_ID, -1);
        if (orderId == -1) {
            Toast.makeText(this, "Không tìm thấy đơn hàng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadOrderData(orderId);
        setupClickListeners();
    }

    /**
     * Tải dữ liệu đơn hàng
     */
    private void loadOrderData(int orderId) {
        orderRepository.getOrder(orderId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    order = (Order) result;
                    loadOrderDetails(orderId);
                    displayOrderInfo();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }

    /**
     * Tải chi tiết đơn hàng
     */
    private void loadOrderDetails(int orderId) {
        orderRepository.getOrderDetails(orderId, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                runOnUiThread(() -> {
                    orderDetails = (List<OrderDetail>) result;
                    displayInvoiceItems();
                    calculateTotals();
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(OrderDetailActivity.this, error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    /**
     * Hiển thị thông tin đơn hàng
     */
    private void displayOrderInfo() {
        if (order == null) return;

        tvInvoiceNumber.setText("HÓA ĐƠN #" + order.getId());
        tvOrderDate.setText("Ngày: " + order.getOrderDate());
        tvStatus.setText("Trạng thái: " + order.getStatus());
    }

    /**
     * Hiển thị danh sách sản phẩm trong hóa đơn
     */
    private void displayInvoiceItems() {
        InvoiceItemAdapter adapter = new InvoiceItemAdapter(orderDetails);
        rvInvoiceItems.setLayoutManager(new LinearLayoutManager(this));
        rvInvoiceItems.setAdapter(adapter);
    }

    /**
     * Tính toán tổng tiền
     */
    private void calculateTotals() {
        if (order == null) return;

        double subtotal = order.getTotalAmount();
        double tax = subtotal * 0.1; // Giả sử thuế là 10%
        double total = subtotal + tax;

        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        tvSubtotal.setText(nf.format(subtotal));
        tvTax.setText(nf.format(tax));
        tvTotal.setText(nf.format(total));
    }

    /**
     * Setup các nút bấm
     */
    private void setupClickListeners() {
        btnContinueShopping.setOnClickListener(v -> {
            // Quay lại Home
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        btnPrintInvoice.setOnClickListener(v -> {
            Toast.makeText(this, "In hóa đơn (chức năng sẽ được thêm sau)", Toast.LENGTH_SHORT).show();
        });
    }
}
