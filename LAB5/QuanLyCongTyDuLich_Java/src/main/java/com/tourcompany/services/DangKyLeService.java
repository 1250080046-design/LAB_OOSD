package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class DangKyLeService {
    public DefaultTableModel danhSach() throws SQLException { return Db.query("SELECT d.SoDKLe,d.MaChuyen,t.TenTour,c.NgayDi,c.NgayVe,b.TenDiemBan,d.TenNguoiDangKy,d.DienThoai,d.SoNguoi,d.ThanhTien,d.TrangThai FROM DangKyLe d JOIN ChuyenLe c ON c.MaChuyen=d.MaChuyen JOIN Tour t ON t.MaTour=c.MaTour JOIN DiemBanVe b ON b.MaDiemBan=d.MaDiemBan ORDER BY d.NgayDangKy DESC"); }
    public DefaultTableModel chuyenMo() throws SQLException { return Db.query("SELECT c.MaChuyen,CONCAT(c.MaChuyen,' - ',t.TenTour,' (',CONVERT(varchar(10),c.NgayDi,103),')') AS HienThi,t.DonGiaKhach FROM ChuyenLe c JOIN Tour t ON t.MaTour=c.MaTour WHERE c.TrangThai=? AND c.NgayDi>=CAST(GETDATE() AS date) ORDER BY c.NgayDi",QuyDinh.MO_DANG_KY); }
    public DefaultTableModel diemBan() throws SQLException { return Db.query("SELECT MaDiemBan,TenDiemBan FROM DiemBanVe ORDER BY TenDiemBan"); }
    public KetQuaXuLy dangKy(String soDK,String maChuyen,String maDiemBan,String ten,String dienThoai,int soNguoi){
        if(blank(soDK,maChuyen,maDiemBan,ten,dienThoai))return KetQuaXuLy.fail("Vui lòng nhập đầy đủ thông tin đăng ký khách lẻ.");
        if(soNguoi<1||soNguoi>=12)return KetQuaXuLy.fail("Khách lẻ phải từ 1 đến 11 người. Đúng 12 người hiện chưa được quy định.");
        try{return Db.transaction(c->{
            Object gia=Db.scalar(c,"SELECT t.DonGiaKhach FROM ChuyenLe c JOIN Tour t ON t.MaTour=c.MaTour WHERE c.MaChuyen=? AND c.TrangThai=? AND c.NgayDi>=CAST(GETDATE() AS date)",maChuyen,QuyDinh.MO_DANG_KY);
            if(gia==null)throw new IllegalArgumentException("Chuyến không tồn tại, đã đóng đăng ký hoặc đã khởi hành.");
            BigDecimal total=((BigDecimal)gia).multiply(BigDecimal.valueOf(soNguoi));
            Db.execute(c,"INSERT INTO DangKyLe(SoDKLe,MaChuyen,MaDiemBan,NgayDangKy,TenNguoiDangKy,DienThoai,SoNguoi,ThanhTien,DaThanhToan,TrangThai) VALUES(?,?,?,SYSDATETIME(),?,?,?,?,1,?)",soDK,maChuyen,maDiemBan,ten,dienThoai,soNguoi,total,QuyDinh.DA_DANG_KY);
            return KetQuaXuLy.ok("Đã đăng ký và thu tiền vé. Thành tiền: "+total.toPlainString()+" đ.");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }
    private boolean blank(String...s){for(String x:s)if(x==null||x.isBlank())return true;return false;}
    static String rootMessage(Throwable e){while(e.getCause()!=null)e=e.getCause();return e.getMessage()==null?e.toString():e.getMessage();}
}
