# Bài 6 – Quản lý công ty du lịch Văn Hóa Việt

## 1. Thông tin sinh viên
- Họ tên: Trương Thị Hương Giang
- MSSV: 1250080046
- Bài lab: Bài 6 – Quản lý công ty du lịch

## 2. Công nghệ
- Java 17
- Java Swing
- JDBC
- Microsoft SQL Server
- Maven

## 3. Chức năng
- **Danh mục:** phương tiện, điểm bán vé, hướng dẫn viên, điểm tham quan.
- **Tour – hành trình:** thêm tour, điểm dừng, phương tiện theo chặng, điểm tham quan theo tour.
- **Lịch chuyến khách lẻ:** tạo chuyến, tự tính ngày về theo `ngày đi + số ngày tour - 1`, đóng đăng ký.
- **Đăng ký khách lẻ:** kiểm tra số người 1–11, chuyến còn mở và chưa khởi hành; tính tiền theo đơn giá tour × số người và ghi nhận đã thanh toán.
- **Đăng ký đoàn:** yêu cầu trên 12 người, ngày đi trong tương lai, tiền cọc dương và không vượt tổng tiền dự kiến; nếu mua bảo hiểm, danh sách thành viên phải đủ số người. Ghi đoàn, phiếu và thành viên trong một transaction.
- **Hủy đăng ký đoàn:** chỉ cho hủy phiếu còn trạng thái “Đã đăng ký” trước ngày đi; gỡ phân công HDV và đánh dấu “Hủy - mất cọc” trong transaction.
- **Phân công HDV:** một HDV không được trùng lịch; một chuyến khách lẻ chỉ có một HDV; đoàn có thể được phân công nhiều HDV.
- **Kết thúc tour – khảo sát:** ghi nhận các lần thanh toán kinh phí đoàn sau ngày kết thúc, không cho thanh toán vượt số còn lại; gửi tối đa một phiếu khảo sát cho mỗi đăng ký; ghi điểm 1–5 và góp ý.
- **Lương – thống kê:** lương tháng = lương cơ bản + tổng thù lao các tour có ngày kết thúc trong tháng; thống kê đăng ký, tiền cọc bị mất, thanh toán và phản hồi khảo sát trong khoảng ngày.

## 4. Kiến trúc
```text
Java Swing Forms → Services → Data/Db.java → SQL Server
```
Các Form không chứa câu SQL trực tiếp. Kiểm tra trùng lịch HDV và nghiệp vụ transaction nằm trong Service.

## 5. Cấu trúc project
```text
QuanLyCongTyDuLich_Java/
├── pom.xml
├── README.md
├── Database/
│   └── QuanLyCongTyDuLich.sql
└── src/main/
    ├── resources/db.properties
    └── java/com/tourcompany/
        ├── Main.java
        ├── data/Db.java
        ├── services/
        │   ├── KetQuaXuLy.java
        │   ├── QuyDinh.java
        │   ├── ThanhVienDoanItem.java
        │   ├── DanhMucService.java
        │   ├── TourService.java
        │   ├── ChuyenLeService.java
        │   ├── DangKyLeService.java
        │   ├── DangKyDoanService.java
        │   ├── PhanCongService.java
        │   ├── KetThucService.java
        │   └── ThongKeService.java
        └── forms/
            ├── FrmMain.java
            ├── TablePanel.java
            ├── Ui.java
            ├── FrmDanhMuc.java
            ├── FrmTour.java
            ├── FrmChuyenLe.java
            ├── FrmDangKyLe.java
            ├── FrmDangKyDoan.java
            ├── FrmPhanCongHDV.java
            ├── FrmKetThucKhaoSat.java
            └── FrmLuongThongKe.java
```

## 6. Chuẩn bị SQL Server
1. Mở SQL Server Management Studio (SSMS).
2. Mở và chạy `Database/QuanLyCongTyDuLich.sql` bằng tài khoản có quyền tạo database.
3. Script tạo database `QuanLyCongTyDuLich`, các bảng/ràng buộc và dữ liệu mẫu.
4. **Lưu ý:** script hiện tại có `DROP TABLE` để tạo lại cấu trúc và seed mẫu. Nếu database đã có dữ liệu cần giữ, hãy sao lưu trước khi chạy lại.

## 7. Cấu hình JDBC
Mở `src/main/resources/db.properties`:
```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=QuanLyCongTyDuLich;encrypt=false;trustServerCertificate=true;loginTimeout=10
db.user=sa
db.password=123
```
Sửa `db.user` và `db.password` theo SQL Server của bạn. Mật khẩu `123` chỉ là giá trị mẫu trong file, không mặc định đúng trên mọi máy.

Project dùng **TCP/IP + port** để tránh phụ thuộc SQL Server Browser và UDP 1434. Nếu máy bạn dùng port khác:
1. Mở **SQL Server Configuration Manager**.
2. Vào `SQL Server Network Configuration` → `Protocols for <instance>` → bật `TCP/IP`.
3. Trong tab `IP Addresses`, kiểm tra `IPAll` và thiết lập `TCP Port` cố định (ví dụ `1433`); xóa giá trị trong `TCP Dynamic Ports` nếu chuyển sang cổng cố định.
4. Restart dịch vụ SQL Server tương ứng.
5. Sửa port trong `db.url` cho khớp. Nếu cần truy cập từ máy khác, thay `localhost` bằng tên máy/địa chỉ máy chủ và cho phép TCP port qua firewall.

Nếu dùng **Named Instance** như `MAYTINH\\SQLEXPRESS`, đừng tự ghép `instanceName` khi chưa cấu hình SQL Server Browser. Cách dễ kiểm soát nhất cho JDBC là bật TCP/IP, xác định port thực tế và khai báo trực tiếp port trong URL.

## 8. Chạy chương trình
Yêu cầu JDK 17 và Maven.

**IntelliJ IDEA / Eclipse:**
1. Open thư mục chứa `pom.xml`.
2. Chọn JDK 17, reload Maven để tải driver `mssql-jdbc`.
3. Kiểm tra `db.properties`.
4. Chạy `com.tourcompany.Main`.

**Dòng lệnh** từ thư mục project:
```bash
mvn clean compile exec:java
```

Ở màn hình chính có thể mở từng module. Nút **Kiểm tra kết nối CSDL** giúp kiểm tra cấu hình JDBC.

## 9. Dữ liệu mẫu để thử
- Tour: `T001`, `T002`, `T003`.
- Chuyến khách lẻ: `CL001`, `CL002`, `CL003`.
- Điểm bán vé: `DB01`, `DB02`.
- HDV: `HDV01`, `HDV02`, `HDV03`.
- Đăng ký đoàn mẫu: `DD001`, `DD002`.
- Đăng ký lẻ mẫu: `DKL001`, `DKL002`.

Một số dữ liệu mẫu có ngày đi/kết thúc trong quá khứ để thử thanh toán đoàn và khảo sát. Để thử tạo chuyến/đăng ký mới, dùng ngày đi trong tương lai.

### Quy trình test gợi ý
1. Mở **Danh mục**, kiểm tra dữ liệu phương tiện, điểm bán vé, HDV và điểm tham quan.
2. Mở **Tour – hành trình**, xem tour có sẵn; thêm tour hoặc các thành phần hành trình.
3. Mở **Lịch chuyến khách lẻ**, tạo chuyến bằng một tour đang mở bán. Ngày về được tự tính.
4. Mở **Đăng ký khách lẻ**, dùng chuyến đang mở (ví dụ `CL002`) và điểm bán (`DB01`). Thử nhập 12 người để xác nhận hệ thống từ chối.
5. Mở **Đăng ký theo đoàn**, tạo phiếu trên 12 người và cọc nhỏ hơn/tối đa bằng tổng dự kiến. Thử mua bảo hiểm với danh sách thành viên không đủ số người để kiểm tra validation.
6. Mở **Phân công HDV**, phân công `HDV01` cho `CL002`; thử phân công lại cùng HDV vào một khoảng ngày chồng lấn để kiểm tra bị từ chối.
7. Mở **Kết thúc tour – khảo sát**, chọn `DD001` để ghi nhận thanh toán sau tour. Không nhập số tiền vượt số còn lại. Gửi khảo sát cho `DKL001` hoặc `DD001` (nếu chưa có khảo sát) và ghi nhận điểm 1–5.
8. Mở **Lương – thống kê**, chạy tính lương theo tháng/năm và thống kê theo khoảng ngày.

## 10. Quy tắc nghiệp vụ chính
- Mọi tour khởi hành và kết thúc tại TP.HCM; điểm dừng được đánh thứ tự, điểm cuối tour nên là TP.HCM.
- Khách lẻ: 1–11 người; đoàn: trên 12 người. Đúng 12 người bị từ chối theo quyết định xử lý của tài liệu vì đề không quy định trường hợp này.
- Ngày về/ngày kết thúc dự kiến = ngày đi + số ngày tour − 1.
- Khách đoàn phải đặt cọc; không đi thì mất cọc; thanh toán phần còn lại sau khi tour kết thúc.
- Đoàn mua bảo hiểm phải có danh sách đủ số người.
- Một chuyến khách lẻ có đúng một HDV; HDV không được trùng lịch.
- Lương tháng = lương cơ bản + tổng thù lao tour kết thúc trong tháng.
- Khảo sát chỉ gửi sau khi tour kết thúc, một khảo sát/đăng ký; điểm đánh giá từ 1 đến 5.

## 11. Lỗi thường gặp
- **Login timeout / instance không tìm thấy:** kiểm tra `db.url`, TCP/IP, port, SQL Server service và firewall. Đừng dùng port `1433` nếu instance thật đang lắng nghe cổng khác.
- **Login failed for user `sa`:** kiểm tra SQL Server Authentication, tài khoản `sa` đã enabled và mật khẩu đúng.
- **Cannot open database:** chạy script SQL trên đúng SQL Server/instance mà URL JDBC đang trỏ đến.
- **Trùng khóa / FK violation:** mã chính phải duy nhất; các mã tour, điểm bán, HDV, chuyến và phiếu tham chiếu phải tồn tại trước khi tạo dữ liệu liên quan.

## 12. Giới hạn prototype
Đây là prototype desktop theo phạm vi bài thực hành. Phần nghiệp vụ quản lý tour, lịch chuyến, đăng ký, phân công, thanh toán đoàn, khảo sát và tính lương kết nối SQL Server thật; chưa bao gồm website công khai cho khách hàng hoặc cổng thanh toán bên ngoài.
