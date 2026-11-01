package com.caooc.forms;
import com.caooc.service.*;import javax.swing.*;import java.awt.*;import java.sql.*;import java.time.*;import java.util.*;import java.math.*;
public final class FormInputs {
 public record Spec(String label,String key,String defaultValue){}
 public interface Handler{void save(Map<String,String> values)throws Exception;}
 private FormInputs(){}
 public static void dialog(Component owner,String title,Spec[] specs,Handler handler){
  JPanel panel=new JPanel(new GridBagLayout());GridBagConstraints gc=new GridBagConstraints();gc.insets=new Insets(5,6,5,6);gc.fill=GridBagConstraints.HORIZONTAL;gc.weightx=1;
  Map<String,JComponent> controls=new LinkedHashMap<>();int row=0;
  for(Spec spec:specs){gc.gridx=0;gc.gridy=row;gc.weightx=0;panel.add(new JLabel(spec.label()),gc);
   JComponent comp;String[] fixed=values(spec.key());
   if(spec.key().equals("@rooms")){JList<CrudService.Choice> items=new JList<>();try{items.setListData(new CrudService().choices("MaPhong").toArray(new CrudService.Choice[0]));}catch(SQLException e){JOptionPane.showMessageDialog(owner,"Không tải phòng: "+e.getMessage());return;}items.setVisibleRowCount(5);comp=new JScrollPane(items);comp.putClientProperty("roomList",items);}
   else if(fixed!=null){JComboBox<String> combo=new JComboBox<>(fixed);if(spec.defaultValue()!=null)combo.setSelectedItem(spec.defaultValue());comp=combo;}
   else if(spec.key().startsWith("@")){JComboBox<CrudService.Choice> combo=new JComboBox<>();try{for(var item:new CrudService().choices(spec.key().substring(1)))combo.addItem(item);}catch(SQLException e){JOptionPane.showMessageDialog(owner,"Không tải được danh sách: "+e.getMessage(),"Lỗi dữ liệu",JOptionPane.ERROR_MESSAGE);return;}comp=combo;}
   else comp=new JTextField(spec.defaultValue()==null?"":spec.defaultValue(),22);
   controls.put(spec.label(),comp);gc.gridx=1;gc.weightx=1;panel.add(comp,gc);row++;
  }
  JScrollPane scroll=new JScrollPane(panel);scroll.setPreferredSize(new Dimension(520,Math.min(540,70*specs.length+20)));
  while(true){int answer=JOptionPane.showConfirmDialog(owner,scroll,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);if(answer!=JOptionPane.OK_OPTION)return;
   Map<String,String> result=new LinkedHashMap<>();for(Spec spec:specs){JComponent comp=controls.get(spec.label());String s=comp.getClientProperty("roomList") instanceof JList<?> rlist?rlist.getSelectedValuesList().stream().map(v->String.valueOf(((CrudService.Choice)v).id())).collect(java.util.stream.Collectors.joining(",")):comp instanceof JTextField t?t.getText().trim():comp instanceof JComboBox<?> cb?(cb.getSelectedItem() instanceof CrudService.Choice ch?String.valueOf(ch.id()):String.valueOf(cb.getSelectedItem())):"";result.put(spec.label(),s);}
   try{handler.save(result);JOptionPane.showMessageDialog(owner,"Đã lưu thành công.");return;}catch(Exception e){JOptionPane.showMessageDialog(owner,message(e),"Dữ liệu chưa hợp lệ - vui lòng sửa",JOptionPane.WARNING_MESSAGE);JComponent first=controls.values().iterator().next();first.requestFocusInWindow();}
  }
 }
 public static String message(Throwable e){Throwable t=e;while(t.getCause()!=null)t=t.getCause();return t.getMessage()==null?e.toString():t.getMessage();}
 private static String[] values(String key){return switch(key){case "method"->new String[]{"CHUYEN_KHOAN","TIEN_MAT"};case "statusRoom"->new String[]{"TRONG","DANG_THUE","BAO_TRI"};case "statusCard"->new String[]{"HIEU_LUC","THU_HOI"};case "direction"->new String[]{"Đông","Tây","Nam","Bắc","Đông Bắc","Tây Bắc","Đông Nam","Tây Nam"};default->null;};}
 public static int number(Map<String,String> m,String key){return OfficeService.parsePositive(m.get(key),key);}
 public static LocalDate date(Map<String,String> m,String key){return OfficeService.parseDate(m.get(key));}
 public static BigDecimal decimal(Map<String,String> m,String key){try{return new BigDecimal(m.get(key));}catch(Exception e){throw new IllegalArgumentException(key+" phải là số hợp lệ.");}}
}
