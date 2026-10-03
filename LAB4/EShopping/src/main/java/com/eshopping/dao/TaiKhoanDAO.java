package com.eshopping.dao;

import com.eshopping.model.KhachHang;
import com.eshopping.model.TaiKhoan;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class TaiKhoanDAO {
    public void insert(Connection cn, TaiKhoan tk) throws SQLException {
        String sql = "INSERT INTO TaiKhoan(MaKH, TenDangNhap, MatKhau, TrangThai) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, tk.getMaKH());
            ps.setString(2, tk.getTenDangNhap());
            ps.setString(3, tk.getMatKhau());
            ps.setBoolean(4, tk.isTrangThai());
            ps.executeUpdate();
        }
    }

    public TaiKhoan findByUsername(String username) throws SQLException {
        String sql = """
                SELECT t.MaTaiKhoan, t.MaKH, t.TenDangNhap, t.MatKhau, t.TrangThai,
                       k.HoTen, k.NgaySinh, k.SoGiayTo, k.LoaiGiayTo, k.DiaChi,
                       k.DienThoai, k.Email, k.NgayDangKy
                FROM TaiKhoan t
                JOIN KhachHang k ON k.MaKH = t.MaKH
                WHERE t.TenDangNhap = ?
                """;
        try (Connection cn = com.eshopping.data.Db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return null;
                TaiKhoan tk = new TaiKhoan();
                tk.setMaTaiKhoan(rs.getInt("MaTaiKhoan"));
                tk.setMaKH(rs.getInt("MaKH"));
                tk.setTenDangNhap(rs.getString("TenDangNhap"));
                tk.setMatKhau(rs.getString("MatKhau"));
                tk.setTrangThai(rs.getBoolean("TrangThai"));
                KhachHang kh = new KhachHang();
                kh.setMaKH(rs.getInt("MaKH"));
                kh.setHoTen(rs.getString("HoTen"));
                Date ns = rs.getDate("NgaySinh");
                if (ns != null)
                    kh.setNgaySinh(ns.toLocalDate());
                kh.setSoGiayTo(rs.getString("SoGiayTo"));
                kh.setLoaiGiayTo(rs.getString("LoaiGiayTo"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.setDienThoai(rs.getString("DienThoai"));
                kh.setEmail(rs.getString("Email"));
                Timestamp dk = rs.getTimestamp("NgayDangKy");
                if (dk != null)
                    kh.setNgayDangKy(dk.toLocalDateTime());
                tk.setKhachHang(kh);
                return tk;
            }
        }
    }
}
