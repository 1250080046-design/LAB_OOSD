package com.donga.service;
import java.util.*;
/** Explicit allowlist: names may be interpolated into SQL only if obtained from these definitions. */
public record Module(String title,String table,String pk,String... columns){
 public static final List<Module> ALL=List.of(
 new Module("Phòng ban","PhongBan","MaPhong","TenPhong","LoaiDonVi","TrangThai"),
 new Module("Chức vụ","ChucVu","MaChucVu","TenChucVu"),
 new Module("Nhân viên","NhanVien","MaNV","HoTen","NgaySinh","DienThoai","DiaChi","NgayVaoLam","MaPhong","TrangThai"),
 new Module("Quyết định tuyển dụng","QuyetDinhTuyenDung","MaQDTD","MaNV","MaPhong","SoQuyetDinh","NgayKy","TuNgay","DenNgay","LuongThuViec","DieuKhoan","KetQua"),
 new Module("Hợp đồng","HopDong","MaHD","MaNV","SoHopDong","LoaiHopDong","NgayKy","TuNgay","DenNgay","NoiDung","TrangThai"),
 new Module("Bảo hiểm","HoSoBaoHiem","MaBH","MaNV","MaHD","SoBHXH","SoBHYT","TuNgay","DenNgay","TyLeBHXH","TyLeBHYT","MucLuongDong"),
 new Module("Quyết định chức vụ","QuyetDinhChucVu","MaQDCV","MaNV","MaChucVu","SoQuyetDinh","NgayKy","HieuLucTu","PhuCapChucVu","GhiChu"),
 new Module("Quyết định lương","QuyetDinhLuong","MaQDL","MaNV","SoQuyetDinh","NgayKy","HieuLucTu","LuongCoBan","PhuCapLuong","PhuCapDocHai"),
 new Module("Ca làm việc","CaLamViec","MaCa","TenCa","GioBatDau","GioKetThuc","SoGioChuan"),
 new Module("Kế hoạch tuần","KeHoachTuan","MaKH","MaPhong","TuanBatDau","NguoiLap","NgayLap","GhiChu"),
 new Module("Phân công ca","PhanCongCa","MaPC","MaKH","MaNV","MaCa","NgayLam","DuKienPhuTroi"),
 new Module("Chấm công","ChamCong","MaCC","MaNV","MaCa","NgayCong","GioThucTe","TrangThai"),
 new Module("Phiếu phụ trội","PhieuPhuTroi","MaPhieu","SoPhieu","NgayPhuTroi","SoGio","HinhThuc","HeSo"),
 new Module("Chi tiết phụ trội","ChiTietPhuTroi","MaCT","MaPhieu","MaNV","SoGioThamGia"),
 new Module("Quyết định thưởng/kỷ luật","QuyetDinhThuongPhat","MaQD","SoQuyetDinh","Loai","DoiTuong","MaNV","MaPhong","NgayKy","LyDo","SoTien","HinhThuc","NguoiKy"),
 new Module("Phiếu thu/chi","PhieuThuChi","MaPhieuTC","MaQD","MaNV","SoPhieu","NgayPhieu","Loai","SoTien","DaThucHien"),
 new Module("Bảng lương","BangLuong","MaBangLuong","Thang","Nam","NgayLap","TrangThai"),
 new Module("Chi tiết lương","ChiTietLuong","MaCTL","MaBangLuong","MaNV","LuongCoBan","PhuCapLuong","PhuCapDocHai","PhuCapChucVu","GioChuan","GioLam","GioPhuTroi","TienPhuTroi","TienThuong","TienPhat","TruBaoHiem","ThucLinh")
 );
 public static Module forTable(String name){return ALL.stream().filter(m->m.table.equals(name)).findFirst().orElseThrow();}
}
