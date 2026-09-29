package com.quanlykhachsan;

import com.quanlykhachsan.data.Db;
import java.sql.Connection;

public class TestConnection {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("KIEM TRA KET NOI SQL SERVER");
        System.out.println("=================================");

        try (Connection conn = Db.open()) {

            System.out.println("KET NOI SQL SERVER THANH CONG!");
            System.out.println("Database : " + conn.getCatalog());
            System.out.println("User SQL : " + conn.getMetaData().getUserName());

        } catch (Exception e) {

            System.out.println("KET NOI SQL SERVER THAT BAI!");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}