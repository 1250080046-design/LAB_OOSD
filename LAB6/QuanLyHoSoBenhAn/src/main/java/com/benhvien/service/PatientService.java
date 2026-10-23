package com.benhvien.service;

import com.benhvien.data.Db;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PatientService {
    public java.math.BigDecimal annualFee(int year) throws SQLException {
        Object amount = Db.scalar("SELECT Amount FROM dbo.AnnualFeeRate WHERE FeeYear=?", year);
        if (amount == null)
            throw new SQLException("Chưa cấu hình lệ phí năm " + year + " trong bảng AnnualFeeRate.");
        return new java.math.BigDecimal(amount.toString());
    }

    public int registerFirstTime(String fullName, String address, LocalDate dob, String phone, String photoPath,
            int feeYear, String paymentMethod, String reference) throws Exception {
        if (blank(fullName) || blank(address) || dob == null || blank(phone))
            throw new IllegalArgumentException("Họ tên, địa chỉ, ngày sinh và điện thoại là bắt buộc.");
        if (dob.isAfter(LocalDate.now()))
            throw new IllegalArgumentException("Ngày sinh không được ở tương lai.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                Object duplicate = Db.scalar(c,
                        "SELECT COUNT(*) FROM dbo.Patient WHERE FullName=? AND DateOfBirth=? AND Phone=?",
                        fullName.trim(), Date.valueOf(dob), phone.trim());
                if (((Number) duplicate).intValue() > 0)
                    throw new IllegalArgumentException(
                            "Bệnh nhân có cùng họ tên, ngày sinh và điện thoại đã tồn tại. Hãy tra cứu hồ sơ trước.");
                Object feeObj = Db.scalar(c, "SELECT Amount FROM dbo.AnnualFeeRate WHERE FeeYear=?", feeYear);
                if (feeObj == null)
                    throw new IllegalArgumentException("Chưa cấu hình lệ phí năm " + feeYear + ".");
                int id = ((Number) Db.scalar(c,
                        "INSERT INTO dbo.Patient(FullName,Address,DateOfBirth,Phone,PhotoPath) OUTPUT INSERTED.PatientID VALUES(?,?,?,?,?)",
                        fullName.trim(), address.trim(), Date.valueOf(dob), phone.trim(), nullable(photoPath)))
                        .intValue();
                java.math.BigDecimal fee = new java.math.BigDecimal(feeObj.toString());
                Db.update(c,
                        "INSERT INTO dbo.AnnualFeePayment(PatientID,FeeYear,Amount,PaymentMethod,Reference) VALUES(?,?,?,?,?)",
                        id, feeYear, fee, paymentMethod, nullable(reference));
                String code = "HS-" + feeYear + "-" + String.format("%07d", id);
                Db.update(c, "INSERT INTO dbo.MedicalRecord(RecordCode,PatientID) VALUES(?,?)", code, id);
                c.commit();
                return id;
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void payAnnualFee(int patientId, int feeYear, String paymentMethod, String reference) throws Exception {
        if (patientId <= 0)
            throw new IllegalArgumentException("Mã bệnh nhân không hợp lệ.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                Object record = Db.scalar(c,
                        "SELECT COUNT(*) FROM dbo.MedicalRecord WHERE PatientID=? AND Status=N'Đang hoạt động'",
                        patientId);
                if (record == null || ((Number) record).intValue() == 0)
                    throw new IllegalArgumentException("Bệnh nhân chưa có hồ sơ bệnh án đang hoạt động.");
                Object feeObj = Db.scalar(c, "SELECT Amount FROM dbo.AnnualFeeRate WHERE FeeYear=?", feeYear);
                if (feeObj == null)
                    throw new IllegalArgumentException("Chưa cấu hình lệ phí năm " + feeYear + ".");
                Object exists = Db.scalar(c,
                        "SELECT COUNT(*) FROM dbo.AnnualFeePayment WHERE PatientID=? AND FeeYear=?", patientId,
                        feeYear);
                if (((Number) exists).intValue() > 0)
                    throw new IllegalArgumentException("Bệnh nhân đã đóng lệ phí năm " + feeYear + ".");
                Db.update(c,
                        "INSERT INTO dbo.AnnualFeePayment(PatientID,FeeYear,Amount,PaymentMethod,Reference) VALUES(?,?,?,?,?)",
                        patientId, feeYear, new java.math.BigDecimal(feeObj.toString()), paymentMethod,
                        nullable(reference));
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Map<String, Object>> searchPatients(String keyword) throws SQLException {
        String k = keyword == null ? "" : keyword.trim();
        String like = "%" + k + "%";
        return Db.query(
                "SELECT p.PatientID AS MaBenhNhan,p.FullName AS HoTen,p.DateOfBirth AS NgaySinh,p.Phone AS DienThoai,p.Address AS DiaChi,m.RecordCode AS MaHoSo,m.IssuedAt AS NgayCap FROM dbo.Patient p LEFT JOIN dbo.MedicalRecord m ON m.PatientID=p.PatientID WHERE (?='' OR CONVERT(varchar(20),p.PatientID)=? OR p.FullName LIKE ? OR p.Phone LIKE ? OR m.RecordCode LIKE ?) ORDER BY p.PatientID DESC",
                k, k, like, like, like);
    }

    public List<Map<String, Object>> history(int patientId) throws SQLException {
        return Db.query(
                "SELECT N'Lệ phí năm' AS Loai, a.PaidAt AS ThoiDiem, CONCAT(N'Năm ',a.FeeYear) AS ThamChieu, CONCAT(N'Số tiền: ',FORMAT(a.Amount,'N0'),N' - ',a.PaymentMethod) AS NoiDung, N'Đã thanh toán' AS TrangThai FROM dbo.AnnualFeePayment a WHERE a.PatientID=? UNION ALL SELECT N'Phiếu khám' AS Loai,v.VisitAt AS ThoiDiem,CONCAT(N'PK-',v.VisitID) AS ThamChieu,CONCAT(v.Symptoms,N' | Chẩn đoán: ',COALESCE(v.Diagnosis,N'')) AS NoiDung,e.Status AS TrangThai FROM dbo.Visit v JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID WHERE m.PatientID=? UNION ALL SELECT N'Xét nghiệm' AS Loai,t.RequestedAt AS ThoiDiem,CONCAT(N'XN-',t.TestID) AS ThamChieu,CONCAT(t.TestName,N' | Kết quả: ',COALESCE(t.Result,N'Chưa có')) AS NoiDung,t.Status AS TrangThai FROM dbo.LabTest t JOIN dbo.Visit v ON v.VisitID=t.VisitID JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID WHERE m.PatientID=? UNION ALL SELECT N'Toa thuốc' AS Loai,p.CreatedAt AS ThoiDiem,CONCAT(N'TO-',p.PrescriptionID) AS ThamChieu,COALESCE(p.Notes,N'Toa thuốc đã lập') AS NoiDung,p.Status AS TrangThai FROM dbo.Prescription p JOIN dbo.Visit v ON v.VisitID=p.VisitID JOIN dbo.TreatmentEpisode e ON e.EpisodeID=v.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID WHERE m.PatientID=? ORDER BY ThoiDiem DESC",
                patientId, patientId, patientId, patientId);
    }

    public List<Map<String, Object>> feePayments(int patientId) throws SQLException {
        return Db.query(
                "SELECT FeeYear AS Nam,Amount AS SoTien,PaidAt AS NgayDong,PaymentMethod AS HinhThuc FROM dbo.AnnualFeePayment WHERE PatientID=? ORDER BY FeeYear DESC",
                patientId);
    }

    private static boolean blank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nullable(String s) {
        return blank(s) ? null : s.trim();
    }
}
