package com.donga.service;
import com.donga.data.Db;
import com.donga.service.Module;
import java.sql.*;
import java.util.*;
public class CrudService {
 public List<Map<String,Object>> list(Module m) throws SQLException {
  List<Map<String,Object>> rows=new ArrayList<>();
  String sql="SELECT TOP (500) * FROM "+m.table()+" ORDER BY "+m.pk()+" DESC";
  try(Connection c=Db.connect(); PreparedStatement s=c.prepareStatement(sql);ResultSet rs=s.executeQuery()){
   ResultSetMetaData md=rs.getMetaData();while(rs.next()){Map<String,Object> row=new LinkedHashMap<>();for(int i=1;i<=md.getColumnCount();i++)row.put(md.getColumnName(i),rs.getObject(i));rows.add(row);}}
  return rows;
 }
 public void insert(Module m,Map<String,Object> values) throws SQLException {execute(m,values,null);}
 public void update(Module m,int id,Map<String,Object> values) throws SQLException {execute(m,values,id);}
 private void execute(Module m,Map<String,Object> values,Integer id) throws SQLException {
  if(values.isEmpty())throw new IllegalArgumentException("Không có dữ liệu nhập");
  for(String k:values.keySet())if(!Arrays.asList(m.columns()).contains(k))throw new IllegalArgumentException("Cột không hợp lệ: "+k);
  List<String> cols=new ArrayList<>(values.keySet());
  String sql=id==null?"INSERT INTO "+m.table()+" ("+String.join(",",cols)+") VALUES ("+String.join(",",Collections.nCopies(cols.size(),"?"))+")":"UPDATE "+m.table()+" SET "+String.join(",",cols.stream().map(k->k+"=?").toList())+" WHERE "+m.pk()+"=?";
  try(Connection c=Db.connect();PreparedStatement ps=c.prepareStatement(sql)){
   int pos=1;for(String k:cols)ps.setObject(pos++,values.get(k));if(id!=null)ps.setInt(pos,id);int n=ps.executeUpdate();if(n==0)throw new SQLException("Không tìm thấy dòng dữ liệu");}
 }
 public void delete(Module m,int id) throws SQLException {
  try(Connection c=Db.connect();PreparedStatement ps=c.prepareStatement("DELETE FROM "+m.table()+" WHERE "+m.pk()+"=?")){ps.setInt(1,id);ps.executeUpdate();}
 }
}
