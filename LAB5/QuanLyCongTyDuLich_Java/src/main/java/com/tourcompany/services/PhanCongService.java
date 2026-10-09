package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class PhanCongService {
    public DefaultTableModel danhSach() throws SQLException { return Db.query("SELECT p.MaPC,h.HoTen,p.LoaiDoiTuong,ISNULL(p.MaChuyen,p.SoDKDoan) AS MaDoiTuong,p.NgayBatDau,p.NgayKetThuc,p.ThuLaoTour FROM PhanCongHDV p JOIN HuongDanVien h ON h.MaHDV=p.MaHDV ORDER BY p.NgayBatDau DESC"); }
    public DefaultTableModel hdv() throws SQLException { return Db.query("SELECT MaHDV,HoTen,DienThoai FROM HuongDanVien WHERE DangLamViec=1 ORDER BY HoTen"); }
    public DefaultTableModel chuyenLe() throws SQLException { return Db.query("SELECT MaChuyen,NgayDi,NgayVe,TrangThai FROM ChuyenLe ORDER BY NgayDi"); }
    public DefaultTableModel doan() throws SQLException { return Db.query("SELECT SoDKDoan,NgayDi,NgayKetThucDuKien,TrangThai FROM DangKyDoan ORDER BY NgayDi"); }

    public KetQuaXuLy phanCong(String maPC,String maHDV,String loai,String maDoiTuong,BigDecimal thuLao){
        if(blank(maPC,maHDV,loai,maDoiTuong)||thuLao==null||thuLao.signum()<0)return KetQuaXuLy.fail("Thông tin phân công chưa đầy đủ hoặc thù lao không hợp lệ.");
        if(!QuyDinh.LE.equals(loai)&&!QuyDinh.DOAN.equals(loai))return KetQuaXuLy.fail("Loại đối tượng phải là LE hoặc DOAN.");
        try{return Db.transaction(c->{
            Object active=Db.scalar(c,"SELECT DangLamViec FROM HuongDanVien WHERE MaHDV=?",maHDV);
            if(active==null||!Boolean.TRUE.equals(active)&&!(active instanceof Number n&&n.intValue()==1))throw new IllegalArgumentException("Hướng dẫn viên không tồn tại hoặc đã nghỉ việc.");
            LocalDate start,end;
            if(QuyDinh.LE.equals(loai)){
                var obj=Db.query(c,"SELECT NgayDi,NgayVe,TrangThai FROM ChuyenLe WHERE MaChuyen=?",maDoiTuong);
                if(obj.getRowCount()==0)throw new IllegalArgumentException("Không tìm thấy chuyến khách lẻ.");
                start=toDate(obj.getValueAt(0,0));end=toDate(obj.getValueAt(0,1));
                if(!QuyDinh.MO_DANG_KY.equals(String.valueOf(obj.getValueAt(0,2))))throw new IllegalArgumentException("Chuyến đã đóng đăng ký.");
            } else {
                var obj=Db.query(c,"SELECT NgayDi,NgayKetThucDuKien,TrangThai FROM DangKyDoan WHERE SoDKDoan=?",maDoiTuong);
                if(obj.getRowCount()==0)throw new IllegalArgumentException("Không tìm thấy đăng ký đoàn.");
                start=toDate(obj.getValueAt(0,0));end=toDate(obj.getValueAt(0,1));
                if(QuyDinh.HUY_MAT_COC.equals(String.valueOf(obj.getValueAt(0,2))))throw new IllegalArgumentException("Phiếu đoàn đã hủy.");
            }
            Object overlap=Db.scalar(c,"SELECT COUNT(*) FROM PhanCongHDV WHERE MaHDV=? AND NOT (NgayKetThuc<? OR NgayBatDau>?)",maHDV,java.sql.Date.valueOf(start),java.sql.Date.valueOf(end));
            if(((Number)overlap).intValue()>0)throw new IllegalArgumentException("Hướng dẫn viên bị trùng lịch trong khoảng "+start+" đến "+end+".");
            if(QuyDinh.LE.equals(loai))Db.execute(c,"INSERT INTO PhanCongHDV(MaPC,MaHDV,LoaiDoiTuong,MaChuyen,SoDKDoan,NgayBatDau,NgayKetThuc,ThuLaoTour) VALUES(?,?,?, ?,NULL,?,?,?)",maPC,maHDV,loai,maDoiTuong,java.sql.Date.valueOf(start),java.sql.Date.valueOf(end),thuLao);
            else Db.execute(c,"INSERT INTO PhanCongHDV(MaPC,MaHDV,LoaiDoiTuong,MaChuyen,SoDKDoan,NgayBatDau,NgayKetThuc,ThuLaoTour) VALUES(?,?,?,NULL,?,?,?,?)",maPC,maHDV,loai,maDoiTuong,java.sql.Date.valueOf(start),java.sql.Date.valueOf(end),thuLao);
            return KetQuaXuLy.ok("Đã phân công HDV cho "+maDoiTuong+" từ "+start+" đến "+end+".");
        });}catch(Exception e){return KetQuaXuLy.fail(DangKyLeService.rootMessage(e));}
    }
    private static LocalDate toDate(Object value){if(value instanceof java.sql.Date d)return d.toLocalDate();if(value instanceof java.sql.Timestamp t)return t.toLocalDateTime().toLocalDate();return LocalDate.parse(String.valueOf(value).substring(0,10));}
    private boolean blank(String...s){for(String x:s)if(x==null||x.isBlank())return true;return false;}
}
