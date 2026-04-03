package com.example.btn_android.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btn_android.R;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.OrderDetail;
import com.example.btn_android.data.local.entity.Product;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * OrderItemAdapter - Adapter hiển thị danh sách sản phẩm trong giỏ hàng
 * Được phát triển bởi Việt Anh
 */
public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderDetail> orderDetails;
    private OnItemActionListener listener;

    public interface OnItemActionListener {
        void onRemove(int orderId, int productId);
        void onQuantityChanged(int orderId, int productId, int newQuantity);
    }

    public OrderItemAdapter(List<OrderDetail> orderDetails, OnItemActionListener listener) {
        this.orderDetails = orderDetails;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        holder.bind(detail, listener);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvUnitPrice;
        private EditText etQuantity;
        private TextView tvSubtotal;
        private Button btnRemove;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvOrderItemName);
            tvUnitPrice = itemView.findViewById(R.id.tvOrderItemPrice);
            etQuantity = itemView.findViewById(R.id.etOrderItemQuantity);
            tvSubtotal = itemView.findViewById(R.id.tvOrderItemSubtotal);
            btnRemove = itemView.findViewById(R.id.btnOrderItemRemove);
        }

        public void bind(OrderDetail detail, OnItemActionListener listener) {
            AppDatabase database = AppDatabase.getDatabase(itemView.getContext());
            Product product = database.productDao().getProductById(detail.getProductId());

            if (product != null) {
                tvProductName.setText(product.getName());
            }

            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
            tvUnitPrice.setText(nf.format(detail.getUnitPrice()));
            etQuantity.setText(String.valueOf(detail.getQuantity()));

            updateSubtotal(detail);

            // Cập nhật số lượng
            etQuantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int newQuantity = Integer.parseInt(s.toString());
                        if (newQuantity > 0 && newQuantity != detail.getQuantity()) {
                            listener.onQuantityChanged(detail.getOrderId(), detail.getProductId(), newQuantity);
                            detail.setQuantity(newQuantity);
                            updateSubtotal(detail);
                        }
                    } catch (NumberFormatException e) {
                        // Bỏ qua nếu không phải số
                    }
                }
            });

            // Xóa sản phẩm
            btnRemove.setOnClickListener(v -> {
                listener.onRemove(detail.getOrderId(), detail.getProductId());
            });
        }

        private void updateSubtotal(OrderDetail detail) {
            double subtotal = detail.getUnitPrice() * detail.getQuantity();
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
            tvSubtotal.setText(nf.format(subtotal));
        }
    }
}
