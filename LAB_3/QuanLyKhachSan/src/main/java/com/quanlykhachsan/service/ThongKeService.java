package com.quanlykhachsan.service;

import com.quanlykhachsan.data.Db;

import javax.swing.table.DefaultTableModel;
import java.sql.Date;

public class ThongKeService {
    public DefaultTableModel tongHop(java.util.Date tu, java.util.Date den){
        Date a=new Date(tu.getTime()), b=new Date(den.getTime());
        return Db.query("SELECT (SELECT COUNT(*) FROM PhieuDatPhong WHERE CAST(NgayLap AS date) BETWEEN ? AND ?) AS SoPhieuDat,"+
                "(SELECT COUNT(*) FROM PhieuDatPhong WHERE TrangThai=N'Đang ở') AS DangO,"+
                "(SELECT COUNT(*) FROM HoaDon WHERE CAST(NgayLap AS date) BETWEEN ? AND ?) AS SoHoaDon,"+
                "(SELECT ISNULL(SUM(TongTien),0) FROM HoaDon WHERE CAST(NgayLap AS date) BETWEEN ? AND ?) AS DoanhThuHoaDon,"+
                "(SELECT ISNULL(SUM(TongTien),0) FROM PhieuDenBu WHERE CAST(NgayLap AS date) BETWEEN ? AND ?) AS TongDenBu",
                a,b,a,b,a,b,a,b);
    }
    public DefaultTableModel dichVu(java.util.Date tu,java.util.Date den){
        return Db.query("SELECT d.MaDV,d.TenDV,SUM(c.SoLuong) AS TongSoLuong,SUM(c.ThanhTien) AS TongTien FROM PhieuSuDungDV p JOIN ChiTietPhieuSuDungDV c ON p.SoPhieuSDDV=c.SoPhieuSDDV JOIN DichVu d ON c.MaDV=d.MaDV WHERE p.NgaySuDung BETWEEN ? AND ? GROUP BY d.MaDV,d.TenDV ORDER BY TongTien DESC",new Date(tu.getTime()),new Date(den.getTime()));
    }
}
