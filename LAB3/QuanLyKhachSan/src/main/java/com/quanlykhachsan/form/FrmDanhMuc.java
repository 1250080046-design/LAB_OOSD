package com.quanlykhachsan.form;

import com.quanlykhachsan.model.KetQuaXuLy;
import com.quanlykhachsan.service.DanhMucService;
import com.quanlykhachsan.util.ComboItem;
import com.quanlykhachsan.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Objects;

public class FrmDanhMuc extends JFrame {
    private final DanhMucService s = new DanhMucService();

    private final JTextField txtKhuMa = new JTextField(), txtKhuTen = new JTextField();
    private final JTextField txtNVMa = new JTextField(), txtNVTen = new JTextField(), txtNVVaiTro = new JTextField(), txtNVSDT = new JTextField();
    private final JTextField txtLoaiMa = new JTextField(), txtLoaiTen = new JTextField();
    private final JTextField txtDVMa = new JTextField(), txtDVTen = new JTextField(), txtDVDVT = new JTextField();
    private final JSpinner numDVGia = money();
    private final JTextField txtQDMa = new JTextField(), txtQDMucDo = new JTextField();
    private final JSpinner numQDTien = money();
    private final JComboBox<ComboItem> cboQDLoai = UI.combo();
    private final JTable dgvKhu=UI.table(), dgvNV=UI.table(), dgvLoaiTN=UI.table(), dgvDV=UI.table(), dgvQD=UI.table();

    public FrmDanhMuc() {
        UiUtil.setupFrame(this, "Danh mục khách sạn", 1000, 680);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("[Khu vực]", simpleTab(txtKhuMa, txtKhuTen, "Mã:", "Tên:", "Thêm", e -> result(s.themKhu(txtKhuMa.getText().trim(), txtKhuTen.getText().trim())), dgvKhu));
        tabs.addTab("[Nhân viên]", nvTab());
        tabs.addTab("[Loại tiện nghi]", simpleTab(txtLoaiMa, txtLoaiTen, "Mã:", "Tên:", "Thêm", e -> result(s.themLoaiTN(txtLoaiMa.getText().trim(), txtLoaiTen.getText().trim())), dgvLoaiTN));
        tabs.addTab("[Dịch vụ]", dvTab());
        tabs.addTab("[Quy định đền bù]", qdTab());
        add(tabs);
        loadAll();
    }

    private JPanel simpleTab(JTextField ma, JTextField ten, String lm, String lt, String bt, java.awt.event.ActionListener action, JTable table) {
        JPanel root = new JPanel(new BorderLayout(4, 4));
        JPanel top = UI.row(); UI.field(top, lm, ma, 140); UI.field(top, lt, ten, 240);
        JButton b = new JButton(bt); b.addActionListener(action); top.add(b);
        root.add(top, BorderLayout.NORTH);
        root.add(UI.scroll(table), BorderLayout.CENTER);
        return root;
    }

    private JPanel nvTab() {
        JPanel root = new JPanel(new BorderLayout(4,4)); JPanel top=UI.row();
        UI.field(top,"Mã:",txtNVMa,120); UI.field(top,"Tên:",txtNVTen,190); UI.field(top,"Vai trò:",txtNVVaiTro,150); UI.field(top,"SĐT:",txtNVSDT,120);
        JButton b=new JButton("Thêm"); b.addActionListener(e->result(s.themNhanVien(txtNVMa.getText().trim(),txtNVTen.getText().trim(),txtNVVaiTro.getText().trim(),txtNVSDT.getText().trim()))); top.add(b);
        root.add(top,BorderLayout.NORTH); root.add(UI.scroll(dgvNV),BorderLayout.CENTER); return root;
    }

    private JPanel dvTab() {
        JPanel root=new JPanel(new BorderLayout(4,4)); JPanel top=UI.row(); UI.field(top,"Mã:",txtDVMa,100);UI.field(top,"Tên:",txtDVTen,180);UI.field(top,"Đơn vị:",txtDVDVT,100);UI.field(top,"Đơn giá:",numDVGia,100);
        JButton b=new JButton("Thêm");b.addActionListener(e->result(s.themDichVu(txtDVMa.getText().trim(),txtDVTen.getText().trim(),txtDVDVT.getText().trim(),((Number)numDVGia.getValue()).doubleValue())));top.add(b);
        root.add(top,BorderLayout.NORTH);root.add(UI.scroll(dgvDV),BorderLayout.CENTER);return root;
    }

    private JPanel qdTab(){
        JPanel root=new JPanel(new BorderLayout(4,4)); JPanel top=UI.row();UI.field(top,"Mã:",txtQDMa,100);UI.field(top,"Loại:",cboQDLoai,150);UI.field(top,"Mức độ:",txtQDMucDo,180);UI.field(top,"Mức đền bù:",numQDTien,100);
        JButton b=new JButton("Thêm");b.addActionListener(e->{ComboItem x=(ComboItem)cboQDLoai.getSelectedItem();result(s.themQuyDinh(txtQDMa.getText().trim(),x==null?"":x.value(),txtQDMucDo.getText().trim(),((Number)numQDTien.getValue()).doubleValue()));});top.add(b);
        root.add(top,BorderLayout.NORTH);root.add(UI.scroll(dgvQD),BorderLayout.CENTER);return root;
    }

    private void loadAll(){
        try {
            dgvKhu.setModel(s.layKhuVuc());
            dgvNV.setModel(s.layNhanVien());
            dgvLoaiTN.setModel(s.layLoaiTienNghi());
            dgvDV.setModel(s.layDichVu());
            dgvQD.setModel(s.layQuyDinhDenBu());
            DefaultTableModel m=s.layLoaiTienNghi();
            DefaultComboBoxModel<ComboItem> cm=new DefaultComboBoxModel<>();
            for(int r=0;r<m.getRowCount();r++) cm.addElement(new ComboItem(Objects.toString(m.getValueAt(r,0),""),Objects.toString(m.getValueAt(r,1),"")));
            cboQDLoai.setModel(cm);
        } catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void result(KetQuaXuLy k){UiUtil.result(this,k);if(k.thanhCong())loadAll();}
    private static JSpinner money(){return new JSpinner(new SpinnerNumberModel(0.0,0.0,2_000_000_000.0,10_000.0));}
}
