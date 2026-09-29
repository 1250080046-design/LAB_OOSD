package com.quanlykhachsan.form;

import com.quanlykhachsan.model.KetQuaXuLy;
import com.quanlykhachsan.service.DanhMucService;
import com.quanlykhachsan.service.PhongTienNghiService;
import com.quanlykhachsan.util.ComboItem;
import com.quanlykhachsan.util.DateUtil;
import com.quanlykhachsan.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class FrmPhongTienNghi extends JFrame {
    private final PhongTienNghiService s=new PhongTienNghiService();
    private final DanhMucService dm=new DanhMucService();
    private final JTextField txtPhong=new JTextField(),txtMaTN=new JTextField(),txtTinhTrang=new JTextField(),txtSoLD=new JTextField(),txtTTLD=new JTextField(),txtGhiChu=new JTextField();
    private final JComboBox<ComboItem> cboKhu=UI.combo(),cboLoai=UI.combo(),cboTN=UI.combo(),cboPhong=UI.combo(),cboNV=UI.combo();
    private final JSpinner numMax=new JSpinner(new SpinnerNumberModel(1,1,100,1));
    private final JSpinner numGia=new JSpinner(new SpinnerNumberModel(0.0,0.0,2_000_000_000.0,10_000.0));
    private final JSpinner numSTT=new JSpinner(new SpinnerNumberModel(1,1,10000,1));
    private final JSpinner dtNgay=DateUtil.dateSpinner();
    private final JTable dgvPhong=UI.table(),dgvTN=UI.table(),dgvLD=UI.table();

    public FrmPhongTienNghi(){
        UiUtil.setupFrame(this,"Phòng - Tiện nghi - Phiếu lắp đặt",1050,700);
        JTabbedPane tabs=new JTabbedPane(); tabs.addTab("[Phòng]",phongTab()); tabs.addTab("[Tiện nghi]",tnTab()); tabs.addTab("[Lắp đặt / luân chuyển]",ldTab()); add(tabs); loadAll();
    }
    private JPanel phongTab(){JPanel p=new JPanel(new BorderLayout(4,4));JPanel r=UI.row();UI.field(r,"Số phòng:",txtPhong,100);UI.field(r,"Khu vực:",cboKhu,130);UI.field(r,"Số người tối đa:",numMax,80);UI.field(r,"Đơn giá/ngày:",numGia,100);JButton b=new JButton("Thêm");b.addActionListener(e->result(s.themPhong(txtPhong.getText().trim(),value(cboKhu),((Number)numMax.getValue()).intValue(),((Number)numGia.getValue()).doubleValue())));r.add(b);p.add(r,BorderLayout.NORTH);p.add(UI.scroll(dgvPhong),BorderLayout.CENTER);return p;}
    private JPanel tnTab(){JPanel p=new JPanel(new BorderLayout(4,4));JPanel r=UI.row();UI.field(r,"Mã tiện nghi:",txtMaTN,110);UI.field(r,"Loại:",cboLoai,140);UI.field(r,"Số thứ tự:",numSTT,70);UI.field(r,"Tình trạng:",txtTinhTrang,150);JButton b=new JButton("Thêm");b.addActionListener(e->result(s.themTienNghi(txtMaTN.getText().trim(),value(cboLoai),((Number)numSTT.getValue()).intValue(),txtTinhTrang.getText().trim())));r.add(b);p.add(r,BorderLayout.NORTH);p.add(UI.scroll(dgvTN),BorderLayout.CENTER);return p;}
    private JPanel ldTab(){JPanel p=new JPanel(new BorderLayout(4,4));JPanel r=UI.row();UI.field(r,"Phiếu lắp đặt:",txtSoLD,110);UI.field(r,"Tiện nghi:",cboTN,110);UI.field(r,"Phòng:",cboPhong,90);UI.field(r,"Ngày:",dtNgay,105);UI.field(r,"Tình trạng:",txtTTLD,130);UI.field(r,"NV:",cboNV,110);JButton b=new JButton("Lập phiếu");b.addActionListener(e->result(s.lapDat(txtSoLD.getText().trim(),value(cboTN),value(cboPhong),DateUtil.value(dtNgay),txtTTLD.getText().trim(),value(cboNV),txtGhiChu.getText().trim())));r.add(b);p.add(r,BorderLayout.NORTH);p.add(UI.scroll(dgvLD),BorderLayout.CENTER);JPanel note=UI.row();UI.field(note,"Ghi chú:",txtGhiChu,400);p.add(note,BorderLayout.SOUTH);return p;}
    private void loadAll(){try{dgvPhong.setModel(s.layPhong());dgvTN.setModel(s.layTienNghi());dgvLD.setModel(s.layLapDat());setCombo(cboKhu,dm.layKhuVuc(),"MaKhuVuc","TenKhuVuc");setCombo(cboLoai,dm.layLoaiTienNghi(),"MaLoaiTN","TenLoaiTN");setCombo(cboTN,s.layTienNghi(),"MaTienNghi","MaTienNghi");setCombo(cboPhong,s.layPhong(),"SoPhong","SoPhong");setCombo(cboNV,dm.layNhanVien(),"MaNV","HoTen");}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}}
    private void result(KetQuaXuLy k){UiUtil.result(this,k);if(k.thanhCong())loadAll();}
    private static String value(JComboBox<ComboItem> c){ComboItem i=(ComboItem)c.getSelectedItem();return i==null?"":i.value();}
    private static void setCombo(JComboBox<ComboItem> c,javax.swing.table.TableModel m,String v,String l){DefaultComboBoxModel<ComboItem> x=new DefaultComboBoxModel<>();int vi=col(m,v),li=col(m,l);for(int r=0;r<m.getRowCount();r++)x.addElement(new ComboItem(Objects.toString(m.getValueAt(r,vi),""),Objects.toString(m.getValueAt(r,li),"")));c.setModel(x);}
    private static int col(javax.swing.table.TableModel m,String n){for(int i=0;i<m.getColumnCount();i++)if(m.getColumnName(i).equalsIgnoreCase(n))return i;return 0;}
}
