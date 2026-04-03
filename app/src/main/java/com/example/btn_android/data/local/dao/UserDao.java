package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.btn_android.data.local.entity.User;

import java.util.List;

/**
 * UserDao - Data Access Object cho bảng Users
 * Phụ trách: Dương - Đăng nhập & xác thực
 */
@Dao
public interface UserDao {

    /**
     * Đăng nhập - kiểm tra username và password
     */
    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    /**
     * Tìm user theo username
     */
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User findByUsername(String username);

    /**
     * Tìm user theo ID
     */
    @Query("SELECT * FROM users WHERE user_id = :userId LIMIT 1")
    User findById(int userId);

    /**
     * Tìm user theo email
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User findByEmail(String email);

    /**
     * Lấy tất cả users
     */
    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    /**
     * Thêm user mới (đăng ký)
     */
    @Insert
    long insert(User user);

    /**
     * Cập nhật thông tin user
     */
    @Update
    int update(User user);

    /**
     * Xóa user
     */
    @Delete
    int delete(User user);

    /**
     * Kiểm tra username đã tồn tại chưa
     */
    @Query("SELECT COUNT(*) FROM users WHERE username = :username")
    int checkUsernameExists(String username);

    /**
     * Kiểm tra email đã tồn tại chưa
     */
    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int checkEmailExists(String email);
}

