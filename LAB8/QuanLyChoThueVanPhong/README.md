# Bài 4 – Quản lý cho thuê văn phòng ở cao ốc

Prototype Java Swing + SQL Server cho môn Phân tích thiết kế hướng đối tượng. **Không dùng WinForms**. Mô hình dựa trên tệp đề `Bai_4_Cho_thue_van_phong_cao_oc.docx`, giải quyết đầy đủ 6 câu hỏi UML ở thư mục `uml/`.

## 1. Yêu cầu môi trường

- JDK 17 trở lên (mã nguồn dùng mức ngôn ngữ Java 17).
- Maven 3.8+ để tải dependency JDBC SQL Server (driver `com.microsoft.sqlserver:mssql-jdbc:12.6.4.jre11`).
- Microsoft SQL Server 2017+ và SQL Server Management Studio.
- IntelliJ IDEA hoặc VS Code có Java Extension Pack; chạy ứng dụng desktop (máy có giao diện đồ họa).

## 2. Khởi tạo CSDL và chạy

1. Trong SSMS, kết nối SQL Server và chạy **một lần** tệp `QuanLyChoThueVanPhong.sql` trên máy thử nghiệm. Script tự tạo CSDL `QuanLyChoThueVanPhong` nếu chưa có; để tránh mất dữ liệu, script dừng khi các bảng đã tồn tại.
2. Cấu hình `src/main/resources/db.properties`. Ví dụ:

   ```properties
   db.url=jdbc:sqlserver://localhost:1433;databaseName=QuanLyChoThueVanPhong;encrypt=true;trustServerCertificate=true
   db.user=sa
   db.password=MAT_KHAU_CUA_BAN
   ```

   SQL Server phải cho phép TCP/IP, cổng tương ứng, tài khoản đăng nhập có quyền cần thiết. Không đưa mật khẩu thật lên Git.
3. Mở **thư mục chứa `pom.xml`** trong IDE, chạy:

   ```bash
   mvn clean test
   mvn exec:java
   ```

   Hoặc chạy class `com.caococ.Main` bằng IDE sau khi tải dependencies.
4. Tại cửa sổ chính, chọn một trong 9 nhóm chức năng.

**Không chạy lại script tạo CSDL trên database có dữ liệu vận hành.** Nếu cần thử lại, tạo CSDL riêng hoặc sao lưu trước khi xử lý.

## 3. Cấu trúc nguồn

```text
QuanLyChoThueVanPhong/
├── pom.xml
├── QuanLyChoThueVanPhong.sql         # Chỉ 1 file SQL: schema, 5 stored procedures, dữ liệu mẫu
├── README.md
├── src/main/java/com/caococ/
│   ├── Main.java
│   ├── forms/
│   │   ├── FrmMain.java
│   │   ├── FrmModule.java
│   │   ├── FrmTable.java
│   │   ├── FormInputs.java
│   │   ├── FrmTangVanPhong.java
│   │   ├── FrmKhachHang.java
│   │   ├── FrmHopDong.java
│   │   ├── FrmThanhToan.java
│   │   ├── FrmDichVu.java
│   │   ├── FrmGiaHan.java
│   │   ├── FrmNhanVien.java
│   │   ├── FrmTheXe.java
│   │   └── FrmKiemTra.java
│   ├── service/
│   │   ├── OfficeService.java       # Gọi các stored procedure nghiệp vụ
│   │   └── CrudService.java         # CRUD qua JDBC và danh sách ComboBox
│   └── data/
│       └── Db.java                  # Kết nối / prepared statement
├── src/main/resources/db.properties
├── uml/                              # 9 file PlantUML (6 câu bắt buộc + 3 bổ sung)
└── tests/                            # Ma trận yêu cầu và test case
```

## 4. Form và cách sử dụng

| Form | Chức năng |
|---|---|
| FrmTangVanPhong | Quản lý tầng, mã phòng, diện tích, hướng, đơn giá, tình trạng |
| FrmKhachHang | Quản lý công ty thuê, đại diện, mã số thuế, điện thoại |
| FrmHopDong | Xem hợp đồng và chi tiết; nút **Ký hợp đồng và thu trước 6 tháng**. Chọn nhiều phòng bằng Ctrl trong danh sách. |
| FrmThanhToan | Xem hóa đơn, thanh toán; **Lập tiền thuê tháng 7+**, **Thu tiền hóa đơn** |
| FrmDichVu | Dịch vụ, tiêu thụ theo phòng và kỳ, **Chốt hóa đơn dịch vụ cuối tháng** |
| FrmGiaHan | Xem và **Lập phiếu gia hạn** có kiểm tra công nợ |
| FrmNhanVien | Thông tin nhân viên công ty thuê, ảnh dưới dạng đường dẫn minh họa |
| FrmTheXe | Thẻ giữ xe, nhân viên sở hữu, biển số xe |
| FrmKiemTra | Kiểm tra tình trạng phòng, ghi nhận chỉnh sửa không phép và tiền phạt minh họa |

- Có nút Thêm / Sửa / Xóa / Làm mới trên bảng CRUD cho phép sửa.
- Hợp đồng, hóa đơn, thanh toán và gia hạn được đánh dấu **chỉ xem** ở bảng dữ liệu; phải dùng nút nghiệp vụ để đảm bảo ràng buộc.
- Trường mã liên kết dùng ComboBox với nhãn `Mã — Tên`. Các trường số, ngày (`yyyy-MM-dd`), họ tên, nội dung và số tiền nhập trực tiếp.
- **Khi sai định dạng hoặc lỗi từ SQL Server**, thông báo hiện ra nhưng hộp nhập và các giá trị vẫn còn; người dùng sửa rồi bấm OK, chỉ bấm Cancel mới hủy.
- Dữ liệu trong bảng hiển thị tối đa 3.000 dòng/lượt để tránh làm giao diện treo trên dữ liệu quá lớn.

## 5. Quy tắc nghiệp vụ và giao dịch

| Mã | Quy tắc | Vị trí triển khai |
|---|---|---|
| BR01 | Mỗi tầng có nhiều phòng, mỗi phòng có mã duy nhất | `Tang`, `VanPhong` + UNIQUE |
| BR02 | Mỗi hợp đồng thuê tối thiểu 6 tháng | `HopDong.CHECK`, `sp_TaoHopDong` |
| BR03 | Một hợp đồng có thể gồm nhiều phòng | `ChiTietHopDong`, `sp_TaoHopDong` |
| BR04 | Thu trước toàn bộ 6 tháng đầu khi ký | Transaction `sp_TaoHopDong` tạo HĐ + hóa đơn + thanh toán cùng lúc |
| BR05 | Tiền thuê tháng 7+ trả đầu kỳ | `sp_LapHoaDonThueThang`; lập hóa đơn trước hoặc vào ngày bắt đầu kỳ |
| BR06 | Dịch vụ trả cuối tháng và tách khỏi tiền thuê | `SuDungDichVu`, `sp_LapHoaDonDichVu` |
| BR07 | Gia hạn không vượt 1/2 thời gian gốc | `sp_GiaHan` |
| BR08 | Gia hạn khi đã trả đầy đủ hóa đơn thuê/dịch vụ/phạt và dịch vụ đến hạn đã được lập hóa đơn | `sp_GiaHan` |
| BR09 | Nhân viên có mã, công ty thuê, phòng làm việc | `NhanVienVanPhong` + FK |
| BR10 | Thẻ giữ xe có mã, nhân viên và biển số xe | `TheGiuXe` + UNIQUE |
| BR11 | Kiểm tra phòng theo chu kỳ năm và ghi nhận thay đổi kết cấu trái phép | `KiemTraPhong`; lịch kiểm tra thực tế do người quản lý tổ chức |
| BR12 | Không thu tiền vượt công nợ | `sp_ThuTien` + khóa dữ liệu |

### Lưu ý kỹ thuật

- **Gia hạn nhiều phòng:** Tài liệu gốc yêu cầu phiếu gia hạn theo từng phòng; phiên bản prototype dùng giới hạn bảo toàn dữ liệu: `sp_GiaHan` chỉ chấp nhận hợp đồng **gồm một phòng**, mỗi phòng **một lần gia hạn**. Để hỗ trợ gia hạn độc lập nhiều phòng/multiple renewals, cần quản lý thời hạn ở bảng ChiTietHopDong thay vì kéo dài ngày kết thúc chung trên HopDong. Đây là giới hạn **chưa đạt đầy đủ yêu cầu thực tế**, không nên bỏ qua khi nghiệm thu.
- Hóa đơn thuê tháng 7+ được **lập** đầu kỳ; ghi nhận đã trả hay chưa phụ thuộc thao tác **Thu tiền hóa đơn**. Ứng dụng không tự trích tiền.
- Mức giá, hình thức phạt, đơn giá điện/nước trong script chỉ là **dữ liệu mẫu học tập**, không coi là biểu phí hay quy định thật.
- Phiếu kiểm tra có thể ghi tiền phạt, nhưng phiên bản này **chưa tự chuyển tiền phạt từ kiểm tra phòng sang hóa đơn PHAT**; việc đó cần bổ sung nghiệp vụ khi áp dụng thật.
- Không có đăng nhập/phân quyền, lịch chạy tự động, phần mềm điều phối ngân hàng, lưu ảnh nhị phân; đây là prototype giảng dạy.

## 6. CSDL mẫu

CSDL có **13 bảng**, **5 stored procedure**, **8 tầng, 48 phòng, 12 công ty, 10 hợp đồng**, nhân viên văn phòng, thẻ xe, kiểm tra phòng, dịch vụ và hóa đơn thanh toán minh họa. Cuối file SQL có truy vấn đếm bản ghi theo bảng.

Quy trình demo:
1. Mở `FrmTangVanPhong` và xem phòng đang thuê/trống.
2. Mở `FrmHopDong`, chọn công ty và nhiều phòng trống, chọn ít nhất 6 tháng, thực hiện ký + thanh toán trước.
3. Mở `FrmDichVu`, ghi số sử dụng dịch vụ, cuối tháng chốt hóa đơn.
4. Mở `FrmThanhToan`, lập hóa đơn tháng thứ 7 và thu tiền.
5. Mở `FrmGiaHan`, chọn hợp đồng/phòng đã trả đủ tất cả hóa đơn; nếu còn nợ, phần mềm từ chối và giữ nguyên ô nhập.

## 7. PlantUML

- `01_Cau1_Lop_ThuocTinh_PhuongThuc.puml`: Các lớp, ID, thuộc tính và phương thức.
- `02_Cau2_ClassDiagram_QuanHe.puml`: Liên kết, association, multiplicity.
- `03_Cau3_UseCaseTongQuat.puml`: Actor và Use Case tổng quát.
- `04_Cau4_UseCasePhanRa_ThueDenThang7.puml`: Use Case phân rã.
- `05_Cau5_Activity_ThueDenThang7.puml`: Activity Diagram.
- `06_Cau6_Sequence_GiaHan.puml`: Sequence Diagram gia hạn.
- `07_State_Phong.puml`, `08_KienTruc_Java.puml`, `09_ERD_SQLServer.puml`: Bổ sung.

Có thể xuất PNG từ mã PlantUML bằng extension VS Code hoặc dịch vụ PlantUML. Các sơ đồ nguồn có thể được chỉnh sửa để phù hợp mẫu bài tập của giảng viên.

## 8. Kiểm thử và giới hạn xác minh

Xem `tests/KichBanKiemThu.md` và `tests/TruyVetYeuCau.csv` cho 18 kịch bản + truy vết. Không tự đánh dấu "Đạt" trước khi chạy trên SQL Server đích.

**Đã xác minh trong quá trình tạo:** toàn bộ Java biên dịch thành công với `javac --release 17` và gói ZIP được kiểm tra toàn vẹn. **Chưa xác minh:** chạy `mvn clean test`, khởi tạo và thực thi SQL Server thực tế, chạy UI kết nối DB, chụp minh chứng kiểm thử. Không dùng ứng dụng trên dữ liệu thật khi chưa có bảo mật và kiểm thử tích hợp.
