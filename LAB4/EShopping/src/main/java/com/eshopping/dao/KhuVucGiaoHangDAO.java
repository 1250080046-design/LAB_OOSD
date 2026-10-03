package com.eshopping.dao;

import com.eshopping.data.Db;
import com.eshopping.model.KhuVucGiaoHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuVucGiaoHangDAO {
    public List<KhuVucGiaoHang> findAllActive() throws SQLException {
        List<KhuVucGiaoHang> list = new ArrayList<>();
        String sql = "SELECT MaKhuVuc, TenKhuVuc, MoTa, TrangThai FROM KhuVucGiaoHang WHERE TrangThai=1 ORDER BY MaKhuVuc";
        try (Connection cn = Db.getConnection();
                PreparedStatement ps = cn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                KhuVucGiaoHang x = new KhuVucGiaoHang();
                x.setMaKhuVuc(rs.getInt("MaKhuVuc"));
                x.setTenKhuVuc(rs.getString("TenKhuVuc"));
                x.setMoTa(rs.getString("MoTa"));
                x.setTrangThai(rs.getBoolean("TrangThai"));
                list.add(x);
            }
        }
        return list;
    }
}
