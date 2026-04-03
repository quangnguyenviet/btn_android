package com.example.btn_android.data.repository;

import android.content.Context;

import com.example.btn_android.data.local.dao.CategoryDao;
import com.example.btn_android.data.local.dao.OrderDao;
import com.example.btn_android.data.local.dao.OrderDetailDao;
import com.example.btn_android.data.local.dao.ProductDao;
import com.example.btn_android.data.local.dao.UserDao;
import com.example.btn_android.data.local.database.FruitDatabase;
import com.example.btn_android.data.local.entity.OrderDetailEntity;
import com.example.btn_android.data.local.entity.OrderEntity;
import com.example.btn_android.data.local.entity.UserEntity;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;

import java.util.List;

public final class FruitRepository {
    private static volatile FruitRepository instance;

    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final ProductDao productDao;
    private final OrderDao orderDao;
    private final OrderDetailDao orderDetailDao;

    private FruitRepository(Context context) {
        FruitDatabase database = FruitDatabase.getInstance(context);
        userDao = database.userDao();
        categoryDao = database.categoryDao();
        productDao = database.productDao();
        orderDao = database.orderDao();
        orderDetailDao = database.orderDetailDao();
    }

    public static FruitRepository getInstance(Context context) {
        if (instance == null) {
            synchronized (FruitRepository.class) {
                if (instance == null) {
                    instance = new FruitRepository(context);
                }
            }
        }
        return instance;
    }

    public List<Category> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    public Category getCategoryById(int categoryId) {
        return categoryDao.getCategoryById(categoryId);
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public List<Product> getFeaturedProducts() {
        return productDao.getFeaturedProducts();
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDao.getProductsByCategory(categoryId);
    }

    public Product getProductById(int productId) {
        return productDao.getProductById(productId);
    }

    public long insertUser(UserEntity user) {
        return userDao.insert(user);
    }

    public UserEntity login(String username, String password) {
        return userDao.login(username, password);
    }

    public long createOrder(OrderEntity order) {
        return orderDao.insert(order);
    }

    public void createOrderDetails(List<OrderDetailEntity> details) {
        orderDetailDao.insertAll(details);
    }

    public void updateOrderStatus(int orderId, String status) {
        orderDao.updateStatus(orderId, status);
    }
}