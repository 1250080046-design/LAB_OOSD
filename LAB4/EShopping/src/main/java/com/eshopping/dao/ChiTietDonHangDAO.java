package com.eshopping.dao;

import com.eshopping.model.CartItem;
import java.sql.*;

public class ChiTietDonHangDAO {
    public void insert(Connection cn, long maDonHang, CartItem item) throws SQLException {
        String sql = "INSERT INTO ChiTietDonHang(MaDonHang, MaSP, TenSPTaiThoiDiemDat, SoLuong, DonGia) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, maDonHang);
            ps.setString(2, item.getSanPham().getMaSP());
            ps.setString(3, item.getSanPham().getTenSP());
            ps.setInt(4, item.getSoLuong());
            ps.setBigDecimal(5, item.getSanPham().getGiaBan());
            ps.executeUpdate();
        }
    }
}
