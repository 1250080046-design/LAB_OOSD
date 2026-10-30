package com.donga.forms;
import com.donga.service.CrudService;
import com.donga.service.Module;
import com.donga.service.HrService;
import com.donga.service.WorkforceService;
import com.donga.data.Db;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
public abstract class FrmNghiepVu extends JFrame {
 private final JTabbedPane tabs=new JTabbedPane();
 private final java.util.List<Module> visibleModules;
 private final String domain;
 private final CrudService dao=new CrudService();
 private final HrService hr=new HrService();
 private final WorkforceService workforce=new WorkforceService();
 protected FrmNghiepVu(String title,String domain, String... tables){super(title);this.domain=domain;this.visibleModules=new ArrayList<>();for(String table:tables)this.visibleModules.add(Module.forTable(table));setSize(1250,780);setLocationRelativeTo(null);setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
  addWindowListener(new java.awt.event.WindowAdapter(){public void windowClosing(java.awt.event.WindowEvent e){if(JOptionPane.showConfirmDialog(FrmNghiepVu.this,"Thoát chương trình?","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)dispose();}});
  for(Module m:visibleModules)tabs.addTab(m.title(),crudPanel(m));add(tabs);}
 /** Action buttons appear in the relevant data tab; there is no separate workflow tab. */
 private void addBusinessButtons(JPanel bar,Module m,Runnable refresh){
  switch(m.table()){
   case "NhanVien", "QuyetDinhTuyenDung" -> workflowButton(bar,"Tuyển dụng / thử việc",this::askHire,refresh);
   case "HopDong" -> workflowButton(bar,"Ký hợp đồng chính thức",this::askContract,refresh);
   case "HoSoBaoHiem" -> workflowButton(bar,"Tạo hồ sơ BHXH / BHYT",this::askInsurance,refresh);
   case "QuyetDinhLuong", "QuyetDinhChucVu" -> workflowButton(bar,"Ký HĐ và lập QĐ lương/chức vụ",this::askContract,refresh);
   case "KeHoachTuan" -> workflowButton(bar,"Lập kế hoạch tuần",this::askWeeklyPlan,refresh);
   case "PhanCongCa" -> workflowButton(bar,"Phân công ca làm",this::askAssignShift,refresh);
   case "ChamCong" -> workflowButton(bar,"Chấm công theo phân công",this::askAttendance,refresh);
   case "ChiTietPhuTroi" -> workflowButton(bar,"Ghi nhận người phụ trội",this::askOvertime,refresh);
   case "QuyetDinhThuongPhat" -> workflowButton(bar,"Lập QĐ thưởng/kỷ luật",this::askReward,refresh);
   case "PhieuThuChi" -> workflowButton(bar,"Phân bổ thưởng/phạt tập thể",this::askCollective,refresh);
   case "BangLuong" -> workflowButton(bar,"Tính bảng lương tháng",this::askPayroll,refresh);
   default -> {}
  }
 }
 private void workflowButton(JPanel bar,String label,Runnable action,Runnable refresh){
  JButton button=new JButton(label);
  button.setFont(new Font("SansSerif",Font.BOLD,12));
  button.addActionListener(e->{
   try {action.run(); refresh.run();}
   catch(Exception ex){error(ex);}
  });
  bar.add(button);
 }

 /** Selectable references use their database ID internally while showing a readable label. */
 private record Choice(String value,String label){
  @Override public String toString(){return label;}
 }
 private record Lookup(String table,String key,String description){}
 private static final Map<String,Lookup> REFERENCES=Map.ofEntries(
  Map.entry("MaPhong",new Lookup("PhongBan","MaPhong","TenPhong")),
  Map.entry("MaNV",new Lookup("NhanVien","MaNV","HoTen")),
  Map.entry("MaChucVu",new Lookup("ChucVu","MaChucVu","TenChucVu")),
  Map.entry("MaHD",new Lookup("HopDong","MaHD","SoHopDong")),
  Map.entry("MaCa",new Lookup("CaLamViec","MaCa","TenCa")),
  Map.entry("MaKH",new Lookup("KeHoachTuan","MaKH","TuanBatDau")),
  Map.entry("MaPhieu",new Lookup("PhieuPhuTroi","MaPhieu","SoPhieu")),
  Map.entry("MaQD",new Lookup("QuyetDinhThuongPhat","MaQD","SoQuyetDinh")),
  Map.entry("MaBangLuong",new Lookup("BangLuong","MaBangLuong","Thang")),
  Map.entry("NguoiLap",new Lookup("NhanVien","MaNV","HoTen"))
 );
 private static final Map<String,String[]> ENUMS=Map.ofEntries(
  Map.entry("PhongBan.LoaiDonVi",new String[]{"PHONG","BAN","PHAN_XUONG"}),
  Map.entry("NhanVien.TrangThai",new String[]{"THU_VIEC","CHINH_THUC","NGHI_VIEC"}),
  Map.entry("QuyetDinhTuyenDung.KetQua",new String[]{"CHO_DANH_GIA","DAT","KHONG_DAT"}),
  Map.entry("HopDong.LoaiHopDong",new String[]{"DAI_HAN","NGAN_HAN"}),
  Map.entry("HopDong.TrangThai",new String[]{"HIEU_LUC","HET_HAN","HUY"}),
  Map.entry("ChamCong.TrangThai",new String[]{"CO_MAT","VANG","NGHI_PHEP"}),
  Map.entry("PhieuPhuTroi.HinhThuc",new String[]{"TANG_CA","NGAY_LE","NGAY_NGHI"}),
  Map.entry("QuyetDinhThuongPhat.Loai",new String[]{"THUONG","KY_LUAT"}),
  Map.entry("QuyetDinhThuongPhat.DoiTuong",new String[]{"CA_NHAN","TAP_THE"}),
  Map.entry("QuyetDinhThuongPhat.HinhThuc",new String[]{"QUA_LUONG","TIEN_MAT"}),
  Map.entry("PhieuThuChi.Loai",new String[]{"THU","CHI"}),
  Map.entry("BangLuong.TrangThai",new String[]{"NHAP","CHOT"})
 );
 private Lookup referenceFor(String module,String name){
  if(name.equals("MaPhieu")&&!module.equals("ChiTietPhuTroi"))return null;
  if(name.equals("MaQD")&&!module.equals("PhieuThuChi"))return null;
  if(name.equals("MaHD")&&!module.equals("HoSoBaoHiem"))return null;
  if(name.equals("MaKH")&&!module.equals("PhanCongCa"))return null;
  if(name.equals("MaBangLuong")&&!module.equals("ChiTietLuong"))return null;
  return REFERENCES.get(name);
 }
 private JComboBox<Choice> dropdown(String module,String field,boolean nullable) throws SQLException {
  JComboBox<Choice> combo=new JComboBox<>();
  combo.setPrototypeDisplayValue(new Choice("","000000 — Tên phòng ban hoặc nhân viên mẫu dài"));
  if(nullable)combo.addItem(new Choice("","— Không chọn —"));
  String[] fixed=ENUMS.get(module+"."+field);
  if(fixed==null){
   if(field.equals("TrangThai") && module.equals("PhongBan"))fixed=new String[]{"1","0"};
   else if(field.equals("DaThucHien")||field.equals("DuKienPhuTroi"))fixed=new String[]{"0","1"};
   else if(field.equals("HeSo")&&module.equals("PhieuPhuTroi"))fixed=new String[]{"1.5"};
  }
  if(fixed!=null){for(String value:fixed)combo.addItem(new Choice(value,value.equals("1")?"Có / Hoạt động (1)":value.equals("0")?"Không / Ngừng (0)":value));return combo;}
  Lookup r=referenceFor(module,field);
  if(r!=null){
   String sql="SELECT TOP (2000) "+r.key()+", "+r.description()+" FROM "+r.table()+" ORDER BY "+r.key()+" DESC";
   try(Connection c=Db.connect();PreparedStatement stmt=c.prepareStatement(sql);ResultSet rs=stmt.executeQuery()){
    while(rs.next()){
     String id=rs.getString(1);
     combo.addItem(new Choice(id,id+" — "+rs.getString(2)));
    }
   }
  }
  return combo;
 }
 private boolean isChoice(String module,String field){
  return ENUMS.containsKey(module+"."+field)
   ||(field.equals("TrangThai")&&module.equals("PhongBan"))
   ||field.equals("DaThucHien")||field.equals("DuKienPhuTroi")
   ||(field.equals("HeSo")&&module.equals("PhieuPhuTroi"))
   ||referenceFor(module,field)!=null;
 }
 private String readField(JComponent field){
  if(field instanceof JTextField t)return t.getText().trim();
  Object item=((JComboBox<?>)field).getSelectedItem();
  return item instanceof Choice c?c.value():"";
 }
 private void selectValue(JComponent component,String value){
  if(component instanceof JTextField tf){tf.setText(value);return;}
  JComboBox<?> combo=(JComboBox<?>)component;
  for(int i=0;i<combo.getItemCount();i++){
   Object item=combo.getItemAt(i);
   if(item instanceof Choice c && c.value().equals(value)){combo.setSelectedIndex(i);return;}
  }
 }
 private void focusField(JComponent component){
  if(component==null)return;
  component.requestFocusInWindow();
  if(component instanceof JTextField text)text.selectAll();
 }
 /** Workflow input caption-to-source mapping. Only IDs and constrained values become selectors. */
 private String workflowColumn(String caption){
  if(caption.startsWith("Mã phòng"))return "MaPhong";
  if(caption.startsWith("Mã nhân viên")||caption.startsWith("Mã trưởng phòng")||caption.equals("Mã nhân viên được phân bổ"))return "MaNV";
  if(caption.startsWith("Mã hợp đồng"))return "MaHD";
  if(caption.startsWith("Mã chức vụ"))return "MaChucVu";
  if(caption.startsWith("Mã ca"))return "MaCa";
  if(caption.startsWith("Mã kế hoạch"))return "MaKH";
  if(caption.startsWith("Mã phiếu phụ trội"))return "MaPhieu";
  if(caption.startsWith("Mã quyết định tập thể"))return "MaQD";
  if(caption.equals("Loại (DAI_HAN / NGAN_HAN)"))return "LoaiHopDong";
  if(caption.equals("Loại (THUONG / KY_LUAT)"))return "Loai";
  if(caption.equals("Đối tượng (CA_NHAN / TAP_THE)"))return "DoiTuong";
  if(caption.equals("Hình thức (QUA_LUONG / TIEN_MAT)"))return "HinhThuc";
  if(caption.startsWith("Trạng thái (CO_MAT"))return "TrangThai";
  if(caption.startsWith("Dự kiến phụ trội?"))return "DuKienPhuTroi";
  return null;
 }
 private String workflowTable(String caption){
  if(caption.equals("Loại (DAI_HAN / NGAN_HAN)"))return "HopDong";
  if(caption.equals("Loại (THUONG / KY_LUAT)"))return "QuyetDinhThuongPhat";
  if(caption.equals("Đối tượng (CA_NHAN / TAP_THE)"))return "QuyetDinhThuongPhat";
  if(caption.equals("Hình thức (QUA_LUONG / TIEN_MAT)"))return "QuyetDinhThuongPhat";
  if(caption.startsWith("Trạng thái (CO_MAT"))return "ChamCong";
  if(caption.startsWith("Dự kiến phụ trội?"))return "PhanCongCa";
  if(caption.startsWith("Mã phiếu phụ trội"))return "ChiTietPhuTroi";
  if(caption.startsWith("Mã quyết định tập thể"))return "PhieuThuChi";
  if(caption.startsWith("Mã hợp đồng"))return "HoSoBaoHiem";
  if(caption.startsWith("Mã kế hoạch"))return "PhanCongCa";
  return "NhanVien";
 }
 @FunctionalInterface
 private interface FormAction { void apply(Map<String,String> input) throws Exception; }
 /** Keeps the same Swing fields alive until the operation succeeds or the user cancels. */
 private void promptAndExecute(String title, FormAction action, String... fields){
  JPanel form=new JPanel(new GridLayout(0,2,8,6));
  LinkedHashMap<String,JComponent> inputs=new LinkedHashMap<>();
  try{
   for(String name:fields){
    form.add(new JLabel(name));
    String col=workflowColumn(name);
    JComponent input=(col!=null&&isChoice(workflowTable(name),col))
     ?dropdown(workflowTable(name),col,true):new JTextField(22);
    inputs.put(name,input);
    form.add(input);
   }
  }catch(SQLException ex){error(ex);return;}
  JScrollPane scroll=new JScrollPane(form);
  scroll.setPreferredSize(new Dimension(680,Math.min(540,fields.length*39+30)));
  while(true){
   int choice=JOptionPane.showConfirmDialog(this,scroll,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
   if(choice!=JOptionPane.OK_OPTION)return;
   Map<String,String> values=new LinkedHashMap<>();
   inputs.forEach((name,field)->values.put(name,readField(field)));
   try{action.apply(values);return;}
   catch(Exception ex){error(ex);focusField(locateErrorField(inputs,ex.getMessage()));}
  }
 }
 private JComponent locateErrorField(Map<String,JComponent> fields,String message){
  if(message==null)return fields.values().stream().findFirst().orElse(null);
  String msg=message.toLowerCase(java.util.Locale.ROOT);
  for(var e:fields.entrySet())if(msg.contains(e.getKey().toLowerCase(java.util.Locale.ROOT)))return e.getValue();
  return fields.values().stream().filter(t->readField(t).isBlank()).findFirst().orElse(null);
 }
 private void askHire(){promptAndExecute("Tuyển dụng",x->{int id=hr.hire(Integer.parseInt(x.get("Mã phòng (số)")),x.get("Họ tên"),LocalDate.parse(x.get("Ngày vào (yyyy-MM-dd)")),x.get("Số quyết định"),LocalDate.parse(x.get("Bắt đầu thử việc (yyyy-MM-dd)")),LocalDate.parse(x.get("Kết thúc thử việc (yyyy-MM-dd)")),new BigDecimal(x.get("Lương thử việc")),x.get("Điều khoản"));info("Đã tạo nhân viên #"+id+" và quyết định thử việc. Cần cập nhật kết quả 'DAT' trước khi ký HĐ.");},"Mã phòng (số)","Họ tên","Ngày vào (yyyy-MM-dd)","Số quyết định","Bắt đầu thử việc (yyyy-MM-dd)","Kết thúc thử việc (yyyy-MM-dd)","Lương thử việc","Điều khoản");}
 private void askContract(){promptAndExecute("Hợp đồng chính thức",x->{String end=x.get("Ngày hết hạn (để trống nếu không có)");hr.confirmEmployment(Integer.parseInt(x.get("Mã nhân viên")),x.get("Số hợp đồng"),x.get("Loại (DAI_HAN / NGAN_HAN)"),LocalDate.parse(x.get("Hiệu lực (yyyy-MM-dd)")),end.isBlank()?null:LocalDate.parse(end),x.get("Số QĐ lương"),new BigDecimal(x.get("Lương cơ bản")),x.get("Số QĐ chức vụ"),Integer.parseInt(x.get("Mã chức vụ")),new BigDecimal(x.get("Phụ cấp chức vụ")));info("Đã ký HĐ và tạo các quyết định liên quan. HĐ dài hạn cần nhập hồ sơ bảo hiểm trước tính lương.");},"Mã nhân viên","Số hợp đồng","Loại (DAI_HAN / NGAN_HAN)","Hiệu lực (yyyy-MM-dd)","Ngày hết hạn (để trống nếu không có)","Số QĐ lương","Lương cơ bản","Số QĐ chức vụ","Mã chức vụ","Phụ cấp chức vụ");}
 private void askReward(){promptAndExecute("Quyết định thưởng/phạt",x->{String nv=x.get("Mã nhân viên (cá nhân)"),pb=x.get("Mã phòng (tập thể)");int id=hr.rewardDecision(x.get("Số quyết định"),x.get("Loại (THUONG / KY_LUAT)"),x.get("Đối tượng (CA_NHAN / TAP_THE)"),nv.isBlank()?null:Integer.parseInt(nv),pb.isBlank()?null:Integer.parseInt(pb),LocalDate.parse(x.get("Ngày ký (yyyy-MM-dd)")),x.get("Lý do"),new BigDecimal(x.get("Số tiền")),x.get("Hình thức (QUA_LUONG / TIEN_MAT)"),x.get("Người ký"));info("Đã tạo quyết định #"+id+". Thưởng/phạt tập thể cần phân bổ phiếu thu/chi cho từng nhân viên trước khi tính lương.");},"Số quyết định","Loại (THUONG / KY_LUAT)","Đối tượng (CA_NHAN / TAP_THE)","Mã nhân viên (cá nhân)","Mã phòng (tập thể)","Ngày ký (yyyy-MM-dd)","Lý do","Số tiền","Hình thức (QUA_LUONG / TIEN_MAT)","Người ký");}
 private void askInsurance(){promptAndExecute("Hồ sơ bảo hiểm",x->{int id=hr.createInsurance(Integer.parseInt(x.get("Mã nhân viên")),Integer.parseInt(x.get("Mã hợp đồng dài hạn")),x.get("Số BHXH"),x.get("Số BHYT"),LocalDate.parse(x.get("Ngày hiệu lực (yyyy-MM-dd)")),new BigDecimal(x.get("Tỷ lệ BHXH (ví dụ 0.08)")),new BigDecimal(x.get("Tỷ lệ BHYT (ví dụ 0.015)")),new BigDecimal(x.get("Mức lương đóng")));info("Đã lập hồ sơ bảo hiểm #"+id);}, "Mã nhân viên","Mã hợp đồng dài hạn","Số BHXH","Số BHYT","Ngày hiệu lực (yyyy-MM-dd)","Tỷ lệ BHXH (ví dụ 0.08)","Tỷ lệ BHYT (ví dụ 0.015)","Mức lương đóng");}
 private void askCollective(){promptAndExecute("Phân bổ thưởng/phạt tập thể",x->{hr.allocateCollectiveDecision(Integer.parseInt(x.get("Mã quyết định tập thể")),Integer.parseInt(x.get("Mã nhân viên được phân bổ")),x.get("Số phiếu"),new BigDecimal(x.get("Số tiền phân bổ")),LocalDate.parse(x.get("Ngày phiếu (yyyy-MM-dd)")));info("Phân bổ thành công");},"Mã quyết định tập thể","Mã nhân viên được phân bổ","Số phiếu","Số tiền phân bổ","Ngày phiếu (yyyy-MM-dd)");}
 private void askPayroll(){promptAndExecute("Tính lương",x->{int n=hr.calculatePayroll(Integer.parseInt(x.get("Tháng (1–12)")),Integer.parseInt(x.get("Năm")),Integer.parseInt(x.get("Tổng giờ chuẩn của tháng")));info("Đã lập bảng lương với "+n+" nhân viên. Kiểm tra tab Bảng lương / Chi tiết lương.");},"Tháng (1–12)","Năm","Tổng giờ chuẩn của tháng");}
 private void askWeeklyPlan(){promptAndExecute("Lập kế hoạch tuần",x->{int id=workforce.createWeeklyPlan(Integer.parseInt(x.get("Mã phòng ban")),LocalDate.parse(x.get("Thứ Hai đầu tuần (yyyy-MM-dd)")),Integer.parseInt(x.get("Mã trưởng phòng (nhân viên)")),x.get("Ghi chú"));info("Đã tạo kế hoạch tuần #"+id);},"Mã phòng ban","Thứ Hai đầu tuần (yyyy-MM-dd)","Mã trưởng phòng (nhân viên)","Ghi chú");}
 private void askAssignShift(){promptAndExecute("Phân công ca",x->{workforce.assignShift(Integer.parseInt(x.get("Mã kế hoạch tuần")),Integer.parseInt(x.get("Mã nhân viên")),Integer.parseInt(x.get("Mã ca")),LocalDate.parse(x.get("Ngày làm (yyyy-MM-dd)")),"1".equals(x.get("Dự kiến phụ trội? (1/0)")));info("Đã phân công ca thành công");},"Mã kế hoạch tuần","Mã nhân viên","Mã ca","Ngày làm (yyyy-MM-dd)","Dự kiến phụ trội? (1/0)");}
 private void askAttendance(){promptAndExecute("Chấm công",x->{workforce.recordAttendance(Integer.parseInt(x.get("Mã nhân viên")),Integer.parseInt(x.get("Mã ca")),LocalDate.parse(x.get("Ngày công (yyyy-MM-dd)")),new BigDecimal(x.get("Giờ làm thực tế")),x.get("Trạng thái (CO_MAT/VANG/NGHI_PHEP)"));info("Đã ghi nhận công thành công");},"Mã nhân viên","Mã ca","Ngày công (yyyy-MM-dd)","Giờ làm thực tế","Trạng thái (CO_MAT/VANG/NGHI_PHEP)");}
 private void askOvertime(){promptAndExecute("Chi tiết phụ trội",x->{workforce.addOvertimeParticipant(Integer.parseInt(x.get("Mã phiếu phụ trội")),Integer.parseInt(x.get("Mã nhân viên")),new BigDecimal(x.get("Số giờ tham gia")));info("Đã ghi nhận người tham gia phụ trội");},"Mã phiếu phụ trội","Mã nhân viên","Số giờ tham gia");}
 private JPanel crudPanel(Module m){JPanel p=new JPanel(new BorderLayout(8,8));DefaultTableModel tm=new DefaultTableModel(){public boolean isCellEditable(int row,int col){return false;}};JTable t=new JTable(tm);t.setAutoCreateRowSorter(true);p.add(new JScrollPane(t),BorderLayout.CENTER);
  Runnable refresh=()->{try{List<Map<String,Object>> rows=dao.list(m);tm.setRowCount(0);if(rows.isEmpty()){String[] names=new String[m.columns().length+1];names[0]=m.pk();System.arraycopy(m.columns(),0,names,1,m.columns().length);tm.setColumnIdentifiers(names);}else{tm.setColumnIdentifiers(rows.get(0).keySet().toArray());for(Map<String,Object> row:rows)tm.addRow(row.values().toArray());}}catch(Exception e){error(e);}};
  JPanel bar=new JPanel(new FlowLayout(FlowLayout.LEFT));JButton add=new JButton("Thêm"),edit=new JButton("Sửa"),del=new JButton("Xóa"),reload=new JButton("Làm mới");for(JButton b:new JButton[]{add,edit,del,reload})bar.add(b);
  add.addActionListener(e->{if(saveRecord(m,null))refresh.run();});
  edit.addActionListener(e->{Map<String,Object> initial=selected(t,tm);if(initial==null)return;if(saveRecord(m,initial))refresh.run();});
  del.addActionListener(e->{Map<String,Object> row=selected(t,tm);if(row==null)return;if(JOptionPane.showConfirmDialog(this,"Xóa bản ghi? Các khóa ngoại có thể chặn thao tác.","Xác nhận",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)try{dao.delete(m,((Number)row.get(m.pk())).intValue());refresh.run();}catch(Exception ex){error(ex);}});
  reload.addActionListener(e->refresh.run());
  boolean computed=m.table().equals("BangLuong")||m.table().equals("ChiTietLuong") ||m.table().equals("PhieuThuChi")||m.table().equals("PhanCongCa")||m.table().equals("ChamCong")||m.table().equals("ChiTietPhuTroi")||m.table().equals("KeHoachTuan")||m.table().equals("HoSoBaoHiem");
  if(computed){add.setEnabled(false);edit.setEnabled(false);del.setEnabled(false);}
  addBusinessButtons(bar,m,refresh);
  p.add(bar,BorderLayout.NORTH);refresh.run();return p;}
 private Map<String,Object> selected(JTable t,DefaultTableModel model){int view=t.getSelectedRow();if(view<0){info("Chọn một dòng trước");return null;}int r=t.convertRowIndexToModel(view);Map<String,Object> row=new LinkedHashMap<>();for(int i=0;i<model.getColumnCount();i++)row.put(model.getColumnName(i),model.getValueAt(r,i));return row;}
 /** Validation (including JDBC errors) occurs before destroying the input component. */
 private boolean saveRecord(Module m,Map<String,Object> existing){
  JPanel form=new JPanel(new GridLayout(0,2,6,5));
  LinkedHashMap<String,JComponent> inputs=new LinkedHashMap<>();
  try{
   for(String col:m.columns()){
    form.add(new JLabel(col));
    JComponent field=isChoice(m.table(),col)?dropdown(m.table(),col,true):new JTextField(24);
    if(existing!=null&&existing.get(col)!=null){
     Object old=existing.get(col);
     String value=old instanceof Boolean b?(b?"1":"0"):old.toString();
     selectValue(field,value);
    }
    inputs.put(col,field);form.add(field);
   }
  }catch(SQLException ex){error(ex);return false;}
  JScrollPane pane=new JScrollPane(form);
  pane.setPreferredSize(new Dimension(610,Math.min(560,m.columns().length*40+25)));
  while(true){
   int choice=JOptionPane.showConfirmDialog(this,pane,(existing==null?"Thêm ":"Sửa ")+m.title(),JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE);
   if(choice!=JOptionPane.OK_OPTION)return false;
   Map<String,Object> values=new LinkedHashMap<>();
   String fieldName=null;
   try{
    for(var item:inputs.entrySet()){
     fieldName=item.getKey();String raw=readField(item.getValue());
     if(raw.isBlank()){if(existing!=null&&existing.get(fieldName)!=null)values.put(fieldName,null);continue;}
     values.put(fieldName,convertField(m,fieldName,raw));
    }
    if(existing==null)dao.insert(m,values);
    else dao.update(m,((Number)existing.get(m.pk())).intValue(),values);
    return true;
   }catch(Exception ex){
    if(ex instanceof IllegalArgumentException && fieldName!=null){
     error(new IllegalArgumentException("Sai định dạng cột "+fieldName+": "+ex.getMessage()));
     focusField(inputs.get(fieldName));
    }else{error(ex);focusField(locateErrorField(inputs,ex.getMessage()));}
   }
  }
 }
 private Object convertField(Module m,String name,String raw){
  if(name.matches("Ma[A-Z].*|Thang|Nam|NguoiLap"))return Integer.parseInt(raw);
  if(name.startsWith("Ngay")||name.equals("TuNgay")||name.equals("DenNgay")||name.equals("HieuLucTu")||name.equals("TuanBatDau"))return java.sql.Date.valueOf(raw.substring(0,Math.min(10,raw.length())));
  if(name.startsWith("GioBat")||name.startsWith("GioKet"))return java.sql.Time.valueOf(raw.length()==5?raw+":00":raw);
  if(name.startsWith("Luong")||name.startsWith("PhuCap")||name.startsWith("TyLe")||name.startsWith("MucLuong")||name.startsWith("SoTien")||name.startsWith("SoGio")||name.startsWith("GioThuc")||name.startsWith("HeSo")||name.startsWith("Tien")||name.startsWith("Tru")||name.equals("ThucLinh")||name.equals("GioChuan")||name.equals("GioLam"))return new BigDecimal(raw);
  if(name.equals("TrangThai")&&m.table().equals("PhongBan"))return parseBool(raw);
  if(name.equals("DaThucHien")||name.equals("DuKienPhuTroi"))return parseBool(raw);
  return raw;
 }
 private boolean parseBool(String text){return text.equals("1")||text.equalsIgnoreCase("true");}
 private void info(String s){JOptionPane.showMessageDialog(this,s,"Thông báo",JOptionPane.INFORMATION_MESSAGE);}
 private void error(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}
}
