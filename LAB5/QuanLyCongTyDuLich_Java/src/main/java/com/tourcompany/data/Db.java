package com.tourcompany.data;

import javax.swing.table.DefaultTableModel;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/** Lớp duy nhất chịu trách nhiệm kết nối và truy cập SQL Server. */
public final class Db {
    private static final Properties CONFIG = new Properties();
    static {
        try (InputStream in = Db.class.getResourceAsStream("/db.properties")) {
            if (in != null) CONFIG.load(in);
            else throw new IllegalStateException("Không tìm thấy /db.properties");
        } catch (Exception e) { throw new ExceptionInInitializerError(e); }
    }
    private Db() {}

    public static Connection open() throws SQLException {
        String url = CONFIG.getProperty("db.url", "").trim();
        String user = CONFIG.getProperty("db.user", "").trim();
        String password = CONFIG.getProperty("db.password", "");
        if (url.isBlank()) throw new SQLException("db.url chưa được cấu hình trong src/main/resources/db.properties");
        return user.isBlank() ? DriverManager.getConnection(url) : DriverManager.getConnection(url, user, password);
    }

    public static void testConnection() throws SQLException {
        try (Connection c = open()) {
            if (!c.isValid(5)) throw new SQLException("Kết nối SQL Server không hợp lệ.");
        }
    }

    public static DefaultTableModel query(String sql, Object... params) throws SQLException {
        try (Connection c = open()) { return query(c, sql, params); }
    }

    public static DefaultTableModel query(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                DefaultTableModel model = new DefaultTableModel() {
                    @Override public boolean isCellEditable(int row, int column) { return false; }
                };
                for (int i = 1; i <= md.getColumnCount(); i++) model.addColumn(md.getColumnLabel(i));
                while (rs.next()) {
                    Object[] row = new Object[md.getColumnCount()];
                    for (int i = 1; i <= row.length; i++) row[i - 1] = rs.getObject(i);
                    model.addRow(row);
                }
                return model;
            }
        }
    }

    public static int execute(String sql, Object... params) throws SQLException {
        try (Connection c = open()) { return execute(c, sql, params); }
    }
    public static int execute(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) { bind(ps, params); return ps.executeUpdate(); }
    }
    public static Object scalar(String sql, Object... params) throws SQLException {
        try (Connection c = open(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getObject(1) : null; }
        }
    }
    public static Object scalar(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, params); try (ResultSet rs = ps.executeQuery()) { return rs.next() ? rs.getObject(1) : null; }
        }
    }
    public static void bind(PreparedStatement ps, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) ps.setObject(i + 1, params[i]);
    }
    @FunctionalInterface public interface TxWork<T> { T run(Connection connection) throws Exception; }
    public static <T> T transaction(TxWork<T> work) throws Exception {
        try (Connection c = open()) {
            c.setAutoCommit(false);
            try { T result = work.run(c); c.commit(); return result; }
            catch (Exception e) { try { c.rollback(); } catch (SQLException ignored) {} throw e; }
            finally { try { c.setAutoCommit(true); } catch (SQLException ignored) {} }
        }
    }
}
