package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.btn_android.data.local.entity.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    long insert(Order order);

    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<Order> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE id = :id")
    Order getOrderById(int id);
}
