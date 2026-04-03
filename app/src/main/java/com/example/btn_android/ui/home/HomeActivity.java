package com.example.btn_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btn_android.R;
import com.example.btn_android.adapter.ProductAdapter;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Product;
import com.example.btn_android.ui.category.CategoryActivity;
import com.example.btn_android.ui.product.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupRecyclerView();
        setupActions();
        loadProductsOfTheDay();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvProductsOfDay);
        tvEmpty = findViewById(R.id.tvEmptyProducts);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(this::openProductDetail);
        recyclerView.setAdapter(productAdapter);
    }

    private void setupActions() {
        Button btnViewCategories = findViewById(R.id.btnViewCategories);
        btnViewCategories.setOnClickListener(v ->
                startActivity(new Intent(this, CategoryActivity.class))
        );
    }

    private void loadProductsOfTheDay() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Product> allProducts = AppDatabase.getDatabase(this).productDao().getAllProducts();
            List<Product> productsOfDay = new ArrayList<>();
            int max = Math.min(allProducts.size(), 5);
            for (int i = 0; i < max; i++) {
                productsOfDay.add(allProducts.get(i));
            }

            runOnUiThread(() -> {
                productAdapter.submitList(productsOfDay);
                tvEmpty.setVisibility(productsOfDay.isEmpty() ? TextView.VISIBLE : TextView.GONE);
            });
        });
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
        startActivity(intent);
    }
}
