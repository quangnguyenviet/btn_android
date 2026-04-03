package com.example.btn_android.data.repository;

import android.content.Context;
import com.example.btn_android.data.local.database.AppDatabase;
import com.example.btn_android.data.local.entity.Order;
import com.example.btn_android.data.local.entity.OrderDetail;
import com.example.btn_android.data.local.entity.Product;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * OrderRepository - Xử lý logic tạo đơn hàng, thêm sản phẩm vào giỏ, và thanh toán
 * Được phát triển bởi Việt Anh
 */
public class OrderRepository {
    private final AppDatabase database;
    
    public interface OrderCallback {
        void onSuccess(Object result);
        void onError(String error);
    }

    public OrderRepository(Context context) {
        this.database = AppDatabase.getDatabase(context);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     * Nếu chưa có Order cho user, tạo mới
     * Nếu đã có, kiểm tra sản phẩm có trong Order không
     * Nếu có rồi, tăng số lượng; nếu chưa, tạo OrderDetail mới
     */
    public void addProductToCart(int userId, int productId, int quantity, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                // Lấy thông tin sản phẩm
                Product product = database.productDao().getProductById(productId);
                if (product == null) {
                    callback.onError("Sản phẩm không tìm thấy");
                    return;
                }

                // Tìm Order chưa thanh toán của user
                List<Order> userOrders = database.orderDao().getOrdersByUser(userId);
                Order currentOrder = null;
                
                for (Order order : userOrders) {
                    if ("Pending".equals(order.getStatus())) {
                        currentOrder = order;
                        break;
                    }
                }

                // Nếu chưa có Order Pending, tạo mới
                if (currentOrder == null) {
                    currentOrder = createNewOrder(userId);
                }

                // Kiểm tra OrderDetail
                List<OrderDetail> orderDetails = database.orderDetailDao()
                        .getOrderDetailsByOrder(currentOrder.getId());

                boolean productExists = false;
                for (OrderDetail detail : orderDetails) {
                    if (detail.getProductId() == productId) {
                        // Tăng số lượng nếu sản phẩm đã có trong giỏ
                        detail.setQuantity(detail.getQuantity() + quantity);
                        database.orderDetailDao().update(detail);
                        productExists = true;
                        break;
                    }
                }

                // Nếu sản phẩm chưa có, tạo OrderDetail mới
                if (!productExists) {
                    OrderDetail newDetail = new OrderDetail(
                            currentOrder.getId(),
                            productId,
                            quantity,
                            product.getPrice()
                    );
                    database.orderDetailDao().insert(newDetail);
                }

                // Cập nhật tổng tiền của Order
                updateOrderTotal(currentOrder.getId());

                callback.onSuccess(currentOrder.getId());

            } catch (Exception e) {
                callback.onError("Lỗi thêm sản phẩm: " + e.getMessage());
            }
        });
    }

    /**
     * Tạo Order mới cho user
     */
    private Order createNewOrder(int userId) {
        String orderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        Order order = new Order(userId, orderDate, 0, "Pending");
        long orderId = database.orderDao().insert(order);
        order.setId((int) orderId);
        return order;
    }

    /**
     * Cập nhật tổng tiền của Order dựa trên OrderDetails
     */
    private void updateOrderTotal(int orderId) {
        List<OrderDetail> details = database.orderDetailDao().getOrderDetailsByOrder(orderId);
        double totalAmount = 0;
        
        for (OrderDetail detail : details) {
            totalAmount += detail.getUnitPrice() * detail.getQuantity();
        }
        
        Order order = database.orderDao().getOrderById(orderId);
        if (order != null) {
            order.setTotalAmount(totalAmount);
            database.orderDao().update(order);
        }
    }

    /**
     * Lấy Order hiện tại (chưa thanh toán) của user
     */
    public void getCurrentOrder(int userId, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<Order> userOrders = database.orderDao().getOrdersByUser(userId);
                Order currentOrder = null;
                
                for (Order order : userOrders) {
                    if ("Pending".equals(order.getStatus())) {
                        currentOrder = order;
                        break;
                    }
                }
                
                if (currentOrder == null) {
                    callback.onError("Không có đơn hàng");
                } else {
                    callback.onSuccess(currentOrder);
                }
            } catch (Exception e) {
                callback.onError("Lỗi: " + e.getMessage());
            }
        });
    }

    /**
     * Lấy chi tiết đơn hàng (OrderDetails)
     */
    public void getOrderDetails(int orderId, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<OrderDetail> details = database.orderDetailDao().getOrderDetailsByOrder(orderId);
                callback.onSuccess(details);
            } catch (Exception e) {
                callback.onError("Lỗi: " + e.getMessage());
            }
        });
    }

    /**
     * Lấy thông tin Order theo ID
     */
    public void getOrder(int orderId, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Order order = database.orderDao().getOrderById(orderId);
                if (order != null) {
                    callback.onSuccess(order);
                } else {
                    callback.onError("Không tìm thấy đơn hàng");
                }
            } catch (Exception e) {
                callback.onError("Lỗi: " + e.getMessage());
            }
        });
    }

    /**
     * Thanh toán đơn hàng - cập nhật trạng thái từ Pending sang Paid
     */
    public void checkoutOrder(int orderId, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                Order order = database.orderDao().getOrderById(orderId);
                if (order == null) {
                    callback.onError("Không tìm thấy đơn hàng");
                    return;
                }
                
                order.setStatus("Paid");
                database.orderDao().update(order);
                callback.onSuccess(order);
                
            } catch (Exception e) {
                callback.onError("Lỗi thanh toán: " + e.getMessage());
            }
        });
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public void removeProductFromCart(int orderId, int productId, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<OrderDetail> details = database.orderDetailDao().getOrderDetailsByOrder(orderId);
                
                for (OrderDetail detail : details) {
                    if (detail.getProductId() == productId) {
                        database.orderDetailDao().delete(detail);
                        updateOrderTotal(orderId);
                        callback.onSuccess(null);
                        return;
                    }
                }
                
                callback.onError("Sản phẩm không tìm thấy trong giỏ");
                
            } catch (Exception e) {
                callback.onError("Lỗi xóa sản phẩm: " + e.getMessage());
            }
        });
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ hàng
     */
    public void updateProductQuantity(int orderId, int productId, int newQuantity, OrderCallback callback) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            try {
                List<OrderDetail> details = database.orderDetailDao().getOrderDetailsByOrder(orderId);
                
                for (OrderDetail detail : details) {
                    if (detail.getProductId() == productId) {
                        if (newQuantity <= 0) {
                            database.orderDetailDao().delete(detail);
                        } else {
                            detail.setQuantity(newQuantity);
                            database.orderDetailDao().update(detail);
                        }
                        updateOrderTotal(orderId);
                        callback.onSuccess(null);
                        return;
                    }
                }
                
                callback.onError("Sản phẩm không tìm thấy");
                
            } catch (Exception e) {
                callback.onError("Lỗi cập nhật: " + e.getMessage());
            }
        });
    }
}

