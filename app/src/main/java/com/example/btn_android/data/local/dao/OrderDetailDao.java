package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.btn_android.data.local.entity.OrderDetail;
import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert
    void insertAll(List<OrderDetail> orderDetails);

    @Insert
    long insert(OrderDetail orderDetail);

    @Update
    void update(OrderDetail orderDetail);

    @Delete
    void delete(OrderDetail orderDetail);

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    List<OrderDetail> getOrderDetailsByOrder(int orderId);
}
