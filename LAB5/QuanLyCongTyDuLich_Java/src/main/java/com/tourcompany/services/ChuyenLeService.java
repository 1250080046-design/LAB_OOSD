package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.time.LocalDate;

public class ChuyenLeService {
    public DefaultTableModel danhSach() throws SQLException { return Db.query("SELECT c.MaChuyen,c.MaTour,t.TenTour,c.NgayDi,c.NgayVe,c.DiaDiemDon,c.TrangThai FROM ChuyenLe c JOIN Tour t ON t.MaTour=c.MaTour ORDER BY c.NgayDi DESC"); }
    public DefaultTableModel tourMoBan() throws SQLException { return Db.query("SELECT MaTour,TenTour,SoNgay,DonGiaKhach FROM Tour WHERE DangMoBan=1 ORDER BY TenTour"); }
    public KetQuaXuLy themChuyen(String ma,String tour,LocalDate ngayDi,String diaDiem){try{if(blank(ma,tour,diaDiem)||ngayDi==null)return KetQuaXuLy.fail("Thông tin chuyến chưa đầy đủ.");Object soNgay=Db.scalar("SELECT SoNgay FROM Tour WHERE MaTour=? AND DangMoBan=1",tour);if(soNgay==null)return KetQuaXuLy.fail("Tour không tồn tại hoặc chưa mở bán.");LocalDate ngayVe=ngayDi.plusDays(((Number)soNgay).longValue()-1);if(!ngayDi.isAfter(LocalDate.now()))return KetQuaXuLy.fail("Ngày đi phải sau ngày hiện tại.");Db.execute("INSERT INTO ChuyenLe(MaChuyen,MaTour,NgayDi,NgayVe,DiaDiemDon,TrangThai) VALUES(?,?,?,?,?,?)",ma,tour,java.sql.Date.valueOf(ngayDi),java.sql.Date.valueOf(ngayVe),diaDiem,QuyDinh.MO_DANG_KY);return KetQuaXuLy.ok("Đã tạo chuyến. Ngày về: "+ngayVe+".");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy dongDangKy(String ma){try{int n=Db.execute("UPDATE ChuyenLe SET TrangThai=? WHERE MaChuyen=? AND TrangThai=?",QuyDinh.DONG_DANG_KY,ma,QuyDinh.MO_DANG_KY);return n>0?KetQuaXuLy.ok("Đã đóng đăng ký chuyến."):KetQuaXuLy.fail("Không tìm thấy chuyến đang mở.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    private boolean blank(String...x){for(String s:x)if(s==null||s.isBlank())return true;return false;}
}
