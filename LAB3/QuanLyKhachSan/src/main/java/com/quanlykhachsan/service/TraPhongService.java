package com.quanlykhachsan.service;

import com.quanlykhachsan.data.Db;
import com.quanlykhachsan.model.DenBuItem;
import com.quanlykhachsan.model.KetQuaXuLy;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class TraPhongService {
    public DefaultTableModel layPhieuDangO(){return Db.query("SELECT d.SoPhieuDat,k.HoTen,d.NgayNhanThucTe,d.NgayTraDuKien FROM PhieuDatPhong d JOIN KhachHang k ON d.MaKhach=k.MaKhach WHERE d.TrangThai=N'Đang ở' ORDER BY d.SoPhieuDat");}
    public DefaultTableModel layPhongTheoPhieu(String so){return Db.query("SELECT c.SoPhong,p.DonGiaNgay FROM ChiTietDatPhong c JOIN Phong p ON c.SoPhong=p.SoPhong WHERE c.SoPhieuDat=?",so);}
    public DefaultTableModel layTienNghiPhong(String phong){return Db.query("SELECT DISTINCT t.MaTienNghi,l.TenLoaiTN,t.TinhTrangHienTai FROM PhieuLapDat p JOIN TienNghi t ON p.MaTienNghi=t.MaTienNghi JOIN LoaiTienNghi l ON t.MaLoaiTN=l.MaLoaiTN WHERE p.SoPhong=? ORDER BY t.MaTienNghi",phong);}
    public DefaultTableModel layHoaDon(){return Db.query("SELECT h.*,k.HoTen FROM HoaDon h JOIN PhieuDatPhong d ON h.SoPhieuDat=d.SoPhieuDat JOIN KhachHang k ON d.MaKhach=k.MaKhach ORDER BY h.NgayLap DESC");}

    public KetQuaXuLy lapPhieuDenBu(String soDB,String soDat,String phong,java.util.Date ngay,String maNV,List<DenBuItem> ds){
        if(blank(soDB)||blank(soDat)||blank(phong)||blank(maNV)||ds==null||ds.isEmpty())return KetQuaXuLy.fail("Phiếu đền bù chưa đủ thông tin.");
        try{return Db.tx(c->{
            BigDecimal tong=BigDecimal.ZERO;
            for(DenBuItem x:ds){if(x.soTien().signum()<0)throw new IllegalArgumentException("Số tiền đền bù không hợp lệ.");tong=tong.add(x.soTien());}
            Db.execute(c,"INSERT INTO PhieuDenBu(SoPhieuDenBu,SoPhieuDat,SoPhong,NgayLap,MaNV,TongTien) VALUES(?,?,?,?,?,?)",soDB,soDat,phong,new Timestamp(ngay.getTime()),maNV,tong);
            for(DenBuItem x:ds)Db.execute(c,"INSERT INTO ChiTietPhieuDenBu(SoPhieuDenBu,MaTienNghi,MucDoThietHai,SoTien) VALUES(?,?,?,?)",soDB,x.maTienNghi(),x.mucDoThietHai(),x.soTien());
            return KetQuaXuLy.ok("Đã lập phiếu đền bù.");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    public KetQuaXuLy lapHoaDon(String soHD,String soDat,java.util.Date ngay,String maNV,int soNgay){
        if(blank(soHD)||blank(soDat)||blank(maNV)||soNgay<=0)return KetQuaXuLy.fail("Thông tin hóa đơn chưa hợp lệ.");
        try{
            BigDecimal phong=new BigDecimal(String.valueOf(Db.scalar("SELECT ISNULL(SUM(p.DonGiaNgay),0) FROM ChiTietDatPhong c JOIN Phong p ON c.SoPhong=p.SoPhong WHERE c.SoPhieuDat=?",soDat))).multiply(BigDecimal.valueOf(soNgay));
            Object dvObj=Db.scalar("SELECT ISNULL(SUM(c.ThanhTien),0) FROM PhieuSuDungDV h JOIN ChiTietPhieuSuDungDV c ON h.SoPhieuSDDV=c.SoPhieuSDDV WHERE h.SoPhieuDat=?",soDat);
            BigDecimal dv=new BigDecimal(String.valueOf(dvObj));
            Db.execute("INSERT INTO HoaDon(SoHoaDon,SoPhieuDat,NgayLap,MaNV,SoNgayTinhTien,TienPhong,TienDichVu,TrangThai) VALUES(?,?,?,?,?,?,?,N'Chưa thanh toán')",soHD,soDat,new Timestamp(ngay.getTime()),maNV,soNgay,phong,dv);
            return KetQuaXuLy.ok("Đã lập hóa đơn tiền phòng và dịch vụ. Số ngày tính tiền do nhân viên xác nhận vì đề không quy định cách làm tròn ngày/đêm.");
        }catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    public KetQuaXuLy thanhToan(String maTT,String soHD,java.util.Date ngay,String hinhThuc,double tien){
        if(blank(maTT)||blank(soHD)||blank(hinhThuc)||tien<=0)return KetQuaXuLy.fail("Thông tin thanh toán không hợp lệ.");
        try{return Db.tx(c->{
            Object o=Db.scalar(c,"SELECT TongTien FROM HoaDon WHERE SoHoaDon=?",soHD); if(o==null)throw new IllegalArgumentException("Không tìm thấy hóa đơn.");
            BigDecimal tong=new BigDecimal(o.toString());
            BigDecimal da=new BigDecimal(String.valueOf(Db.scalar(c,"SELECT ISNULL(SUM(SoTien),0) FROM ThanhToan WHERE SoHoaDon=?",soHD)));
            BigDecimal moi=da.add(BigDecimal.valueOf(tien)); if(moi.compareTo(tong)>0)throw new IllegalArgumentException("Số tiền thanh toán vượt số còn phải trả.");
            Db.execute(c,"INSERT INTO ThanhToan(MaThanhToan,SoHoaDon,NgayThanhToan,HinhThuc,SoTien) VALUES(?,?,?,?,?)",maTT,soHD,new Timestamp(ngay.getTime()),hinhThuc,BigDecimal.valueOf(tien));
            if(moi.compareTo(tong)==0)Db.execute(c,"UPDATE HoaDon SET TrangThai=N'Đã thanh toán' WHERE SoHoaDon=?",soHD);
            return KetQuaXuLy.ok("Đã ghi nhận thanh toán bằng "+hinhThuc+".");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    public KetQuaXuLy traPhong(String soDat,java.util.Date ngayTra){
        try{return Db.tx(c->{
            Object h=Db.scalar(c,"SELECT TrangThai FROM HoaDon WHERE SoPhieuDat=?",soDat); if(h==null)throw new IllegalArgumentException("Chưa lập hóa đơn cho phiếu đặt phòng.");
            if(!"Đã thanh toán".equals(String.valueOf(h)))throw new IllegalArgumentException("Hóa đơn chưa thanh toán đủ.");
            Db.execute(c,"UPDATE PhieuDatPhong SET TrangThai=N'Đã trả',NgayTraThucTe=? WHERE SoPhieuDat=?",new Timestamp(ngayTra.getTime()),soDat);
            Db.execute(c,"UPDATE Phong SET TrangThai=N'Trống' WHERE SoPhong IN(SELECT SoPhong FROM ChiTietDatPhong WHERE SoPhieuDat=?)",soDat);
            return KetQuaXuLy.ok("Đã hoàn tất trả phòng.");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
    private static String rootMessage(Exception e){Throwable t=e;while(t.getCause()!=null)t=t.getCause();return t.getMessage()!=null?t.getMessage():e.getMessage();}
}
