package com.example.btn_android.data.seed;

import android.content.Context;

import com.example.btn_android.data.local.entity.User;
import com.example.btn_android.data.repository.UserRepository;

import java.util.concurrent.ExecutionException;

/**
 * UserSeed - Dữ liệu mẫu cho bảng Users
 * Phụ trách: Dương - Đăng nhập & xác thực
 *
 * Tạo sẵn một vài user để test nhanh chức năng đăng nhập
 */
public class UserSeed {

    /**
     * Thêm dữ liệu mẫu cho Users
     *
     * Gọi hàm này trong MainActivity hoặc Application class khi khởi động app lần đầu
     */
    public static void seedUsers(Context context) {
        UserRepository userRepository = new UserRepository(context);

        new Thread(() -> {
            try {
                // Kiểm tra đã có user chưa
                int userCount = userRepository.getAllUsers().get().size();
                if (userCount > 0) {
                    // Đã có dữ liệu, không cần seed nữa
                    return;
                }

                // Tạo user mẫu 1 - Admin
                User admin = new User(
                        "admin",
                        "admin123",
                        "Quản Trị Viên",
                        "admin@fruitapp.com",
                        "0123456789",
                        "123 Đường ABC, Quận 1, TP.HCM"
                );
                userRepository.register(admin).get();

                // Tạo user mẫu 2 - Test User
                User testUser = new User(
                        "user1",
                        "123456",
                        "Nguyễn Văn A",
                        "nguyenvana@gmail.com",
                        "0987654321",
                        "456 Đường XYZ, Quận 3, TP.HCM"
                );
                userRepository.register(testUser).get();

                // Tạo user mẫu 3 - Dương (để test)
                User duong = new User(
                        "duong",
                        "123456",
                        "Dương Test",
                        "duong@test.com",
                        "0901234567",
                        "789 Test Street"
                );
                userRepository.register(duong).get();

                System.out.println("✅ Seed Users thành công!");
                System.out.println("📝 Có thể đăng nhập với:");
                System.out.println("   - username: admin, password: admin123");
                System.out.println("   - username: user1, password: 123456");
                System.out.println("   - username: duong, password: 123456");

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
                System.out.println("❌ Lỗi khi seed Users: " + e.getMessage());
            }
        }).start();
    }
}

