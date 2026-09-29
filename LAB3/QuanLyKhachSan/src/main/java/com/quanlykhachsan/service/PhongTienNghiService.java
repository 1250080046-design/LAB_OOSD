package com.quanlykhachsan.service;

import com.quanlykhachsan.data.Db;
import com.quanlykhachsan.model.KetQuaXuLy;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.Date;

public class PhongTienNghiService {
    public DefaultTableModel layPhong() {
        return Db.query("SELECT p.*,k.TenKhuVuc FROM Phong p JOIN KhuVuc k ON p.MaKhuVuc=k.MaKhuVuc ORDER BY p.SoPhong");
    }
    public DefaultTableModel layTienNghi() {
        return Db.query("SELECT t.*,l.TenLoaiTN FROM TienNghi t JOIN LoaiTienNghi l ON t.MaLoaiTN=l.MaLoaiTN ORDER BY t.MaTienNghi");
    }
    public DefaultTableModel layLapDat() {
        return Db.query("SELECT p.*,l.TenLoaiTN FROM PhieuLapDat p JOIN TienNghi t ON p.MaTienNghi=t.MaTienNghi JOIN LoaiTienNghi l ON t.MaLoaiTN=l.MaLoaiTN ORDER BY NgayLap DESC");
    }
    public KetQuaXuLy themPhong(String so, String khu, int max, double gia) {
        if (blank(so) || blank(khu) || max <= 0 || gia < 0) return KetQuaXuLy.fail("Thông tin phòng không hợp lệ.");
        try { Db.execute("INSERT INTO Phong(SoPhong,MaKhuVuc,SoNguoiToiDa,DonGiaNgay,TrangThai) VALUES(?,?,?,?,'Trống')", so,khu,max, BigDecimal.valueOf(gia)); return KetQuaXuLy.ok("Đã thêm phòng."); }
        catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}
    }
    public KetQuaXuLy themTienNghi(String ma,String loai,int stt,String tt){
        if(blank(ma)||blank(loai)||stt<=0)return KetQuaXuLy.fail("Thông tin tiện nghi không hợp lệ.");
        try { Db.execute("INSERT INTO TienNghi(MaTienNghi,MaLoaiTN,SoThuTu,TinhTrangHienTai) VALUES(?,?,?,?)",ma,loai,stt,tt); return KetQuaXuLy.ok("Đã thêm tiện nghi."); }
        catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}
    }
    public KetQuaXuLy lapDat(String soPhieu,String maTN,String soPhong,java.util.Date ngay,String tinhTrang,String maNV,String ghiChu){
        if(blank(soPhieu)||blank(maTN)||blank(soPhong)||blank(tinhTrang)||blank(maNV))return KetQuaXuLy.fail("Phiếu lắp đặt chưa đủ thông tin.");
        try {
            return Db.tx(c -> {
                Db.execute(c,"INSERT INTO PhieuLapDat(SoPhieuLapDat,MaTienNghi,SoPhong,NgayLap,TinhTrang,MaNV,GhiChu) VALUES(?,?,?,?,?,?,?)",soPhieu,maTN,soPhong,new Date(ngay.getTime()),tinhTrang,maNV,blank(ghiChu)?null:ghiChu);
                Db.execute(c,"UPDATE TienNghi SET TinhTrangHienTai=? WHERE MaTienNghi=?",tinhTrang,maTN);
                return KetQuaXuLy.ok("Đã lập phiếu lắp đặt.");
            });
        } catch(Exception e){
            String m=e.getCause()!=null?e.getCause().getMessage():e.getMessage();
            if(m!=null&&(m.contains("2601")||m.contains("2627")||m.contains("UQ_PhieuLapDat"))) return KetQuaXuLy.fail("Thiết bị này đã được lắp cho một phòng khác trong ngày đã chọn.");
            return KetQuaXuLy.fail(m);
        }
    }
    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
}
