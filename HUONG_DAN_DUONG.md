# Hướng Dẫn Phần Của Dương - Đăng Nhập & Xác Thực

## 📋 Tổng Quan

Phần của Dương đã hoàn thành các chức năng sau theo đúng cấu trúc thư mục trong README:

### ✅ Đã Hoàn Thành

1. **Entity & Database** (`data/local/entity/`, `data/local/dao/`, `data/local/database/`)
   - `User.java` - Entity cho bảng Users với đầy đủ thông tin
   - `UserDao.java` - Data Access Object với các query cần thiết
   - `AppDatabase.java` - Room Database chính (để cho Minh mở rộng thêm)

2. **Repository** (`data/repository/`)
   - `UserRepository.java` - Xử lý trung gian giữa UI và Database

3. **SharedPreferences** (`preference/`)
   - `AuthPreference.java` - Quản lý trạng thái đăng nhập

4. **UI Authentication** (`ui/auth/`)
   - `LoginActivity.java` - Màn hình đăng nhập
   - `RegisterActivity.java` - Màn hình đăng ký
   - `activity_login.xml` - Layout đăng nhập
   - `activity_register.xml` - Layout đăng ký

5. **Utilities** (`utils/`)
   - `AuthHelper.java` - Helper class để kiểm tra đăng nhập và điều hướng

6. **Seed Data** (`data/seed/`)
   - `UserSeed.java` - Dữ liệu mẫu cho Users

## 🎯 Chức Năng Chính

### 1. Đăng Nhập
- Nhập username và password
- Validate input
- Kiểm tra thông tin với database
- Lưu trạng thái đăng nhập vào SharedPreferences
- Điều hướng về màn hình trước đó

### 2. Đăng Ký
- Nhập thông tin đầy đủ
- Validate dữ liệu
- Kiểm tra username/email đã tồn tại
- Thêm user mới vào database

### 3. Kiểm Tra Đăng Nhập
- `AuthHelper.requireLogin()` - Dùng trong các màn hình khác để kiểm tra đăng nhập
- Tự động chuyển đến LoginActivity nếu chưa đăng nhập
- Điều hướng quay lại sau khi đăng nhập thành công

## 📝 Cách Sử Dụng AuthHelper Trong Các Màn Hình Khác

### Ví dụ 1: Product Detail Activity (Phần của Quang)

```java
// Khi user nhấn nút "Thêm vào giỏ hàng"
btnAddToCart.setOnClickListener(v -> {
    // Kiểm tra đăng nhập trước
    if (!AuthHelper.requireLogin(this)) {
        return; // Chưa đăng nhập, đã tự động chuyển sang Login
    }
    
    // Đã đăng nhập, tiếp tục thêm vào giỏ
    addProductToCart();
});
```

### Ví dụ 2: Checkout Activity (Phần của Việt Anh)

```java
// Khi user vào màn hình thanh toán
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    // Kiểm tra đăng nhập ngay khi vào màn hình
    if (!AuthHelper.requireLoginWithRedirect(this, 
            LoginActivity.REDIRECT_CHECKOUT, 0)) {
        return; // Sẽ quay lại Checkout sau khi đăng nhập
    }
    
    setContentView(R.layout.activity_checkout);
    // ... tiếp tục xử lý
}
```

### Ví dụ 3: Lấy thông tin user đang đăng nhập

```java
// Lấy User ID để tạo Order
int userId = AuthHelper.getLoggedInUserId(this);

// Lấy tên để hiển thị
String fullName = AuthHelper.getLoggedInFullName(this);
tvWelcome.setText("Xin chào, " + fullName);

// Kiểm tra đã đăng nhập chưa
if (AuthHelper.isUserLoggedIn(this)) {
    // User đã đăng nhập
}
```

## 🧪 Test & Dữ Liệu Mẫu

### Tài khoản test có sẵn:
1. **Admin**: username: `admin`, password: `admin123`
2. **User 1**: username: `user1`, password: `123456`
3. **Dương**: username: `duong`, password: `123456`

Dữ liệu sẽ tự động được thêm vào database khi chạy app lần đầu (xem `UserSeed.java`)

## 🔄 Luồng Hoạt Động

### Luồng 1: User chưa đăng nhập muốn mua hàng
1. User xem sản phẩm → nhấn "Thêm vào giỏ"
2. System kiểm tra đăng nhập qua `AuthHelper.requireLogin()`
3. Chưa đăng nhập → chuyển đến LoginActivity
4. User đăng nhập thành công → quay lại màn hình Product Detail
5. Tự động thêm vào giỏ hàng

### Luồng 2: User đã đăng nhập
1. System kiểm tra qua `AuthHelper.isUserLoggedIn()`
2. Đã đăng nhập → cho phép thực hiện hành động ngay
3. Lấy userId để tạo Order: `AuthHelper.getLoggedInUserId()`

### Luồng 3: Đăng xuất
1. User nhấn nút Đăng xuất
2. Gọi `AuthHelper.logout(context)`
3. Xóa thông tin trong SharedPreferences
4. Cập nhật UI

## 📂 Cấu Trúc Thư Mục Đã Tạo

```
app/src/main/java/com/example/btn_android/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   │   └── UserDao.java ✅
│   │   ├── database/
│   │   │   └── AppDatabase.java ✅
│   │   └── entity/
│   │       └── User.java ✅
│   ├── repository/
│   │   └── UserRepository.java ✅
│   └── seed/
│       └── UserSeed.java ✅
├── preference/
│   └── AuthPreference.java ✅
├── ui/
│   └── auth/
│       ├── LoginActivity.java ✅
│       └── RegisterActivity.java ✅
└── utils/
    └── AuthHelper.java ✅

app/src/main/res/layout/
├── activity_login.xml ✅
└── activity_register.xml ✅
```

## 🤝 Tích Hợp Với Phần Của Các Thành Viên Khác

### Minh (Trưởng nhóm & Quản lý dữ liệu)
- Minh sẽ mở rộng `AppDatabase.java` để thêm các entity khác: Categories, Products, Orders, OrderDetails
- Minh sẽ tạo các DAO tương ứng
- File `UserSeed.java` có thể làm mẫu cho Minh tạo seed data cho Categories và Products

### Quang (Giao diện & xem sản phẩm)
- Quang sẽ dùng `AuthHelper.requireLogin()` trong ProductDetailActivity
- Khi user nhấn "Thêm vào giỏ", kiểm tra đăng nhập trước
- Hiển thị tên user: `AuthHelper.getLoggedInFullName()`

### Việt Anh (Đơn hàng & thanh toán)
- Việt Anh sẽ dùng `AuthHelper.getLoggedInUserId()` để tạo Orders
- Kiểm tra đăng nhập trước khi cho phép checkout
- Trong OrderRepository, lấy userId từ AuthHelper

## 🔍 Lưu Ý

1. **Room Database**: Đã cấu hình Room với `.fallbackToDestructiveMigration()` để dễ dàng reset database khi test
2. **Background Thread**: Tất cả database operations đều chạy trong background thread (ExecutorService)
3. **UI Thread**: Update UI luôn được gọi trong `runOnUiThread()`
4. **Validation**: Đã có validation đầy đủ cho form đăng nhập và đăng ký
5. **Security**: Password hiện tại chưa được mã hóa (có thể thêm MD5/SHA256 sau)

## 🚀 Chạy Thử

1. Build project
2. Chạy app trên emulator hoặc device
3. Màn hình chính sẽ hiển thị:
   - Trạng thái đăng nhập
   - Button "Đăng Nhập"
   - Button "Đăng Xuất"
   - Button "Test Kiểm Tra Đăng Nhập"
   - Danh sách tài khoản test

4. Test các tình huống:
   - Đăng nhập với tài khoản đúng
   - Đăng nhập với tài khoản sai
   - Đăng ký tài khoản mới
   - Đăng xuất
   - Test kiểm tra đăng nhập

## 📞 Hỗ Trợ

Nếu các thành viên khác cần hỗ trợ về authentication hoặc cách sử dụng AuthHelper, có thể tham khảo:
- `AuthHelper.java` - Có đầy đủ comment và ví dụ
- `LoginActivity.java` - Có flow chi tiết về cách kiểm tra và điều hướng
- File này - Có đầy đủ ví dụ sử dụng

---

**Hoàn thành bởi: Dương**  
**Ngày: April 3, 2026**  
**Trạng thái: ✅ HOÀN THÀNH**

