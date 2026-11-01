package com.caooc.service;
import com.caooc.data.Db;import java.sql.*;import java.time.*;import java.util.*;
public class OfficeService {
 public int createContract(String code,int customer,LocalDate signed,LocalDate starts,int months,String rooms,String paymentMethod)throws SQLException{
  if(months<6)throw new IllegalArgumentException("Phải thuê ít nhất 6 tháng.");
  try(Connection c=Db.open();CallableStatement cs=c.prepareCall("{call dbo.sp_TaoHopDong(?,?,?,?,?,?,?,?)}")){
   cs.setString(1,code);cs.setInt(2,customer);cs.setDate(3,java.sql.Date.valueOf(signed));cs.setDate(4,java.sql.Date.valueOf(starts));cs.setInt(5,months);cs.setString(6,rooms);cs.setString(7,paymentMethod);cs.registerOutParameter(8,Types.INTEGER);cs.execute();return cs.getInt(8);
  }
 }
 public int rentBill(int contract,int monthIndex,LocalDate issued)throws SQLException{
  if(monthIndex<7)throw new IllegalArgumentException("6 tháng đầu đã trả trước.");
  try(Connection c=Db.open();CallableStatement cs=c.prepareCall("{call dbo.sp_LapHoaDonThueThang(?,?,?,?)}")){
   cs.setInt(1,contract);cs.setInt(2,monthIndex);cs.setDate(3,java.sql.Date.valueOf(issued));cs.registerOutParameter(4,Types.INTEGER);cs.execute();return cs.getInt(4);
  }
 }
 public int serviceBill(int contract,int year,int month,LocalDate issued)throws SQLException{
  try(Connection c=Db.open();CallableStatement cs=c.prepareCall("{call dbo.sp_LapHoaDonDichVu(?,?,?,?,?)}")){
   cs.setInt(1,contract);cs.setInt(2,year);cs.setInt(3,month);cs.setDate(4,java.sql.Date.valueOf(issued));cs.registerOutParameter(5,Types.INTEGER);cs.execute();return cs.getInt(5);
  }
 }
 public void payment(int bill,java.math.BigDecimal amount,LocalDate paid,String method,String reference)throws SQLException{
  if(amount.signum()<=0)throw new IllegalArgumentException("Số tiền phải > 0.");
  try(Connection c=Db.open();CallableStatement cs=c.prepareCall("{call dbo.sp_ThuTien(?,?,?,?,?)}")){
   cs.setInt(1,bill);cs.setBigDecimal(2,amount);cs.setDate(3,java.sql.Date.valueOf(paid));cs.setString(4,method);cs.setString(5,reference.isBlank()?null:reference);cs.execute();
  }
 }
 public int extend(int contract,int room,int months,LocalDate date)throws SQLException{
  try(Connection c=Db.open();CallableStatement cs=c.prepareCall("{call dbo.sp_GiaHan(?,?,?,?,?)}")){
   cs.setInt(1,contract);cs.setInt(2,room);cs.setInt(3,months);cs.setDate(4,java.sql.Date.valueOf(date));cs.registerOutParameter(5,Types.INTEGER);cs.execute();return cs.getInt(5);
  }
 }
 public static LocalDate parseDate(String value){try{return LocalDate.parse(value.trim());}catch(Exception e){throw new IllegalArgumentException("Ngày phải theo yyyy-MM-dd.");}}
 public static int parsePositive(String value,String name){try{int v=Integer.parseInt(value.trim());if(v<=0)throw new NumberFormatException();return v;}catch(NumberFormatException e){throw new IllegalArgumentException(name+" phải là số nguyên dương.");}}
}
