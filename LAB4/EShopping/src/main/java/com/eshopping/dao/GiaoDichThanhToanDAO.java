package com.eshopping.dao;

import com.eshopping.adapter.PaymentResult;
import com.eshopping.model.PaymentInfo;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;

public class GiaoDichThanhToanDAO {
    public void insert(Connection cn, long maDonHang, PaymentInfo p, BigDecimal amount, PaymentResult result)
            throws SQLException {
        String sql = "INSERT INTO GiaoDichThanhToan(MaDonHang, LoaiThe, SoTheDaChe, NgayHetHan, TenChuThe, SoTienThanhToan, MaGiaoDichCongThanhToan, ThoiDiemThanhToan, TrangThaiThanhToan, ThongBaoGateway) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, maDonHang);
            ps.setString(2, p.getLoaiThe());
            ps.setString(3, maskCard(p.getSoThe()));
            if (p.getNgayHetHan() == null)
                ps.setNull(4, Types.DATE);
            else
                ps.setDate(4, Date.valueOf(p.getNgayHetHan()));
            ps.setString(5, p.getTenChuThe());
            ps.setBigDecimal(6, amount);
            ps.setString(7, result.getTransactionId());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(9, result.isSuccess() ? "Thành công" : "Thất bại");
            ps.setString(10, result.getMessage());
            ps.executeUpdate();
        }
    }

    private String maskCard(String card) {
        if (card == null)
            return null;
        String digits = card.replaceAll("\\s+", "");
        if (digits.length() <= 4)
            return "****";
        return "**** **** **** " + digits.substring(digits.length() - 4);
    }
}
