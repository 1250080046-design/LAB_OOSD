package com.benhvien.service;

import com.benhvien.data.Db;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class BillingService {
    public long createInvoice(long episodeId, java.math.BigDecimal total) throws Exception {
        if (total == null || total.signum() < 0)
            throw new IllegalArgumentException("Tổng viện phí phải lớn hơn hoặc bằng 0.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                Object status = Db.scalar(c, "SELECT Status FROM dbo.TreatmentEpisode WHERE EpisodeID=?", episodeId);
                if (status == null)
                    throw new IllegalArgumentException("Không tìm thấy đợt điều trị.");
                if (!"Đang điều trị".equals(status))
                    throw new IllegalArgumentException("Đợt điều trị đã hoàn tất.");
                Object inv = Db.scalar(c, "SELECT InvoiceID FROM dbo.HospitalInvoice WHERE EpisodeID=?", episodeId);
                long id;
                if (inv == null) {
                    id = ((Number) Db.scalar(c,
                            "INSERT INTO dbo.HospitalInvoice(EpisodeID,TotalAmount) OUTPUT INSERTED.InvoiceID VALUES(?,?)",
                            episodeId, total)).longValue();
                } else {
                    id = ((Number) inv).longValue();
                    Object paid = Db.scalar(c,
                            "SELECT COALESCE(SUM(Amount),0) FROM dbo.HospitalPayment WHERE InvoiceID=?", id);
                    if (new java.math.BigDecimal(paid.toString()).signum() > 0)
                        throw new IllegalArgumentException(
                                "Đã có thanh toán cho hóa đơn, không thể sửa tổng viện phí.");
                    Db.update(c,
                            "UPDATE dbo.HospitalInvoice SET TotalAmount=?,Status=N'Chưa thanh toán' WHERE InvoiceID=?",
                            total, id);
                }
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

    public void payInvoice(long invoiceId, java.math.BigDecimal amount, String method, String reference)
            throws Exception {
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Số tiền thanh toán phải lớn hơn 0.");
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                List<Map<String, Object>> inv = Db.query(c,
                        "SELECT TotalAmount,Status FROM dbo.HospitalInvoice WHERE InvoiceID=?", invoiceId);
                if (inv.isEmpty())
                    throw new IllegalArgumentException("Không tìm thấy hóa đơn viện phí.");
                if ("Đã thanh toán".equals(inv.get(0).get("Status")))
                    throw new IllegalArgumentException("Hóa đơn đã thanh toán đủ.");
                java.math.BigDecimal total = new java.math.BigDecimal(inv.get(0).get("TotalAmount").toString());
                java.math.BigDecimal paid = new java.math.BigDecimal(
                        Db.scalar(c, "SELECT COALESCE(SUM(Amount),0) FROM dbo.HospitalPayment WHERE InvoiceID=?",
                                invoiceId).toString());
                if (amount.compareTo(total.subtract(paid)) > 0)
                    throw new IllegalArgumentException(
                            "Số tiền vượt quá số viện phí còn phải trả: " + total.subtract(paid));
                Db.update(c,
                        "INSERT INTO dbo.HospitalPayment(InvoiceID,Amount,PaymentMethod,Reference) VALUES(?,?,?,?)",
                        invoiceId, amount, method, reference == null || reference.isBlank() ? null : reference.trim());
                java.math.BigDecimal next = paid.add(amount);
                if (next.compareTo(total) >= 0)
                    Db.update(c, "UPDATE dbo.HospitalInvoice SET Status=N'Đã thanh toán' WHERE InvoiceID=?", invoiceId);
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public void completeEpisode(long episodeId) throws Exception {
        try (Connection c = Db.getConnection()) {
            c.setAutoCommit(false);
            try {
                List<Map<String, Object>> rows = Db.query(c,
                        "SELECT e.Status,i.InvoiceID,i.TotalAmount,i.Status AS InvoiceStatus FROM dbo.TreatmentEpisode e LEFT JOIN dbo.HospitalInvoice i ON i.EpisodeID=e.EpisodeID WHERE e.EpisodeID=?",
                        episodeId);
                if (rows.isEmpty())
                    throw new IllegalArgumentException("Không tìm thấy đợt điều trị.");
                Map<String, Object> row = rows.get(0);
                if (!"Đang điều trị".equals(row.get("Status")))
                    throw new IllegalArgumentException("Đợt điều trị đã hoàn tất trước đó.");
                if (row.get("InvoiceID") == null)
                    throw new IllegalArgumentException("Chưa lập hóa đơn viện phí.");
                java.math.BigDecimal total = new java.math.BigDecimal(row.get("TotalAmount").toString());
                java.math.BigDecimal paid = new java.math.BigDecimal(
                        Db.scalar(c, "SELECT COALESCE(SUM(Amount),0) FROM dbo.HospitalPayment WHERE InvoiceID=?",
                                row.get("InvoiceID")).toString());
                if (paid.compareTo(total) < 0)
                    throw new IllegalArgumentException(
                            "Chưa thể hoàn tất đợt điều trị. Còn phải thanh toán: " + total.subtract(paid));
                Db.update(c, "UPDATE dbo.HospitalInvoice SET Status=N'Đã thanh toán' WHERE InvoiceID=?",
                        row.get("InvoiceID"));
                Db.update(c,
                        "UPDATE dbo.TreatmentEpisode SET Status=N'Đã hoàn tất',EndAt=SYSDATETIME() WHERE EpisodeID=? AND Status=N'Đang điều trị'",
                        episodeId);
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    public List<Map<String, Object>> invoices() throws SQLException {
        return Db.query(
                "SELECT i.InvoiceID AS MaHoaDon,i.EpisodeID AS MaDotDieuTri,p.FullName AS BenhNhan,i.TotalAmount AS TongVienPhi,COALESCE(x.DaTra,0) AS DaThanhToan,(i.TotalAmount-COALESCE(x.DaTra,0)) AS ConLai,i.Status AS TrangThai,i.CreatedAt AS NgayLap FROM dbo.HospitalInvoice i JOIN dbo.TreatmentEpisode e ON e.EpisodeID=i.EpisodeID JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID JOIN dbo.Patient p ON p.PatientID=m.PatientID OUTER APPLY(SELECT SUM(Amount) DaTra FROM dbo.HospitalPayment hp WHERE hp.InvoiceID=i.InvoiceID)x ORDER BY i.InvoiceID DESC");
    }

    public List<Map<String, Object>> episodesOpen() throws SQLException {
        return Db.query(
                "SELECT e.EpisodeID AS MaDotDieuTri,p.PatientID AS MaBenhNhan,p.FullName AS BenhNhan,e.StartAt AS BatDau,e.Status AS TrangThai FROM dbo.TreatmentEpisode e JOIN dbo.MedicalRecord m ON m.RecordID=e.RecordID JOIN dbo.Patient p ON p.PatientID=m.PatientID WHERE e.Status=N'Đang điều trị' ORDER BY e.EpisodeID DESC");
    }

    public List<Map<String, Object>> payments(long invoiceId) throws SQLException {
        return Db.query(
                "SELECT PaymentID AS MaThanhToan,Amount AS SoTien,PaidAt AS NgayThanhToan,PaymentMethod AS HinhThuc,Reference AS ThamChieu FROM dbo.HospitalPayment WHERE InvoiceID=? ORDER BY PaidAt DESC",
                invoiceId);
    }
}
