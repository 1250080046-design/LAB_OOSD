package com.quanlykhachsan.form;

import com.quanlykhachsan.model.KetQuaXuLy;
import com.quanlykhachsan.service.DanhMucService;
import com.quanlykhachsan.service.DichVuService;
import com.quanlykhachsan.util.ComboItem;
import com.quanlykhachsan.util.DateUtil;
import com.quanlykhachsan.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class FrmDichVu extends JFrame {
    private final DichVuService s=new DichVuService(); private final DanhMucService dm=new DanhMucService();
    private final JComboBox<ComboItem> cboLuot=UI.combo(),cboDV=UI.combo(),cboNV=UI.combo();
    private final JTextField txtPhong=new JTextField(); private final JSpinner dtNgay=DateUtil.dateSpinner(); private final JSpinner numSL=new JSpinner(new SpinnerNumberModel(1,1,10000,1)); private final JTable dgvLichSu=UI.table();
    public FrmDichVu(){UiUtil.setupFrame(this,"Sử dụng dịch vụ",1000,610);JPanel root=new JPanel(new BorderLayout(4,4));JPanel r=UI.row();UI.field(r,"Phiếu lưu trú:",cboLuot,180);UI.field(r,"Phòng:",txtPhong,75);UI.field(r,"Dịch vụ:",cboDV,170);UI.field(r,"Ngày sử dụng:",dtNgay,105);UI.field(r,"Số lượng:",numSL,60);UI.field(r,"Nhân viên:",cboNV,130);JButton ghi=new JButton("Ghi nhận");ghi.addActionListener(e->ghi());r.add(ghi);root.add(r,BorderLayout.NORTH);JPanel card=UI.card();card.setBorder(BorderFactory.createTitledBorder("Lịch sử sử dụng dịch vụ"));card.add(UI.scroll(dgvLichSu));root.add(card,BorderLayout.CENTER);JButton dong=new JButton("Đóng");dong.addActionListener(e->dispose());root.add(UI.buttonRow(dong),BorderLayout.SOUTH);add(root);cboLuot.addActionListener(e->selectionChanged());loadAll();}
    private void loadAll(){try{DefaultTableModel m=s.layPhieuDangO();DefaultComboBoxModel<ComboItem> cm=new DefaultComboBoxModel<>();for(int r=0;r<m.getRowCount();r++){String so=Objects.toString(m.getValueAt(r,col(m,"SoPhieuDat")),"");String phong=Objects.toString(m.getValueAt(r,col(m,"SoPhong")),"");String ten=Objects.toString(m.getValueAt(r,col(m,"HoTen")),"");cm.addElement(new ComboItem(so+"|"+phong,so+" - "+ten+" - phòng "+phong));}cboLuot.setModel(cm);setCombo(cboDV,s.layDichVu(),"MaDV","TenDV");setCombo(cboNV,dm.layNhanVien(),"MaNV","HoTen");selectionChanged();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}}
    private void selectionChanged(){ComboItem it=(ComboItem)cboLuot.getSelectedItem();if(it==null){txtPhong.setText("");dgvLichSu.setModel(new DefaultTableModel());return;}String[] a=it.value().split("\\|",2);txtPhong.setText(a.length>1?a[1]:"");try{dgvLichSu.setModel(s.layLichSu(a[0]));}catch(Exception e){UiUtil.result(this,KetQuaXuLy.fail(e.getMessage()));}}
    private void ghi(){ComboItem l=(ComboItem)cboLuot.getSelectedItem(),dv=(ComboItem)cboDV.getSelectedItem(),nv=(ComboItem)cboNV.getSelectedItem();if(l==null||dv==null||nv==null)return;String[] a=l.value().split("\\|",2);result(s.ghiNhan(a[0],txtPhong.getText().trim(),DateUtil.value(dtNgay),nv.value(),dv.value(),((Number)numSL.getValue()).intValue()));}
    private void result(KetQuaXuLy k){UiUtil.result(this,k);if(k.thanhCong())loadAll();}
    private static int col(javax.swing.table.TableModel m,String n){for(int i=0;i<m.getColumnCount();i++)if(m.getColumnName(i).equalsIgnoreCase(n))return i;return 0;}
    private static void setCombo(JComboBox<ComboItem> c,javax.swing.table.TableModel m,String v,String l){DefaultComboBoxModel<ComboItem> cm=new DefaultComboBoxModel<>();int vi=col(m,v),li=col(m,l);for(int r=0;r<m.getRowCount();r++)cm.addElement(new ComboItem(Objects.toString(m.getValueAt(r,vi),""),Objects.toString(m.getValueAt(r,li),"")));c.setModel(cm);}
}
