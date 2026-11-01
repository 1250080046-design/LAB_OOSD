package com.caooc.data;
import java.io.*;import java.sql.*;import java.util.*;
public final class Db {
 private static final Properties CFG=new Properties();
 static { try(InputStream in=Db.class.getClassLoader().getResourceAsStream("db.properties")){if(in==null)throw new IllegalStateException("Thiếu db.properties");CFG.load(in);}catch(IOException e){throw new ExceptionInInitializerError(e);} }
 private Db(){}
 public static Connection open() throws SQLException {return DriverManager.getConnection(CFG.getProperty("db.url"),CFG.getProperty("db.user"),CFG.getProperty("db.password"));}
 public static List<Map<String,Object>> query(String sql,Object... values) throws SQLException {try(Connection c=open();PreparedStatement ps=c.prepareStatement(sql)){bind(ps,values);try(ResultSet rs=ps.executeQuery()){List<Map<String,Object>> rows=new ArrayList<>();ResultSetMetaData m=rs.getMetaData();while(rs.next()){Map<String,Object> row=new LinkedHashMap<>();for(int i=1;i<=m.getColumnCount();i++)row.put(m.getColumnLabel(i),rs.getObject(i));rows.add(row);}return rows;}}}
 public static void bind(PreparedStatement ps,Object... values)throws SQLException{for(int i=0;i<values.length;i++)ps.setObject(i+1,values[i]);}
}
