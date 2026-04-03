package com.example.btn_android.data.repository;

import android.content.Context;
import com.example.btn_android.data.local.dao.UserDao;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.User;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getDatabase(context);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public interface LoginCallback {
        void onSuccess(User user);
        void onError(String message);
    }

    public interface RegisterCallback {
        void onSuccess(long userId);
        void onError(String message);
    }

    public void login(String username, String password, LoginCallback callback) {
        executorService.execute(() -> {
            try {
                if (username == null || username.trim().isEmpty()) {
                    callback.onError("Vui lòng nhập tên đăng nhập");
                    return;
                }
                if (password == null || password.trim().isEmpty()) {
                    callback.onError("Vui lòng nhập mật khẩu");
                    return;
                }

                User user = userDao.login(username.trim(), password);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Tên đăng nhập hoặc mật khẩu không đúng");
                }
            } catch (Exception e) {
                callback.onError("Lỗi đăng nhập: " + e.getMessage());
            }
        });
    }

    public void register(String username, String password, String fullName,
                        String email, String phone, RegisterCallback callback) {
        executorService.execute(() -> {
            try {
                // Validate input
                if (username == null || username.trim().isEmpty()) {
                    callback.onError("Vui lòng nhập tên đăng nhập");
                    return;
                }
                if (password == null || password.trim().isEmpty()) {
                    callback.onError("Vui lòng nhập mật khẩu");
                    return;
                }
                if (password.length() < 6) {
                    callback.onError("Mật khẩu phải có ít nhất 6 ký tự");
                    return;
                }

                // Check if username already exists
                User existingUser = userDao.getUserByUsername(username.trim());
                if (existingUser != null) {
                    callback.onError("Tên đăng nhập đã tồn tại");
                    return;
                }

                // Create new user
                User newUser = new User(
                    username.trim(),
                    password,
                    fullName != null ? fullName.trim() : "",
                    email != null ? email.trim() : "",
                    phone != null ? phone.trim() : ""
                );

                long userId = userDao.insert(newUser);
                if (userId > 0) {
                    callback.onSuccess(userId);
                } else {
                    callback.onError("Không thể tạo tài khoản");
                }
            } catch (Exception e) {
                callback.onError("Lỗi đăng ký: " + e.getMessage());
            }
        });
    }

    public void getUserByUsername(String username, LoginCallback callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByUsername(username);
                if (user != null) {
                    callback.onSuccess(user);
                } else {
                    callback.onError("Không tìm thấy người dùng");
                }
            } catch (Exception e) {
                callback.onError("Lỗi: " + e.getMessage());
            }
        });
    }
}

