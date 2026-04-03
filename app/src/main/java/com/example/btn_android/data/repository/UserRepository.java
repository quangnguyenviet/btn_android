package com.example.btn_android.data.repository;

import android.content.Context;

import com.example.btn_android.data.local.dao.UserDao;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * UserRepository - Xử lý trung gian giữa UI và Database cho User
 * Phụ trách: Dương - Đăng nhập & xác thực
 */
public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context);
        userDao = database.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Đăng nhập - kiểm tra username và password
     */
    public Future<User> login(String username, String password) {
        return executorService.submit(() -> userDao.login(username, password));
    }

    /**
     * Đăng ký user mới
     */
    public Future<Long> register(User user) {
        return executorService.submit(() -> userDao.insert(user));
    }

    /**
     * Tìm user theo username
     */
    public Future<User> findByUsername(String username) {
        return executorService.submit(() -> userDao.findByUsername(username));
    }

    /**
     * Tìm user theo ID
     */
    public Future<User> findById(int userId) {
        return executorService.submit(() -> userDao.findById(userId));
    }

    /**
     * Kiểm tra username đã tồn tại chưa
     */
    public Future<Boolean> isUsernameExists(String username) {
        return executorService.submit(() -> userDao.checkUsernameExists(username) > 0);
    }

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    public Future<Boolean> isEmailExists(String email) {
        return executorService.submit(() -> userDao.checkEmailExists(email) > 0);
    }

    /**
     * Lấy tất cả users
     */
    public Future<List<User>> getAllUsers() {
        return executorService.submit(userDao::getAllUsers);
    }

    /**
     * Cập nhật thông tin user
     */
    public Future<Integer> updateUser(User user) {
        return executorService.submit(() -> userDao.update(user));
    }

    /**
     * Xóa user
     */
    public Future<Integer> deleteUser(User user) {
        return executorService.submit(() -> userDao.delete(user));
    }
}

