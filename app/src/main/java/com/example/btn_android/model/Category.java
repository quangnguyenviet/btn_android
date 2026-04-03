package com.example.btn_android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "categories")
public class Category implements Serializable {
    @PrimaryKey
    private final int id;
    private final String name;
    private final String description;
    private final String emoji;

    public Category(int id, String name, String description, String emoji) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    public int getId() {
        return id;
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
}
