# Testing Guide - Authentication Module

## 🧪 Hướng dẫn test chức năng Authentication

### 1. Cài đặt và chạy app

```bash
cd D:\btn_android
.\gradlew assembleDebug
.\gradlew installDebug
```

Hoặc chạy trực tiếp từ Android Studio: Run > Run 'app'

---

## 2. Test Cases

### ✅ Test Case 1: Đăng nhập thành công
1. Mở app → Click vào sản phẩm bất kỳ
2. Click nút "Thêm vào giỏ hàng"
3. Màn hình Login xuất hiện
4. Nhập:
   - Username: `admin`
   - Password: `password`
5. Click "Đăng nhập"
6. **Kết quả mong đợi**: 
   - Toast "Đăng nhập thành công!"
   - Tự động quay về màn hình Product Detail
   - Toast hiển thị userId và username

### ✅ Test Case 2: Đăng nhập thất bại - Sai mật khẩu
1. Mở Login screen
2. Nhập:
   - Username: `admin`
   - Password: `wrongpassword`
3. Click "Đăng nhập"
4. **Kết quả mong đợi**: 
   - Error message: "Tên đăng nhập hoặc mật khẩu không đúng"
   - Nút "Đăng nhập" enabled lại

### ✅ Test Case 3: Đăng nhập thất bại - Thiếu thông tin
1. Mở Login screen
2. Để trống username hoặc password
3. Click "Đăng nhập"
4. **Kết quả mong đợi**:
   - Error: "Vui lòng nhập tên đăng nhập" hoặc "Vui lòng nhập mật khẩu"

### ✅ Test Case 4: Đăng ký tài khoản mới
1. Mở Login screen
2. Click "Đăng ký tài khoản"
3. Nhập thông tin:
   - Username: `testuser`
   - Password: `123456`
   - Confirm Password: `123456`
   - Full Name: `Test User`
   - Email: `test@example.com`
   - Phone: `0901234567`
4. Click "Đăng ký"
5. **Kết quả mong đợi**:
   - Success message: "Đăng ký thành công!"
   - Tự động login và quay về màn hình trước đó

### ✅ Test Case 5: Đăng ký thất bại - Username đã tồn tại
1. Register screen
2. Nhập username: `admin` (đã tồn tại)
3. Nhập các field khác
4. Click "Đăng ký"
5. **Kết quả mong đợi**:
   - Error: "Tên đăng nhập đã tồn tại"

### ✅ Test Case 6: Đăng ký thất bại - Mật khẩu không khớp
1. Register screen
2. Password: `123456`
3. Confirm Password: `654321`
4. Click "Đăng ký"
5. **Kết quả mong đợi**:
   - Error: "Mật khẩu xác nhận không khớp"

### ✅ Test Case 7: Đăng ký thất bại - Mật khẩu quá ngắn
1. Register screen
2. Password: `123` (< 6 ký tự)
3. Click "Đăng ký"
4. **Kết quả mong đợi**:
   - Error: "Mật khẩu phải có ít nhất 6 ký tự"

### ✅ Test Case 8: Session persistence
1. Đăng nhập thành công
2. Thoát app (kill app)
3. Mở lại app
4. Click "Thêm vào giỏ hàng" ở bất kỳ sản phẩm nào
5. **Kết quả mong đợi**:
   - KHÔNG redirect đến Login
   - Trực tiếp hiển thị toast với userId (vì đã login trước đó)

### ✅ Test Case 9: Back navigation từ Login
1. Vào Product Detail → Click "Thêm vào giỏ"
2. Login screen hiển thị
3. Press back button
4. **Kết quả mong đợi**:
   - Quay về Home screen (không quay về Product Detail)

### ✅ Test Case 10: Back navigation từ Register
1. Mở Register screen từ Login
2. Press back button
3. **Kết quả mong đợi**:
   - Quay về Login screen

---

## 3. Test với các tài khoản có sẵn

| Test Scenario | Username | Password | Expected Result |
|--------------|----------|----------|-----------------|
| Admin login | admin | password | ✅ Success |
| Customer 1 | customer1 | 123456 | ✅ Success |
| Customer 2 | customer2 | 123456 | ✅ Success |
| Wrong password | admin | wrong | ❌ Error |
| Non-existent user | ghost | any | ❌ Error |

---

## 4. Test AuthHelper methods (Unit Testing)

Có thể test trong bất kỳ Activity nào:

```java
// Test 1: Check login status
boolean isLoggedIn = AuthHelper.isLoggedIn(this);
Log.d("TEST", "Is logged in: " + isLoggedIn);

// Test 2: Get current user info
if (isLoggedIn) {
    int userId = AuthHelper.getCurrentUserId(this);
    String username = AuthHelper.getCurrentUsername(this);
    Log.d("TEST", "User: " + username + " (ID: " + userId + ")");
}

// Test 3: Logout
AuthHelper.logout(this);
Log.d("TEST", "Logged out. Is logged in: " + AuthHelper.isLoggedIn(this));
```

---

## 5. Integration Test với Order Module

Khi Việt Anh hoàn thành Order module, test luồng hoàn chỉnh:

```
1. Browse products (not logged in)
2. Add to cart → redirect to Login
3. Login successfully → return to product
4. Add to cart → create Order with correct userId
5. View cart → see items
6. Checkout → complete order
7. Kill app → reopen
8. View order history → see previous orders with correct userId
```

---

## 6. Manual Testing Checklist

- [ ] Login với admin account
- [ ] Login với customer1 account
- [ ] Login thất bại với wrong password
- [ ] Login thất bại với empty fields
- [ ] Register new account thành công
- [ ] Register thất bại với existing username
- [ ] Register thất bại với password mismatch
- [ ] Register thất bại với short password
- [ ] Session persists sau khi kill app
- [ ] Back button hoạt động đúng từ Login
- [ ] Back button hoạt động đúng từ Register
- [ ] Auto-return về Product Detail sau login
- [ ] Toast messages hiển thị rõ ràng
- [ ] Loading states hiển thị ("Đang đăng nhập...")
- [ ] Error messages màu đỏ, dễ nhìn

---

## 7. Known Issues / Limitations

1. ⚠️ Mật khẩu lưu plain text - chỉ dùng cho demo
2. ⚠️ Không có "Forgot Password" feature
3. ⚠️ Không có rate limiting cho login attempts
4. ⚠️ Không có email verification
5. ⚠️ Không có profile management UI (có thể thêm sau)

---

## 8. Performance Notes

- ✅ Database operations chạy trên background thread
- ✅ UI updates chạy trên main thread via Handler
- ✅ No blocking operations on main thread
- ✅ SharedPreferences reads are fast (synchronous OK)

---

## 9. Debug Commands

```bash
# Clear app data (logout)
adb shell pm clear com.example.btn_android

# View SharedPreferences
adb shell run-as com.example.btn_android cat /data/data/com.example.btn_android/shared_prefs/user_prefs.xml

# View database
adb shell run-as com.example.btn_android sqlite3 /data/data/com.example.btn_android/databases/fruit_app_db
.tables
SELECT * FROM users;
.quit

# Check logcat
adb logcat | grep "LoginActivity\|RegisterActivity\|AuthHelper"
```

---

## 10. Next Steps for QA

1. ✅ Verify all test cases pass
2. ✅ Test trên nhiều Android versions (API 24+)
3. ✅ Test trên devices khác nhau (phone, tablet)
4. ✅ Test rotation (landscape/portrait)
5. ✅ Stress test với nhiều login/logout cycles
6. ⏳ Integration test với Order module (đợi Việt Anh)

---

**Test Status**: ✅ Ready for Testing
**Priority**: HIGH (blocking cho Order module)

