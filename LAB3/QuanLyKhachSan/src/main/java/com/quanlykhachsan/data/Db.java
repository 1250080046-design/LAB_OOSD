package com.quanlykhachsan.data;

import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public final class Db {
    private static final Properties PROPS = loadProperties();

    private Db() {
    }

    private static Properties loadProperties() {
        Properties p = new Properties();
        try (InputStream in = Db.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) throw new IllegalStateException("Không tìm thấy config.properties");
            p.load(in);
            return p;
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String getUrl() { return PROPS.getProperty("db.url", "").trim(); }
    public static String getUser() { return PROPS.getProperty("db.user", "").trim(); }
    public static String getPassword() { return PROPS.getProperty("db.password", ""); }
    public static boolean isIntegratedSecurity() {
        return Boolean.parseBoolean(PROPS.getProperty("db.integratedSecurity", "false"));
    }

    public static Connection open() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            if (isIntegratedSecurity()) return DriverManager.getConnection(getUrl());
            return DriverManager.getConnection(getUrl(), getUser(), getPassword());
        } catch (Exception e) {
            throw new RuntimeException("Không thể kết nối SQL Server:\n" + e.getMessage(), e);
        }
    }

    // Alias để các service có thể dùng Db.openConnection() theo cách cũ.
    public static Connection openConnection() { return open(); }

    private static void setParameters(PreparedStatement st, Object... params) throws SQLException {
        if (params == null) return;
        for (int i = 0; i < params.length; i++) {
            Object p = params[i];
            if (p instanceof java.util.Date d && !(p instanceof java.sql.Date) && !(p instanceof Timestamp)) {
                p = new java.sql.Date(d.getTime());
            }
            if (p == null) st.setNull(i + 1, Types.NULL);
            else st.setObject(i + 1, p);
        }
    }

    public static DefaultTableModel query(String sql, Object... params) {
        try (Connection c = open(); PreparedStatement st = c.prepareStatement(sql)) {
            setParameters(st, params);
            try (ResultSet rs = st.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int n = md.getColumnCount();
                String[] cols = new String[n];
                for (int i = 0; i < n; i++) cols[i] = md.getColumnLabel(i + 1);
                DefaultTableModel model = new DefaultTableModel(cols, 0) {
                    @Override public boolean isCellEditable(int row, int column) { return false; }
                };
                while (rs.next()) {
                    Object[] row = new Object[n];
                    for (int i = 0; i < n; i++) row[i] = rs.getObject(i + 1);
                    model.addRow(row);
                }
                return model;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static int execute(String sql, Object... params) {
        try (Connection c = open(); PreparedStatement st = c.prepareStatement(sql)) {
            setParameters(st, params);
            return st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Object scalar(String sql, Object... params) {
        try (Connection c = open(); PreparedStatement st = c.prepareStatement(sql)) {
            setParameters(st, params);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? rs.getObject(1) : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static int execute(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(sql)) {
            setParameters(st, params);
            return st.executeUpdate();
        }
    }

    public static Object scalar(Connection c, String sql, Object... params) throws SQLException {
        try (PreparedStatement st = c.prepareStatement(sql)) {
            setParameters(st, params);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next() ? rs.getObject(1) : null;
            }
        }
    }

    public static <T> T tx(SqlWork<T> work) {
        try (Connection c = open()) {
            c.setAutoCommit(false);
            try {
                T result = work.run(c);
                c.commit();
                return result;
            } catch (Exception e) {
                try { c.rollback(); } catch (SQLException ignored) {}
                if (e instanceof RuntimeException re) throw re;
                throw new RuntimeException(e.getMessage(), e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @FunctionalInterface
    public interface SqlWork<T> {
        T run(Connection c) throws Exception;
    }
}
