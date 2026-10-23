# Bài 5 - Quản lý hồ sơ bệnh án của bệnh nhân (Java)

Prototype học tập hiện thực theo mô tả Bài 5: Java 17, Java Swing, JDBC, SQL Server và Maven. Đây là phần mềm mẫu để thực hành phân tích thiết kế hướng đối tượng; không dùng để chẩn đoán hoặc điều trị bệnh nhân thật.

## 1. Công nghệ và kiến trúc

- Java 17
- Java Swing
- JDBC - Microsoft SQL Server JDBC Driver
- Microsoft SQL Server
- Maven
- Kiến trúc: `Swing UI -> Service -> Db/JDBC -> SQL Server`

## 2. Chức năng

1. Đăng ký bệnh nhân lần đầu, ghi nhận lệ phí năm và cấp mã hồ sơ bệnh án trong một transaction; thu lệ phí các năm tiếp theo cho bệnh nhân đã có hồ sơ.
2. Tra cứu bệnh nhân theo mã bệnh nhân, mã hồ sơ, họ tên hoặc số điện thoại; xem lịch sử lệ phí, phiếu khám, xét nghiệm và toa thuốc.
3. Đặt lịch khám sức khỏe định kỳ theo quý; mỗi bệnh nhân tối đa một lịch hẹn mỗi quý/năm trong prototype.
4. Lập phiếu khám, tạo đợt điều trị mới hoặc thêm lượt khám vào đợt đang mở; hỗ trợ liên kết lịch định kỳ.
5. Tạo yêu cầu xét nghiệm gắn trực tiếp với phiếu khám, ghi nhận kết quả.
6. Lập toa thuốc cùng chi tiết thuốc trong transaction; toa tham chiếu `VisitID`.
7. Lập hóa đơn viện phí, ghi nhận một hoặc nhiều khoản thanh toán; chỉ hoàn tất đợt điều trị khi đã thanh toán đủ.
8. Thống kê tổng quan và số lịch khám theo quý.

## 3. Cấu trúc thư mục

```text
ProjectJava/
├── pom.xml
├── README.md
├── Database/QuanLyHoSoBenhAn.sql
└── src/main/
    ├── resources/db.properties
    └── java/com/benhvien/
        ├── Main.java
        ├── data/Db.java
        ├── model/PrescriptionItemData.java
        ├── service/
        │   ├── PatientService.java
        │   ├── DirectoryService.java
        │   ├── AppointmentService.java
        │   ├── ClinicalService.java
        │   ├── BillingService.java
        │   └── ReportService.java
        └── forms/
            ├── FrmMain.java
            ├── FrmPatients.java
            ├── FrmHistory.java
            ├── FrmAppointments.java
            ├── FrmExaminations.java
            ├── FrmLabTests.java
            ├── FrmPrescriptions.java
            ├── FrmBilling.java
            └── FrmReports.java
```

## 4. Cấu hình SQL Server

1. Mở SQL Server Management Studio.
2. Chạy `Database/QuanLyHoSoBenhAn.sql` bằng tài khoản có quyền tạo database/bảng.
3. Mở `src/main/resources/db.properties` và sửa thông tin theo máy:

```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=QuanLyHoSoBenhAn;encrypt=true;trustServerCertificate=true
db.user=sa
db.password=YOUR_SQL_PASSWORD
```

Dùng TCP port cụ thể, không dùng `instanceName`, giúp tránh phụ thuộc SQL Server Browser/UDP 1434. Nếu máy dùng port khác, thay `1433` bằng TCP port đúng. Nếu dùng Windows Authentication, cần cấu hình JDBC URL và driver tương ứng theo môi trường máy.

**Lưu ý về lệ phí:** PDF không quy định số tiền lệ phí năm. Script chèn mức demo `500000` cho năm hiện tại vào `AnnualFeeRate`; đây chỉ là dữ liệu ví dụ, có thể cập nhật trong SQL Server. Muốn đăng ký cho một năm khác, hãy cấu hình trước dòng `AnnualFeeRate` cho năm đó.

## 5. Chạy project

Mở thư mục `ProjectJava` bằng IntelliJ IDEA / Eclipse / VS Code có hỗ trợ Maven, chọn JDK 17, reload Maven, sau đó chạy `com.benhvien.Main`.

Hoặc trong terminal ở thư mục có `pom.xml`:

```bash
mvn clean compile exec:java
```

## 6. Dữ liệu và quy tắc quan trọng

- Một bệnh nhân có một hồ sơ bệnh án; mã hồ sơ là duy nhất.
- Một bệnh nhân chỉ được ghi nhận một lần đóng lệ phí mỗi năm (`UNIQUE(PatientID, FeeYear)`). Việc đăng ký lần đầu, ghi nhận lệ phí và cấp hồ sơ cùng transaction; lỗi ở bất kỳ bước nào sẽ rollback.
- Lịch khám định kỳ được lưu với năm và quý, duy nhất theo bệnh nhân/năm/quý.
- Xét nghiệm và toa thuốc phải tham chiếu phiếu khám bằng `VisitID`.
- Mỗi phiếu khám có tối đa một toa đang lập trong prototype (`UNIQUE(VisitID)`). Toa và chi tiết thuốc được lưu transaction.
- Có thể ghi nhiều khoản thanh toán cho một hóa đơn. Không cho vượt số còn nợ.
- Không thể hoàn tất đợt điều trị nếu chưa lập hóa đơn hoặc tổng thanh toán thấp hơn viện phí.

## 7. Kịch bản kiểm thử

Xem `TestCases_Traceability.md` trong thư mục project để tìm mã test, bước thực hiện, kết quả mong đợi và truy vết từ yêu cầu đến service/bảng dữ liệu.

## 8. Ghi chú an toàn

Prototype dùng dữ liệu minh họa. Không lưu nội dung chẩn đoán thật, thông tin định danh nhạy cảm hoặc quyết định lâm sàng ngoài môi trường được phép. Hệ thống chưa triển khai phân quyền nhân viên, mã hóa dữ liệu y tế, audit log, sao lưu hay chuẩn tích hợp y tế; cần bổ sung trước khi dùng thực tế.
