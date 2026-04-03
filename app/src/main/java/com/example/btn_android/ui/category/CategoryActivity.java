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
import com.example.btn_android.data.seed.FruitDataProvider;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;
import com.example.btn_android.ui.home.HomeActivity;
import com.example.btn_android.ui.product.ProductDetailActivity;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private TextView tvCategoryTitle;
    private TextView tvCategoryDescription;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        tvCategoryTitle = findViewById(R.id.tvCategoryScreenTitle);
        tvCategoryDescription = findViewById(R.id.tvCategoryScreenDescription);
        RecyclerView rvCategories = findViewById(R.id.rvAllCategories);
        RecyclerView rvCategoryProducts = findViewById(R.id.rvCategoryProducts);

        categoryAdapter = new CategoryAdapter(this::selectCategory);
        productAdapter = new ProductAdapter(this::openProductDetail);

        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(categoryAdapter);
        rvCategories.setNestedScrollingEnabled(false);

        rvCategoryProducts.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryProducts.setAdapter(productAdapter);
        rvCategoryProducts.setNestedScrollingEnabled(false);

        categoryAdapter.setItems(FruitDataProvider.getCategories());

        Intent intent = getIntent();
        if (intent != null) {
            selectedCategoryId = intent.getIntExtra(HomeActivity.EXTRA_CATEGORY_ID, -1);
        }

        if (selectedCategoryId == -1) {
            List<Category> categories = FruitDataProvider.getCategories();
            if (!categories.isEmpty()) {
                selectedCategoryId = categories.get(0).getId();
            }
        }

        updateCategoryContent();
    }

    private void selectCategory(Category category) {
        selectedCategoryId = category.getId();
        updateCategoryContent();
    }

    private void updateCategoryContent() {
        Category selected = FruitDataProvider.getCategoryById(selectedCategoryId);
        if (selected != null) {
            tvCategoryTitle.setText(selected.getEmoji() + " " + selected.getName());
            tvCategoryDescription.setText(selected.getDescription());
            productAdapter.setItems(FruitDataProvider.getProductsByCategory(selected.getId()));
        } else {
            tvCategoryTitle.setText("Danh mục hoa quả");
            tvCategoryDescription.setText("Chọn một danh mục để xem sản phẩm.");
            productAdapter.setItems(FruitDataProvider.getAllProducts());
        }
    }

    private void openProductDetail(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(HomeActivity.EXTRA_PRODUCT, product);
        startActivity(intent);
    }
}
