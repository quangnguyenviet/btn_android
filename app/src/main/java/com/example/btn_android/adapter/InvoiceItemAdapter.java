package com.example.btn_android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * InvoiceItemAdapter - Adapter hiển thị danh sách sản phẩm trong hóa đơn (read-only)
 * Được phát triển bởi Việt Anh
 */
public class InvoiceItemAdapter extends RecyclerView.Adapter<InvoiceItemAdapter.InvoiceItemViewHolder> {

    private List<OrderDetail> orderDetails;

    public InvoiceItemAdapter(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @NonNull
    @Override
    public InvoiceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);
        return new InvoiceItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceItemViewHolder holder, int position) {
        OrderDetail detail = orderDetails.get(position);
        holder.bind(detail);
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class InvoiceItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName;
        private TextView tvQuantity;
        private TextView tvUnitPrice;
        private TextView tvLineTotal;

        public InvoiceItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvInvoiceItemName);
            tvQuantity = itemView.findViewById(R.id.tvInvoiceItemQuantity);
            tvUnitPrice = itemView.findViewById(R.id.tvInvoiceItemPrice);
            tvLineTotal = itemView.findViewById(R.id.tvInvoiceItemTotal);
        }

        public void bind(OrderDetail detail) {
            AppDatabase database = AppDatabase.getDatabase(itemView.getContext());
            Product product = database.productDao().getProductById(detail.getProductId());

            if (product != null) {
                tvProductName.setText(product.getName());
            }

            tvQuantity.setText("x" + detail.getQuantity());

            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
            tvUnitPrice.setText(nf.format(detail.getUnitPrice()));

            double lineTotal = detail.getUnitPrice() * detail.getQuantity();
            tvLineTotal.setText(nf.format(lineTotal));
        }
    }
}
