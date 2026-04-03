package com.example.btn_android.data.local.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.btn_android.data.local.dao.CategoryDao;
import com.example.btn_android.data.local.dao.OrderDao;
import com.example.btn_android.data.local.dao.OrderDetailDao;
import com.example.btn_android.data.local.dao.ProductDao;
import com.example.btn_android.data.local.dao.UserDao;
import com.example.btn_android.data.local.entity.OrderDetailEntity;
import com.example.btn_android.data.local.entity.OrderEntity;
import com.example.btn_android.data.local.entity.UserEntity;
import com.example.btn_android.data.seed.FruitDataProvider;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;

@Database(
		entities = {UserEntity.class, Category.class, Product.class, OrderEntity.class, OrderDetailEntity.class},
		version = 1,
		exportSchema = false
)
public abstract class FruitDatabase extends RoomDatabase {
	private static volatile FruitDatabase instance;

	public abstract UserDao userDao();

	public abstract CategoryDao categoryDao();

	public abstract ProductDao productDao();

	public abstract OrderDao orderDao();

	public abstract OrderDetailDao orderDetailDao();

	public static FruitDatabase getInstance(Context context) {
		if (instance == null) {
			synchronized (FruitDatabase.class) {
				if (instance == null) {
					instance = Room.databaseBuilder(
									context.getApplicationContext(),
									FruitDatabase.class,
									"fruit_app.db")
							.allowMainThreadQueries()
							.fallbackToDestructiveMigration()
							.build();
					seedDatabase();
				}
			}
		}
		return instance;
	}

	private static void seedDatabase() {
		FruitDatabase database = instance;
		if (database == null) {
			return;
		}

		if (!database.categoryDao().getAllCategories().isEmpty()) {
			return;
		}

		database.userDao().insert(new UserEntity(0, "admin", "123456", "Quản trị Fruit App", "0900000000"));
		database.categoryDao().insertAll(FruitDataProvider.getSeedCategories());
		database.productDao().insertAll(FruitDataProvider.getSeedProducts());
	}
}