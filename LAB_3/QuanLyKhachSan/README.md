# Bài 3 - Hệ thống quản lý khách sạn
## 1. Thông tin sinh viên
Họ và tên: Trương Thị Hương Giang
MSSV: 1250080046
Tên bài Lab: Bài 3 - Hệ thống quản lý khách sạn
## 2. Môi trường / Version
Java: JDK 21
GUI: Java Swing
Database: Microsoft SQL Server
Kết nối: JDBC
Build: Maven
IDE: IntelliJ IDEA / Eclipse / VS Code
## 3. Nội dung đã thực hiện
Quản lý danh mục, phòng và tiện nghi.
Lắp đặt/luân chuyển tiện nghi.
Đặt phòng, nhận phòng và quản lý người lưu trú.
Ghi nhận và cộng dồn dịch vụ trong ngày.
Trả phòng, đền bù hư hỏng/mất mát.
Lập hóa đơn và thanh toán.
Thống kê.
Kiểm tra các quy tắc sức chứa, trùng lịch và thanh toán.
## 4. Kết quả
Xây dựng đầy đủ các Form chính của bài Lab.
Kết nối thành công Java với SQL Server bằng JDBC.
Các chức năng nghiệp vụ chính đã được cài đặt và kiểm tra.
Có thể build project bằng Maven và chạy chương trình từ Main.
## 5. Lỗi gặp phải
Kết nối SQL Server bằng tên instance ban đầu bị timeout do port không được xác định tự động.
Một số file source bị không đồng nhất giữa UiUtil và UI, gây lỗi UiUtil cannot be resolved.
## 6. Cách khắc phục
Kiểm tra port SQL Server và cấu hình JDBC bằng port cụ thể trong config.properties.
Đồng bộ lại package, import và tên class helper giao diện để thống nhất UI.
Kiểm tra lại project bằng mvn clean package sau khi sửa.
## 7. Hướng dẫn giảng viên kiểm tra / chạy lại
Bước 1: Tạo database
Mở SQL Server Management Studio và chạy:
database/QuanLyKhachSan.sql
Bước 2: Cấu hình kết nối
Mở:
src/main/resources/config.properties
và cập nhật thông tin SQL Server:
db.url=jdbc:sqlserver://localhost:PORT;databaseName=QuanLyKhachSan;encrypt=true;trustServerCertificate=true;loginTimeout=10
db.user=sa
db.password=YOUR_PASSWORD
db.integratedSecurity=false
Bước 3: Kiểm tra kết nối
Chạy:
com.quanlykhachsan.TestConnection
Nếu thành công sẽ hiển thị thông báo kết nối SQL Server thành công.
Bước 4: Chạy chương trình
Chạy:
com.quanlykhachsan.Main
hoặc:
mvn clean package
java -jar target/QuanLyKhachSanJava-1.0.0.jar