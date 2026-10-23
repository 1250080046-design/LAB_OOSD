package com.benhvien.service;

import com.benhvien.data.Db;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AppointmentService {
    public long bookQuarterly(int patientId, Integer doctorId, LocalDateTime at, String notes) throws Exception {
        if (at == null)
            throw new IllegalArgumentException("Ngày giờ hẹn là bắt buộc.");
        int year = at.getYear(), quarter = ((at.getMonthValue() - 1) / 3) + 1;
        if (at.isBefore(LocalDateTime.now().minusMinutes(5)))
            throw new IllegalArgumentException("Ngày hẹn định kỳ phải ở hiện tại hoặc tương lai.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                Object record = Db.scalar(c,
                        "SELECT COUNT(*) FROM dbo.MedicalRecord WHERE PatientID=? AND Status=N'Đang hoạt động'",
                        patientId);
                if (((Number) record).intValue() == 0)
                    throw new IllegalArgumentException("Bệnh nhân chưa có hồ sơ bệnh án.");
                Object fee = Db.scalar(c, "SELECT COUNT(*) FROM dbo.AnnualFeePayment WHERE PatientID=? AND FeeYear=?",
                        patientId, year);
                if (((Number) fee).intValue() == 0)
                    throw new IllegalArgumentException("Bệnh nhân chưa đóng lệ phí năm " + year
                            + ". Khám định kỳ không thu phí riêng nhưng cần hồ sơ/lệ phí hợp lệ.");
                Object id = Db.scalar(c,
                        "INSERT INTO dbo.Appointment(PatientID,DoctorID,AppointmentAt,AppointmentYear,QuarterNo,Notes) OUTPUT INSERTED.AppointmentID VALUES(?,?,?,?,?,?)",
                        patientId, doctorId, Timestamp.valueOf(at), year, quarter,
                        notes == null || notes.isBlank() ? null : notes.trim());
                c.commit();
                return ((Number) id).longValue();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Map<String, Object>> list() throws SQLException {
        return Db.query(
                "SELECT a.AppointmentID AS MaLichHen,p.PatientID AS MaBenhNhan,p.FullName AS BenhNhan,a.AppointmentAt AS NgayGio,a.AppointmentYear AS Nam,a.QuarterNo AS Quy,d.FullName AS BacSi,a.Status AS TrangThai,a.Notes AS GhiChu FROM dbo.Appointment a JOIN dbo.Patient p ON p.PatientID=a.PatientID LEFT JOIN dbo.Doctor d ON d.DoctorID=a.DoctorID ORDER BY a.AppointmentAt DESC");
    }

    public void cancel(long id) throws SQLException {
        int n = Db.update(
                "UPDATE dbo.Appointment SET Status=N'Đã hủy' WHERE AppointmentID=? AND Status=N'Đã hẹn' AND NOT EXISTS(SELECT 1 FROM dbo.Visit WHERE AppointmentID=?)",
                id, id);
        if (n == 0)
            throw new SQLException("Không hủy được lịch: lịch không tồn tại, đã khám hoặc đã hủy.");
    }
}
