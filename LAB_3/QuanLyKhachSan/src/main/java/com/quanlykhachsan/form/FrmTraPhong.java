package com.quanlykhachsan.form;

import com.quanlykhachsan.model.DenBuItem;
import com.quanlykhachsan.model.KetQuaXuLy;
import com.quanlykhachsan.service.DanhMucService;
import com.quanlykhachsan.service.TraPhongService;
import com.quanlykhachsan.util.ComboItem;
import com.quanlykhachsan.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FrmTraPhong extends JFrame {
    private final TraPhongService s=new TraPhongService();
    private final DanhMucService dm=new DanhMucService();
    private final JComboBox<ComboItem> cboDat=UI.combo(),cboNV=UI.combo(),cboNV2=UI.combo();
    private final JComboBox<String> cboHT=new JComboBox<>(new String[]{"Tiền mặt","Chuyển khoản","Thẻ","Ví điện tử"});
    private final JTextField txtPhong=new JTextField(),txtSoDB=new JTextField(),txtMucDo=new JTextField(),txtSoHD=new JTextField(),txtHDChon=new JTextField(),txtMaTT=new JTextField();
    private final JSpinner numDenBu=money(),numSoNgay=new JSpinner(new SpinnerNumberModel(1,1,1000,1)),numTienTT=money();
    private final JTable dgvPhong=UI.table(),dgvTN=UI.table(),dgvDBChon=UI.table(),dgvHD=UI.table();
    private final DefaultTableModel dbModel=new DefaultTableModel(new Object[]{"Tiện nghi đền bù","Loại","Mức độ","Số tiền"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final List<DenBuItem> db=new ArrayList<>();

    public FrmTraPhong(){
        UiUtil.setupFrame(this,"Trả phòng - Đền bù - Hóa đơn - Thanh toán",1220,820);
        JPanel root=new JPanel(new BorderLayout(4,4));
        JPanel top=UI.row(); UI.field(top,"Phiếu đang ở:",cboDat,180); UI.field(top,"Phòng:",txtPhong,80); txtPhong.setEditable(false); root.add(top,BorderLayout.NORTH);

        JPanel center=new JPanel(); center.setLayout(new BoxLayout(center,BoxLayout.Y_AXIS));
        JPanel three=new JPanel(new GridLayout(1,3,6,0));
        three.add(panel("Phòng",dgvPhong)); three.add(panel("Tiện nghi",dgvTN)); dgvDBChon.setModel(dbModel); three.add(panel("Tiện nghi đền bù",dgvDBChon));
        three.setPreferredSize(new Dimension(1180,190)); three.setMaximumSize(new Dimension(Integer.MAX_VALUE,190)); center.add(three);

        JPanel denbu=UI.row(); UI.field(denbu,"Số phiếu đền bù:",txtSoDB,105); UI.field(denbu,"Mức độ:",txtMucDo,130); UI.field(denbu,"Số tiền:",numDenBu,90); UI.field(denbu,"NV:",cboNV,130); JButton add=new JButton("Thêm đền bù"); JButton lapDB=new JButton("Lập phiếu đền bù"); add.addActionListener(e->addDB()); lapDB.addActionListener(e->lapDB()); denbu.add(add); denbu.add(lapDB); center.add(denbu);

        JPanel hdHead=UI.row(); UI.field(hdHead,"Số hóa đơn:",txtSoHD,105); UI.field(hdHead,"NV lập HĐ:",cboNV2,135); UI.field(hdHead,"Số ngày tính tiền:",numSoNgay,70); JButton lapHD=new JButton("Lập hóa đơn"); lapHD.addActionListener(e->lapHD()); hdHead.add(lapHD); center.add(hdHead);

        JPanel hdWrap=panel("Danh sách hóa đơn",dgvHD); hdWrap.setPreferredSize(new Dimension(1180,185)); hdWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE,185)); center.add(hdWrap);

        JPanel pay=UI.row(); UI.field(pay,"Hóa đơn:",txtHDChon,110); txtHDChon.setEditable(false); UI.field(pay,"Mã thanh toán:",txtMaTT,110); UI.field(pay,"Hình thức:",cboHT,120); UI.field(pay,"Số tiền:",numTienTT,100); JButton thanh=new JButton("Thanh toán"); JButton tra=new JButton("Hoàn tất trả phòng"); thanh.addActionListener(e->pay()); tra.addActionListener(e->tra()); pay.add(thanh); pay.add(tra); center.add(pay);

        root.add(new JScrollPane(center),BorderLayout.CENTER);
        add(root);
        cboDat.addActionListener(e->loadRooms());
        dgvPhong.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())loadEquipment();});
        dgvHD.getSelectionModel().addListSelectionListener(e->{if(!e.getValueIsAdjusting())selectHD();});
        loadAll();
    }

    private JPanel panel(String title,JTable table){JPanel p=new JPanel(new BorderLayout(2,2));p.setBorder(BorderFactory.createTitledBorder(title));p.add(UI.scroll(table));return p;}
    private void loadAll(){try{DefaultTableModel m=s.layPhieuDangO();setCombo(cboDat,m,"SoPhieuDat","HoTen");setCombo(cboNV,dm.layNhanVien(),"MaNV","HoTen");setCombo(cboNV2,dm.layNhanVien(),"MaNV","HoTen");dgvHD.setModel(s.layHoaDon());loadRooms();}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}}
    private void loadRooms(){ComboItem i=(ComboItem)cboDat.getSelectedItem();if(i==null){txtPhong.setText("");return;}try{dgvPhong.setModel(s.layPhongTheoPhieu(i.value()));}catch(Exception e){UiUtil.result(this,KetQuaXuLy.fail(e.getMessage()));}}
    private void loadEquipment(){int r=dgvPhong.getSelectedRow();if(r<0)return;int row=dgvPhong.convertRowIndexToModel(r);String phong=Objects.toString(dgvPhong.getModel().getValueAt(row,col(dgvPhong.getModel(),"SoPhong")),"");txtPhong.setText(phong);try{dgvTN.setModel(s.layTienNghiPhong(phong));}catch(Exception e){UiUtil.result(this,KetQuaXuLy.fail(e.getMessage()));}}
    private void addDB(){int r=dgvTN.getSelectedRow();if(r<0)return;int row=dgvTN.convertRowIndexToModel(r);String ma=Objects.toString(dgvTN.getModel().getValueAt(row,col(dgvTN.getModel(),"MaTienNghi")),"");String loai=Objects.toString(dgvTN.getModel().getValueAt(row,col(dgvTN.getModel(),"TenLoaiTN")),"");for(DenBuItem x:db)if(x.maTienNghi().equals(ma)){JOptionPane.showMessageDialog(this,"Tiện nghi đã có trong phiếu đền bù.");return;}DenBuItem x=new DenBuItem(ma,loai,txtMucDo.getText().trim(),BigDecimal.valueOf(((Number)numDenBu.getValue()).doubleValue()));db.add(x);dbModel.addRow(new Object[]{x.maTienNghi(),x.tenLoaiTN(),x.mucDoThietHai(),x.soTien()});}
    private void lapDB(){ComboItem dat=(ComboItem)cboDat.getSelectedItem(),nv=(ComboItem)cboNV.getSelectedItem();if(dat==null||nv==null)return;result(s.lapPhieuDenBu(txtSoDB.getText().trim(),dat.value(),txtPhong.getText().trim(),new java.util.Date(),nv.value(),new ArrayList<>(db)));}
    private void lapHD(){ComboItem dat=(ComboItem)cboDat.getSelectedItem(),nv=(ComboItem)cboNV2.getSelectedItem();if(dat==null||nv==null)return;result(s.lapHoaDon(txtSoHD.getText().trim(),dat.value(),new java.util.Date(),nv.value(),((Number)numSoNgay.getValue()).intValue()));}
    private void selectHD(){int r=dgvHD.getSelectedRow();if(r<0)return;int row=dgvHD.convertRowIndexToModel(r);txtHDChon.setText(Objects.toString(dgvHD.getModel().getValueAt(row,col(dgvHD.getModel(),"SoHoaDon")),""));}
    private void pay(){result(s.thanhToan(txtMaTT.getText().trim(),txtHDChon.getText().trim(),new java.util.Date(),String.valueOf(cboHT.getSelectedItem()),((Number)numTienTT.getValue()).doubleValue()));}
    private void tra(){ComboItem dat=(ComboItem)cboDat.getSelectedItem();if(dat!=null)result(s.traPhong(dat.value(),new java.util.Date()));}
    private void result(KetQuaXuLy k){UiUtil.result(this,k);if(k.thanhCong()){db.clear();dbModel.setRowCount(0);loadAll();}}
    private static JSpinner money(){return new JSpinner(new SpinnerNumberModel(0.0,0.0,2_000_000_000.0,10_000.0));}
    private static int col(javax.swing.table.TableModel m,String n){for(int i=0;i<m.getColumnCount();i++)if(m.getColumnName(i).equalsIgnoreCase(n))return i;return 0;}
    private static void setCombo(JComboBox<ComboItem> c,javax.swing.table.TableModel m,String v,String l){DefaultComboBoxModel<ComboItem> cm=new DefaultComboBoxModel<>();int vi=col(m,v),li=col(m,l);for(int r=0;r<m.getRowCount();r++)cm.addElement(new ComboItem(Objects.toString(m.getValueAt(r,vi),""),Objects.toString(m.getValueAt(r,li),"")));c.setModel(cm);}
}
