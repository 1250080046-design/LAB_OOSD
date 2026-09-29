package com.quanlykhachsan.service;

import com.quanlykhachsan.data.Db;
import com.quanlykhachsan.model.KetQuaXuLy;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DichVuService {
    public DefaultTableModel layPhieuDangO(){
        return Db.query("SELECT d.SoPhieuDat,k.HoTen,c.SoPhong FROM PhieuDatPhong d JOIN KhachHang k ON d.MaKhach=k.MaKhach JOIN ChiTietDatPhong c ON d.SoPhieuDat=c.SoPhieuDat WHERE d.TrangThai=N'Đang ở' ORDER BY d.SoPhieuDat,c.SoPhong");
    }
    public DefaultTableModel layDichVu(){return Db.query("SELECT * FROM DichVu ORDER BY MaDV");}
    public DefaultTableModel layLichSu(String so){
        return Db.query("SELECT p.SoPhieuSDDV,p.SoPhong,p.NgaySuDung,d.TenDV,c.SoLuong,c.DonGia,c.ThanhTien FROM PhieuSuDungDV p JOIN ChiTietPhieuSuDungDV c ON p.SoPhieuSDDV=c.SoPhieuSDDV JOIN DichVu d ON c.MaDV=d.MaDV WHERE p.SoPhieuDat=? ORDER BY p.NgaySuDung,p.SoPhong,d.TenDV",so);
    }
    public KetQuaXuLy ghiNhan(String soPhieuDat,String soPhong,java.util.Date ngay,String maNV,String maDV,int soLuong){
        if(blank(soPhieuDat)||blank(soPhong)||blank(maNV)||blank(maDV)||soLuong<=0)return KetQuaXuLy.fail("Thông tin sử dụng dịch vụ không hợp lệ.");
        try{return Db.tx(c->{
            Object st=Db.scalar(c,"SELECT TrangThai FROM PhieuDatPhong WHERE SoPhieuDat=?",soPhieuDat);
            if(!"Đang ở".equals(String.valueOf(st)))throw new IllegalArgumentException("Chỉ ghi nhận dịch vụ cho phiếu đang lưu trú.");
            Object og=Db.scalar(c,"SELECT DonGia FROM DichVu WHERE MaDV=?",maDV);
            if(og==null)throw new IllegalArgumentException("Không tìm thấy dịch vụ.");
            BigDecimal gia=new BigDecimal(og.toString());
            Date n=new Date(ngay.getTime());
            Object so=Db.scalar(c,"SELECT SoPhieuSDDV FROM PhieuSuDungDV WHERE SoPhieuDat=? AND SoPhong=? AND NgaySuDung=?",soPhieuDat,soPhong,n);
            String maPhieu=so==null||so.toString().isBlank()?"SD"+new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date()):so.toString();
            if(so==null){Db.execute(c,"INSERT INTO PhieuSuDungDV(SoPhieuSDDV,SoPhieuDat,SoPhong,NgaySuDung,MaNV) VALUES(?,?,?,?,?)",maPhieu,soPhieuDat,soPhong,n,maNV);}
            int count=((Number)Db.scalar(c,"SELECT COUNT(*) FROM ChiTietPhieuSuDungDV WHERE SoPhieuSDDV=? AND MaDV=?",maPhieu,maDV)).intValue();
            if(count>0)Db.execute(c,"UPDATE ChiTietPhieuSuDungDV SET SoLuong=SoLuong+?,DonGia=? WHERE SoPhieuSDDV=? AND MaDV=?",soLuong,gia,maPhieu,maDV);
            else Db.execute(c,"INSERT INTO ChiTietPhieuSuDungDV(SoPhieuSDDV,MaDV,SoLuong,DonGia) VALUES(?,?,?,?)",maPhieu,maDV,soLuong,gia);
            return KetQuaXuLy.ok("Đã ghi nhận dịch vụ. Nếu cùng dịch vụ được dùng nhiều lần trong ngày, số lượng/tiền được cộng dồn thành một lần.");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }
    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
    private static String rootMessage(Exception e){Throwable t=e;while(t.getCause()!=null)t=t.getCause();return t.getMessage()!=null?t.getMessage():e.getMessage();}
}
