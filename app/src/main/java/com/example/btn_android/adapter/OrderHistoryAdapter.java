package com.example.btn_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.btn_android.R;
import com.example.btn_android.data.local.entity.Order;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder> {

    private final List<Order> orders;
    private final OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrderHistoryAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_history, parent, false);
        return new OrderHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderHistoryViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOrderId;
        private final TextView tvOrderDate;
        private final TextView tvOrderTotal;
        private final TextView tvOrderStatus;

        public OrderHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvHistoryOrderId);
            tvOrderDate = itemView.findViewById(R.id.tvHistoryOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvHistoryOrderTotal);
            tvOrderStatus = itemView.findViewById(R.id.tvHistoryOrderStatus);
        }

        public void bind(Order order, OnOrderClickListener listener) {
            tvOrderId.setText("Đơn hàng #" + order.getId());
            tvOrderDate.setText(order.getOrderDate());

            NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvOrderTotal.setText(nf.format(order.getTotalAmount()));

            tvOrderStatus.setText(order.getStatus());

            itemView.setOnClickListener(v -> listener.onOrderClick(order));
        }
    }
}

