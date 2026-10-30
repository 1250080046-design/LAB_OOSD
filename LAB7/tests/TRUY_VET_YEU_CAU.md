# Ma trận truy vết yêu cầu → UML → Form/Service → CSDL → Test case

| Mã | Yêu cầu | UML | Java UI / Service | CSDL | Test |
|---|---|---|---|---|---|
| FR01 | Tuyển dụng, thử việc | UC phân rã + Activity tuyển dụng | FrmTuyenDung / HrService.hire | NhanVien, QuyetDinhTuyenDung | TC01–03 |
| FR02 | Ký hợp đồng | UC phân rã + Activity tuyển dụng | FrmHopDongBaoHiem / confirmEmployment | HopDong, NhanVien | TC04–06 |
| FR03 | Quyết định chức vụ | Class, UC tổng quát | FrmLuongChucVu / confirmEmployment | QuyetDinhChucVu, ChucVu | TC05 |
| FR04 | Quyết định lương | Class, UC tổng quát | FrmLuongChucVu / confirmEmployment | QuyetDinhLuong | TC05–06 |
| FR05 | Bảo hiểm dài hạn | UC tổng quát + Activity lương | FrmHopDongBaoHiem / createInsurance, calculatePayroll | HoSoBaoHiem, HopDong | TC07–08 |
| FR06 | Kế hoạch tuần, phân công ca | UC tổng quát | FrmKeHoach / WorkforceService | KeHoachTuan, PhanCongCa, CaLamViec | TC09 |
| FR07 | Chấm công | UC tổng quát + Activity lương | FrmChamCong / recordAttendance | ChamCong | TC10 |
| FR08 | Phiếu phụ trội | UC tổng quát + Activity lương | FrmChamCong / addOvertimeParticipant | PhieuPhuTroi, ChiTietPhuTroi | TC11–12 |
| FR09 | Thưởng, kỷ luật cá nhân/tập thể | Sequence khen thưởng | FrmThuongKyLuat / rewardDecision, allocateCollectiveDecision | QuyetDinhThuongPhat, PhieuThuChi | TC13–15 |
| FR10 | Bảng lương tháng | Activity/Sequence tính lương | FrmBangLuong / calculatePayroll | BangLuong, ChiTietLuong | TC16–17 |
| NFR01 | Toàn vẹn PK/FK/UNIQUE | Class Diagram | CrudService / SQL Server | Tất cả bảng | TC02, TC10, TC18 |
| NFR02 | Transaction | Sequence thưởng, Sequence lương | HrService | Nhiều bảng | TC02, TC06, TC15 |
| NFR03 | UI theo module | Component Diagram | FrmMain | Tất cả bảng | Khảo sát UI |
| BR01 | Không tạo QĐ TD sai thời gian | Activity tuyển dụng | hire | QuyetDinhTuyenDung | TC03 |
| BR02 | Chỉ ký HĐ sau thử việc đạt | Activity tuyển dụng | confirmEmployment | HopDong / QuyetDinhTuyenDung | TC04 |
| BR08 | Hệ số phụ trội 1,5 | Activity lương | calculatePayroll | PhieuPhuTroi | TC11–12 |
| BR09 | Quyết định thưởng/phạt phải có chứng từ | Sequence khen thưởng | rewardDecision | QuyetDinhThuongPhat, PhieuThuChi | TC13–15 |
| BR10 | Không tạo bảng lương trùng kỳ | Sequence lương | calculatePayroll | BangLuong | TC17 |

**Giới hạn của prototype:** phần đăng nhập/phân quyền theo tác nhân chưa được hiện thực; Actor là vai trò nghiệp vụ trong mô hình, không phải cam kết ứng dụng đã phân quyền. Các khoản thưởng tập thể chỉ được khấu trừ/tính qua lương sau khi kế toán phân bổ chi tiết vào `PhieuThuChi`. Hệ thống chưa thực hiện xử lý thuế thu nhập, nghỉ phép có lương, nhiều hệ số phụ trội theo luật hoặc thay đổi quyết định có hiệu lực giữa tháng.
