# Fruit App - Android Quản lý Bán hàng Hoa quả

## 1. Mô tả dự án

Xây dựng ứng dụng Android cho phép người dùng:

- Xem danh mục hoa quả
- Xem danh sách sản phẩm
- Xem chi tiết sản phẩm
- Đăng nhập người dùng
- Tạo hóa đơn mua hàng và thanh toán

Ứng dụng được phát triển theo hướng dễ mở rộng, phù hợp làm việc nhóm và chia module rõ ràng.

## 2. Công nghệ sử dụng

- Ngôn ngữ: Java hoặc Kotlin
- Cơ sở dữ liệu: Room Database
- Lưu trạng thái: SharedPreferences

## 3. Phân chia công việc

### 3.1 Minh: Trưởng nhóm & Quản lý dữ liệu

- Thiết lập cấu trúc cơ sở dữ liệu với 5 bảng:
	- Users
	- Categories
	- Products
	- OrderDetails
	- Orders
- Xây dựng mối quan hệ giữa các bảng theo sơ đồ thực thể
- Thêm dữ liệu mẫu cho Categories và Products để nhóm test nhanh
- Viết hàm lưu/xóa trạng thái đăng nhập bằng SharedPreferences

### 3.2 Quang: Giao diện & xem sản phẩm

- Thiết kế Home Screen hiển thị sản phẩm bán trong ngày
- Thiết kế Categories Screen hiển thị danh sách danh mục
- Xây dựng Product Detail Screen cho từng loại hoa quả
- Quản lý RecyclerView Adapter và các thành phần UI liên quan

### 3.3 Dương: Đăng nhập & xác thực

- Thiết kế và xử lý logic Login Screen
- Kiểm tra trạng thái đăng nhập trước khi vào quy trình mua hàng
- Điều hướng người dùng quay lại luồng mua hàng sau khi đăng nhập thành công
- Quản lý các thao tác liên quan đến bảng Users

### 3.4 Việt Anh: Đơn hàng & thanh toán

- Xử lý sự kiện khi người dùng chọn sản phẩm
- Tự động tạo Orders và OrderDetails khi thêm hàng vào giỏ
- Xử lý checkout và cập nhật trạng thái đơn hàng sang Paid
- Thiết kế màn hình hóa đơn cuối cùng sau khi thanh toán

## 4. Luồng hệ thống chính

1. Khởi động: Home Screen → xem sản phẩm / danh mục / chi tiết
2. Chọn hàng: Người dùng chọn sản phẩm → hệ thống kiểm tra Login qua SharedPreferences
3. Tạo đơn: Nếu đã đăng nhập → tạo Orders và OrderDetails
4. Kết thúc: Tiếp tục mua hàng hoặc tiến hành thanh toán → hiển thị hóa đơn

## 5. Cấu trúc thư mục chuẩn cho nhóm

> Cấu trúc này giúp chia việc rõ ràng, dễ quản lý và dễ mở rộng về sau.

```text
app/src/main/java/com/example/btn_android/
├── adapter/
├── controller/
├── data/
│   ├── local/
│   │   ├── dao/
│   │   ├── database/
│   │   └── entity/
│   ├── repository/
│   └── seed/
├── model/
├── preference/
├── ui/
│   ├── auth/
│   ├── category/
│   ├── home/
│   ├── order/
│   └── product/
├── utils/
└── view/
```

## 6. Gợi ý tổ chức nhiệm vụ trong từng thư mục

- `data/local/entity/`: Lưu các Entity của Room hoặc model bảng SQLite
- `data/local/dao/`: Chứa các interface truy vấn dữ liệu
- `data/local/database/`: Khởi tạo Room Database hoặc SQLite helper
- `data/repository/`: Xử lý trung gian giữa UI và database
- `data/seed/`: Dữ liệu mẫu ban đầu cho Categories và Products
- `preference/`: Quản lý SharedPreferences và trạng thái đăng nhập
- `ui/auth/`: Màn hình đăng nhập, xác thực, tài khoản
- `ui/home/`: Màn hình trang chủ và danh sách sản phẩm hôm nay
- `ui/category/`: Màn hình danh mục sản phẩm
- `ui/product/`: Chi tiết sản phẩm
- `ui/order/`: Giỏ hàng, tạo đơn, thanh toán, hóa đơn
- `adapter/`: RecyclerView Adapter, ViewHolder
- `utils/`: Hằng số, tiện ích, format dữ liệu, helper chung

## 7. Ghi chú cho nhóm

- Ưu tiên tách rõ phần UI, dữ liệu và logic xử lý
- Dữ liệu mẫu nên được thêm sẵn để test nhanh giao diện
- Các hàm kiểm tra đăng nhập nên dùng lại ở nhiều màn hình
- Khi thêm sản phẩm vào giỏ, cần cập nhật cả `Orders` và `OrderDetails`

## 8. Trạng thái hiện tại

- Dự án đã có sẵn cấu trúc Android cơ bản
- Thư mục chuẩn cho nhóm đã được tạo để bắt đầu chia việc
- Có thể tiếp tục phát triển từng module theo từng người phụ trách
