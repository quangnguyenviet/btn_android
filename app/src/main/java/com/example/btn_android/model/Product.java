package com.example.btn_android.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "products",
        foreignKeys = @ForeignKey(entity = Category.class,
                parentColumns = "id",
                childColumns = "categoryId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("categoryId")})
public class Product implements Serializable {
    @PrimaryKey
    private final int id;
    private final int categoryId;
    private final String name;
    private final String description;
    private final String emoji;
    private final int price;
    private final String unit;
    private final int stock;
    private final boolean featured;

    public Product(int id, int categoryId, String name, String description, String emoji, int price, String unit, int stock, boolean featured) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
        this.price = price;
        this.unit = unit;
        this.stock = stock;
        this.featured = featured;
    }

    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getEmoji() {
        return emoji;
    }

    public int getPrice() {
        return price;
    }

    public String getUnit() {
        return unit;
    }

    public int getStock() {
        return stock;
    }

    public boolean isFeatured() {
        return featured;
    }
}
