package com.benhvien.service;

import com.benhvien.data.Db;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ReportService {
    public List<Map<String, Object>> summary() throws SQLException {
        return Db.query(
                "SELECT (SELECT COUNT(*) FROM dbo.Patient) AS TongBenhNhan,(SELECT COUNT(*) FROM dbo.MedicalRecord WHERE Status=N'Đang hoạt động') AS HoSoDangHoatDong,(SELECT COUNT(*) FROM dbo.TreatmentEpisode WHERE Status=N'Đang điều trị') AS DotDangDieuTri,(SELECT COUNT(*) FROM dbo.TreatmentEpisode WHERE Status=N'Đã hoàn tất') AS DotDaHoanTat,(SELECT COUNT(*) FROM dbo.HospitalInvoice WHERE Status=N'Chưa thanh toán') AS HoaDonChuaThanhToan,(SELECT COUNT(*) FROM dbo.Appointment WHERE Status=N'Đã hẹn') AS LichHenSapToi");
    }

    public List<Map<String, Object>> periodicCoverage() throws SQLException {
        return Db.query(
                "SELECT AppointmentYear AS Nam,QuarterNo AS Quy,COUNT(*) AS SoLichHen FROM dbo.Appointment GROUP BY AppointmentYear,QuarterNo ORDER BY AppointmentYear DESC,QuarterNo");
    }
}
