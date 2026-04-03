package com.example.btn_android.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.btn_android.data.local.entity.OrderDetailEntity;

import java.util.List;

@Dao
public interface OrderDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<OrderDetailEntity> details);

    @Query("DELETE FROM order_details")
    void deleteAll();
}