package com.eshopping.dao;

import com.eshopping.model.KhachHang;
import java.sql.*;

public class KhachHangDAO {
    public int insert(Connection cn, KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang(HoTen, NgaySinh, SoGiayTo, LoaiGiayTo, DiaChi, DienThoai, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, kh.getHoTen());
            if (kh.getNgaySinh() == null)
                ps.setNull(2, Types.DATE);
            else
                ps.setDate(2, Date.valueOf(kh.getNgaySinh()));
            ps.setString(3, kh.getSoGiayTo());
            ps.setString(4, kh.getLoaiGiayTo());
            ps.setString(5, kh.getDiaChi());
            ps.setString(6, kh.getDienThoai());
            ps.setString(7, kh.getEmail());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (!rs.next())
                    throw new SQLException("Không lấy được MaKH.");
                kh.setMaKH(rs.getInt(1));
                return kh.getMaKH();
            }
        }
    }
}
