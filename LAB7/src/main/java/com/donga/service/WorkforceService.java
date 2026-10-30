package com.donga.service;

import com.donga.data.Db;
import java.sql.*;
import java.time.*;
import java.math.BigDecimal;

/** Planning and attendance writes with domain validation (not generic CRUD). */
public class WorkforceService {
  public int createWeeklyPlan(int departmentId, LocalDate monday, int managerId, String note) throws SQLException {
    if(monday.getDayOfWeek()!=DayOfWeek.MONDAY)throw new IllegalArgumentException("Tuần phải bắt đầu vào thứ Hai");
    try(Connection c=Db.connect()){
      try(PreparedStatement manager=c.prepareStatement("SELECT COUNT(*) FROM NhanVien WHERE MaNV=? AND MaPhong=? AND TrangThai<>'NGHI_VIEC'")){
        manager.setInt(1,managerId);manager.setInt(2,departmentId);
        try(ResultSet r=manager.executeQuery()){r.next();if(r.getInt(1)==0)throw new SQLException("Người lập không thuộc phòng ban hoặc đã nghỉ việc");}
      }
      try(PreparedStatement p=c.prepareStatement("INSERT INTO KeHoachTuan(MaPhong,TuanBatDau,NguoiLap,GhiChu) VALUES(?,?,?,?)",Statement.RETURN_GENERATED_KEYS)){
        p.setInt(1,departmentId);p.setDate(2,Date.valueOf(monday));p.setInt(3,managerId);p.setString(4,note);p.executeUpdate();
        try(ResultSet r=p.getGeneratedKeys()){r.next();return r.getInt(1);}
      }
    }
  }
  public void assignShift(int planId,int employeeId,int shiftId,LocalDate workDay,boolean plannedOvertime)throws SQLException {
    try(Connection c=Db.connect()){
      try(PreparedStatement p=c.prepareStatement("""
        SELECT k.TuanBatDau,k.MaPhong,n.MaPhong,N.TrangThai
        FROM KeHoachTuan k CROSS JOIN NhanVien n WHERE k.MaKH=? AND n.MaNV=?
        """)){
        p.setInt(1,planId);p.setInt(2,employeeId);
        try(ResultSet r=p.executeQuery()){
          if(!r.next())throw new SQLException("Không tìm thấy kế hoạch hoặc nhân viên");
          LocalDate monday=r.getDate(1).toLocalDate();
          if(workDay.isBefore(monday)||!workDay.isBefore(monday.plusDays(7)))throw new IllegalArgumentException("Ngày phân công nằm ngoài tuần đã lập");
          if(r.getInt(2)!=r.getInt(3)||"NGHI_VIEC".equals(r.getString(4)))throw new IllegalArgumentException("Nhân viên không thuộc phòng ban hoặc đã nghỉ việc");
        }
      }
      try(PreparedStatement p=c.prepareStatement("INSERT INTO PhanCongCa(MaKH,MaNV,MaCa,NgayLam,DuKienPhuTroi) VALUES(?,?,?,?,?)")){
        p.setInt(1,planId);p.setInt(2,employeeId);p.setInt(3,shiftId);p.setDate(4,Date.valueOf(workDay));p.setBoolean(5,plannedOvertime);p.executeUpdate();
      }
    }
  }
  public void recordAttendance(int employeeId,int shiftId,LocalDate day,BigDecimal workedHours,String status)throws SQLException {
    if(workedHours.compareTo(BigDecimal.ZERO)<0||workedHours.compareTo(BigDecimal.valueOf(24))>0)throw new IllegalArgumentException("Giờ chấm công phải nằm trong 0–24");
    if(!status.equals("CO_MAT")&&!status.equals("VANG")&&!status.equals("NGHI_PHEP"))throw new IllegalArgumentException("Trạng thái chấm công không hợp lệ");
    if(!status.equals("CO_MAT")&&workedHours.signum()!=0)throw new IllegalArgumentException("Vắng hoặc nghỉ phép phải ghi 0 giờ thực tế");
    try(Connection c=Db.connect()){
      try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM PhanCongCa WHERE MaNV=? AND MaCa=? AND NgayLam=?")){
        p.setInt(1,employeeId);p.setInt(2,shiftId);p.setDate(3,Date.valueOf(day));
        try(ResultSet r=p.executeQuery()){r.next();if(r.getInt(1)==0)throw new SQLException("Chưa phân công ca tương ứng trong kế hoạch tuần");}
      }
      try(PreparedStatement p=c.prepareStatement("INSERT INTO ChamCong(MaNV,MaCa,NgayCong,GioThucTe,TrangThai) VALUES(?,?,?,?,?)")){
        p.setInt(1,employeeId);p.setInt(2,shiftId);p.setDate(3,Date.valueOf(day));p.setBigDecimal(4,workedHours);p.setString(5,status);p.executeUpdate();
      }
    }
  }
  public void addOvertimeParticipant(int slipId,int employeeId,BigDecimal hours) throws SQLException {
    if(hours.signum()<=0)throw new IllegalArgumentException("Số giờ phụ trội phải lớn hơn 0");
    try(Connection c=Db.connect()){
      c.setAutoCommit(false);
      try{
        BigDecimal available=null;LocalDate day=null;
        try(PreparedStatement p=c.prepareStatement("SELECT SoGio,NgayPhuTroi FROM PhieuPhuTroi WITH(UPDLOCK,HOLDLOCK) WHERE MaPhieu=?")){
          p.setInt(1,slipId);try(ResultSet r=p.executeQuery()){if(r.next()){available=r.getBigDecimal(1);day=r.getDate(2).toLocalDate();}}
        }
        if(available==null||hours.compareTo(available)>0)throw new SQLException("Số giờ tham gia vượt số giờ của phiếu hoặc phiếu không tồn tại");
        try(PreparedStatement p=c.prepareStatement("SELECT COUNT(*) FROM PhanCongCa WHERE MaNV=? AND NgayLam=?")){
          p.setInt(1,employeeId);p.setDate(2,Date.valueOf(day));
          try(ResultSet r=p.executeQuery()){r.next();if(r.getInt(1)==0)throw new SQLException("Nhân viên không có phân công trong ngày phụ trội");}
        }
        try(PreparedStatement p=c.prepareStatement("INSERT INTO ChiTietPhuTroi(MaPhieu,MaNV,SoGioThamGia) VALUES(?,?,?)")){
          p.setInt(1,slipId);p.setInt(2,employeeId);p.setBigDecimal(3,hours);p.executeUpdate();
        }
        c.commit();
      }catch(Exception e){c.rollback();if(e instanceof SQLException q)throw q;throw new SQLException(e);}
    }
  }
}
