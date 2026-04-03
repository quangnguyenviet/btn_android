package com.example.btn_android.ui.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btn_android.R;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Product;
import com.example.btn_android.data.repository.OrderRepository;
import com.example.btn_android.ui.order.OrderActivity;
import com.example.btn_android.utils.AuthHelper;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private TextView tvName;
    private TextView tvPrice;
    private TextView tvDescription;
    private TextView tvCategoryId;
    private Button btnAddToCart;

    private Product currentProduct;
    private int productId;
    private OrderRepository orderRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvCategoryId = findViewById(R.id.tvDetailCategory);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        orderRepository = new OrderRepository(this);

        productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
        if (productId == -1) {
            finish();
            return;
        }

        loadProductDetail(productId);
        setupAddToCartButton();
    }

    private void setupAddToCartButton() {
        btnAddToCart.setOnClickListener(v -> {
            // Check if user is logged in before adding to cart
            if (!AuthHelper.isLoggedIn(this)) {
                // Redirect to login and pass this activity info to return after login
                Toast.makeText(this, "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                AuthHelper.redirectToLogin(this, ProductDetailActivity.class, productId);
            } else {
                // User is logged in, proceed to add to cart
                addToCart();
            }
        });
    }

    private void addToCart() {
        int userId = AuthHelper.getCurrentUserId(this);
        if (userId == -1) {
            Toast.makeText(this, "Lỗi: Không thể xác định người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thêm sản phẩm vào giỏ với số lượng = 1
        orderRepository.addProductToCart(userId, productId, 1, new OrderRepository.OrderCallback() {
            @Override
            public void onSuccess(Object result) {
                int orderId = (int) result;
                Toast.makeText(ProductDetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                
                // Chuyển đến OrderActivity để hiển thị giỏ hàng
                Intent intent = new Intent(ProductDetailActivity.this, OrderActivity.class);
                intent.putExtra(OrderActivity.EXTRA_ORDER_ID, orderId);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // If user came back from login, check if they're logged in now
        // and maybe show a welcome message or automatically add to cart
    }

    private void loadProductDetail(int productId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Product product = AppDatabase.getDatabase(this).productDao().getProductById(productId);

            runOnUiThread(() -> {
                if (product == null) {
                    finish();
                    return;
                }

                currentProduct = product;
                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
                tvName.setText(product.getName());
                tvPrice.setText(currencyFormatter.format(product.getPrice()));
                tvDescription.setText(product.getDescription());
                tvCategoryId.setText("Mã danh mục: " + product.getCategoryId());
            });
        });
    }
}
