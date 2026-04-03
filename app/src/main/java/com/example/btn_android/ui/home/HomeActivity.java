package com.example.btn_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btn_android.R;
import com.example.btn_android.adapter.ProductAdapter;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Product;
import com.example.btn_android.ui.auth.LoginActivity;
import com.example.btn_android.ui.category.CategoryActivity;
import com.example.btn_android.ui.order.OrderActivity;
import com.example.btn_android.ui.product.ProductDetailActivity;
import com.example.btn_android.ui.profile.ProfileActivity;
import com.example.btn_android.utils.AuthHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ProductAdapter productAdapter;
    private TextView tvEmpty;
    private FloatingActionButton fabCart;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupToolbar();
        setupRecyclerView();
        setupActions();
        loadProductsOfTheDay();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("🍎 Fruit Shop");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_profile) {
            if (AuthHelper.isLoggedIn(this)) {
                startActivity(new Intent(this, ProfileActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        } else if (id == R.id.menu_cart) {
            if (AuthHelper.isLoggedIn(this)) {
                startActivity(new Intent(this, OrderActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rvProductsOfDay);
        tvEmpty = findViewById(R.id.tvEmptyProducts);

        // Use GridLayoutManager for better product display
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        productAdapter = new ProductAdapter(this::openProductDetail);
        recyclerView.setAdapter(productAdapter);
    }

    private void setupActions() {
        Button btnViewCategories = findViewById(R.id.btnViewCategories);
        btnViewCategories.setOnClickListener(v ->
                startActivity(new Intent(this, CategoryActivity.class))
        );

        fabCart = findViewById(R.id.fabCart);
        fabCart.setOnClickListener(v -> {
            if (AuthHelper.isLoggedIn(this)) {
                startActivity(new Intent(this, OrderActivity.class));
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
        });
    }

    private void loadProductsOfTheDay() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            List<Product> allProducts = AppDatabase.getDatabase(this).productDao().getAllProducts();
            List<Product> productsOfDay = new ArrayList<>();
            int max = Math.min(allProducts.size(), 10);
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

    @Override
    protected void onResume() {
        super.onResume();
        // Reload products when returning to home
        loadProductsOfTheDay();
    }
}
