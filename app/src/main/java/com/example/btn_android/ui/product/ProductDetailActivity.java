package com.example.btn_android.ui.product;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btn_android.R;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Product;

import java.text.NumberFormat;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "extra_product_id";

    private TextView tvName;
    private TextView tvPrice;
    private TextView tvDescription;
    private TextView tvCategoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvName = findViewById(R.id.tvDetailName);
        tvPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDetailDescription);
        tvCategoryId = findViewById(R.id.tvDetailCategory);

        int productId = getIntent().getIntExtra(EXTRA_PRODUCT_ID, -1);
        if (productId == -1) {
            finish();
            return;
        }

        loadProductDetail(productId);
    }

    private void loadProductDetail(int productId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            Product product = AppDatabase.getDatabase(this).productDao().getProductById(productId);

            runOnUiThread(() -> {
                if (product == null) {
                    finish();
                    return;
                }

                NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("vi-VN"));
                tvName.setText(product.getName());
                tvPrice.setText(currencyFormatter.format(product.getPrice()));
                tvDescription.setText(product.getDescription());
                tvCategoryId.setText("Mã danh mục: " + product.getCategoryId());
            });
        });
    }
}
