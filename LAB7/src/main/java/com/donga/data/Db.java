package com.donga.data;
import java.sql.*;
import java.util.*;
import java.io.*;
public final class Db {
 private static final Properties p = new Properties();
 static { try(InputStream in=Db.class.getResourceAsStream("/db.properties")){if(in!=null)p.load(in);}catch(IOException e){throw new ExceptionInInitializerError(e);} }
 private Db(){}
 public static Connection connect() throws SQLException {
  String url=System.getenv().getOrDefault("DB_URL",p.getProperty("db.url"));
  String user=System.getenv().getOrDefault("DB_USER",p.getProperty("db.user"));
  String pass=System.getenv().getOrDefault("DB_PASSWORD",p.getProperty("db.password"));
  return DriverManager.getConnection(url,user,pass);
 }
}
