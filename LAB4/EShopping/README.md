# Hệ thống e-SHOPPING - Java

## Thông tin sinh viên
- Họ tên: Trương Thị Hương Giang
- MSSV: 1250080046
- Tên bài Lab: LAB 4  - Hệ thống phần mềm Cửa hàng online e-SHOPPING

## Môi trường / Version
- Java 17
- Java Swing
- JDBC
- SQL Server
- Maven
- IntelliJ IDEA / NetBeans / Eclipse

## Kiến trúc
``text
UI (Swing) -> Service -> DAO/Data -> SQL Server
                    |
                    +-> ProductAdapter -> Hệ thống quản lý sản phẩm (mock prototype)
                    +-> PaymentAdapter -> Dịch vụ thanh toán trực tuyến (mock prototype)
                    +-> EmailAdapter   -> Dịch vụ email (mock prototype)
``

## Phạm vi prototype
- Đăng ký tài khoản và đăng nhập.
- Xem sản phẩm theo nhóm, xem chi tiết.
- Thêm/xóa/cập nhật giỏ hàng.
- Chọn loại phiếu đặt hàng.
- Tính phí giao hàng theo khu vực + loại giao hàng.
- Miễn phí chuyển phát nhanh từ 1.000.000đ; miễn phí chuyển phát nhanh trong ngày từ 5.000.000đ.
- Nhập người nhận khác người mua.
- Thanh toán thẻ tín dụng qua PaymentAdapter mô phỏng.
- Ghi nhận đơn hàng, chi tiết đơn hàng và giao dịch thanh toán.
- Gửi email xác nhận qua EmailAdapter mô phỏng; không đưa CSV/CVV vào CSDL/email.

## Cấu hình database
Mở `src/main/resources/db.properties` và sửa theo SQL Server của máy.

Mặc định:
``properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=EShopping;encrypt=false;trustServerCertificate=true
db.user=sa
db.password=123
``

Nếu tài khoản/password khác, hãy thay lại.

## Tạo CSDL
1. Mở SQL Server Management Studio.
2. Chạy file `EShopping_Database.sql`.
3. Sau khi tạo xong CSDL `EShopping`, kiểm tra lại `db.properties`.

## Chạy project
1. Mở project bằng IntelliJ IDEA/NetBeans/Eclipse.
2. Reload Maven.
3. Chạy `com.eshopping.Main`.
4. Có thể bấm `Kiểm tra CSDL` trên màn hình chính.
5. Chọn `Đăng ký` để tạo tài khoản.
6. Đăng nhập, vào `Sản phẩm`, thêm sản phẩm vào `Giỏ hàng`, sau đó `Đặt hàng`.

## Thẻ test cho PaymentAdapter
PaymentAdapter hiện là mock prototype. Có thể nhập dữ liệu hợp lệ về hình thức/độ dài:
- VISA/Mastercard/Discover: 16 chữ số, CSV 3 chữ số.
- American Express: 15 chữ số, CSV 4 chữ số.
- Ngày hết hạn phải chưa qua.

Ví dụ:
- Loại thẻ: VISA
- Số thẻ: 4111111111111111
- Ngày hết hạn: 2030-12
- Tên chủ thẻ: NGUYEN VAN A
- CSV/CVV: 123

## Kiểm tra luồng đặt hàng
- Tạo tài khoản.
- Đăng nhập.
- Chọn sản phẩm và số lượng.
- Mở giỏ hàng.
- Chọn loại phiếu + khu vực.
- Kiểm tra tiền hàng, phí giao hàng, tổng thanh toán.
- Nhập người nhận.
- Thanh toán.
- Kiểm tra dữ liệu trong các bảng `DonHang`, `ChiTietDonHang`, `GiaoDichThanhToan`.

## Lỗi thường gặp
### Login failed for user 'sa'
Kiểm tra `db.password` và SQL Server Authentication.

### Cannot open database 'EShopping'
Chạy `EShopping_Database.sql` trước.

### Connection refused / TCP 1433
Kiểm tra SQL Server đang chạy và TCP/IP/port 1433.

### Email không đến hộp thư thật
Prototype dùng `SmtpEmailAdapter` dạng mock, nội dung email được in ra Console. Có thể thay adapter bằng SMTP/API thật sau.

### Sản phẩm không nằm trong SQL Server
Đúng thiết kế: sản phẩm thuộc hệ thống quản lý sản phẩm bên ngoài, prototype này mô phỏng bằng `ProductServiceAdapter`.
