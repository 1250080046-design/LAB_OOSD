package com.eshopping.dao;

import com.eshopping.model.DonHangDTO;
import java.sql.*;

public class DonHangDAO {
    public long insert(Connection cn, DonHangDTO dh) throws SQLException {
        String sql = "INSERT INTO DonHang(SoDonHang, MaKH, MaLoaiPhieu, MaKhuVuc, HoTenNguoiNhan, DiaChiNguoiNhan, DienThoaiNguoiNhan, TongTienHang, PhiGiaoHang, TrangThaiDonHang) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dh.getSoDonHang());
            ps.setInt(2, dh.getMaKH());
            ps.setInt(3, dh.getMaLoaiPhieu());
            ps.setInt(4, dh.getMaKhuVuc());
            ps.setString(5, dh.getHoTenNguoiNhan());
            ps.setString(6, dh.getDiaChiNguoiNhan());
            ps.setString(7, dh.getDienThoaiNguoiNhan());
            ps.setBigDecimal(8, dh.getTongTienHang());
            ps.setBigDecimal(9, dh.getPhiGiaoHang());
            ps.setString(10, dh.getTrangThaiDonHang());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next())
                    throw new SQLException("Không lấy được MaDonHang.");
                dh.setMaDonHang(rs.getLong(1));
                return dh.getMaDonHang();
            }
        }
    }

    public void updateStatus(Connection cn, long maDonHang, String trangThai) throws SQLException {
        try (PreparedStatement ps = cn.prepareStatement("UPDATE DonHang SET TrangThaiDonHang=? WHERE MaDonHang=?")) {
            ps.setString(1, trangThai);
            ps.setLong(2, maDonHang);
            ps.executeUpdate();
        }
    }
}
