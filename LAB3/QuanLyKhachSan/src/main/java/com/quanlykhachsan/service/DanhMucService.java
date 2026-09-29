package com.quanlykhachsan.service;

import com.quanlykhachsan.data.Db;
import com.quanlykhachsan.model.KetQuaXuLy;

import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;

public class DanhMucService {
    public DefaultTableModel layKhuVuc() { return Db.query("SELECT * FROM KhuVuc ORDER BY MaKhuVuc"); }
    public DefaultTableModel layNhanVien() { return Db.query("SELECT * FROM NhanVien ORDER BY MaNV"); }
    public DefaultTableModel layLoaiTienNghi() { return Db.query("SELECT * FROM LoaiTienNghi ORDER BY MaLoaiTN"); }
    public DefaultTableModel layDichVu() { return Db.query("SELECT * FROM DichVu ORDER BY MaDV"); }
    public DefaultTableModel layQuyDinhDenBu() {
        return Db.query("SELECT q.*,l.TenLoaiTN FROM QuyDinhDenBu q JOIN LoaiTienNghi l ON q.MaLoaiTN=l.MaLoaiTN ORDER BY q.MaQuyDinh");
    }

    public KetQuaXuLy themKhu(String ma, String ten) {
        if (blank(ma) || blank(ten)) return KetQuaXuLy.fail("Mã khu vực và tên khu vực không được để trống.");
        try {
            Db.execute("INSERT INTO KhuVuc(MaKhuVuc,TenKhuVuc) VALUES(?,?)", ma, ten);
            return KetQuaXuLy.ok("Đã thêm khu vực.");
        } catch (Exception e) { return KetQuaXuLy.fail(e.getMessage()); }
    }

    public KetQuaXuLy themNhanVien(String ma, String ten, String vaiTro, String sdt) {
        if (blank(ma) || blank(ten) || blank(vaiTro)) return KetQuaXuLy.fail("Thông tin nhân viên chưa đầy đủ.");
        try {
            Db.execute("INSERT INTO NhanVien(MaNV,HoTen,VaiTro,SoDienThoai) VALUES(?,?,?,?)", ma, ten, vaiTro, blank(sdt) ? null : sdt);
            return KetQuaXuLy.ok("Đã thêm nhân viên.");
        } catch (Exception e) { return KetQuaXuLy.fail(e.getMessage()); }
    }

    public KetQuaXuLy themLoaiTN(String ma, String ten) {
        if (blank(ma) || blank(ten)) return KetQuaXuLy.fail("Thông tin loại tiện nghi chưa đủ.");
        try {
            Db.execute("INSERT INTO LoaiTienNghi(MaLoaiTN,TenLoaiTN) VALUES(?,?)", ma, ten);
            return KetQuaXuLy.ok("Đã thêm loại tiện nghi.");
        } catch (Exception e) { return KetQuaXuLy.fail(e.getMessage()); }
    }

    public KetQuaXuLy themDichVu(String ma, String ten, String dvt, double gia) {
        if (blank(ma) || blank(ten) || blank(dvt) || gia < 0) return KetQuaXuLy.fail("Thông tin dịch vụ không hợp lệ.");
        try {
            Db.execute("INSERT INTO DichVu(MaDV,TenDV,DonViTinh,DonGia) VALUES(?,?,?,?)", ma, ten, dvt, BigDecimal.valueOf(gia));
            return KetQuaXuLy.ok("Đã thêm dịch vụ.");
        } catch (Exception e) { return KetQuaXuLy.fail(e.getMessage()); }
    }

    public KetQuaXuLy themQuyDinh(String ma, String loai, String muc, double tien) {
        if (blank(ma) || blank(loai) || blank(muc) || tien < 0) return KetQuaXuLy.fail("Quy định đền bù không hợp lệ.");
        try {
            Db.execute("INSERT INTO QuyDinhDenBu(MaQuyDinh,MaLoaiTN,MucDoThietHai,MucDenBu) VALUES(?,?,?,?)", ma, loai, muc, BigDecimal.valueOf(tien));
            return KetQuaXuLy.ok("Đã thêm quy định đền bù.");
        } catch (Exception e) { return KetQuaXuLy.fail(e.getMessage()); }
    }

    private static boolean blank(String s) { return s == null || s.trim().isEmpty(); }
}
