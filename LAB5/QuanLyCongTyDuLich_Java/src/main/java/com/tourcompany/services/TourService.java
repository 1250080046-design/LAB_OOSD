package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;

public class TourService {
    public DefaultTableModel tours() throws SQLException { return Db.query("SELECT MaTour,TenTour,SoNgay,SoDem,DonGiaKhach,MoTa,DangMoBan FROM Tour ORDER BY MaTour"); }
    public DefaultTableModel diemDung(String tour) throws SQLException { return Db.query("SELECT ThuTu,TenDiemDung,DoiPhuongTien,CoNoiAn,CoKhachSan,HangSaoKhachSan,GhiChu FROM TourDiemDung WHERE MaTour=? ORDER BY ThuTu",tour); }
    public DefaultTableModel chang(String tour) throws SQLException { return Db.query("SELECT t.ThuTuChang,p.TenPT,t.MaPT,t.GhiChu FROM TourPhuongTien t JOIN PhuongTien p ON p.MaPT=t.MaPT WHERE t.MaTour=? ORDER BY t.ThuTuChang",tour); }
    public DefaultTableModel diemTQTour(String tour) throws SQLException { return Db.query("SELECT x.ThuTu,d.MaDiemTQ,d.TenDiemTQ,d.DiaDiem FROM TourDiemThamQuan x JOIN DiemThamQuan d ON d.MaDiemTQ=x.MaDiemTQ WHERE x.MaTour=? ORDER BY x.ThuTu",tour); }
    public DefaultTableModel danhSachMaTenTour() throws SQLException { return Db.query("SELECT MaTour,TenTour FROM Tour WHERE DangMoBan=1 ORDER BY TenTour"); }
    public DefaultTableModel phuongTien() throws SQLException { return Db.query("SELECT MaPT,TenPT FROM PhuongTien ORDER BY TenPT"); }
    public DefaultTableModel diemTQ() throws SQLException { return Db.query("SELECT MaDiemTQ,TenDiemTQ FROM DiemThamQuan ORDER BY TenDiemTQ"); }
    public KetQuaXuLy themTour(String ma,String ten,int soNgay,int soDem,BigDecimal gia,String moTa){try{if(blank(ma,ten)||soNgay<=0||soDem<0||soDem>soNgay||gia==null||gia.signum()<0)return KetQuaXuLy.fail("Mã/tên, số ngày, số đêm hoặc đơn giá không hợp lệ.");Db.execute("INSERT INTO Tour(MaTour,TenTour,SoNgay,SoDem,DonGiaKhach,MoTa,DangMoBan) VALUES(?,?,?,?,?,?,1)",ma,ten,soNgay,soDem,gia,moTa);return KetQuaXuLy.ok("Đã thêm tour.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy themDiemDung(String maTour,int thuTu,String ten,boolean doiPT,boolean coAn,boolean coKS,Integer sao,String ghiChu){try{if(blank(maTour,ten)||thuTu<=0)return KetQuaXuLy.fail("Thông tin điểm dừng chưa đầy đủ.");if(coKS&&(sao==null||sao<2||sao>5))return KetQuaXuLy.fail("Khách sạn phải có hạng từ 2 đến 5 sao.");if(!coKS)sao=null;Db.execute("INSERT INTO TourDiemDung(MaTour,ThuTu,TenDiemDung,DoiPhuongTien,CoNoiAn,CoKhachSan,HangSaoKhachSan,GhiChu) VALUES(?,?,?,?,?,?,?,?)",maTour,thuTu,ten,doiPT,coAn,coKS,sao,ghiChu);return KetQuaXuLy.ok("Đã thêm điểm dừng.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy themChang(String tour,int thuTu,String maPT,String ghiChu){try{if(blank(tour,maPT)||thuTu<=0)return KetQuaXuLy.fail("Thông tin chặng/phương tiện không hợp lệ.");Db.execute("INSERT INTO TourPhuongTien(MaTour,ThuTuChang,MaPT,GhiChu) VALUES(?,?,?,?)",tour,thuTu,maPT,ghiChu);return KetQuaXuLy.ok("Đã gắn phương tiện cho chặng.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    public KetQuaXuLy themDiemTQ(String tour,String maDiem,int thuTu){try{if(blank(tour,maDiem)||thuTu<=0)return KetQuaXuLy.fail("Thông tin điểm tham quan không hợp lệ.");Db.execute("INSERT INTO TourDiemThamQuan(MaTour,MaDiemTQ,ThuTu) VALUES(?,?,?)",tour,maDiem,thuTu);return KetQuaXuLy.ok("Đã gắn điểm tham quan cho tour.");}catch(Exception e){return KetQuaXuLy.fail(e.getMessage());}}
    private static boolean blank(String...s){for(String x:s)if(x==null||x.isBlank())return true;return false;}
}
