package com.benhvien.service;

import com.benhvien.data.Db;
import com.benhvien.model.PrescriptionItemData;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ClinicalService {
    public long createVisit(int patientId, int doctorId, int departmentId, Long episodeId, Long appointmentId,
            String visitType, String symptoms, String diagnosis, String notes) throws Exception {
        if (symptoms == null || symptoms.isBlank())
            throw new IllegalArgumentException("Triệu chứng là bắt buộc.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                Object recordObj = Db.scalar(c,
                        "SELECT RecordID FROM dbo.MedicalRecord WHERE PatientID=? AND Status=N'Đang hoạt động'",
                        patientId);
                if (recordObj == null)
                    throw new IllegalArgumentException("Bệnh nhân chưa được cấp hồ sơ bệnh án.");
                Object doctorOk = Db.scalar(c,
                        "SELECT COUNT(*) FROM dbo.Doctor WHERE DoctorID=? AND DepartmentID=? AND IsActive=1", doctorId,
                        departmentId);
                if (doctorOk == null || ((Number) doctorOk).intValue() == 0)
                    throw new IllegalArgumentException("Bác sĩ không thuộc khoa đã chọn hoặc đã ngừng hoạt động.");

                Long appt = appointmentId;
                Integer feeYear = null;
                if (appt != null) {
                    List<Map<String, Object>> a = Db.query(c,
                            "SELECT PatientID,DoctorID,Status,AppointmentAt,AppointmentYear FROM dbo.Appointment WHERE AppointmentID=?",
                            appt);
                    if (a.isEmpty())
                        throw new IllegalArgumentException("Không tìm thấy lịch hẹn.");
                    Map<String, Object> arow = a.get(0);
                    if (((Number) arow.get("PatientID")).intValue() != patientId)
                        throw new IllegalArgumentException("Lịch hẹn không thuộc bệnh nhân này.");
                    if (!"Đã hẹn".equals(arow.get("Status")))
                        throw new IllegalArgumentException("Lịch hẹn đã được xử lý hoặc đã hủy.");
                    if (arow.get("AppointmentAt") instanceof Timestamp ts
                            && ts.toLocalDateTime().toLocalDate().isAfter(LocalDate.now()))
                        throw new IllegalArgumentException("Chưa đến ngày khám theo lịch hẹn.");
                    if (arow.get("DoctorID") != null && ((Number) arow.get("DoctorID")).intValue() != doctorId)
                        throw new IllegalArgumentException("Bác sĩ không khớp với bác sĩ đã đặt trong lịch hẹn.");
                    feeYear = ((Number) arow.get("AppointmentYear")).intValue();
                    visitType = "Khám định kỳ";
                } else if ("Khám định kỳ".equals(visitType)) {
                    feeYear = LocalDate.now().getYear();
                }
                if (feeYear != null) {
                    Object fee = Db.scalar(c,
                            "SELECT COUNT(*) FROM dbo.AnnualFeePayment WHERE PatientID=? AND FeeYear=?", patientId,
                            feeYear);
                    if (((Number) fee).intValue() == 0)
                        throw new IllegalArgumentException("Bệnh nhân chưa đóng lệ phí năm " + feeYear + ".");
                }

                long ep;
                if (episodeId == null) {
                    ep = ((Number) Db.scalar(c,
                            "INSERT INTO dbo.TreatmentEpisode(RecordID,Notes) OUTPUT INSERTED.EpisodeID VALUES(?,?)",
                            recordObj, notes == null || notes.isBlank() ? null : notes.trim())).longValue();
                } else {
                    List<Map<String, Object>> e = Db.query(c,
                            "SELECT RecordID,Status FROM dbo.TreatmentEpisode WHERE EpisodeID=?", episodeId);
                    if (e.isEmpty())
                        throw new IllegalArgumentException("Không tìm thấy đợt điều trị.");
                    if (((Number) e.get(0).get("RecordID")).intValue() != ((Number) recordObj).intValue())
                        throw new IllegalArgumentException("Đợt điều trị không thuộc bệnh nhân đã chọn.");
                    if (!"Đang điều trị".equals(e.get(0).get("Status")))
                        throw new IllegalArgumentException("Đợt điều trị đã hoàn tất.");
                    ep = episodeId;
                }
                long visit = ((Number) Db.scalar(c,
                        "INSERT INTO dbo.Visit(EpisodeID,AppointmentID,DoctorID,DepartmentID,VisitType,Symptoms,Diagnosis,ClinicalNotes) OUTPUT INSERTED.VisitID VALUES(?,?,?,?,?,?,?,?)",
                        ep, appt, doctorId, departmentId, visitType, symptoms.trim(), nullable(diagnosis),
                        nullable(notes))).longValue();
                if (appt != null)
                    Db.update(c, "UPDATE dbo.Appointment SET Status=N'Đã khám' WHERE AppointmentID=?", appt);
                c.commit();
                return visit;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public long addLabTest(long visitId, String testName, String notes) throws Exception {
        if (testName == null || testName.isBlank())
            throw new IllegalArgumentException("Tên xét nghiệm là bắt buộc.");
        try (Connection c = Db.getConnection()) {
            Object open = Db.scalar(c,
                    "SELECT COUNT(*) FROM dbo.Visit v JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID WHERE v.VisitID=? AND e.Status=N'Đang điều trị'",
                    visitId);
            if (open == null || ((Number) open).intValue() == 0)
                throw new IllegalArgumentException("Phiếu khám không tồn tại hoặc đợt điều trị đã hoàn tất.");
            Object id = Db.scalar(c,
                    "INSERT INTO dbo.LabTest(VisitID,TestName,Notes) OUTPUT INSERTED.TestID VALUES(?,?,?)", visitId,
                    testName.trim(), nullable(notes));
            return ((Number) id).longValue();
        }
    }

    public void recordLabResult(long testId, String result) throws SQLException {
        if (result == null || result.isBlank())
            throw new IllegalArgumentException("Kết quả xét nghiệm không được để trống.");
        int n = Db.update(
                "UPDATE dbo.LabTest SET Result=?,ResultAt=SYSDATETIME(),Status=N'Đã có kết quả' WHERE TestID=? AND Status=N'Chờ kết quả'",
                result.trim(), testId);
        if (n == 0)
            throw new SQLException("Không tìm thấy xét nghiệm đang chờ kết quả.");
    }

    public long savePrescription(long visitId, String notes, List<PrescriptionItemData> items) throws Exception {
        if (items == null || items.isEmpty())
            throw new IllegalArgumentException("Toa thuốc phải có ít nhất một loại thuốc.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                List<Map<String, Object>> visit = Db.query(c,
                        "SELECT v.DoctorID,v.VisitID,e.Status FROM dbo.Visit v JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID WHERE v.VisitID=?",
                        visitId);
                if (visit.isEmpty())
                    throw new IllegalArgumentException("Không tìm thấy phiếu khám.");
                if (!"Đang điều trị".equals(visit.get(0).get("Status")))
                    throw new IllegalArgumentException("Không thể lập toa cho đợt điều trị đã hoàn tất.");
                Object exists = Db.scalar(c,
                        "SELECT COUNT(*) FROM dbo.Prescription WHERE VisitID=? AND Status=N'Đã lập'", visitId);
                if (((Number) exists).intValue() > 0)
                    throw new IllegalArgumentException(
                            "Phiếu khám này đã có toa thuốc. Mỗi phiếu khám chỉ lập một toa trong prototype.");
                long doctorId = ((Number) visit.get(0).get("DoctorID")).longValue();
                long prescription = ((Number) Db.scalar(c,
                        "INSERT INTO dbo.Prescription(VisitID,DoctorID,Notes) OUTPUT INSERTED.PrescriptionID VALUES(?,?,?)",
                        visitId, doctorId, nullable(notes))).longValue();
                for (PrescriptionItemData item : items) {
                    Db.update(c,
                            "INSERT INTO dbo.PrescriptionItem(PrescriptionID,MedicineName,Dosage,Frequency,DurationDays,Quantity,Instructions) VALUES(?,?,?,?,?,?,?)",
                            prescription, item.medicineName().trim(), item.dosage().trim(), item.frequency().trim(),
                            item.durationDays(), item.quantity(), nullable(item.instructions()));
                }
                c.commit();
                return prescription;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Map<String, Object>> labTests() throws SQLException {
        return Db.query(
                "SELECT t.TestID AS MaXetNghiem,t.VisitID AS MaPhieuKham,p.FullName AS BenhNhan,t.TestName AS TenXetNghiem,t.RequestedAt AS NgayYeuCau,t.Result AS KetQua,t.ResultAt AS NgayCoKetQua,t.Status AS TrangThai FROM dbo.LabTest t JOIN dbo.Visit v ON v.VisitID=t.VisitID JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID JOIN dbo.Patient p ON p.PatientID=m.PatientID ORDER BY t.TestID DESC");
    }

    public List<Map<String, Object>> prescriptions() throws SQLException {
        return Db.query(
                "SELECT p.PrescriptionID AS MaToa,p.VisitID AS MaPhieuKham,pt.FullName AS BenhNhan,d.FullName AS BacSi,p.CreatedAt AS NgayLap,p.Notes AS GhiChu,p.Status AS TrangThai,COUNT(i.ItemID) AS SoLoaiThuoc FROM dbo.Prescription p JOIN dbo.Visit v ON v.VisitID=p.VisitID JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID JOIN dbo.Patient pt ON pt.PatientID=m.PatientID JOIN dbo.Doctor d ON d.DoctorID=p.DoctorID LEFT JOIN dbo.PrescriptionItem i ON i.PrescriptionID=p.PrescriptionID GROUP BY p.PrescriptionID,p.VisitID,pt.FullName,d.FullName,p.CreatedAt,p.Notes,p.Status ORDER BY p.PrescriptionID DESC");
    }

    public List<Map<String, Object>> prescriptionItems(long prescriptionId) throws SQLException {
        return Db.query(
                "SELECT MedicineName AS TenThuoc,Dosage AS LieuDung,Frequency AS CachDung,DurationDays AS SoNgay,Quantity AS SoLuong,Instructions AS HuongDan FROM dbo.PrescriptionItem WHERE PrescriptionID=? ORDER BY ItemID",
                prescriptionId);
    }

    private static String nullable(String s) {
        return s == null || s.isBlank() ? null : s.trim();
    }
}
