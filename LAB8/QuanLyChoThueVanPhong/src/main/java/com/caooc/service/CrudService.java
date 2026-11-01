package com.caooc.service;
import com.caooc.data.Db;import java.sql.*;import java.util.*;import java.util.stream.*;
public final class CrudService {
 public record Field(String name,int type,boolean required,boolean identity){}
 public record Choice(Object id,String label){public String toString(){return label;}}
 private static final Set<String> TABLES=Set.of("Tang","VanPhong","KhachHang","HopDong","ChiTietHopDong","HoaDon","ThanhToan","DichVu","SuDungDichVu","GiaHan","NhanVienVanPhong","TheGiuXe","KiemTraPhong");
 private static final Set<String> READ_ONLY=Set.of("HopDong","ChiTietHopDong","HoaDon","ThanhToan","GiaHan");
 public boolean editable(String table){return !READ_ONLY.contains(table);}
 private String safe(String table){if(!TABLES.contains(table))throw new IllegalArgumentException("Bảng không hợp lệ: "+table);return table;}
 public List<Field> fields(String table)throws SQLException{safe(table);try(Connection c=Db.open();ResultSet rs=c.getMetaData().getColumns(c.getCatalog(),"dbo",table,null)){
  List<Field> out=new ArrayList<>();while(rs.next()){String name=rs.getString("COLUMN_NAME");int type=rs.getInt("DATA_TYPE");boolean req=rs.getInt("NULLABLE")==DatabaseMetaData.columnNoNulls;boolean identity="YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));out.add(new Field(name,type,req,identity));}return out;}}
 public List<Map<String,Object>> rows(String table)throws SQLException{safe(table);return Db.query("SELECT TOP (3000) * FROM dbo."+table+" ORDER BY 1 DESC");}
 public List<Choice> choices(String key)throws SQLException{
  String[] cols=switch(key){case "MaTang"->new String[]{"Tang","MaTang","TenTang"};case "MaPhong"->new String[]{"VanPhong","MaPhong","SoPhong"};case "MaKH"->new String[]{"KhachHang","MaKH","TenCongTy"};case "MaHD"->new String[]{"HopDong","MaHD","SoHopDong"};case "MaDV"->new String[]{"DichVu","MaDV","TenDV"};case "MaHoaDon"->new String[]{"HoaDon","MaHoaDon","Loai"};case "MaNV"->new String[]{"NhanVienVanPhong","MaNV","HoTen"};default->null;};
  if(cols==null)return List.of();List<Choice> opts=new ArrayList<>();for(Map<String,Object> row:Db.query("SELECT "+cols[1]+", "+cols[2]+" FROM "+cols[0]+" ORDER BY 1")){opts.add(new Choice(row.get(cols[1]),row.get(cols[1])+" — "+row.get(cols[2])));}return opts;
 }
 public void insert(String table,Map<String,Object> row)throws SQLException{save(table,row,null);}
 public void update(String table,Map<String,Object> row,Object id)throws SQLException{save(table,row,id);}
 private void save(String table,Map<String,Object> row,Object id)throws SQLException{
  safe(table);if(!editable(table))throw new IllegalArgumentException("Bảng này chỉ xem; hãy dùng nút nghiệp vụ để cập nhật.");
  List<String> cols=new ArrayList<>(row.keySet());if(cols.isEmpty())throw new IllegalArgumentException("Chưa nhập dữ liệu.");
  String sql=id==null?"INSERT INTO dbo."+table+" ("+String.join(",",cols)+") VALUES ("+cols.stream().map(x->"?").collect(Collectors.joining(","))+")":"UPDATE dbo."+table+" SET "+cols.stream().map(x->x+"=?").collect(Collectors.joining(","))+" WHERE "+fields(table).get(0).name()+"=?";
  try(Connection c=Db.open();PreparedStatement ps=c.prepareStatement(sql)){int i=1;for(String col:cols)ps.setObject(i++,row.get(col));if(id!=null)ps.setObject(i,id);ps.executeUpdate();}
 }
 public void delete(String table,Object id)throws SQLException{safe(table);if(!editable(table))throw new IllegalArgumentException("Bảng chỉ đọc.");String pk=fields(table).get(0).name();try(Connection c=Db.open();PreparedStatement ps=c.prepareStatement("DELETE FROM dbo."+table+" WHERE "+pk+"=?")){ps.setObject(1,id);ps.executeUpdate();}}
}
