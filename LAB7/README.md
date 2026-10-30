
# QUẢN LÝ NHÂN SỰ – CÔNG TY SƠN ĐÔNG Á

> **Bài 7 – Thực hành Phân tích và Thiết kế hướng đối tượng**  
> **Công nghệ:** Java 25 · Java Swing · JDBC · Microsoft SQL Server · Maven  
> **Kiến trúc:** `forms → service → data`  
> **Trạng thái:** Prototype phục vụ học tập, chưa xác nhận kiểm thử tích hợp trên SQL Server trong môi trường phát triển tài liệu.

## 1. Giới thiệu

Đề tài xây dựng ứng dụng desktop hỗ trợ quản lý nhân sự tại Công ty Sơn Đông Á theo các nghiệp vụ: tuyển dụng và thử việc; hợp đồng lao động và bảo hiểm; quyết định lương/chức vụ; kế hoạch tuần, phân ca và chấm công; phụ trội; khen thưởng/kỷ luật; tính lương hằng tháng. Bản Java được lựa chọn thay cho yêu cầu C# WinForms trong đề, giữ mục tiêu phân tích hướng đối tượng, tổ chức hệ thống theo tầng và SQL Server.

Giao diện chính là bảng chọn **3 × 3**: tám nhóm chức năng và nút thoát. Các nhóm mở Form riêng; không có tab tên **“Nghiệp vụ”**. Nút xử lý nghiệp vụ được đặt trong tab/bảng dữ liệu có liên quan. Dòng phụ đề (`sub`) dưới tiêu đề và các ô ở `FrmMain` đã được loại bỏ.

## 2. Yêu cầu môi trường

| Thành phần | Yêu cầu |
|---|---|
| Java Development Kit | **JDK 25 (LTS)** |
| Build | Apache Maven (sử dụng `mvn` trong terminal) |
| Hệ quản trị dữ liệu | Microsoft SQL Server; SSMS để chạy SQL và xem dữ liệu |
| Giao diện | Java Swing, chạy trên máy tính có môi trường đồ họa |
| IDE | VS Code với Java Extension Pack, IntelliJ IDEA, hoặc IDE Java tương đương |
| JDBC | `com.microsoft.sqlserver:mssql-jdbc:12.8.1.jre11` (khai báo trong `pom.xml`) |
| Kiểm thử đơn vị | JUnit Jupiter `5.11.4` |

Cổng SQL mặc định trong file cấu hình là `1433`; nếu SQL Server của máy bạn chạy cổng khác hoặc là named instance, phải chỉnh URL JDBC tương ứng. Kiểm tra SQL Server đang chạy, TCP/IP đã bật và tài khoản có quyền truy cập CSDL.

## 3. Cấu trúc project

```text
QuanLyNhanSuDongA/
├── pom.xml
├── README.md
├── QuanLyNhanSuDongA.sql      # Một file SQL chính: CSDL + bảng + seed
├── CAU_TRUC_DU_AN.md
├── DU_LIEU_MAU_MO_RONG.md
├── THIET_KE_LAI.md
├── src/
│   ├── main/
│   │   ├── java/com/donga/
│   │   │   ├── Main.java
│   │   │   ├── forms/
│   │   │   │   ├── FrmMain.java
│   │   │   │   ├── FrmNghiepVu.java  # Lớp cơ sở giao diện, KHÔNG phải tab
│   │   │   │   ├── FrmNhanVien.java
│   │   │   │   ├── FrmTuyenDung.java
│   │   │   │   ├── FrmHopDongBaoHiem.java
│   │   │   │   ├── FrmLuongChucVu.java
│   │   │   │   ├── FrmKeHoach.java
│   │   │   │   ├── FrmChamCong.java
│   │   │   │   ├── FrmThuongKyLuat.java
│   │   │   │   └── FrmBangLuong.java
│   │   │   ├── service/
│   │   │   │   ├── Module.java
│   │   │   │   ├── CrudService.java
│   │   │   │   ├── HrService.java
│   │   │   │   ├── WorkforceService.java
│   │   │   │   └── PayrollMath.java
│   │   │   └── data/Db.java
│   │   └── resources/db.properties
│   └── test/java/com/donga/service/PayrollMathTest.java
├── uml/                      # 10 sơ đồ PlantUML
└── tests/
    ├── KICH_BAN_KIEM_THU.md
    └── TRUY_VET_YEU_CAU.md
```

### Trách nhiệm các package

- **`forms`**: trình bày dữ liệu, bảng và hộp thoại; nhận thao tác người dùng; hiển thị lỗi; chứa các Form theo nhóm nghiệp vụ.
- **`service`**: kiểm tra đầu vào, tính toán, kiểm soát điều kiện nghiệp vụ và transaction; `Module` mô tả các trường/bảng để tái sử dụng giao diện quản lý.
- **`data`**: `Db.connect()` tạo kết nối JDBC và đọc cấu hình từ `db.properties` hoặc biến môi trường.

## 4. Cài đặt và chạy lần đầu

### Bước 1 – Giải nén và mở project

Giải nén file ZIP, sau đó mở **thư mục `QuanLyNhanSuDongA` chứa `pom.xml`**, không chỉ mở riêng một file `.java`.

### Bước 2 – Tạo cơ sở dữ liệu SQL Server

Trong **SQL Server Management Studio (SSMS)**, kết nối đúng máy chủ, mở `QuanLyNhanSuDongA.sql` rồi thực thi **một lần trên CSDL thử nghiệm mới**. Script tạo CSDL `QuanLyNhanSuDongA`, **18 bảng**, khóa chính/ngoại, các ràng buộc và dữ liệu mẫu mở rộng.

> **Cẩn thận:** Script chính có lệnh tạo bảng và nạp dữ liệu. **Không chạy lại toàn bộ script trên CSDL đã tồn tại**; hãy sao lưu trước khi điều chỉnh. Nếu chỉ muốn chuẩn hóa tên nhân viên mẫu đã nhập từ phiên bản cũ, tìm đoạn `CHUẨN HÓA TÊN NHÂN VIÊN MẪU` ở cuối SQL và chỉ thực thi đoạn thích hợp sau khi kiểm tra điều kiện của nó. Không cần một file SQL sửa tên riêng.

### Bước 3 – Cấu hình kết nối

Mở `src/main/resources/db.properties`:

```properties
db.url=jdbc:sqlserver://localhost:1433;databaseName=QuanLyNhanSuDongA;encrypt=true;trustServerCertificate=true
db.user=sa
db.password=CHANGE_ME
```

Thay `CHANGE_ME` bằng mật khẩu trên máy bạn và chỉnh server/cổng/tên DB khi cần. `trustServerCertificate=true` chỉ nên dùng trong môi trường thực hành tin cậy, không phải khuyến nghị mặc định khi triển khai thật. **Không đưa mật khẩu thực lên GitHub hoặc đính kèm vào bài nộp công khai.**

`Db.java` hỗ trợ cấu hình qua các biến môi trường `DB_URL`, `DB_USER`, `DB_PASSWORD` (các biến này được ưu tiên hơn thuộc tính trong file).

### Bước 4 – Biên dịch, kiểm thử và chạy

Trong terminal của thư mục chứa `pom.xml`:

```bash
java -version
mvn -version
mvn clean test
mvn exec:java
```

Hoặc chạy `com.donga.Main` bằng IDE sau khi Maven đã tải dependency. Từ màn hình chính, có thể nhấn **Kiểm tra kết nối SQL Server** trước khi mở các Form.

### Bước 5 – Khi kết nối lỗi

Kiểm tra lần lượt: SQL Server service đã chạy → TCP/IP và cổng thực tế → `db.url` → tài khoản/mật khẩu → CSDL `QuanLyNhanSuDongA` đã được tạo → SQL Server cho phép xác thực phù hợp. Nếu dùng named instance, cần xác định đúng cấu hình và cổng; không mặc định mọi máy đều có SQL Server trên `localhost:1433`.

## 5. Danh sách Form và chức năng

| Form | Dữ liệu/nghiệp vụ chính |
|---|---|
| `FrmNhanVien` | Nhân viên, phòng ban, chức vụ; thao tác tuyển dụng liên quan |
| `FrmTuyenDung` | Quyết định tuyển dụng, thử việc, cập nhật kết quả đánh giá |
| `FrmHopDongBaoHiem` | Hợp đồng dài/ngắn hạn, tạo hồ sơ BHXH/BHYT theo điều kiện |
| `FrmLuongChucVu` | Quyết định lương, phụ cấp, tăng lương và chức vụ |
| `FrmKeHoach` | Ca làm, kế hoạch tuần, phân công ca |
| `FrmChamCong` | Chấm công, phiếu phụ trội, nhân viên tham gia phụ trội |
| `FrmThuongKyLuat` | Quyết định khen thưởng/kỷ luật, cá nhân/tập thể, phiếu thu–chi và phân bổ |
| `FrmBangLuong` | Tính, xem bảng lương và chi tiết lương theo kỳ |

Các thao tác nhiều bảng phải đi qua các nút nghiệp vụ tương ứng để Service kiểm tra quy tắc và dùng transaction. Một số bảng được giới hạn chỉnh sửa trực tiếp nhằm tránh bỏ qua kiểm tra nghiệp vụ.

## 6. Cách nhập liệu sau khi cải tiến giao diện

- **ComboBox:** Chọn phòng ban, nhân viên, chức vụ, hợp đồng, kế hoạch, ca làm, các mã tham chiếu và những trạng thái/loại/hình thức có danh sách cố định. Danh sách liên kết thường hiển thị **mã — tên**, còn giá trị ghi CSDL là mã tương ứng.
- **Nhập tay:** Họ tên, số điện thoại, địa chỉ, ngày, số tiền, số hợp đồng/quyết định, điều khoản, lý do và ghi chú.
- **Nhập sai không mất dữ liệu:** Nếu định dạng, ràng buộc nghiệp vụ hoặc câu lệnh CSDL bị từ chối, đóng hộp thoại thông báo lỗi sẽ trở lại hộp nhập với **các giá trị trước đó được giữ lại** để sửa và lưu tiếp. Chỉ chọn **Hủy** khi muốn thoát nhập liệu.
- **Tải lại lựa chọn:** Sau khi thêm danh mục ở Form khác, đóng và mở lại hộp thoại nhập để lấy dữ liệu ComboBox mới.
- **Họ tên mẫu:** Cột `NhanVien.HoTen` chỉ chứa tên, mã riêng là `NhanVien.MaNV`; không ghép mã vào tên.

## 7. Quy trình thao tác minh họa

1. **Danh mục:** Kiểm tra phòng ban, chức vụ và ca làm đã có dữ liệu mẫu.
2. **Tuyển dụng:** Lập quyết định tuyển dụng, ghi ngày bắt đầu/kết thúc thử việc, mức lương thử việc và điều khoản.
3. **Đánh giá thử việc:** Cập nhật kết quả `DAT` sau khi đánh giá. Chỉ ký hợp đồng chính thức khi đáp ứng các điều kiện thử việc trong Service.
4. **Hợp đồng và quyết định:** Ký `DAI_HAN` hoặc `NGAN_HAN`; ghi nhận quyết định lương, chức vụ và phụ cấp. Với hợp đồng dài hạn, tạo hồ sơ bảo hiểm phù hợp.
5. **Kế hoạch tuần:** Tạo kế hoạch có ngày đầu tuần là **thứ Hai**; phân nhân viên vào ca/ngày của phòng ban đúng quan hệ.
6. **Chấm công và phụ trội:** Ghi công theo ca; tạo phiếu phụ trội và từng nhân viên tham gia. Theo bài tập, hệ số phụ trội cố định **1,5**.
7. **Khen thưởng/kỷ luật:** Tạo quyết định cho cá nhân hoặc phòng ban. Với quyết định tập thể, phân bổ các phiếu thu/chi cho nhân viên theo chức năng được cung cấp; tránh trùng tính khoản đã áp dụng.
8. **Tính lương:** Chọn tháng/năm và số giờ chuẩn; dùng chức năng tính lương để tổng hợp công, phụ trội, phụ cấp, thưởng/phạt và khấu trừ bảo hiểm.

### Công thức lương trong prototype

Mô hình minh họa dựa trên lương giờ và số giờ thực tế:

```text
LuongGio = LuongCoBan / GioChuan
TienCong = LuongGio * GioLam
TienPhuTroi = LuongGio * GioPhuTroi * 1.5
ThucLinh = TienCong + PhuCapLuong + PhuCapDocHai
         + PhuCapChucVu + TienPhuTroi + TienThuong
         - TienPhat - TruBaoHiem
```

Ví dụ kiểm thử: lương cơ bản **8.000.000**, giờ chuẩn **160**, giờ công **160**, phụ cấp tổng **1.000.000**, phụ trội **10 giờ**, thưởng **500.000**, phạt **200.000**, bảo hiểm **840.000** ⇒ **thực lĩnh 9.210.000 đồng**. Đây là **kịch bản kiểm thử**, không phải quy định trả lương hoặc tỷ lệ đóng bảo hiểm thực tế.

## 8. Cơ sở dữ liệu và dữ liệu mẫu

File SQL chính: **`QuanLyNhanSuDongA.sql`**. Hệ thống có **18 bảng**:

| Nhóm | Các bảng |
|---|---|
| Danh mục và nhân sự | `PhongBan`, `ChucVu`, `NhanVien` |
| Tuyển dụng, hợp đồng, quyết định | `QuyetDinhTuyenDung`, `HopDong`, `QuyetDinhLuong`, `QuyetDinhChucVu`, `HoSoBaoHiem` |
| Kế hoạch và công | `CaLamViec`, `KeHoachTuan`, `PhanCongCa`, `ChamCong`, `PhieuPhuTroi`, `ChiTietPhuTroi` |
| Thưởng/phạt, lương | `QuyetDinhThuongPhat`, `PhieuThuChi`, `BangLuong`, `ChiTietLuong` |

Dữ liệu mẫu trong script được **thiết kế với quy mô dự kiến**: 12 phòng ban, 9 chức vụ, 120 nhân viên, 120 quyết định tuyển dụng, 96 hợp đồng, 80 hồ sơ bảo hiểm, 115 quyết định lương, 102 quyết định chức vụ, 5 ca làm, 84 kế hoạch tuần, khoảng 3.600 phân công và 3.600 bản ghi chấm công, 160 phiếu/320 chi tiết phụ trội, 120 quyết định thưởng/phạt, 160 phiếu thu chi và bảng lương mẫu tháng **09/2026** gồm 96 chi tiết. **Các con số này cần xác nhận bằng truy vấn sau khi chạy script trên SQL Server thực tế.**

**Khuyến nghị thử nghiệm:** Vì tháng 09/2026 đã có bảng lương mẫu, nên thử tính **tháng 10/2026** nếu dữ liệu công tương ứng đã nạp và điều kiện nghiệp vụ được đáp ứng. Không cố tính trùng kỳ lương đã tồn tại.

## 9. Các quy tắc nghiệp vụ trọng tâm

- Không ký hợp đồng chính thức khi chưa đủ điều kiện kết thúc thử việc/đạt đánh giá.
- Hồ sơ bảo hiểm phải tham chiếu đúng nhân viên và hợp đồng; chức năng nghiệp vụ giới hạn cho loại hợp đồng phù hợp.
- Quyết định tăng lương/bổ nhiệm có thời điểm hiệu lực để theo dõi lịch sử.
- Kế hoạch tuần và phân công phải nhất quán về thời gian, phòng ban, nhân viên và ca.
- Không tạo bản ghi chấm công trùng tổ hợp nhân viên – ngày – ca.
- Phụ trội sử dụng hệ số `1.5` theo đề bài; từng chi tiết không vượt giờ của phiếu theo kiểm tra nghiệp vụ.
- Quyết định thưởng/kỷ luật có thể áp dụng cho cá nhân hoặc tập thể; phân bổ tập thể không vượt tổng tiền quyết định.
- Một tháng/năm chỉ có một bảng lương; khoản thưởng/phạt đã áp dụng phải được theo dõi để không cộng/trừ trùng.
- Các thao tác nhiều bảng cần transaction và rollback khi thất bại.

## 10. UML và yêu cầu của bài tập

Thư mục `uml/` gồm **10 file PlantUML**:

1. `01_Class_Analysis.puml` – Class Diagram phân tích, định danh và quan hệ.
2. `02_UseCase_TongQuat.puml` – Actor và Use Case tổng quát.
3. `03_UseCase_PhanRa_TuyenDung_HopDong.puml` – Use Case phân rã tuyển dụng → thử việc → hợp đồng.
4. `04_Activity_TuyenDung_HopDong.puml` – Activity Diagram của quy trình trên.
5. `05_Sequence_LapQuyetDinhKhenThuong.puml` – Sequence Diagram lập quyết định khen thưởng.
6. `06_Class_Design_Java.puml` – Sơ đồ lớp thiết kế Java.
7. `07_State_NhanVien.puml` – Biểu đồ trạng thái nhân viên.
8. `08_Activity_TinhLuongThang.puml` – Hoạt động tính lương tháng.
9. `09_Sequence_TinhLuong.puml` – Tuần tự tính lương.
10. `10_Component_Architecture.puml` – Kiến trúc thành phần.

Có thể mở `.puml` bằng PlantUML hoặc các công cụ hỗ trợ trong draw.io. Các sơ đồ trong mã nguồn là tài liệu thiết kế; việc có sơ đồ không đồng nghĩa tính năng đã được kiểm thử chạy thực tế.

## 11. Kiểm thử và truy vết

- `src/test/java/com/donga/service/PayrollMathTest.java`: kiểm thử đơn vị công thức lương, chạy bằng `mvn test`.
- `tests/KICH_BAN_KIEM_THU.md`: **18 kịch bản TC01–TC18** gồm tuyển dụng, hợp đồng, bảo hiểm, phân ca, chấm công, phụ trội, thưởng/phạt, tính lương và khóa ngoại.
- `tests/TRUY_VET_YEU_CAU.md`: đối chiếu yêu cầu → UML → Form/Service → bảng SQL → test case.

**Các ca kiểm thử tích hợp chưa được đánh dấu PASS.** Để nghiệm thu, hãy chạy chúng trên máy có SQL Server thật, lưu ảnh kết quả và ghi rõ thực tế Đạt/Không đạt cùng thông báo lỗi nếu có.

## 12. Sự cố thường gặp

| Triệu chứng | Nguyên nhân cần kiểm tra | Hướng xử lý |
|---|---|---|
| `mvn` không nhận diện | Chưa cài Maven hoặc PATH chưa đúng | Kiểm tra `mvn -version`, cấu hình PATH rồi mở terminal mới |
| Lỗi Java version | JDK không phải 25 | Đặt JAVA_HOME/JDK 25; kiểm tra `java -version` |
| Không kết nối SQL Server | Service/cổng/URL/tài khoản/DB sai | Sửa `db.properties`, bật TCP/IP, kiểm tra SSMS và quyền truy cập |
| Form trống hoặc ComboBox ít giá trị | Chưa nạp dữ liệu mẫu, hoặc đang kết nối nhầm DB | Kiểm tra tên CSDL và chạy truy vấn `SELECT COUNT(*)` |
| Lỗi khóa ngoại khi xóa | Bản ghi đang được tham chiếu | Không xóa tùy tiện; rà soát dữ liệu liên quan |
| Lỗi trùng số hợp đồng/quyết định/kỳ lương | Có ràng buộc UNIQUE | Dùng số mới hoặc tra cứu bản ghi đã tồn tại |
| Không ký được hợp đồng | Thử việc chưa đạt/chưa hết hạn | Kiểm tra quyết định tuyển dụng, kết quả và thời gian |
| Thông báo sai định dạng | Ngày, số, trường bắt buộc hoặc khóa ngoại không hợp lệ | Đóng thông báo, sửa ngay hộp nhập đang giữ dữ liệu |
| Tính lại kỳ 09/2026 bị từ chối | Dữ liệu mẫu đã có bảng lương tháng này | Chọn kỳ mới hợp lệ, không xóa bảng lương mẫu tùy tiện |

## 13. Giới hạn và lưu ý bảo mật

- Ứng dụng phục vụ **bài thực hành**, chưa có đăng nhập/phân quyền theo từng nhân viên và chưa đủ để vận hành nhân sự thực tế.
- Quy định pháp luật lao động, bảo hiểm, thuế thu nhập cá nhân, phụ trội từng loại ngày và thay đổi lương giữa kỳ có thể phức tạp hơn mô hình minh họa. Hệ số 1,5 là theo **đề bài**, các tỷ lệ bảo hiểm trong dữ liệu mẫu không đại diện mức pháp lý hiện hành.
- Không nhập dữ liệu nhân sự thật khi trình diễn; chỉ dùng dữ liệu giả lập. Không đẩy tài khoản/mật khẩu SQL lên kho mã công khai.
- Các kiểm tra được mô tả ở đây phản ánh thiết kế và mã hiện có; **chưa xác minh trọn vẹn bằng thử nghiệm tích hợp SQL Server**.

## 14. Hồ sơ nộp bài và danh sách kiểm tra

- [ ] File project Java Maven, mở được trong VS Code/IntelliJ IDEA.
- [ ] File `QuanLyNhanSuDongA.sql` duy nhất chứa lược đồ và dữ liệu mẫu.
- [ ] Đã chỉnh cấu hình SQL cho máy thực hành và chạy được `mvn exec:java`.
- [ ] Đã chụp giao diện chính và các màn hình nghiệp vụ.
- [ ] Đã xuất hình UML cho các câu hỏi 1–6.
- [ ] Đã chạy các kiểm thử TC01–TC18 và cập nhật kết quả thật.
- [ ] Đã đối chiếu các lớp, bảng SQL và chức năng Java với ma trận truy vết.
- [ ] Không đóng gói mật khẩu hoặc dữ liệu cá nhân thật.

---

**Ghi chú phiên bản:** README này được cập nhật theo bản project có Form Main không còn `sub`, không còn tab “Nghiệp vụ”, hỗ trợ ComboBox, giữ dữ liệu khi nhập sai và dùng **một file SQL chính đã tích hợp chuẩn hóa tên nhân viên mẫu**.
