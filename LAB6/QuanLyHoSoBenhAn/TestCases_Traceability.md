# Test case và truy vết yêu cầu - Bài 5 Quản lý hồ sơ bệnh án

> Chuẩn bị: chạy `ProjectJava/Database/QuanLyHoSoBenhAn.sql`, cấu hình `db.properties`, mở ứng dụng và dùng dữ liệu kiểm thử do người kiểm thử tạo. Lệ phí năm lấy từ `AnnualFeeRate`; script dùng 500.000 VNĐ làm giá trị demo vì đề không quy định mức phí.

| Test | Yêu cầu / Use Case | Thao tác | Kết quả mong đợi | Service / bảng |
|---|---|---|---|---|
| TC01 | Đăng ký lần đầu + lệ phí + cấp hồ sơ | Nhập bệnh nhân mới, năm đã cấu hình phí, chọn hình thức thu và đăng ký | Có Patient, AnnualFeePayment, MedicalRecord; mã hồ sơ duy nhất; thao tác là một transaction | PatientService; Patient, AnnualFeePayment, MedicalRecord |
| TC02 | Đăng ký không có cấu hình lệ phí | Chọn năm chưa có AnnualFeeRate | Thông báo chưa cấu hình; không tạo bệnh nhân/hồ sơ nửa chừng | PatientService; AnnualFeeRate, Patient, AnnualFeePayment, MedicalRecord |
| TC03 | Chống đăng ký trùng | Đăng ký lại cùng họ tên + ngày sinh + số điện thoại | Từ chối và yêu cầu tra cứu trước; không tạo thêm hồ sơ | PatientService; Patient, MedicalRecord |
| TC19 | Thu lệ phí năm tiếp theo | Chọn bệnh nhân đã có hồ sơ, chọn năm đã cấu hình phí và ghi nhận lệ phí | Tạo AnnualFeePayment cho năm đó; từ chối nếu đã thu trong cùng năm hoặc chưa cấu hình phí | PatientService; AnnualFeeRate, AnnualFeePayment, MedicalRecord |
| TC04 | Tra cứu hồ sơ | Tìm bằng PatientID, RecordCode, họ tên hoặc điện thoại | Hiển thị bệnh nhân và mã hồ sơ phù hợp | PatientService; Patient, MedicalRecord |
| TC05 | Tra cứu lịch sử | Chọn bệnh nhân đã có lệ phí / phiếu khám / xét nghiệm / toa | Bảng lịch sử hiển thị các loại bản ghi với thời điểm và trạng thái | PatientService; AnnualFeePayment, Visit, LabTest, Prescription |
| TC06 | Lịch định kỳ hợp lệ | Bệnh nhân có hồ sơ và đã đóng phí năm tương ứng; đặt lịch trong quý chưa có lịch | Lịch được tạo; lưu năm/quý; khám định kỳ không thu phí riêng | AppointmentService; Appointment, AnnualFeePayment, MedicalRecord |
| TC07 | Trùng lịch định kỳ cùng quý | Tạo lịch thứ hai cho cùng bệnh nhân, năm, quý | Từ chối do quy tắc unique | AppointmentService; Appointment |
| TC08 | Đặt lịch chưa đóng lệ phí năm | Đặt lịch cho bệnh nhân chưa có AnnualFeePayment năm tương ứng | Từ chối và yêu cầu hoàn tất lệ phí năm | AppointmentService; AnnualFeePayment, Appointment |
| TC09 | Lập phiếu khám | Bệnh nhân có hồ sơ, chọn bác sĩ/khoa và nhập triệu chứng | Tạo TreatmentEpisode nếu mới và Visit tham chiếu episode | ClinicalService; TreatmentEpisode, Visit |
| TC10 | Xét nghiệm gắn phiếu khám | Nhập VisitID hợp lệ, tạo xét nghiệm và ghi kết quả | LabTest lưu VisitID, kết quả và thời điểm; trạng thái chuyển sang Đã có kết quả | ClinicalService; Visit, LabTest |
| TC11 | Xét nghiệm với VisitID sai / đợt đã hoàn tất | Dùng ID không tồn tại hoặc thuộc đợt đã hoàn tất | Từ chối, không tạo xét nghiệm | ClinicalService; Visit, TreatmentEpisode, LabTest |
| TC12 | Lập toa có chi tiết | Chọn VisitID hợp lệ và ít nhất một thuốc; lưu toa | Prescription và toàn bộ PrescriptionItem được lưu chung transaction | ClinicalService; Visit, Prescription, PrescriptionItem |
| TC13 | Toa không có thuốc | Nhấn lưu với danh sách thuốc rỗng | Từ chối; không có Prescription được lưu | ClinicalService; Prescription, PrescriptionItem |
| TC14 | Toa trùng phiếu khám | Lập toa lần thứ hai cho cùng VisitID | Từ chối do UNIQUE(VisitID) | ClinicalService; Prescription |
| TC15 | Hoàn tất chưa có hóa đơn | Nhấn hoàn tất khi chưa lập HospitalInvoice | Từ chối; đợt vẫn Đang điều trị | BillingService; TreatmentEpisode, HospitalInvoice |
| TC16 | Thanh toán một phần | Lập viện phí 1.000.000, thanh toán 400.000, thử hoàn tất | Không cho hoàn tất; còn nợ 600.000 | BillingService; HospitalInvoice, HospitalPayment, TreatmentEpisode |
| TC17 | Thanh toán đủ và hoàn tất | Thanh toán nốt số còn lại rồi hoàn tất | Hóa đơn Đã thanh toán; episode Đã hoàn tất và có EndAt | BillingService; HospitalInvoice, HospitalPayment, TreatmentEpisode |
| TC18 | Không cho thanh toán vượt nợ | Thử nộp số tiền cao hơn số còn lại | Từ chối; không thêm giao dịch vượt mức | BillingService; HospitalPayment |

## Ma trận truy vết tổng quát

| Yêu cầu | Use Case / module | Service | Bảng chính | Test |
|---|---|---|---|---|
| R01 Đăng ký lần đầu + lệ phí năm + cấp hồ sơ | Đăng ký bệnh nhân | PatientService | Patient, AnnualFeeRate, AnnualFeePayment, MedicalRecord | TC01-TC03 |
| R02 Tra cứu lịch sử sức khỏe | Tra cứu hồ sơ | PatientService | MedicalRecord, TreatmentEpisode, Visit, LabTest, Prescription | TC04-TC05 |
| R03 Khám định kỳ theo quý | Lịch khám định kỳ | AppointmentService | Appointment, AnnualFeePayment | TC06-TC08 |
| R04 Khám, xét nghiệm, toa thuốc gắn phiếu khám | Khám / xét nghiệm / toa thuốc | ClinicalService | Visit, LabTest, Prescription, PrescriptionItem | TC09-TC14 |
| R05 Chỉ hoàn tất sau thanh toán viện phí | Viện phí và hoàn tất đợt | BillingService | HospitalInvoice, HospitalPayment, TreatmentEpisode | TC15-TC18 |
