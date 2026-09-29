package com.quanlykhachsan.service;

import com.quanlykhachsan.data.Db;
import com.quanlykhachsan.model.KetQuaXuLy;
import com.quanlykhachsan.model.PhongDatItem;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class DatPhongService {
    public DefaultTableModel layKhach() { return Db.query("SELECT * FROM KhachHang ORDER BY HoTen"); }
    public DefaultTableModel layPhong() { return Db.query("SELECT p.*,k.TenKhuVuc FROM Phong p JOIN KhuVuc k ON p.MaKhuVuc=k.MaKhuVuc ORDER BY p.SoPhong"); }
    public DefaultTableModel layPhieuDat() { return Db.query("SELECT d.*,k.HoTen FROM PhieuDatPhong d JOIN KhachHang k ON d.MaKhach=k.MaKhach ORDER BY d.NgayLap DESC"); }
    public DefaultTableModel layChiTiet(String so) { return Db.query("SELECT c.*,p.SoNguoiToiDa,p.DonGiaNgay FROM ChiTietDatPhong c JOIN Phong p ON c.SoPhong=p.SoPhong WHERE c.SoPhieuDat=?",so); }
    public DefaultTableModel layNguoiLuuTru(String so) { return Db.query("SELECT * FROM NguoiLuuTru WHERE SoPhieuDat=? ORDER BY SoPhong,MaNguoiLT",so); }

    public KetQuaXuLy themKhach(String ma,String ten,String cmnd,String qt,String sdt){
        if(blank(ma)||blank(ten)||blank(cmnd)||blank(qt))return KetQuaXuLy.fail("Thông tin khách chưa đầy đủ.");
        try{Db.execute("INSERT INTO KhachHang(MaKhach,HoTen,SoCMND,QuocTich,SoDienThoai) VALUES(?,?,?,?,?)",ma,ten,cmnd,qt,blank(sdt)?null:sdt);return KetQuaXuLy.ok("Đã lưu khách hàng.");}
        catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}
    }

    private boolean phongTrungLich(java.sql.Connection c,String phong,Date nhan,Date tra)throws Exception{
        Object o=Db.scalar(c,"SELECT COUNT(*) FROM ChiTietDatPhong c JOIN PhieuDatPhong d ON c.SoPhieuDat=d.SoPhieuDat WHERE c.SoPhong=? AND d.TrangThai IN(N'Đã đặt',N'Đang ở') AND ?<=d.NgayTraDuKien AND ? >= d.NgayNhan",phong,nhan,tra);
        return ((Number)o).intValue()>0;
    }

    public KetQuaXuLy taoDatPhong(String so,String maKhach,String maNV,java.util.Date ngayLap,java.util.Date nhan,java.util.Date tra,double coc,String kenh,List<PhongDatItem> ds){
        if(blank(so)||blank(maKhach)||blank(maNV)||ds==null||ds.isEmpty())return KetQuaXuLy.fail("Phiếu đặt phòng chưa đủ thông tin.");
        Date n=new Date(nhan.getTime()), t=new Date(tra.getTime());
        if(t.before(n))return KetQuaXuLy.fail("Ngày trả dự kiến không được trước ngày nhận.");
        try{
            return Db.tx(c->{
                for(PhongDatItem x:ds){
                    Object q=Db.scalar(c,"SELECT SoNguoiToiDa FROM Phong WHERE SoPhong=?",x.soPhong());
                    if(q==null)throw new IllegalArgumentException("Không tìm thấy phòng "+x.soPhong());
                    int max=((Number)q).intValue();
                    if(x.soNguoi()<=0||x.soNguoi()>max)throw new IllegalArgumentException("Số người của phòng "+x.soPhong()+" vượt sức chứa.");
                    if(phongTrungLich(c,x.soPhong(),n,t))throw new IllegalArgumentException("Phòng "+x.soPhong()+" bị trùng lịch đặt.");
                }
                Db.execute(c,"INSERT INTO PhieuDatPhong(SoPhieuDat,MaKhach,MaNVLeTan,NgayLap,NgayNhan,NgayTraDuKien,TienCoc,KenhDat,TrangThai) VALUES(?,?,?,?,?,?,?,? ,N'Đã đặt')",so,maKhach,maNV,new Timestamp(ngayLap.getTime()),n,t,BigDecimal.valueOf(coc),kenh);
                for(PhongDatItem x:ds){
                    Db.execute(c,"INSERT INTO ChiTietDatPhong(SoPhieuDat,SoPhong,SoNguoi) VALUES(?,?,?)",so,x.soPhong(),x.soNguoi());
                    Db.execute(c,"UPDATE Phong SET TrangThai=N'Đã đặt' WHERE SoPhong=?",x.soPhong());
                }
                return KetQuaXuLy.ok("Đã lập phiếu đặt phòng.");
            });
        }catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    public KetQuaXuLy themNguoiLuuTru(String so,String phong,String ten,String cmnd,String qt){
        if(blank(so)||blank(phong)||blank(ten)||blank(cmnd)||blank(qt))return KetQuaXuLy.fail("Thông tin người lưu trú chưa đầy đủ.");
        try{
            Object m=Db.scalar("SELECT SoNguoi FROM ChiTietDatPhong WHERE SoPhieuDat=? AND SoPhong=?",so,phong);
            if(m==null)return KetQuaXuLy.fail("Không tìm thấy phòng trong phiếu đặt.");
            int max=((Number)m).intValue();
            int dem=((Number)Db.scalar("SELECT COUNT(*) FROM NguoiLuuTru WHERE SoPhieuDat=? AND SoPhong=?",so,phong)).intValue();
            if(dem>=max)return KetQuaXuLy.fail("Đã đủ số người đăng ký cho phòng này.");
            Db.execute("INSERT INTO NguoiLuuTru(SoPhieuDat,SoPhong,HoTen,SoCMND,QuocTich) VALUES(?,?,?,?,?)",so,phong,ten,cmnd,qt);
            return KetQuaXuLy.ok("Đã thêm người lưu trú.");
        }catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}
    }

    public KetQuaXuLy nhanPhong(String so,java.util.Date thucTe){
        try{return Db.tx(c->{
            int n=Db.execute(c,"UPDATE PhieuDatPhong SET TrangThai=N'Đang ở',NgayNhanThucTe=? WHERE SoPhieuDat=? AND TrangThai=N'Đã đặt'",new Timestamp(thucTe.getTime()),so);
            if(n==0)throw new IllegalArgumentException("Phiếu không ở trạng thái có thể nhận phòng.");
            Db.execute(c,"UPDATE Phong SET TrangThai=N'Đang ở' WHERE SoPhong IN(SELECT SoPhong FROM ChiTietDatPhong WHERE SoPhieuDat=?)",so);
            return KetQuaXuLy.ok("Đã nhận phòng.");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    public KetQuaXuLy danhDauNoShow(String so){
        try{return Db.tx(c->{
            int n=Db.execute(c,"UPDATE PhieuDatPhong SET TrangThai=N'No-show' WHERE SoPhieuDat=? AND TrangThai=N'Đã đặt'",so);
            if(n==0)throw new IllegalArgumentException("Phiếu không ở trạng thái Đã đặt.");
            Db.execute(c,"UPDATE Phong SET TrangThai=N'Trống' WHERE SoPhong IN(SELECT SoPhong FROM ChiTietDatPhong WHERE SoPhieuDat=?)",so);
            return KetQuaXuLy.ok("Đã đánh dấu không nhận phòng. Việc mất cọc được giữ theo quy định gốc; đề không nêu thời gian tự động nên thao tác này do nhân viên xác nhận.");
        });}catch(Exception e){return KetQuaXuLy.fail(rootMessage(e));}
    }

    private static String rootMessage(Exception e){
        Throwable t=e; while(t.getCause()!=null)t=t.getCause(); return t.getMessage()!=null?t.getMessage():e.getMessage();
    }
    private static boolean blank(String s){return s==null||s.trim().isEmpty();}
}
