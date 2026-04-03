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
        categories.add(new Category("Hoa quả nhiệt đới", "Các loại quả từ vùng khí hậu nóng ẩm"));
        categories.add(new Category("Hoa quả nhập khẩu", "Hoa quả cao cấp từ nước ngoài"));
        categories.add(new Category("Hoa quả sấy", "Sản phẩm hoa quả đã qua chế biến"));
        categories.add(new Category("Trái cây việt nam", "Đặc sản từng vùng miền Việt Nam"));
        categories.add(new Category("Berry & Cherry", "Các loại quả họ dâu & cherry"));

        for (Category category : categories) {
            long categoryId = categoryDao.insert(category);
            
            // Sample Products for each category
            if (category.getName().equals("Hoa quả nhiệt đới")) {
                productDao.insert(new Product("Xoài Cát Hòa Lộc", 55000, "Xoài ngọt đậm, thơm nồng đặc trưng vùng Tiền Giang", "mango_url", (int) categoryId));
                productDao.insert(new Product("Sầu riêng Ri6", 120000, "Sầu riêng cơm vàng hạt lép, múi dày béo ngậy", "durian_url", (int) categoryId));
                productDao.insert(new Product("Dừa xiêm xanh", 25000, "Dừa ngọt mát, nước trong veo từ Bến Tre", "coconut_url", (int) categoryId));
                productDao.insert(new Product("Thanh long ruột đỏ", 35000, "Thanh long Bình Thuận, ngọt tự nhiên", "dragon_url", (int) categoryId));
                productDao.insert(new Product("Măng cụt", 85000, "Quả măng cụt múi trắng mọng nước, vị ngọt thanh", "mangosteen_url", (int) categoryId));
            } else if (category.getName().equals("Hoa quả nhập khẩu")) {
                productDao.insert(new Product("Táo Envy New Zealand", 150000, "Táo giòn, ngọt dịu, vỏ đỏ bóng đẹp", "apple_url", (int) categoryId));
                productDao.insert(new Product("Nho mẫu đơn Nhật", 450000, "Nho xanh cao cấp, quả to múi ngọt lịm", "grape_url", (int) categoryId));
                productDao.insert(new Product("Lê Nam Phi", 120000, "Lê mọng nước, thơm ngọt", "pear_url", (int) categoryId));
                productDao.insert(new Product("Cam Úc", 95000, "Cam tươi vị ngọt chua cân bằng", "orange_url", (int) categoryId));
                productDao.insert(new Product("Kiwi vàng", 180000, "Kiwi vàng New Zealand, giàu vitamin C", "kiwi_url", (int) categoryId));
            } else if (category.getName().equals("Hoa quả sấy")) {
                productDao.insert(new Product("Xoài sấy dẻo", 65000, "Xoài sấy giữ nguyên vị ngọt tự nhiên", "dried_mango_url", (int) categoryId));
                productDao.insert(new Product("Chuối sấy", 45000, "Chuối sấy giòn tan, không dầu mỡ", "dried_banana_url", (int) categoryId));
                productDao.insert(new Product("Mít sấy giòn", 55000, "Mít sấy giữ nguyên hương thơm đặc trưng", "dried_jackfruit_url", (int) categoryId));
                productDao.insert(new Product("Mix hoa quả sấy", 120000, "Hỗn hợp nhiều loại quả sấy cao cấp", "mixed_dried_url", (int) categoryId));
            } else if (category.getName().equals("Trái cây việt nam")) {
                productDao.insert(new Product("Chôm chôm", 45000, "Chôm chôm tươi ngon từ miền Tây", "rambutan_url", (int) categoryId));
                productDao.insert(new Product("Vải thiều Bắc Giang", 95000, "Vải thiều đặc sản mùa hè", "lychee_url", (int) categoryId));
                productDao.insert(new Product("Bưởi da xanh", 70000, "Bưởi Bến Tre múi hồng, vị ngọt thanh", "pomelo_url", (int) categoryId));
                productDao.insert(new Product("Mận Mộc Châu", 85000, "Mận tươi từ cao nguyên Mộc Châu", "plum_url", (int) categoryId));
            } else if (category.getName().equals("Berry & Cherry")) {
                productDao.insert(new Product("Dâu tây Đà Lạt", 180000, "Dâu tây tươi ngon từ Đà Lạt", "strawberry_url", (int) categoryId));
                productDao.insert(new Product("Cherry Úc", 380000, "Cherry đỏ tươi nhập khẩu Úc", "cherry_url", (int) categoryId));
                productDao.insert(new Product("Blueberry Mỹ", 290000, "Việt quất nhập khẩu giàu chất chống oxy hóa", "blueberry_url", (int) categoryId));
                productDao.insert(new Product("Raspberry", 320000, "Mâm xôi tươi cao cấp", "raspberry_url", (int) categoryId));
            }
        }
    }

    private static void seedUsers(UserDao userDao) {
        // Demo admin account
        User admin = new User("admin", "admin123", "Quản trị viên", "admin@fruitapp.com", "0901234567");
        userDao.insert(admin);

        // Demo customer accounts
        User customer1 = new User("customer1", "123456", "Nguyễn Văn A", "nguyenvana@gmail.com", "0909123456");
        userDao.insert(customer1);

        User customer2 = new User("customer2", "123456", "Trần Thị B", "tranthib@gmail.com", "0909234567");
        userDao.insert(customer2);

        User customer3 = new User("demo", "demo", "Nguyễn Minh Demo", "demo@fruitapp.com", "0912345678");
        userDao.insert(customer3);
    }
}
