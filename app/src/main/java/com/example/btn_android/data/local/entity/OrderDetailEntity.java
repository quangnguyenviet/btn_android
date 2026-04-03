package com.example.btn_android.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.btn_android.model.Product;

@Entity(tableName = "order_details",
        foreignKeys = {
                @ForeignKey(entity = OrderEntity.class,
                        parentColumns = "id",
                        childColumns = "orderId",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Product.class,
                        parentColumns = "id",
                        childColumns = "productId",
                        onDelete = ForeignKey.RESTRICT)
        },
        indices = {@Index("orderId"), @Index("productId")})
public class OrderDetailEntity {
    @PrimaryKey(autoGenerate = true)
    private final int id;
    private final int orderId;
    private final int productId;
    private final int quantity;
    private final double unitPrice;
    private final double subtotal;

    public OrderDetailEntity(int id, int orderId, int productId, int quantity, double unitPrice, double subtotal) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getSubtotal() {
        return subtotal;
    }
}