package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.btn_android.data.local.entity.OrderEntity;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(OrderEntity order);

    @Query("SELECT * FROM orders WHERE userId = :userId")
    List<OrderEntity> getOrdersByUser(int userId);

    @Query("SELECT * FROM orders WHERE id = :id")
    OrderEntity getOrderById(int id);

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    void updateStatus(int orderId, String status);

    @Query("DELETE FROM orders")
    void deleteAll();
}
