# 🚀 Hướng Dẫn Build và Chạy Ứng Dụng

## 📋 Yêu Cầu Hệ Thống

- **Android Studio**: Arctic Fox (2020.3.1) hoặc mới hơn
- **JDK**: Java 11 trở lên
- **Gradle**: 7.0 trở lên (tự động download bởi Gradle Wrapper)
- **Android SDK**:
  - Min SDK: 24 (Android 7.0)
  - Target SDK: 36
  - Compile SDK: 36

## 🔧 Các Bước Build Project

### 1. **Mở Project trong Android Studio**

```bash
File → Open → Chọn thư mục D:\btn_android
```

### 2. **Sync Gradle**

Android Studio sẽ tự động sync Gradle. Nếu không, click:
```
File → Sync Project with Gradle Files
```

Hoặc chạy trong terminal:
```powershell
cd D:\btn_android
.\gradlew clean build
```

### 3. **Chạy Ứng Dụng**

#### **Trên Emulator:**
1. Mở AVD Manager (Android Virtual Device)
2. Tạo hoặc chạy một emulator (Android 7.0+)
3. Click nút "Run" (▶️) trong Android Studio

#### **Trên Thiết Bị Thật:**
1. Bật Developer Options và USB Debugging
2. Kết nối thiết bị qua USB
3. Click nút "Run" (▶️)

#### **Qua Terminal:**
```powershell
cd D:\btn_android
.\gradlew installDebug
```

## 🗂️ Cấu Trúc Project Sau Khi Cải Tiến

```
btn_android/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/btn_android/
│   │   │   │   ├── adapter/
│   │   │   │   │   ├── CategoryAdapter.java
│   │   │   │   │   ├── InvoiceItemAdapter.java
│   │   │   │   │   ├── OrderHistoryAdapter.java ✨ MỚI
│   │   │   │   │   ├── OrderItemAdapter.java
│   │   │   │   │   └── ProductAdapter.java
│   │   │   │   ├── data/
│   │   │   │   │   ├── local/
│   │   │   │   │   │   ├── dao/
│   │   │   │   │   │   │   ├── CategoryDao.java
│   │   │   │   │   │   │   ├── OrderDao.java
│   │   │   │   │   │   │   ├── OrderDetailDao.java
│   │   │   │   │   │   │   ├── ProductDao.java
│   │   │   │   │   │   │   └── UserDao.java (updated)
│   │   │   │   │   │   ├── database/
│   │   │   │   │   │   │   └── AppDatabase.java
│   │   │   │   │   │   └── entity/
│   │   │   │   │   │       ├── Category.java
│   │   │   │   │   │       ├── Order.java
│   │   │   │   │   │       ├── OrderDetail.java
│   │   │   │   │   │       ├── Product.java
│   │   │   │   │   │       └── User.java
│   │   │   │   │   ├── repository/
│   │   │   │   │   │   ├── OrderRepository.java
│   │   │   │   │   │   └── UserRepository.java
│   │   │   │   │   └── seed/
│   │   │   │   │       └── DataSeeder.java (updated)
│   │   │   │   ├── preference/
│   │   │   │   │   └── UserPreference.java
│   │   │   │   ├── ui/
│   │   │   │   │   ├── auth/
│   │   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   │   └── RegisterActivity.java
│   │   │   │   │   ├── category/
│   │   │   │   │   │   └── CategoryActivity.java
│   │   │   │   │   ├── home/
│   │   │   │   │   │   └── HomeActivity.java (updated)
│   │   │   │   │   ├── order/
│   │   │   │   │   │   ├── OrderActivity.java
│   │   │   │   │   │   └── OrderDetailActivity.java
│   │   │   │   │   ├── product/
│   │   │   │   │   │   └── ProductDetailActivity.java
│   │   │   │   │   └── profile/ ✨ MỚI
│   │   │   │   │       └── ProfileActivity.java
│   │   │   │   ├── utils/
│   │   │   │   │   └── AuthHelper.java
│   │   │   │   └── view/
│   │   │   │       └── MainActivity.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_category.xml
│   │   │   │   │   ├── activity_home.xml (updated)
│   │   │   │   │   ├── activity_login.xml (updated)
│   │   │   │   │   ├── activity_order.xml
│   │   │   │   │   ├── activity_order_detail.xml
│   │   │   │   │   ├── activity_product_detail.xml (updated)
│   │   │   │   │   ├── activity_profile.xml ✨ MỚI
│   │   │   │   │   ├── activity_register.xml
│   │   │   │   │   ├── item_category.xml (updated)
│   │   │   │   │   ├── item_invoice.xml
│   │   │   │   │   ├── item_order.xml
│   │   │   │   │   ├── item_order_history.xml ✨ MỚI
│   │   │   │   │   └── item_product.xml (updated)
│   │   │   │   ├── menu/
│   │   │   │   │   └── menu_home.xml ✨ MỚI
│   │   │   │   ├── values/
│   │   │   │   │   ├── colors.xml (updated)
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml (updated)
│   │   │   │   └── ...
│   │   │   └── AndroidManifest.xml (updated)
│   │   └── ...
│   └── build.gradle.kts
├── README.md
├── IMPROVEMENTS_REPORT.md ✨ MỚI
├── BUILD_GUIDE.md ✨ (File này)
└── ...
```

## 🎯 Testing Flow

### Test Case 1: Xem Sản Phẩm (Không Cần Login)
1. Mở app → HomeActivity
2. Xem danh sách 10 sản phẩm nổi bật
3. Click "Xem danh mục sản phẩm"
4. Chọn category → xem products trong category đó
5. Click vào 1 product → xem chi tiết

✅ **Expected**: Tất cả hoạt động bình thường

### Test Case 2: Thêm Vào Giỏ (Cần Login)
1. Từ ProductDetailActivity
2. Click "Thêm vào giỏ hàng"
3. Nếu chưa login → redirect đến LoginActivity

✅ **Expected**: Login screen hiển thị với thông báo

### Test Case 3: Login & Complete Purchase
1. Login với `demo / demo`
2. Tự động quay lại ProductDetailActivity
3. Click "Thêm vào giỏ hàng" lại
4. Redirect đến OrderActivity (giỏ hàng)
5. Xem items, có thể update quantity hoặc remove
6. Click "Thanh toán"
7. Redirect đến OrderDetailActivity (hóa đơn)
8. Click "Tiếp tục mua hàng" → về HomeActivity

✅ **Expected**: Flow hoàn chỉnh không lỗi

### Test Case 4: Profile & Order History
1. Từ HomeActivity, click icon Profile (menu trên toolbar)
2. Nếu chưa login → redirect LoginActivity
3. Login xong → ProfileActivity
4. Xem thông tin user
5. Xem order history (các đơn đã Paid)
6. Click vào 1 order → xem chi tiết hóa đơn

✅ **Expected**: Hiển thị đúng thông tin

### Test Case 5: Logout
1. Từ ProfileActivity
2. Click "Đăng xuất"
3. Confirm trong dialog
4. Redirect về HomeActivity
5. Click Profile icon lại → phải login lại

✅ **Expected**: Logout thành công

## 🐛 Troubleshooting

### Lỗi: "Cannot resolve symbol R"
**Giải pháp:**
```
1. File → Invalidate Caches / Restart
2. Build → Clean Project
3. Build → Rebuild Project
```

### Lỗi: "Room schema export directory not set"
**Giải pháp:** Đã set `exportSchema = false` trong `@Database` annotation

### Lỗi: "Manifest merger failed"
**Giải pháp:** Check AndroidManifest.xml có conflict không, đảm bảo tất cả activities đã được declare

### Lỗi: Layout inflation errors
**Giải pháp:** 
1. Check tất cả IDs trong layout files match với findViewById() calls
2. Sync Gradle để regenerate R.java
3. Clean & Rebuild

### App crashes khi mở
**Giải pháp:**
1. Check Logcat để xem stack trace
2. Đảm bảo database được khởi tạo đúng
3. Check permissions trong AndroidManifest

## 📱 Tài Khoản Test

### Admin Account
- **Username**: `admin`
- **Password**: `admin123`
- **Email**: admin@fruitapp.com

### Demo Account (Khuyên dùng)
- **Username**: `demo`
- **Password**: `demo`
- **Email**: demo@fruitapp.com

### Customer Accounts
- **Username**: `customer1` / Password: `123456`
- **Username**: `customer2` / Password: `123456`

## 🎨 Screenshots (Expected)

### Home Screen
- Toolbar với title "🍎 Fruit Shop"
- Menu icons: Cart & Profile
- Header card màu xanh nhạt
- Button "Xem danh mục"
- Grid view 2 cột hiển thị products
- FAB cart màu cam ở góc phải dưới

### Login Screen
- Logo emoji 🍎
- Text fields cho username & password
- Button "Đăng nhập" màu xanh
- Button "Đăng ký" outline
- Text hint tài khoản demo

### Product Detail
- Card lớn với emoji 🍎 120sp
- Card thông tin sản phẩm
- Tên, giá (màu xanh), category, mô tả
- Button "Thêm vào giỏ hàng" lớn

### Profile Screen
- Header card màu xanh với icon 👤
- Username, fullname
- Card thông tin liên hệ
- Card lịch sử đơn hàng
- Button "Về trang chủ" và "Đăng xuất"

## 🚀 Next Steps

1. **Run the app** trên emulator hoặc device
2. **Test all flows** theo test cases
3. **Check UI** xem có match với design không
4. **Report bugs** nếu có vấn đề
5. **Extend features** từ danh sách trong IMPROVEMENTS_REPORT.md

## 💡 Tips

- Sử dụng **Android Studio's Layout Inspector** để debug UI
- Sử dụng **Database Inspector** để xem Room database
- Enable **StrictMode** trong development để catch performance issues
- Sử dụng **Logcat** để debug runtime errors

## 📞 Support

Nếu gặp vấn đề khi build hoặc chạy app:
1. Check file `IMPROVEMENTS_REPORT.md` để hiểu các thay đổi
2. Check file `README.md` để hiểu workflow
3. Check Logcat để xem error logs
4. Clean & Rebuild project

---

**Happy Coding! 🎉**

