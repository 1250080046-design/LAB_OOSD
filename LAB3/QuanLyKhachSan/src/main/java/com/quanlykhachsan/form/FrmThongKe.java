package com.quanlykhachsan.form;

import com.quanlykhachsan.service.ThongKeService;
import com.quanlykhachsan.util.DateUtil;
import com.quanlykhachsan.util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class FrmThongKe extends JFrame {
    private final ThongKeService s=new ThongKeService();
    private final JSpinner dtTu=DateUtil.dateSpinner(),dtDen=DateUtil.dateSpinner();
    private final JTable dgvTongHop=UI.table(),dgvDV=UI.table();
    public FrmThongKe(){
        UiUtil.setupFrame(this,"Thống kê khách sạn",920,610);
        JPanel root=new JPanel(new BorderLayout(5,5)); JPanel top=UI.row();UI.field(top,"Từ ngày:",dtTu,105);UI.field(top,"Đến ngày:",dtDen,105);JButton tk=new JButton("Thống kê");JButton dong=new JButton("Đóng");tk.addActionListener(e->run());dong.addActionListener(e->dispose());top.add(tk);top.add(dong);root.add(top,BorderLayout.NORTH);
        JPanel summary=new JPanel(new BorderLayout(4,4));summary.setBorder(BorderFactory.createTitledBorder("Tổng hợp"));summary.add(UI.scroll(dgvTongHop),BorderLayout.CENTER);JPanel dv=new JPanel(new BorderLayout(4,4));dv.setBorder(BorderFactory.createTitledBorder("Dịch vụ sử dụng"));dv.add(UI.scroll(dgvDV),BorderLayout.CENTER);JSplitPane split=new JSplitPane(JSplitPane.VERTICAL_SPLIT,summary,dv);split.setResizeWeight(0.33);root.add(split,BorderLayout.CENTER);add(root);
    }
    private void run(){java.util.Date tu=DateUtil.value(dtTu),den=DateUtil.value(dtDen);if(den.before(tu)){JOptionPane.showMessageDialog(this,"Đến ngày không được trước từ ngày.");return;}try{dgvTongHop.setModel(s.tongHop(tu,den));dgvDV.setModel(s.dichVu(tu,den));}catch(Exception e){JOptionPane.showMessageDialog(this,e.getMessage(),"Lỗi",JOptionPane.ERROR_MESSAGE);}}
}
