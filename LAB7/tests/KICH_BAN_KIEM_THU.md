# Bộ kịch bản kiểm thử Bài 7 – dữ liệu giả lập

**Trạng thái:** TC01–TC18 dưới đây là **kịch bản cần chạy với SQL Server**, chưa được đánh dấu ĐẠT. Các số tiền và tỷ lệ dùng làm dữ liệu minh họa, không phải quy định công ty hay tỷ lệ pháp định. Trước khi chạy, tạo CSDL bằng `QuanLyNhanSuDongA.sql`.

| TC | Đầu vào / thao tác | Kết quả mong đợi | Truy vết |
|---|---|---|---|
| TC01 | Thêm nhân viên Nguyễn Văn An, phòng 1; QĐ TD `TD-001`, 2026-10-01 đến 2026-11-30, lương thử việc 6.000.000 | Tạo nhân viên và QĐ; trạng thái `THU_VIEC`; transaction thành công | FR01, UC tuyển dụng, HrService.hire, NhanVien + QuyetDinhTuyenDung |
| TC02 | Tạo `TD-001` lần nữa | Bị từ chối bởi UNIQUE; không sinh nhân viên mồ côi | FR01, BR01, HrService.hire |
| TC03 | Thời gian thử việc kết thúc trước ngày bắt đầu | Báo lỗi, không lưu | BR01, HrService.hire |
| TC04 | Ký hợp đồng khi QĐ tuyển dụng đang `CHO_DANH_GIA` | Bị chặn | FR02, BR02, HrService.confirmEmployment |
| TC05 | Đổi `KetQua=DAT`, ký HĐ dài hạn `HD-001` hiệu lực 2026-12-01, QDL `L-001`, QDCV `CV-001` | Tạo HĐ và hai quyết định; NV `CHINH_THUC` | FR02–04, HrService.confirmEmployment, HopDong, QuyetDinhLuong, QuyetDinhChucVu |
| TC06 | Làm lỗi số quyết định lương trùng khi ký hợp đồng | Rollback tất cả: không còn HĐ mới và NV chưa chuyển trạng thái | NFR giao dịch, HrService.confirmEmployment |
| TC07 | HĐ dài hạn, chưa có hồ sơ BH; tính lương | Báo thiếu BH và rollback bảng lương | FR05, HrService.calculatePayroll, HoSoBaoHiem |
| TC08 | Nhập hồ sơ BH nhân viên có HĐ dài hạn; tỷ lệ BHXH 0,08, BHYT 0,015, cơ sở 8.000.000 | Khấu trừ minh họa 760.000 / tháng | FR05, HoSoBaoHiem, ChiTietLuong |
| TC09 | Lập kế hoạch tuần phòng 1, phân công NV ca hành chính ngày 2026-12-07 | Lưu kế hoạch và phân công | FR06, KeHoachTuan + PhanCongCa |
| TC10 | Chấm công trùng NV+ngày+ca | Ràng buộc UNIQUE từ chối | FR07, ChamCong |
| TC11 | Phiếu phụ trội hình thức `NGAY_LE`, 4 giờ; chi tiết NV 4 giờ | Hệ số mặc định 1,5; số giờ quy đổi = 6 | FR08, PhieuPhuTroi + ChiTietPhuTroi |
| TC12 | Tạo phiếu phụ trội có HeSo 2 | Bị từ chối bởi CHECK HeSo=1.5 theo đề | BR08, PhieuPhuTroi |
| TC13 | Quyết định thưởng cá nhân `KT-001`, số tiền 500.000, `QUA_LUONG`, người ký Giám đốc | Có QĐ và phiếu chi tương ứng | FR09, HrService.rewardDecision, QuyetDinhThuongPhat + PhieuThuChi |
| TC14 | Quyết định kỷ luật phòng 3 (tập thể), 1.000.000 | Có QĐ cấp phòng; phải tạo phiếu phân bổ cho từng NV trước khi tính lương | FR09, QuyetDinhThuongPhat |
| TC15 | QĐ thưởng `KT-001` trùng mã | Lỗi UNIQUE; transaction rollback | BR09, HrService.rewardDecision |
| TC16 | Lương cơ bản 8.000.000, giờ chuẩn 160, giờ công 160, phụ cấp 1.000.000, 10 giờ tăng ca ×1,5, thưởng 500.000, phạt 200.000, BH 840.000 | Thực lĩnh = 9.210.000 | FR10, PayrollMath.net, ChiTietLuong |
| TC17 | Tính lương cùng tháng/năm lần hai | Từ chối ghi đè, không cộng thưởng/phạt lần hai | BR10, BangLuong UNIQUE |
| TC18 | Cố xóa phòng ban đang có nhân viên | Bị khóa ngoại chặn; không mất dữ liệu | NFR toàn vẹn, PhongBan / NhanVien |

**Kiểm thử đơn vị:** `mvn test` chạy `PayrollMathTest` kiểm tra công thức và giờ chuẩn không hợp lệ. **Kiểm thử tích hợp:** chạy các TC trên SQL Server thật, ghi bằng chứng ảnh/log và trạng thái PASS/FAIL.

## Minh họa công thức TC16

- Lương giờ: 8.000.000 / 160 = 50.000.
- Tiền công: 160 × 50.000 = 8.000.000.
- Phụ trội: 10 × 1,5 × 50.000 = 750.000.
- Thực lĩnh: 8.000.000 + 1.000.000 + 750.000 + 500.000 − 200.000 − 840.000 = **9.210.000 đồng**.
