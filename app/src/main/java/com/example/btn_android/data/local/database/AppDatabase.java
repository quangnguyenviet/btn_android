package com.example.btn_android.data.local.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.btn_android.data.local.dao.UserDao;
import com.example.btn_android.data.local.entity.User;

/**
 * AppDatabase - Room Database chính của ứng dụng
 * Phụ trách: Minh - Trưởng nhóm & Quản lý dữ liệu
 *
 * Note: File này sẽ được Minh mở rộng để thêm các entity khác:
 * - Categories, Products, Orders, OrderDetails
 */
@Database(entities = {User.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "fruit_app.db";
    private static volatile AppDatabase instance;

    // DAO cho User - Phần của Dương
    public abstract UserDao userDao();

    // TODO: Minh sẽ thêm các DAO khác ở đây:
    // public abstract CategoryDao categoryDao();
    // public abstract ProductDao productDao();
    // public abstract OrderDao orderDao();
    // public abstract OrderDetailDao orderDetailDao();

    /**
     * Singleton pattern để đảm bảo chỉ có 1 instance của database
     */
    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return instance;
    }
}

