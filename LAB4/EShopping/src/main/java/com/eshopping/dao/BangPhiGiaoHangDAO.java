package com.eshopping.dao;

import com.eshopping.data.Db;
import java.math.BigDecimal;
import java.sql.*;

public class BangPhiGiaoHangDAO {
    public BigDecimal findFee(int maKhuVuc, int maLoaiPhieu) throws SQLException {
        String sql = "SELECT PhiGiaoHang FROM BangPhiGiaoHang WHERE MaKhuVuc=? AND MaLoaiPhieu=?";
        try (Connection cn = Db.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, maKhuVuc);
            ps.setInt(2, maLoaiPhieu);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next())
                    return null;
                return rs.getBigDecimal(1);
            }
        }
    }
}
