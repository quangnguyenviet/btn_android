package com.example.btn_android.data.seed;

import android.content.Context;

import com.example.btn_android.data.repository.FruitRepository;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;

import java.util.ArrayList;
import java.util.List;

public final class FruitDataProvider {
    private static final List<Category> CATEGORIES = new ArrayList<>();
    private static final List<Product> PRODUCTS = new ArrayList<>();
    private static FruitRepository repository;

    static {
        CATEGORIES.add(new Category(1, "Cam - Quýt", "Họ trái cây giàu vitamin C", "🍊"));
        CATEGORIES.add(new Category(2, "Táo - Lê", "Giòn ngọt, dễ ăn mỗi ngày", "🍎"));
        CATEGORIES.add(new Category(3, "Nho", "Trái cây tiện lợi, ăn vặt ngon", "🍇"));
        CATEGORIES.add(new Category(4, "Chuối", "Năng lượng nhanh, tốt cho bữa phụ", "🍌"));
        CATEGORIES.add(new Category(5, "Dưa - Xoài", "Mát, ngọt và thơm", "🥭"));
        CATEGORIES.add(new Category(6, "Berry", "Nhóm quả mọng tươi ngon", "🫐"));

        PRODUCTS.add(new Product(1, 1, "Cam sành", "Cam ngọt đậm, mọng nước, phù hợp ép nước hoặc ăn trực tiếp.", "🍊", 32000, "kg", 24, true));
        PRODUCTS.add(new Product(2, 1, "Quýt hồng", "Vị ngọt thanh, ít hạt, rất phù hợp làm quà.", "🍊", 38000, "kg", 18, true));
        PRODUCTS.add(new Product(3, 2, "Táo đỏ Mỹ", "Táo giòn, ngọt nhẹ, giữ tươi lâu.", "🍎", 45000, "kg", 16, true));
        PRODUCTS.add(new Product(4, 2, "Lê Hàn Quốc", "Lê mọng nước, thơm mát, ăn ngon khi ướp lạnh.", "🍐", 52000, "kg", 12, false));
        PRODUCTS.add(new Product(5, 3, "Nho xanh", "Chùm nho xanh giòn, ngọt, phù hợp ăn vặt.", "🍇", 60000, "kg", 20, true));
        PRODUCTS.add(new Product(6, 3, "Nho đỏ", "Nho đỏ tươi, vị ngọt đậm và ít chua.", "🍇", 68000, "kg", 14, false));
        PRODUCTS.add(new Product(7, 4, "Chuối già", "Chuối chín vừa, mềm, dễ ăn và giàu năng lượng.", "🍌", 22000, "nải", 30, true));
        PRODUCTS.add(new Product(8, 5, "Xoài cát", "Xoài chín thơm, ngọt đậm, rất được ưa chuộng.", "🥭", 40000, "kg", 15, true));
        PRODUCTS.add(new Product(9, 5, "Dưa hấu", "Dưa hấu ngọt, mọng nước, mát lạnh.", "🍉", 18000, "kg", 40, false));
        PRODUCTS.add(new Product(10, 6, "Việt quất", "Quả mọng nhỏ, thơm, giàu chất chống oxy hóa.", "🫐", 85000, "hộp", 10, true));
        PRODUCTS.add(new Product(11, 6, "Dâu tây", "Dâu tây đỏ tươi, chua ngọt hài hòa.", "🍓", 75000, "hộp", 11, false));
        PRODUCTS.add(new Product(12, 4, "Chuối tây", "Ngọt nhẹ, mềm và phù hợp cho cả trẻ em.", "🍌", 24000, "nải", 25, false));
    }

    private FruitDataProvider() {
    }

    public static void init(Context context) {
        repository = FruitRepository.getInstance(context);
    }

    public static List<Category> getCategories() {
        if (repository != null) {
            return repository.getAllCategories();
        }
        return new ArrayList<>(CATEGORIES);
    }

    public static List<Product> getAllProducts() {
        if (repository != null) {
            return repository.getAllProducts();
        }
        return new ArrayList<>(PRODUCTS);
    }

    public static List<Product> getFeaturedProducts() {
        if (repository != null) {
            return repository.getFeaturedProducts();
        }
        List<Product> result = new ArrayList<>();
        for (Product product : PRODUCTS) {
            if (product.isFeatured()) {
                result.add(product);
            }
        }
        return result;
    }

    public static List<Product> getProductsByCategory(int categoryId) {
        if (repository != null) {
            return repository.getProductsByCategory(categoryId);
        }
        List<Product> result = new ArrayList<>();
        for (Product product : PRODUCTS) {
            if (product.getCategoryId() == categoryId) {
                result.add(product);
            }
        }
        return result;
    }

    public static Category getCategoryById(int categoryId) {
        if (repository != null) {
            return repository.getCategoryById(categoryId);
        }
        for (Category category : CATEGORIES) {
            if (category.getId() == categoryId) {
                return category;
            }
        }
        return null;
    }

    public static Product getProductById(int productId) {
        if (repository != null) {
            return repository.getProductById(productId);
        }
        for (Product product : PRODUCTS) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    public static List<Category> getSeedCategories() {
        return new ArrayList<>(CATEGORIES);
    }

    public static List<Product> getSeedProducts() {
        return new ArrayList<>(PRODUCTS);
    }
}
