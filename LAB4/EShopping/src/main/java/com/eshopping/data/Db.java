package com.eshopping.data;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Db {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = Db.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Không tìm thấy src/main/resources/db.properties");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Không thể đọc db.properties: " + e.getMessage());
        }
    }

    private Db() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                PROPS.getProperty("db.url"),
                PROPS.getProperty("db.user"),
                PROPS.getProperty("db.password"));
    }

    public static boolean testConnection() {
        try (Connection ignored = getConnection()) {
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
