package com.example.btn_android.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btn_android.R;
import com.example.btn_android.adapter.CategoryAdapter;
import com.example.btn_android.adapter.ProductAdapter;
import com.example.btn_android.data.seed.FruitDataProvider;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;
import com.example.btn_android.ui.category.CategoryActivity;
import com.example.btn_android.ui.product.ProductDetailActivity;

public class HomeActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "extra_category_id";
    public static final String EXTRA_PRODUCT = "extra_product";

    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView rvCategories = findViewById(R.id.rvHomeCategories);
        RecyclerView rvProducts = findViewById(R.id.rvHomeProducts);

        categoryAdapter = new CategoryAdapter(this::openCategoryScreen);
        productAdapter = new ProductAdapter(this::openProductDetail);

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);
        rvCategories.setNestedScrollingEnabled(false);

        rvProducts.setLayoutManager(new LinearLayoutManager(this));
        rvProducts.setAdapter(productAdapter);
        rvProducts.setNestedScrollingEnabled(false);

        categoryAdapter.setItems(FruitDataProvider.getCategories());
        productAdapter.setItems(FruitDataProvider.getFeaturedProducts());

        findViewById(R.id.btnGoCategories).setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
        });
    }

    private void openCategoryScreen(Category category) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, category.getId());
        startActivity(intent);
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(EXTRA_PRODUCT, product);
        startActivity(intent);
    }
}
