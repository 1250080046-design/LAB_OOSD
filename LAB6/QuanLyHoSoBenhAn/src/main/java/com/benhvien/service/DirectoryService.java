package com.benhvien.service;

import com.benhvien.data.Db;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DirectoryService {
    public List<Map<String, Object>> doctors() throws SQLException {
        return Db.query(
                "SELECT d.DoctorID AS MaBacSi,d.FullName AS BacSi,d.DepartmentID AS MaKhoa,dp.DepartmentName AS Khoa FROM dbo.Doctor d JOIN dbo.Department dp ON dp.DepartmentID=d.DepartmentID WHERE d.IsActive=1 ORDER BY dp.DepartmentName,d.FullName");
    }

    public List<Map<String, Object>> departments() throws SQLException {
        return Db.query(
                "SELECT DepartmentID AS MaKhoa,DepartmentName AS TenKhoa FROM dbo.Department WHERE IsActive=1 ORDER BY DepartmentName");
    }

    public List<Map<String, Object>> patients() throws SQLException {
        return Db.query(
                "SELECT p.PatientID AS MaBenhNhan,p.FullName AS HoTen,m.RecordCode AS MaHoSo FROM dbo.Patient p JOIN dbo.MedicalRecord m ON m.PatientID=p.PatientID WHERE m.Status=N'Đang hoạt động' ORDER BY p.FullName");
    }

    public List<Map<String, Object>> visits() throws SQLException {
        return Db.query(
                "SELECT v.VisitID AS MaPhieuKham,p.FullName AS BenhNhan,v.VisitAt AS ThoiDiem,b.FullName AS BacSi,d.DepartmentName AS Khoa,v.VisitType AS Loai,v.Symptoms AS TrieuChung,v.Diagnosis AS ChanDoan,e.Status AS TrangThaiDot FROM dbo.Visit v JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID JOIN dbo.Patient p ON p.PatientID=m.PatientID JOIN dbo.Doctor b ON b.DoctorID=v.DoctorID JOIN dbo.Department d ON d.DepartmentID=v.DepartmentID ORDER BY v.VisitID DESC");
    }

    public List<Map<String, Object>> episodes() throws SQLException {
        return Db.query(
                "SELECT e.EpisodeID AS MaDotDieuTri,p.PatientID AS MaBenhNhan,p.FullName AS BenhNhan,e.StartAt AS BatDau,e.EndAt AS KetThuc,e.Status AS TrangThai FROM dbo.TreatmentEpisode e JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID JOIN dbo.Patient p ON p.PatientID=m.PatientID ORDER BY e.EpisodeID DESC");
    }
}
