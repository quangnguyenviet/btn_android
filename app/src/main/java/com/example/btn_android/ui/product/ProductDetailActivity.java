package com.example.btn_android.ui.product;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.btn_android.R;
import com.example.btn_android.data.seed.FruitDataProvider;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;
import com.example.btn_android.ui.home.HomeActivity;

import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        TextView tvEmoji = findViewById(R.id.tvDetailEmoji);
        TextView tvName = findViewById(R.id.tvDetailName);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvPrice = findViewById(R.id.tvDetailPrice);
        TextView tvStock = findViewById(R.id.tvDetailStock);
        TextView tvDescription = findViewById(R.id.tvDetailDescription);
        Button btnAction = findViewById(R.id.btnDetailAction);

        Product product = (Product) getIntent().getSerializableExtra(HomeActivity.EXTRA_PRODUCT);
        if (product == null) {
            Toast.makeText(this, "Không tìm thấy thông tin sản phẩm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Category category = FruitDataProvider.getCategoryById(product.getCategoryId());

        tvEmoji.setText(product.getEmoji());
        tvName.setText(product.getName());
        tvCategory.setText(category != null ? category.getName() : "Danh mục khác");
        tvPrice.setText(String.format(Locale.getDefault(), "%s đ / %s", String.format(Locale.getDefault(), "%,d", product.getPrice()), product.getUnit()));
        tvStock.setText(String.format(Locale.getDefault(), "Tồn kho: %d", product.getStock()));
        tvDescription.setText(product.getDescription());

        btnAction.setOnClickListener(v -> Toast.makeText(this, "Đã thêm " + product.getName() + " vào giỏ", Toast.LENGTH_SHORT).show());
    }
}
