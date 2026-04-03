package com.example.btn_android.data.seed;

import com.example.btn_android.data.local.dao.CategoryDao;
import com.example.btn_android.data.local.dao.ProductDao;
import com.example.btn_android.data.local.dao.UserDao;
import com.example.btn_android.data.local.entity.Category;
import com.example.btn_android.data.local.entity.Product;
import com.example.btn_android.data.local.entity.User;
import java.util.ArrayList;
import java.util.List;

public class DataSeeder {
    public static void seedData(CategoryDao categoryDao, ProductDao productDao, UserDao userDao) {
        // Sample Users for testing
        seedUsers(userDao);

        // Sample Categories
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Hoa quả nhiệt đới", "Các loại quả từ vùng khí hậu nóng"));
        categories.add(new Category("Hoa quả nhập khẩu", "Hoa quả cao cấp từ nước ngoài"));
        categories.add(new Category("Hoa quả sấy", "Sản phẩm hoa quả đã qua chế biến"));

        for (Category category : categories) {
            long categoryId = categoryDao.insert(category);
            
            // Sample Products for each category
            if (category.getName().equals("Hoa quả nhiệt đới")) {
                productDao.insert(new Product("Xoài Cát Hòa Lộc", 55000, "Xoài ngọt, thơm nồng", "mango_url", (int) categoryId));
                productDao.insert(new Product("Sầu riêng Ri6", 120000, "Sầu riêng cơm vàng hạt lép", "durian_url", (int) categoryId));
            } else if (category.getName().equals("Hoa quả nhập khẩu")) {
                productDao.insert(new Product("Táo Envy", 150000, "Táo giòn, ngọt từ Mỹ", "apple_url", (int) categoryId));
                productDao.insert(new Product("Nho mẫu đơn", 450000, "Nho xanh cao cấp Nhật Bản", "grape_url", (int) categoryId));
            }
        }
    }

    private static void seedUsers(UserDao userDao) {
        // Demo admin account
        User admin = new User("admin", "password", "Quản trị viên", "admin@fruitapp.com", "0901234567");
        userDao.insert(admin);

        // Demo customer accounts
        User customer1 = new User("customer1", "123456", "Nguyễn Văn A", "nguyenvana@gmail.com", "0909123456");
        userDao.insert(customer1);

        User customer2 = new User("customer2", "123456", "Trần Thị B", "tranthib@gmail.com", "0909234567");
        userDao.insert(customer2);
    }
}
