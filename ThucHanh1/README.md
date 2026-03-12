# Product Management System

Ứng dụng Java Console quản lý sản phẩm theo yêu cầu bài thực hành.

## Tính năng
- Thêm sản phẩm mới
- Hiển thị danh sách dạng bảng
- Cập nhật số lượng theo ID bằng `Optional`
- Xóa các sản phẩm có `quantity = 0`
- Kiểm tra trùng ID bằng `ArrayList<Product>` và Stream API
- Dùng custom exception `InvalidProductException`

## Cấu trúc
- `src/Product.java`: model sản phẩm, encapsulation đầy đủ
- `src/InvalidProductException.java`: custom exception
- `src/Main.java`: menu console và xử lý nghiệp vụ

## Chạy chương trình
Biên dịch tất cả file trong thư mục `src`, sau đó chạy lớp `Main`.

## Kiểm tra nhanh
Các case đã hỗ trợ:
- ID trùng khi thêm mới
- ID không tồn tại khi cập nhật
- Giá <= 0
- Số lượng < 0
- Tên hoặc danh mục rỗng
- Xóa toàn bộ sản phẩm hết hàng
