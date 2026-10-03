package com.eshopping.dao;

import com.eshopping.data.Db;
import com.eshopping.model.LoaiPhieuDatHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoaiPhieuDatHangDAO {
    public List<LoaiPhieuDatHang> findAllActive() throws SQLException {
        List<LoaiPhieuDatHang> list = new ArrayList<>();
        String sql = "SELECT MaLoaiPhieu, TenLoaiPhieu, ThoiGianXuLyGio, MienPhiTu, MoTa, TrangThai FROM LoaiPhieuDatHang WHERE TrangThai=1 ORDER BY MaLoaiPhieu";
        try (Connection cn = Db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                LoaiPhieuDatHang x = new LoaiPhieuDatHang();
                x.setMaLoaiPhieu(rs.getInt("MaLoaiPhieu"));
                x.setTenLoaiPhieu(rs.getString("TenLoaiPhieu"));
                x.setThoiGianXuLyGio(rs.getInt("ThoiGianXuLyGio"));
                x.setMienPhiTu(rs.getBigDecimal("MienPhiTu"));
                x.setMoTa(rs.getString("MoTa"));
                x.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(x);
            }
        }
        return list;
    }
}
