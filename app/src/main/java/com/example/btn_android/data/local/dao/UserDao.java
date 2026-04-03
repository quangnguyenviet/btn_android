package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.btn_android.data.local.entity.UserEntity;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    UserEntity getByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    UserEntity login(String username, String password);

    @Query("DELETE FROM users")
    void deleteAll();
}
