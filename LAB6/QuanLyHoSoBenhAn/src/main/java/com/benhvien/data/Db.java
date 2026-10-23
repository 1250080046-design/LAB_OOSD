package com.benhvien.data;

import java.io.InputStream;
import java.sql.*;
import java.util.*;

/** Shared JDBC helper. SQL statements are kept out of Swing forms. */
public final class Db {
    private static final Properties CONFIG = new Properties();
    static {
        try (InputStream in = Db.class.getResourceAsStream("/db.properties")) {
            if (in == null) throw new IllegalStateException("Không tìm thấy db.properties trong resources.");
            CONFIG.load(in);
        } catch (Exception ex) { throw new ExceptionInInitializerError(ex); }
    }
    private Db() {}
    public static Connection getConnection() throws SQLException {
        String url = required("db.url");
        String user = CONFIG.getProperty("db.user", "").trim();
        String password = CONFIG.getProperty("db.password", "");
        if (password.isBlank() || password.equals("CHANGE_ME"))
            throw new SQLException("Hãy sửa db.password trong src/main/resources/db.properties trước khi kết nối.");
        return DriverManager.getConnection(url, user, password);
    }
    private static String required(String k) throws SQLException {
        String v = CONFIG.getProperty(k, "").trim();
        if (v.isEmpty()) throw new SQLException("Thiếu cấu hình " + k + " trong db.properties.");
        return v;
    }
    public static List<Map<String,Object>> query(String sql, Object... params) throws SQLException {
        try (Connection c = getConnection()) { return query(c, sql, params); }
    }
    public static List<Map<String,Object>> query(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData(); int n = md.getColumnCount();
                List<Map<String,Object>> rows = new ArrayList<>();
                while (rs.next()) {
                    Map<String,Object> row = new LinkedHashMap<>();
                    for (int i=1;i<=n;i++) row.put(md.getColumnLabel(i), rs.getObject(i));
                    rows.add(row);
                }
                return rows;
            }
        }
    }
    public static int update(String sql, Object... params) throws SQLException {
        try (Connection c = getConnection()) { return update(c, sql, params); }
    }
    public static int update(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) { bind(ps, params); return ps.executeUpdate(); }
    }
    public static Object scalar(String sql, Object... params) throws SQLException {
        try (Connection c = getConnection()) { return scalar(c, sql, params); }
    }
    public static Object scalar(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getObject(1) : null; }
        }
    }
    private static void bind(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i=0;i<params.length;i++) ps.setObject(i+1, params[i]);
    }
    public static void testConnection() throws SQLException {
        try (Connection c = getConnection()) {
            if (!c.isValid(5)) throw new SQLException("SQL Server không phản hồi.");
        }
    }
}
