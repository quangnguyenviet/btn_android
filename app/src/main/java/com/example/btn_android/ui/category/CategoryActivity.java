package com.example.btn_android.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btn_android.R;
import com.example.btn_android.adapter.CategoryAdapter;
import com.example.btn_android.adapter.ProductAdapter;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Category;
import com.example.btn_android.data.local.entity.Product;
import com.example.btn_android.ui.product.ProductDetailActivity;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private TextView tvSelectedCategory;
    private TextView tvEmptyProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setupCategoryRecycler();
        setupProductRecycler();
        loadCategories();
    }

    private void setupCategoryRecycler() {
        RecyclerView rvCategories = findViewById(R.id.rvCategories);
        tvSelectedCategory = findViewById(R.id.tvSelectedCategory);

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(this::onCategorySelected);
        rvCategories.setAdapter(categoryAdapter);
    }

    private void setupProductRecycler() {
        RecyclerView rvProductsByCategory = findViewById(R.id.rvProductsByCategory);
        tvEmptyProducts = findViewById(R.id.tvEmptyProductsByCategory);

        rvProductsByCategory.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductAdapter(product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, product.getId());
            startActivity(intent);
        });
        rvProductsByCategory.setAdapter(productAdapter);
    }

    private void loadCategories() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Category> categories = AppDatabase.getDatabase(this).categoryDao().getAllCategories();
            runOnUiThread(() -> {
                categoryAdapter.submitList(categories);
                if (!categories.isEmpty()) {
                    categoryAdapter.setSelectedPosition(0);
                    onCategorySelected(categories.get(0), 0);
                }
            });
        });
    }

    private void onCategorySelected(Category category, int position) {
        categoryAdapter.setSelectedPosition(position);
        tvSelectedCategory.setText("Sản phẩm: " + category.getName());

        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Product> products = AppDatabase.getDatabase(this)
                    .productDao()
                    .getProductsByCategory(category.getId());
            runOnUiThread(() -> {
                productAdapter.submitList(products);
                tvEmptyProducts.setVisibility(products.isEmpty() ? TextView.VISIBLE : TextView.GONE);
            });
        });
    }
}
