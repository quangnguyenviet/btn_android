package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.btn_android.data.local.entity.Product;
import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM products ORDER BY id ASC")
    List<Product> getAllProducts();

    @Query("SELECT * FROM products WHERE featured = 1 ORDER BY id ASC")
    List<Product> getFeaturedProducts();

    @Query("SELECT * FROM products WHERE categoryId = :categoryId ORDER BY id ASC")
    List<Product> getProductsByCategory(int categoryId);

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    Product getProductById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Product product);

    @Query("DELETE FROM products")
    void deleteAll();
}
