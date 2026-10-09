package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;

public class DanhMucService {
    public DefaultTableModel phuongTien() throws SQLException { return Db.query("SELECT MaPT,TenPT,GhiChu FROM PhuongTien ORDER BY MaPT"); }
    public DefaultTableModel diemBanVe() throws SQLException { return Db.query("SELECT MaDiemBan,TenDiemBan,DiaChi,DienThoai FROM DiemBanVe ORDER BY MaDiemBan"); }
    public DefaultTableModel huongDanVien() throws SQLException { return Db.query("SELECT MaHDV,HoTen,DienThoai,LuongCoBan,DangLamViec FROM HuongDanVien ORDER BY MaHDV"); }
    public DefaultTableModel diemThamQuan() throws SQLException { return Db.query("SELECT MaDiemTQ,TenDiemTQ,DiaDiem,NoiDung,YNghia FROM DiemThamQuan ORDER BY MaDiemTQ"); }
    public KetQuaXuLy themPhuongTien(String ma,String ten,String ghiChu) {try{if(blank(ma,ten))return KetQuaXuLy.fail("Mã và tên phương tiện là bắt buộc.");Db.execute("INSERT INTO PhuongTien(MaPT,TenPT,GhiChu) VALUES(?,?,?)",ma,ten,ghiChu);return KetQuaXuLy.ok("Đã thêm phương tiện.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy themDiemBan(String ma,String ten,String diaChi,String phone) {try{if(blank(ma,ten,diaChi))return KetQuaXuLy.fail("Mã, tên và địa chỉ điểm bán vé là bắt buộc.");Db.execute("INSERT INTO DiemBanVe(MaDiemBan,TenDiemBan,DiaChi,DienThoai) VALUES(?,?,?,?)",ma,ten,diaChi,phone);return KetQuaXuLy.ok("Đã thêm điểm bán vé.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy themHDV(String ma,String ten,String phone,BigDecimal luong) {try{if(blank(ma,ten)||luong==null||luong.signum()<0)return KetQuaXuLy.fail("Thông tin hướng dẫn viên không hợp lệ.");Db.execute("INSERT INTO HuongDanVien(MaHDV,HoTen,DienThoai,LuongCoBan,DangLamViec) VALUES(?,?,?,?,1)",ma,ten,phone,luong);return KetQuaXuLy.ok("Đã thêm hướng dẫn viên.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy themDiemTQ(String ma,String ten,String diaDiem,String noiDung,String yNghia) {try{if(blank(ma,ten,diaDiem))return KetQuaXuLy.fail("Mã, tên và địa điểm tham quan là bắt buộc.");Db.execute("INSERT INTO DiemThamQuan(MaDiemTQ,TenDiemTQ,DiaDiem,NoiDung,YNghia) VALUES(?,?,?,?,?)",ma,ten,diaDiem,noiDung,yNghia);return KetQuaXuLy.ok("Đã thêm điểm tham quan.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    private static boolean blank(String... values){for(String s:values)if(s==null||s.isBlank())return true;return false;}
}
