package com.tourcompany.services;

import com.tourcompany.data.Db;
import javax.swing.table.DefaultTableModel;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DangKyDoanService {
    public DefaultTableModel danhSach() throws SQLException { return Db.query("SELECT d.SoDKDoan,k.TenCoQuanDaiDien,t.TenTour,d.NgayDi,d.NgayKetThucDuKien,d.SoNguoi,d.MuaBaoHiem,d.TienCoc,d.TongTienDuKien,d.TrangThai FROM DangKyDoan d JOIN DoanKhach k ON k.MaDoan=d.MaDoan JOIN Tour t ON t.MaTour=d.MaTour ORDER BY d.NgayDangKy DESC"); }
    public DefaultTableModel thanhVien(String soDK) throws SQLException { return Db.query("SELECT STT,HoTen,NgaySinh,SoGiayTo FROM ThanhVienDoan WHERE SoDKDoan=? ORDER BY STT",soDK); }
    public DefaultTableModel toursMo() throws SQLException { return Db.query("SELECT MaTour,TenTour,SoNgay,DonGiaKhach FROM Tour WHERE DangMoBan=1 ORDER BY TenTour"); }

    public KetQuaXuLy dangKy(String soDK,String maDoan,String tenCoQuan,String diaChi,String dienThoai,String nguoiDaiDien,
                             String maTour,LocalDate ngayDi,int soNguoi,String diaDiemDon,boolean muaBaoHiem,BigDecimal tienCoc,List<ThanhVienDoanItem> thanhVien){
        if(blank(soDK,maDoan,tenCoQuan,diaChi,dienThoai,nguoiDaiDien,maTour,diaDiemDon)||ngayDi==null||tienCoc==null)return KetQuaXuLy.fail("Thông tin phiếu đăng ký đoàn chưa đầy đủ.");
        if(soNguoi<=12)return KetQuaXuLy.fail("Đoàn phải trên 12 người. Dưới 12 người đăng ký khách lẻ; đúng 12 người đề chưa quy định.");
        if(!ngayDi.isAfter(LocalDate.now()))return KetQuaXuLy.fail("Ngày đi phải sau ngày lập phiếu.");
        if(tienCoc.signum()<=0)return KetQuaXuLy.fail("Đoàn phải đặt cọc trước một khoản tiền.");
        if(muaBaoHiem){if(thanhVien==null||thanhVien.size()!=soNguoi)return KetQuaXuLy.fail("Đoàn mua bảo hiểm phải kèm danh sách đủ "+soNguoi+" người.");for(ThanhVienDoanItem x:thanhVien)if(x==null||x.hoTen()==null||x.hoTen().isBlank())return KetQuaXuLy.fail("Danh sách người cùng đi có dòng thiếu họ tên.");}
        try{return Db.transaction(c->{
            var q=Db.query(c,"SELECT SoNgay,DonGiaKhach FROM Tour WHERE MaTour=? AND DangMoBan=1",maTour);
            if(q.getRowCount()==0)throw new IllegalArgumentException("Tour không tồn tại hoặc chưa mở bán.");
            int soNgay=((Number)q.getValueAt(0,0)).intValue(); BigDecimal gia=(BigDecimal)q.getValueAt(0,1);
            LocalDate ngayKetThuc=ngayDi.plusDays(soNgay-1L); BigDecimal tong=gia.multiply(BigDecimal.valueOf(soNguoi));
            if(tienCoc.compareTo(tong)>0)throw new IllegalArgumentException("Tiền cọc không được vượt tổng tiền dự kiến.");
            Db.execute(c,"IF EXISTS(SELECT 1 FROM DoanKhach WHERE MaDoan=?) UPDATE DoanKhach SET TenCoQuanDaiDien=?,DiaChi=?,DienThoai=?,NguoiDaiDien=? WHERE MaDoan=? ELSE INSERT INTO DoanKhach(MaDoan,TenCoQuanDaiDien,DiaChi,DienThoai,NguoiDaiDien) VALUES(?,?,?,?,?)",
                maDoan,tenCoQuan,diaChi,dienThoai,nguoiDaiDien,maDoan,maDoan,tenCoQuan,diaChi,dienThoai,nguoiDaiDien);
            Db.execute(c,"INSERT INTO DangKyDoan(SoDKDoan,MaDoan,MaTour,NgayDangKy,NgayDi,NgayKetThucDuKien,SoNguoi,DiaDiemDon,MuaBaoHiem,TienCoc,DaThanhToanCoc,TongTienDuKien,TrangThai) VALUES(?,?,?,SYSDATETIME(),?,?,?,?,?,?,1,?,?)",
                soDK,maDoan,maTour,java.sql.Date.valueOf(ngayDi),java.sql.Date.valueOf(ngayKetThuc),soNguoi,diaDiemDon,muaBaoHiem,tienCoc,tong,QuyDinh.DA_DANG_KY);
            if(muaBaoHiem)for(int i=0;i<thanhVien.size();i++){ThanhVienDoanItem x=thanhVien.get(i);Db.execute(c,"INSERT INTO ThanhVienDoan(SoDKDoan,STT,HoTen,NgaySinh,SoGiayTo) VALUES(?,?,?,?,?)",soDK,i+1,x.hoTen(),x.ngaySinh()==null?null:java.sql.Date.valueOf(x.ngaySinh()),x.soGiayTo());}
            return KetQuaXuLy.ok("Đã lập phiếu đoàn. Kết thúc dự kiến "+ngayKetThuc+"; tổng dự kiến "+String.format("%,.0f",tong)+" đ; tiền cọc "+String.format("%,.0f",tienCoc)+" đ.");
        });}catch(Exception e){return KetQuaXuLy.fail(DangKyLeService.rootMessage(e));}
    }
    public KetQuaXuLy huyDangKy(String soDK){
        if(blank(soDK))return KetQuaXuLy.fail("Chưa chọn phiếu đăng ký đoàn.");
        try{return Db.transaction(c->{var t=Db.query(c,"SELECT NgayDi,TrangThai FROM DangKyDoan WHERE SoDKDoan=?",soDK);if(t.getRowCount()==0)throw new IllegalArgumentException("Không tìm thấy phiếu đoàn.");if(!QuyDinh.DA_DANG_KY.equals(String.valueOf(t.getValueAt(0,1))))throw new IllegalArgumentException("Chỉ hủy được phiếu đang ở trạng thái Đã đăng ký.");if(!((java.sql.Date)t.getValueAt(0,0)).toLocalDate().isAfter(LocalDate.now()))throw new IllegalArgumentException("Đoàn đã khởi hành, không thể hủy.");Db.execute(c,"DELETE FROM PhanCongHDV WHERE SoDKDoan=?",soDK);Db.execute(c,"UPDATE DangKyDoan SET TrangThai=? WHERE SoDKDoan=?",QuyDinh.HUY_MAT_COC,soDK);return KetQuaXuLy.ok("Đã hủy phiếu "+soDK+"; đoàn không đi bị mất tiền cọc.");});}catch(Exception e){return KetQuaXuLy.fail(DangKyLeService.rootMessage(e));}
    }
    private boolean blank(String...s){for(String x:s)if(x==null||x.isBlank())return true;return false;}
}
