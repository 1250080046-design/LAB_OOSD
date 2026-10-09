package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

public class KetThucService {
    public DefaultTableModel phieuDoanThanhToan() throws SQLException { return Db.query("SELECT d.SoDKDoan,k.TenCoQuanDaiDien,t.TenTour,d.NgayKetThucDuKien,d.TongTienDuKien,d.TienCoc,ISNULL(p.DaThanhToan,0) AS DaThanhToan,d.TongTienDuKien-d.TienCoc-ISNULL(p.DaThanhToan,0) AS ConLai,d.TrangThai FROM DangKyDoan d JOIN DoanKhach k ON k.MaDoan=d.MaDoan JOIN Tour t ON t.MaTour=d.MaTour OUTER APPLY(SELECT SUM(SoTien) DaThanhToan FROM ThanhToanDoan x WHERE x.SoDKDoan=d.SoDKDoan)p WHERE d.TrangThai<>? ORDER BY d.NgayKetThucDuKien DESC",QuyDinh.HUY_MAT_COC); }
    public DefaultTableModel khaoSat() throws SQLException { return Db.query("SELECT MaKhaoSat,LoaiKhach,COALESCE(SoDKLe,SoDKDoan) AS SoDangKy,NgayGui,NgayPhanHoi,DiemDanhGia,GopY FROM KhaoSat ORDER BY NgayGui DESC"); }
    public DefaultTableModel dangKyChoKhaoSat(String loai) throws SQLException {
        if(QuyDinh.LE.equals(loai))return Db.query("SELECT d.SoDKLe AS Ma,CONCAT(d.SoDKLe,' - ',d.TenNguoiDangKy) AS HienThi FROM DangKyLe d JOIN ChuyenLe c ON c.MaChuyen=d.MaChuyen WHERE c.NgayVe<CAST(GETDATE() AS date) AND NOT EXISTS(SELECT 1 FROM KhaoSat k WHERE k.SoDKLe=d.SoDKLe) ORDER BY d.SoDKLe");
        return Db.query("SELECT d.SoDKDoan AS Ma,CONCAT(d.SoDKDoan,' - ',k.TenCoQuanDaiDien) AS HienThi FROM DangKyDoan d JOIN DoanKhach k ON k.MaDoan=d.MaDoan WHERE d.TrangThai<>? AND d.NgayKetThucDuKien<CAST(GETDATE() AS date) AND NOT EXISTS(SELECT 1 FROM KhaoSat s WHERE s.SoDKDoan=d.SoDKDoan) ORDER BY d.SoDKDoan",QuyDinh.HUY_MAT_COC);
    }
    public KetQuaXuLy thanhToanDoan(String maTT,String soDK,LocalDate ngay,BigDecimal soTien,String ghiChu){
        if(blank(maTT,soDK)||ngay==null||soTien==null||soTien.signum()<=0)return KetQuaXuLy.fail("Thông tin thanh toán không hợp lệ.");
        try{return Db.transaction(c->{var r=Db.query(c,"SELECT NgayKetThucDuKien,TongTienDuKien,TienCoc,TrangThai FROM DangKyDoan WHERE SoDKDoan=?",soDK);if(r.getRowCount()==0)throw new IllegalArgumentException("Không tìm thấy phiếu đoàn.");if(!QuyDinh.DA_DANG_KY.equals(String.valueOf(r.getValueAt(0,3))))throw new IllegalArgumentException("Phiếu đã hủy hoặc thanh toán đủ.");if(!ngay.isAfter(toDate(r.getValueAt(0,0))))throw new IllegalArgumentException("Chỉ thanh toán kinh phí đoàn sau ngày kết thúc tour.");BigDecimal tong=(BigDecimal)r.getValueAt(0,1),coc=(BigDecimal)r.getValueAt(0,2);Object paid=Db.scalar(c,"SELECT ISNULL(SUM(SoTien),0) FROM ThanhToanDoan WHERE SoDKDoan=?",soDK);BigDecimal da=paid instanceof BigDecimal b?b:new BigDecimal(String.valueOf(paid));BigDecimal conLai=tong.subtract(coc).subtract(da);if(conLai.signum()<0)conLai=BigDecimal.ZERO;if(soTien.compareTo(conLai)>0)throw new IllegalArgumentException("Số tiền vượt phần còn lại: "+String.format("%,.0f",conLai)+" đ.");if(soTien.compareTo(conLai)==0&&conLai.signum()>0){Db.execute(c,"UPDATE DangKyDoan SET TrangThai=? WHERE SoDKDoan=?",QuyDinh.HOAN_TAT,soDK);}
            Db.execute(c,"INSERT INTO ThanhToanDoan(SoTT,SoDKDoan,NgayThanhToan,SoTien,GhiChu) VALUES(?,?,?,?,?)",maTT,soDK,java.sql.Date.valueOf(ngay),soTien,ghiChu);
            BigDecimal balance=conLai.subtract(soTien);return KetQuaXuLy.ok(balance.signum()==0?"Đã ghi nhận thanh toán; đoàn đã hoàn tất thanh toán.":"Đã ghi nhận thanh toán; còn lại "+String.format("%,.0f",balance)+" đ.");
        });}catch(Exception e){return KetQuaXuLy.fail(DangKyLeService.rootMessage(e));}
    }
    public KetQuaXuLy guiKhaoSat(String maKS,String loai,String soDK,LocalDate ngayGui){
        if(blank(maKS,loai,soDK)||ngayGui==null||(!QuyDinh.LE.equals(loai)&&!QuyDinh.DOAN.equals(loai)))return KetQuaXuLy.fail("Thông tin khảo sát chưa đầy đủ.");
        try{Object end=QuyDinh.LE.equals(loai)?Db.scalar("SELECT c.NgayVe FROM DangKyLe d JOIN ChuyenLe c ON c.MaChuyen=d.MaChuyen WHERE d.SoDKLe=?",soDK):Db.scalar("SELECT NgayKetThucDuKien FROM DangKyDoan WHERE SoDKDoan=? AND TrangThai<>?",soDK,QuyDinh.HUY_MAT_COC);if(end==null)return KetQuaXuLy.fail("Không tìm thấy đăng ký hợp lệ.");if(!ngayGui.isAfter(toDate(end)))return KetQuaXuLy.fail("Chỉ gửi phiếu khảo sát sau khi kết thúc tour.");Db.execute("INSERT INTO KhaoSat(MaKhaoSat,LoaiKhach,SoDKLe,SoDKDoan,NgayGui) VALUES(?,?,?,?,?)",maKS,loai,QuyDinh.LE.equals(loai)?soDK:null,QuyDinh.DOAN.equals(loai)?soDK:null,java.sql.Date.valueOf(ngayGui));return KetQuaXuLy.ok("Đã gửi phiếu khảo sát "+maKS+".");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}
    }
    public KetQuaXuLy ghiPhanHoi(String maKS,LocalDate ngay,int diem,String yKien){try{if(blank(maKS)||ngay==null)return KetQuaXuLy.fail("Chưa chọn phiếu khảo sát hoặc ngày phản hồi.");if(diem<1||diem>5)return KetQuaXuLy.fail("Điểm đánh giá phải từ 1 đến 5.");Object send=Db.scalar("SELECT NgayGui FROM KhaoSat WHERE MaKhaoSat=?",maKS);if(send==null)return KetQuaXuLy.fail("Không tìm thấy phiếu khảo sát.");if(ngay.isBefore(toDate(send)))return KetQuaXuLy.fail("Ngày phản hồi không được trước ngày gửi.");Db.execute("UPDATE KhaoSat SET NgayPhanHoi=?,DiemDanhGia=?,GopY=? WHERE MaKhaoSat=?",java.sql.Date.valueOf(ngay),diem,yKien,maKS);return KetQuaXuLy.ok("Đã ghi nhận góp ý của khách hàng.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    private static LocalDate toDate(Object value){if(value instanceof java.sql.Date d)return d.toLocalDate();if(value instanceof java.sql.Timestamp t)return t.toLocalDateTime().toLocalDate();return LocalDate.parse(String.valueOf(value).substring(0,10));}
    private boolean blank(String...s){for(String x:s)if(x==null||x.isBlank())return true;return false;}
}
