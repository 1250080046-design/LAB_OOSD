package com.donga.service;
import com.donga.data.Db;
import java.sql.*;
import java.math.*;
import java.time.*;
/** Transactional workflows for hiring, bonus payment and monthly payroll. */
public class HrService {
 public int hire(int maPhong,String hoTen,LocalDate ngayVao,String soQD,LocalDate tu,LocalDate den,BigDecimal luong,String dieuKhoan) throws SQLException {
  if(!den.isAfter(tu))throw new IllegalArgumentException("Ngày hết thử việc phải sau ngày bắt đầu");
  try(Connection c=Db.connect()){
   c.setAutoCommit(false);
   try {
    int nv;
    try(PreparedStatement p=c.prepareStatement("INSERT INTO NhanVien(HoTen,NgayVaoLam,MaPhong) VALUES(?,?,?)",Statement.RETURN_GENERATED_KEYS)){
     p.setString(1,hoTen);p.setDate(2,Date.valueOf(ngayVao));p.setInt(3,maPhong);p.executeUpdate();try(ResultSet rs=p.getGeneratedKeys()){rs.next();nv=rs.getInt(1);}}
    try(PreparedStatement p=c.prepareStatement("INSERT INTO QuyetDinhTuyenDung(MaNV,MaPhong,SoQuyetDinh,NgayKy,TuNgay,DenNgay,LuongThuViec,DieuKhoan) VALUES(?,?,?,CONVERT(DATE,GETDATE()),?,?,?,?)")){
     p.setInt(1,nv);p.setInt(2,maPhong);p.setString(3,soQD);p.setDate(4,Date.valueOf(tu));p.setDate(5,Date.valueOf(den));p.setBigDecimal(6,luong);p.setString(7,dieuKhoan);p.executeUpdate();}
    c.commit();return nv;
   }catch(Exception ex){c.rollback();if(ex instanceof SQLException sql)throw sql;throw new SQLException(ex);}
  }
 }
 public void confirmEmployment(int maNV,String soHD,String loai,LocalDate tu,LocalDate den,String soQDL,BigDecimal luong,String soQDCV,int maChucVu,BigDecimal phuCap) throws SQLException {
  try(Connection c=Db.connect()){
   c.setAutoCommit(false);try {
    String state=null;LocalDate end=null;
    try(PreparedStatement p=c.prepareStatement("SELECT TOP 1 KetQua,DenNgay FROM QuyetDinhTuyenDung WITH (UPDLOCK,HOLDLOCK) WHERE MaNV=? ORDER BY MaQDTD DESC")){
     p.setInt(1,maNV);try(ResultSet r=p.executeQuery()){if(r.next()){state=r.getString(1);end=r.getDate(2).toLocalDate();}}}
    if(!"DAT".equals(state)||end==null||tu.isBefore(end))throw new SQLException("Chưa đạt hoặc chưa kết thúc thử việc");
    if(!"DAI_HAN".equals(loai)&&!"NGAN_HAN".equals(loai))throw new SQLException("Loại hợp đồng không hợp lệ");
    try(PreparedStatement p=c.prepareStatement("INSERT INTO HopDong(MaNV,SoHopDong,LoaiHopDong,NgayKy,TuNgay,DenNgay) VALUES(?,?,?,CONVERT(DATE,GETDATE()),?,?)")){
     p.setInt(1,maNV);p.setString(2,soHD);p.setString(3,loai);p.setDate(4,Date.valueOf(tu));if(den==null)p.setNull(5,Types.DATE);else p.setDate(5,Date.valueOf(den));p.executeUpdate();}
    try(PreparedStatement p=c.prepareStatement("INSERT INTO QuyetDinhLuong(MaNV,SoQuyetDinh,NgayKy,HieuLucTu,LuongCoBan) VALUES(?,?,CONVERT(DATE,GETDATE()),?,?)")){
     p.setInt(1,maNV);p.setString(2,soQDL);p.setDate(3,Date.valueOf(tu));p.setBigDecimal(4,luong);p.executeUpdate();}
    try(PreparedStatement p=c.prepareStatement("INSERT INTO QuyetDinhChucVu(MaNV,MaChucVu,SoQuyetDinh,NgayKy,HieuLucTu,PhuCapChucVu) VALUES(?,?,?,CONVERT(DATE,GETDATE()),?,?)")){
     p.setInt(1,maNV);p.setInt(2,maChucVu);p.setString(3,soQDCV);p.setDate(4,Date.valueOf(tu));p.setBigDecimal(5,phuCap);p.executeUpdate();}
    try(PreparedStatement p=c.prepareStatement("UPDATE NhanVien SET TrangThai='CHINH_THUC' WHERE MaNV=?")){p.setInt(1,maNV);p.executeUpdate();}
    c.commit();
   }catch(Exception e){c.rollback();if(e instanceof SQLException q)throw q;throw new SQLException(e);}
  }
 }
 public int rewardDecision(String so,String loai,String doiTuong,Integer maNV,Integer maPhong,LocalDate ngay,String lyDo,BigDecimal tien,String hinhThuc,String nguoiKy) throws SQLException {
  try(Connection c=Db.connect()){c.setAutoCommit(false);try {
   int id;try(PreparedStatement p=c.prepareStatement("INSERT INTO QuyetDinhThuongPhat(SoQuyetDinh,Loai,DoiTuong,MaNV,MaPhong,NgayKy,LyDo,SoTien,HinhThuc,NguoiKy) VALUES(?,?,?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
    p.setString(1,so);p.setString(2,loai);p.setString(3,doiTuong);if(maNV==null)p.setNull(4,Types.INTEGER);else p.setInt(4,maNV);if(maPhong==null)p.setNull(5,Types.INTEGER);else p.setInt(5,maPhong);p.setDate(6,Date.valueOf(ngay));p.setString(7,lyDo);p.setBigDecimal(8,tien);p.setString(9,hinhThuc);p.setString(10,nguoiKy);p.executeUpdate();try(ResultSet r=p.getGeneratedKeys()){r.next();id=r.getInt(1);}}
   // Individual cash vouchers are created once; department-wide awards must be allocated explicitly per employee.
   if("CA_NHAN".equals(doiTuong))try(PreparedStatement p=c.prepareStatement("INSERT INTO PhieuThuChi(MaQD,MaNV,SoPhieu,NgayPhieu,Loai,SoTien,DaThucHien) VALUES(?,?,?,? ,?,?,0)")){
    p.setInt(1,id);p.setInt(2,maNV);p.setString(3,"TC-"+id);p.setDate(4,Date.valueOf(ngay));p.setString(5,"THUONG".equals(loai)?"CHI":"THU");p.setBigDecimal(6,tien);p.executeUpdate();}
   c.commit();return id;
  }catch(Exception e){c.rollback();if(e instanceof SQLException q)throw q;throw new SQLException(e);}}
 }
 /** Lập hồ sơ BHXH/BHYT chỉ cho hợp đồng dài hạn, đúng nhân viên. Tỷ lệ do người dùng nhập. */
 public int createInsurance(int employeeId,int contractId,String socialNumber,String healthNumber,LocalDate effective,BigDecimal socialRate,BigDecimal healthRate,BigDecimal basis) throws SQLException {
  if(employeeId<=0||contractId<=0||effective==null||basis==null||basis.signum()<0||socialRate==null||healthRate==null||socialRate.signum()<0||healthRate.signum()<0||socialRate.compareTo(BigDecimal.ONE)>0||healthRate.compareTo(BigDecimal.ONE)>0)
   throw new IllegalArgumentException("Thông tin bảo hiểm không hợp lệ");
  try(Connection c=Db.connect()){
   try(PreparedStatement p=c.prepareStatement("SELECT LoaiHopDong,TuNgay,DenNgay,TrangThai FROM HopDong WHERE MaHD=? AND MaNV=?")){
    p.setInt(1,contractId);p.setInt(2,employeeId);try(ResultSet r=p.executeQuery()){
     if(!r.next()||!"DAI_HAN".equals(r.getString(1))||!"HIEU_LUC".equals(r.getString(4)))throw new SQLException("Chỉ được tạo bảo hiểm cho hợp đồng dài hạn đang hiệu lực");
     if(effective.isBefore(r.getDate(2).toLocalDate())||(r.getDate(3)!=null&&effective.isAfter(r.getDate(3).toLocalDate())))throw new SQLException("Ngày bảo hiểm ngoài thời hạn hợp đồng");
    }
   }
   try(PreparedStatement p=c.prepareStatement("INSERT INTO HoSoBaoHiem(MaNV,MaHD,SoBHXH,SoBHYT,TuNgay,TyLeBHXH,TyLeBHYT,MucLuongDong) VALUES(?,?,?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
    p.setInt(1,employeeId);p.setInt(2,contractId);p.setString(3,socialNumber);p.setString(4,healthNumber);p.setDate(5,Date.valueOf(effective));p.setBigDecimal(6,socialRate);p.setBigDecimal(7,healthRate);p.setBigDecimal(8,basis);p.executeUpdate();
    try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getInt(1);}
   }
  }
 }
 /** Phân bổ thưởng/phạt tập thể cho nhân viên. Tổng phiếu không vượt tiền quyết định. */
 public void allocateCollectiveDecision(int decisionId,int employeeId,String voucherCode,BigDecimal amount,LocalDate voucherDate) throws SQLException {
  if(amount==null||amount.signum()<=0||voucherCode==null||voucherCode.isBlank())throw new IllegalArgumentException("Số phiếu hoặc số tiền không hợp lệ");
  try(Connection c=Db.connect()){
   c.setAutoCommit(false);
   try{
    String kind=null;BigDecimal allowed=null;int dept=-1;
    try(PreparedStatement p=c.prepareStatement("SELECT Loai,SoTien,MaPhong FROM QuyetDinhThuongPhat WITH(UPDLOCK,HOLDLOCK) WHERE MaQD=? AND DoiTuong='TAP_THE'")){
     p.setInt(1,decisionId);try(ResultSet r=p.executeQuery()){if(r.next()){kind=r.getString(1);allowed=r.getBigDecimal(2);dept=r.getInt(3);}}
    }
    if(kind==null)throw new SQLException("Không tìm thấy quyết định tập thể");
    try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM NhanVien WHERE MaNV=? AND MaPhong=?")){
     p.setInt(1,employeeId);p.setInt(2,dept);try(ResultSet r=p.executeQuery()){r.next();if(r.getInt(1)==0)throw new SQLException("Nhân viên không thuộc phòng ban của quyết định");}
    }
    BigDecimal allocated=BigDecimal.ZERO;
    try(PreparedStatement p=c.prepareStatement("SELECT COALESCE(SUM(SoTien),0) FROM PhieuThuChi WHERE MaQD=?")){
     p.setInt(1,decisionId);try(ResultSet r=p.executeQuery()){r.next();allocated=r.getBigDecimal(1);}
    }
    if(allocated.add(amount).compareTo(allowed)>0)throw new SQLException("Tổng phân bổ vượt giá trị quyết định");
    try(PreparedStatement p=c.prepareStatement("INSERT INTO PhieuThuChi(MaQD,MaNV,SoPhieu,NgayPhieu,Loai,SoTien) VALUES(?,?,?,?,?,?)")){
     p.setInt(1,decisionId);p.setInt(2,employeeId);p.setString(3,voucherCode);p.setDate(4,Date.valueOf(voucherDate));p.setString(5,"THUONG".equals(kind)?"CHI":"THU");p.setBigDecimal(6,amount);p.executeUpdate();
    }
    c.commit();
   }catch(Exception ex){c.rollback();if(ex instanceof SQLException q)throw q;throw new SQLException(ex);}
  }
 }
 public int calculatePayroll(int month,int year,int standardHours) throws SQLException {
  if(month<1||month>12||year<2000||standardHours<=0)throw new IllegalArgumentException("Tháng/năm/giờ chuẩn không hợp lệ");
  LocalDate start=LocalDate.of(year,month,1), end=start.plusMonths(1);
  try(Connection c=Db.connect()){c.setAutoCommit(false);try {
   int payrollId;
   try(PreparedStatement ps=c.prepareStatement("SELECT MaBangLuong,TrangThai FROM BangLuong WITH(UPDLOCK,HOLDLOCK) WHERE Thang=? AND Nam=?")){
    ps.setInt(1,month);ps.setInt(2,year);try(ResultSet r=ps.executeQuery()){if(r.next())throw new SQLException("Bảng lương tháng này đã tồn tại (không tự ghi đè)");}}
   try(PreparedStatement ps=c.prepareStatement("INSERT INTO BangLuong(Thang,Nam) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS)){
    ps.setInt(1,month);ps.setInt(2,year);ps.executeUpdate();try(ResultSet r=ps.getGeneratedKeys()){r.next();payrollId=r.getInt(1);}}
   String sql="""
    SELECT n.MaNV,
      COALESCE(l.LuongCoBan,td.LuongThuViec,0) LuongCoBan,
      COALESCE(l.PhuCapLuong,0) PhuCapLuong, COALESCE(l.PhuCapDocHai,0) PhuCapDocHai,
      COALESCE(cv.PhuCapChucVu,0) PhuCapChucVu,
      COALESCE(cc.GioLam,0) GioLam,
      COALESCE(pt.GioPhuTroi,0) GioPhuTroi,
      COALESCE(tp.TienThuong,0) TienThuong, COALESCE(tp.TienPhat,0) TienPhat,
      CASE WHEN hd.LoaiHopDong='DAI_HAN' THEN COALESCE(bh.MucLuongDong*(bh.TyLeBHXH+bh.TyLeBHYT),0) ELSE 0 END TruBH,
      CASE WHEN hd.LoaiHopDong='DAI_HAN' AND bh.MaBH IS NULL THEN 1 ELSE 0 END MissingInsurance
    FROM NhanVien n
    OUTER APPLY (SELECT TOP 1 * FROM QuyetDinhLuong WHERE MaNV=n.MaNV AND HieuLucTu<? ORDER BY HieuLucTu DESC,MaQDL DESC) l
    OUTER APPLY (SELECT TOP 1 * FROM QuyetDinhTuyenDung WHERE MaNV=n.MaNV AND TuNgay<? AND DenNgay>=? ORDER BY MaQDTD DESC) td
    OUTER APPLY (SELECT TOP 1 * FROM QuyetDinhChucVu WHERE MaNV=n.MaNV AND HieuLucTu<? ORDER BY HieuLucTu DESC,MaQDCV DESC) cv
    OUTER APPLY (SELECT TOP 1 * FROM HopDong WHERE MaNV=n.MaNV AND TrangThai='HIEU_LUC' AND TuNgay<? AND (DenNgay IS NULL OR DenNgay>=?) ORDER BY TuNgay DESC,MaHD DESC) hd
    OUTER APPLY (SELECT TOP 1 * FROM HoSoBaoHiem WHERE MaNV=n.MaNV AND MaHD=hd.MaHD AND TuNgay<? AND (DenNgay IS NULL OR DenNgay>=?) ORDER BY TuNgay DESC,MaBH DESC) bh
    OUTER APPLY (SELECT SUM(GioThucTe) GioLam FROM ChamCong WHERE MaNV=n.MaNV AND NgayCong>=? AND NgayCong<? AND TrangThai='CO_MAT') cc
    OUTER APPLY (SELECT SUM(ct.SoGioThamGia*p.HeSo) GioPhuTroi FROM ChiTietPhuTroi ct JOIN PhieuPhuTroi p ON p.MaPhieu=ct.MaPhieu WHERE ct.MaNV=n.MaNV AND p.NgayPhuTroi>=? AND p.NgayPhuTroi<?) pt
    OUTER APPLY (SELECT SUM(CASE WHEN q.Loai='THUONG' THEN x.SoTien ELSE 0 END) TienThuong,SUM(CASE WHEN q.Loai='KY_LUAT' THEN x.SoTien ELSE 0 END) TienPhat FROM PhieuThuChi x JOIN QuyetDinhThuongPhat q ON q.MaQD=x.MaQD WHERE x.MaNV=n.MaNV AND x.DaThucHien=0 AND x.MaBangLuongApDung IS NULL AND q.HinhThuc='QUA_LUONG' AND q.NgayKy>=? AND q.NgayKy<?) tp
    WHERE n.NgayVaoLam<? AND n.TrangThai<>'NGHI_VIEC'
    """;
   int count=0;
   try(PreparedStatement ps=c.prepareStatement(sql)){
    int i=1;for(LocalDate d:new LocalDate[]{end,end,start,end,end,start,end,start,start,end,start,end,start,end,end})ps.setDate(i++,Date.valueOf(d));
    try(ResultSet r=ps.executeQuery();PreparedStatement insert=c.prepareStatement("INSERT INTO ChiTietLuong(MaBangLuong,MaNV,LuongCoBan,PhuCapLuong,PhuCapDocHai,PhuCapChucVu,GioChuan,GioLam,GioPhuTroi,TienPhuTroi,TienThuong,TienPhat,TruBaoHiem,ThucLinh) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)")){
     while(r.next()){
      BigDecimal base=r.getBigDecimal("LuongCoBan");if(base.signum()==0)continue;
      if(r.getInt("MissingInsurance")==1)throw new SQLException("Nhân viên #"+r.getInt("MaNV")+" có hợp đồng dài hạn nhưng chưa có hồ sơ bảo hiểm");
      BigDecimal al=r.getBigDecimal("PhuCapLuong"),tox=r.getBigDecimal("PhuCapDocHai"),position=r.getBigDecimal("PhuCapChucVu");
      BigDecimal hours=r.getBigDecimal("GioLam"), overtimeWeighted=r.getBigDecimal("GioPhuTroi");
      BigDecimal unit=base.divide(BigDecimal.valueOf(standardHours),8,RoundingMode.HALF_UP);
      BigDecimal ot=unit.multiply(overtimeWeighted).setScale(2,RoundingMode.HALF_UP);
      BigDecimal wage=unit.multiply(hours).setScale(2,RoundingMode.HALF_UP);
      BigDecimal bonus=r.getBigDecimal("TienThuong"),fine=r.getBigDecimal("TienPhat"),ins=r.getBigDecimal("TruBH");
      BigDecimal net=PayrollMath.net(base,al.add(tox).add(position),hours,BigDecimal.valueOf(standardHours),overtimeWeighted,bonus,fine,ins);
      int j=1;insert.setInt(j++,payrollId);insert.setInt(j++,r.getInt("MaNV"));for(BigDecimal v:new BigDecimal[]{base,al,tox,position,BigDecimal.valueOf(standardHours),hours,overtimeWeighted,ot,bonus,fine,ins,net})insert.setBigDecimal(j++,v);insert.addBatch();count++;
     }insert.executeBatch();
    }
   }
   // Mark only vouchers already allocated to employee salary in the current period.
   // The UPDATE shares the same transaction with the payslip rows.
   try(PreparedStatement mark=c.prepareStatement("""
     UPDATE x SET MaBangLuongApDung=?
     FROM PhieuThuChi x JOIN QuyetDinhThuongPhat q ON q.MaQD=x.MaQD
     JOIN ChiTietLuong ct ON ct.MaNV=x.MaNV AND ct.MaBangLuong=?
     WHERE x.DaThucHien=0 AND x.MaBangLuongApDung IS NULL AND q.HinhThuc='QUA_LUONG'
       AND q.NgayKy>=? AND q.NgayKy<?
     """)){
      mark.setInt(1,payrollId);mark.setInt(2,payrollId);
      mark.setDate(3,Date.valueOf(start));mark.setDate(4,Date.valueOf(end));mark.executeUpdate();
   }
   c.commit();return count;
  }catch(Exception ex){c.rollback();if(ex instanceof SQLException q)throw q;throw new SQLException(ex);}}
 }
}
