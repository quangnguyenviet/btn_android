# Hướng dẫn sử dụng chức năng Authentication (Đăng nhập)

## Tổng quan
Module Authentication đã được hoàn thành bởi Dương, cung cấp đầy đủ chức năng đăng nhập, đăng ký và quản lý phiên đăng nhập cho ứng dụng.

## Các thành phần đã hoàn thành

### 1. Database & Repository
- **UserDao**: Interface truy vấn users từ database
- **UserRepository**: Xử lý logic đăng nhập, đăng ký với callback async
- **User Entity**: Model người dùng với các trường: id, username, password, fullName, email, phone

### 2. UI Components
- **LoginActivity**: Màn hình đăng nhập
- **RegisterActivity**: Màn hình đăng ký tài khoản mới
- Layouts tương ứng với Material Design components

### 3. Utilities
- **AuthHelper**: Class tiện ích để kiểm tra trạng thái đăng nhập
- **UserPreference**: Lưu trữ trạng thái đăng nhập qua SharedPreferences

### 4. Sample Data
Đã thêm 3 tài khoản demo vào DataSeeder:
- **admin** / password (Quản trị viên)
- **customer1** / 123456 (Nguyễn Văn A)
- **customer2** / 123456 (Trần Thị B)

## Cách sử dụng cho team members

### A. Kiểm tra người dùng đã đăng nhập chưa

```java
import com.example.btn_android.utils.AuthHelper;

// Kiểm tra đơn giản
if (AuthHelper.isLoggedIn(context)) {
    // Người dùng đã đăng nhập
} else {
    // Người dùng chưa đăng nhập
}

// Lấy thông tin người dùng hiện tại
int userId = AuthHelper.getCurrentUserId(context);  // Returns -1 if not logged in
String username = AuthHelper.getCurrentUsername(context);  // Returns "" if not logged in
```

### B. Redirect đến Login nếu chưa đăng nhập

```java
// Cách 1: Redirect đơn giản
AuthHelper.redirectToLogin(context, YourActivity.class);

// Cách 2: Redirect với product ID (cho màn hình chi tiết sản phẩm)
AuthHelper.redirectToLogin(context, ProductDetailActivity.class, productId);

// Cách 3: Kiểm tra và redirect một lần
if (!AuthHelper.requireLogin(context, YourActivity.class)) {
    // User not logged in, redirected to login
    return;
}
// User is logged in, continue with your logic
```

### C. Ví dụ thực tế - Thêm sản phẩm vào giỏ hàng (cho Việt Anh)

```java
public class ProductDetailActivity extends AppCompatActivity {
    
    private void setupAddToCartButton() {
        btnAddToCart.setOnClickListener(v -> {
            // Kiểm tra đăng nhập trước khi cho phép mua hàng
            if (!AuthHelper.isLoggedIn(this)) {
                Toast.makeText(this, "Vui lòng đăng nhập để mua hàng", Toast.LENGTH_SHORT).show();
                // Redirect to login, will return to this activity after login
                AuthHelper.redirectToLogin(this, ProductDetailActivity.class, productId);
                return;
            }
            
            // User đã đăng nhập, tiến hành thêm vào giỏ
            int userId = AuthHelper.getCurrentUserId(this);
            addToCart(userId, productId);
        });
    }
    
    private void addToCart(int userId, int productId) {
        // TODO: Việt Anh sẽ implement phần này
        // Gọi OrderRepository để tạo/cập nhật đơn hàng
        // OrderRepository orderRepository = new OrderRepository(this);
        // orderRepository.addProductToCart(userId, productId, quantity, callback);
    }
}
```

### D. Đăng xuất người dùng

```java
// Đăng xuất và xóa session
AuthHelper.logout(context);

// Sau đó có thể redirect về trang chủ hoặc login
Intent intent = new Intent(context, HomeActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
startActivity(intent);
finish();
```

### E. Sử dụng UserRepository trực tiếp (nếu cần)

```java
UserRepository userRepository = new UserRepository(context);

// Login
userRepository.login(username, password, new UserRepository.LoginCallback() {
    @Override
    public void onSuccess(User user) {
        // Đăng nhập thành công
        // Nhớ save login status
        UserPreference userPreference = new UserPreference(context);
        userPreference.saveLoginStatus(user.getId(), user.getUsername());
    }
    
    @Override
    public void onError(String message) {
        // Xử lý lỗi
    }
});

// Register
userRepository.register(username, password, fullName, email, phone, 
    new UserRepository.RegisterCallback() {
        @Override
        public void onSuccess(long userId) {
            // Đăng ký thành công
        }
        
        @Override
        public void onError(String message) {
            // Xử lý lỗi
        }
    });
```

## Luồng hoạt động

### 1. Người dùng chưa đăng nhập
```
ProductDetail → Click "Thêm vào giỏ" 
→ Check login (false) 
→ Redirect to LoginActivity 
→ User login success 
→ Return to ProductDetail 
→ Can add to cart
```

### 2. Người dùng đã đăng nhập
```
ProductDetail → Click "Thêm vào giỏ" 
→ Check login (true) 
→ Get userId 
→ Create/Update Order 
→ Show success
```

## Tích hợp với Order Module (cho Việt Anh)

Khi tạo Order/OrderDetail, luôn luôn:
1. Kiểm tra login trước: `if (!AuthHelper.isLoggedIn(context)) return;`
2. Lấy userId hiện tại: `int userId = AuthHelper.getCurrentUserId(context);`
3. Sử dụng userId này để tạo Order

```java
// Ví dụ trong OrderRepository
public void createOrder(int userId, List<OrderDetail> details, CreateOrderCallback callback) {
    executorService.execute(() -> {
        // Create order with userId
        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("PENDING");
        order.setCreatedAt(System.currentTimeMillis());
        
        long orderId = orderDao.insert(order);
        
        // Add order details...
    });
}
```

## Testing

Sử dụng các tài khoản demo để test:
- Username: **admin**, Password: **password**
- Username: **customer1**, Password: **123456**
- Username: **customer2**, Password: **123456**

## Notes

- Tất cả callback trong Repository đều chạy trên background thread, nhớ post về main thread khi update UI
- UserPreference tự động lưu và đồng bộ trạng thái login
- LoginActivity và RegisterActivity tự động handle việc quay về màn hình trước đó sau khi đăng nhập thành công
- Mật khẩu hiện tại lưu plain text (demo), production nên hash bằng BCrypt hoặc tương tự

## Liên hệ
Nếu có thắc mắc về module Authentication, liên hệ Dương.

