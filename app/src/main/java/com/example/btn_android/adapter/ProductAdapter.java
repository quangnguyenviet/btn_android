package com.example.btn_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btn_android.R;
import com.example.btn_android.data.seed.FruitDataProvider;
import com.example.btn_android.model.Category;
import com.example.btn_android.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private final OnProductClickListener listener;

    public ProductAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Product> items) {
        products.clear();
        if (items != null) {
            products.addAll(items);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvEmoji;
        private final TextView tvName;
        private final TextView tvCategory;
        private final TextView tvPrice;
        private final TextView tvStock;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tvProductEmoji);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvCategory = itemView.findViewById(R.id.tvProductCategory);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvStock = itemView.findViewById(R.id.tvProductStock);
        }

        void bind(Product product) {
            tvEmoji.setText(product.getEmoji());
            tvName.setText(product.getName());

            Category category = FruitDataProvider.getCategoryById(product.getCategoryId());
            tvCategory.setText(category != null ? category.getName() : "Danh mục khác");
            tvPrice.setText(String.format(Locale.getDefault(), "%s đ / %s", String.format(Locale.getDefault(), "%,d", product.getPrice()), product.getUnit()));
            tvStock.setText(String.format(Locale.getDefault(), "Còn %d", product.getStock()));

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}
