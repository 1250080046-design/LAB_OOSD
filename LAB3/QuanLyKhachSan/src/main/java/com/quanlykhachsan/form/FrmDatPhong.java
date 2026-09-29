package com.quanlykhachsan.form;

import com.quanlykhachsan.model.KetQuaXuLy;
import com.quanlykhachsan.model.PhongDatItem;
import com.quanlykhachsan.service.DanhMucService;
import com.quanlykhachsan.service.DatPhongService;
import com.quanlykhachsan.util.ComboItem;
import com.quanlykhachsan.util.DateUtil;
import com.quanlykhachsan.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FrmDatPhong extends JFrame {
    private final DatPhongService s=new DatPhongService();
    private final DanhMucService dm=new DanhMucService();
    private final JTextField txtMaKH=new JTextField(),txtTenKH=new JTextField(),txtCMND=new JTextField(),txtQT=new JTextField(),txtSDT=new JTextField();
    private final JTextField txtSoPhieu=new JTextField(),txtPhieuChon=new JTextField(),txtNguoiPhong=new JTextField(),txtNguoiTen=new JTextField(),txtNguoiCMND=new JTextField(),txtNguoiQT=new JTextField();
    private final JComboBox<ComboItem> cboKhach=UI.combo(),cboNV=UI.combo();
    private final JComboBox<String> cboKenh=new JComboBox<>(new String[]{"Điện thoại","Website","Trực tiếp"});
    private final JSpinner dtLap=DateUtil.dateSpinner(),dtNhan=DateUtil.dateSpinner(),dtTra=DateUtil.dateSpinner();
    private final JSpinner numCoc=money();
    private final JSpinner numSoNguoi=new JSpinner(new SpinnerNumberModel(1,1,100,1));
    private final JTable dgvKhach=UI.table(),dgvPhong=UI.table(),dgvChon=UI.table(),dgvPhieu=UI.table(),dgvCT=UI.table(),dgvNguoi=UI.table();
    private final DefaultTableModel chonModel=new DefaultTableModel(new Object[]{"Phòng chọn","Số người","Đơn giá/ngày"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final List<PhongDatItem> chon=new ArrayList<>();

    public FrmDatPhong(){
        UiUtil.setupFrame(this,"Khách đặt phòng - Nhận phòng",1180,760);
        JTabbedPane tabs=new JTabbedPane();tabs.addTab("[Khách hàng]",khachTab());tabs.addTab("[Đặt phòng]",datTab());tabs.addTab("[Nhận phòng / Người lưu trú]",nhanTab());add(tabs);loadAll();
    }
    private JPanel khachTab(){JPanel root=new JPanel(new BorderLayout(4,4));JPanel r=UI.row();UI.field(r,"Mã:",txtMaKH,100);UI.field(r,"Họ tên:",txtTenKH,190);UI.field(r,"CCCD:",txtCMND,120);UI.field(r,"Quốc tịch:",txtQT,110);UI.field(r,"SĐT:",txtSDT,110);JButton b=new JButton("Thêm khách");b.addActionListener(e->result(s.themKhach(txtMaKH.getText().trim(),txtTenKH.getText().trim(),txtCMND.getText().trim(),txtQT.getText().trim(),txtSDT.getText().trim())));r.add(b);root.add(r,BorderLayout.NORTH);root.add(UI.scroll(dgvKhach),BorderLayout.CENTER);return root;}
    private JPanel datTab(){
        JPanel root=new JPanel(new BorderLayout(4,4)); JPanel head=UI.row();UI.field(head,"Số phiếu:",txtSoPhieu,100);UI.field(head,"Khách:",cboKhach,170);UI.field(head,"Lễ tân:",cboNV,140);UI.field(head,"Kênh:",cboKenh,110);UI.field(head,"Ngày lập:",dtLap,100);UI.field(head,"Ngày nhận:",dtNhan,100);UI.field(head,"Ngày trả:",dtTra,100);UI.field(head,"Tiền cọc:",numCoc,95);root.add(head,BorderLayout.NORTH);
        JPanel center=new JPanel(new GridLayout(1,2,8,0));JPanel left=new JPanel(new BorderLayout(3,3));left.setBorder(BorderFactory.createTitledBorder("Danh sách phòng"));left.add(UI.scroll(dgvPhong),BorderLayout.CENTER);JPanel add=UI.row();UI.field(add,"Số người:",numSoNguoi,60);JButton bp=new JButton("Thêm phòng");bp.addActionListener(e->addRoom());add.add(bp);left.add(add,BorderLayout.SOUTH);
        JPanel right=new JPanel(new BorderLayout(3,3));right.setBorder(BorderFactory.createTitledBorder("Phòng đã chọn"));dgvChon.setModel(chonModel);right.add(UI.scroll(dgvChon),BorderLayout.CENTER);JButton br=new JButton("Bỏ phòng");br.addActionListener(e->removeRoom());right.add(UI.buttonRow(br),BorderLayout.SOUTH);center.add(left);center.add(right);root.add(center,BorderLayout.CENTER);JButton lap=new JButton("Lập phiếu đặt");lap.addActionListener(e->createBooking());root.add(UI.buttonRow(lap),BorderLayout.SOUTH);return root;
    }
    private JPanel nhanTab(){
        JPanel root=new JPanel(new BorderLayout(4,4));JPanel center=new JPanel(new GridLayout(1,2,8,0));JPanel a=new JPanel(new BorderLayout(3,3));a.setBorder(BorderFactory.createTitledBorder("Phòng trong phiếu"));a.add(UI.scroll(dgvCT));JPanel b=new JPanel(new BorderLayout(3,3));b.setBorder(BorderFactory.createTitledBorder("Người lưu trú"));b.add(UI.scroll(dgvNguoi));center.add(a);center.add(b);
        JPanel f=UI.row();UI.field(f,"Phiếu:",txtPhieuChon,100);UI.field(f,"Phòng:",txtNguoiPhong,75);UI.field(f,"Họ tên:",txtNguoiTen,150);UI.field(f,"CCCD:",txtNguoiCMND,110);UI.field(f,"Quốc tịch:",txtNguoiQT,100);JButton add=new JButton("Thêm người");JButton nhan=new JButton("Nhận phòng");JButton no=new JButton("No-show");txtPhieuChon.setEditable(false);add.addActionListener(e->result(s.themNguoiLuuTru(txtPhieuChon.getText().trim(),txtNguoiPhong.getText().trim(),txtNguoiTen.getText().trim(),txtNguoiCMND.getText().trim(),txtNguoiQT.getText().trim())));nhan.addActionListener(e->result(s.nhanPhong(txtPhieuChon.getText().trim(),new java.util.Date())));no.addActionListener(e->result(s.danhDauNoShow(txtPhieuChon.getText().trim())));f.add(add);f.add(nhan);f.add(no);root.add(center,BorderLayout.CENTER);root.add(f,BorderLayout.SOUTH);JPanel top=UI.row();top.add(new JLabel("Chọn phiếu ở bảng bên dưới:"));root.add(top,BorderLayout.NORTH);
        JPanel wrap=new JPanel(new BorderLayout(2,2));wrap.add(UI.scroll(dgvPhieu),BorderLayout.CENTER);wrap.setBorder(BorderFactory.createTitledBorder("Danh sách phiếu đặt"));root.add(wrap,BorderLayout.WEST);wrap.setPreferredSize(new Dimension(560,260));dgvPhieu.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())selectBooking();});return root;
    }
    private void loadAll(){try{dgvKhach.setModel(s.layKhach());dgvPhong.setModel(s.layPhong());dgvPhieu.setModel(s.layPhieuDat());setCombo(cboKhach,s.layKhach(),"MaKhach","HoTen");setCombo(cboNV,dm.layNhanVien(),"MaNV","HoTen");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}}
    private void addRoom(){int r=dgvPhong.getSelectedRow();if(r<0)return;int row=dgvPhong.convertRowIndexToModel(r);String phong=Objects.toString(dgvPhong.getModel().getValueAt(row,col(dgvPhong.getModel(),"SoPhong")),"");for(PhongDatItem x:chon)if(x.soPhong().equals(phong)){JOptionPane.showMessageDialog(this,"Phòng đã có trong phiếu.");return;}int n=((Number)numSoNguoi.getValue()).intValue();BigDecimal g=new BigDecimal(String.valueOf(dgvPhong.getModel().getValueAt(row,col(dgvPhong.getModel(),"DonGiaNgay"))));chon.add(new PhongDatItem(phong,n,g));chonModel.addRow(new Object[]{phong,n,g});}
    private void removeRoom(){int r=dgvChon.getSelectedRow();if(r<0)return;int row=dgvChon.convertRowIndexToModel(r);chon.remove(row);chonModel.removeRow(row);}
    private void createBooking(){ComboItem k=(ComboItem)cboKhach.getSelectedItem(),nv=(ComboItem)cboNV.getSelectedItem();if(k==null||nv==null)return;KetQuaXuLy res=s.taoDatPhong(txtSoPhieu.getText().trim(),k.value(),nv.value(),DateUtil.value(dtLap),DateUtil.value(dtNhan),DateUtil.value(dtTra),((Number)numCoc.getValue()).doubleValue(),String.valueOf(cboKenh.getSelectedItem()),new ArrayList<>(chon));result(res);if(res.thanhCong()){chon.clear();chonModel.setRowCount(0);}}
    private void selectBooking(){int r=dgvPhieu.getSelectedRow();if(r<0)return;int row=dgvPhieu.convertRowIndexToModel(r);String so=Objects.toString(dgvPhieu.getModel().getValueAt(row,col(dgvPhieu.getModel(),"SoPhieuDat")),"");txtPhieuChon.setText(so);try{dgvCT.setModel(s.layChiTiet(so));dgvNguoi.setModel(s.layNguoiLuuTru(so));}catch(Exception e){UiUtil.result(this,KetQuaXuLy.fail(e.getMessage()));}}
    private void result(KetQuaXuLy k){UiUtil.result(this,k);if(k.thanhCong())loadAll();}
    private static int col(javax.swing.table.TableModel m,String n){for(int i=0;i<m.getColumnCount();i++)if(m.getColumnName(i).equalsIgnoreCase(n))return i;return 0;}
    private static void setCombo(JComboBox<ComboItem> c,javax.swing.table.TableModel m,String v,String l){DefaultComboBoxModel<ComboItem> cm=new DefaultComboBoxModel<>();int vi=col(m,v),li=col(m,l);for(int r=0;r<m.getRowCount();r++)cm.addElement(new ComboItem(Objects.toString(m.getValueAt(r,vi),""),Objects.toString(m.getValueAt(r,li),"")));c.setModel(cm);}
    private static JSpinner money(){return new JSpinner(new SpinnerNumberModel(0.0,0.0,2_000_000_000.0,10_000.0));}
}
