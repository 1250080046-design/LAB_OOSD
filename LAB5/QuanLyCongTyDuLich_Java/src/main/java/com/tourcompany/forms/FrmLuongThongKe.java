package com.tourcompany.forms;

import com.tourcompany.services.ThongKeService;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FrmLuongThongKe {
    private final ThongKeService service=new ThongKeService();
    public void open(){
        JTabbedPane tabs=new JTabbedPane();
        JPanel payroll=new JPanel(new BorderLayout(8,8));TablePanel p=new TablePanel(()->service.luongHDV(LocalDate.now().getMonthValue(),LocalDate.now().getYear()));
        JPanel controls=new JPanel(new FlowLayout(FlowLayout.LEFT));JTextField month=new JTextField(String.valueOf(LocalDate.now().getMonthValue()),4),year=new JTextField(String.valueOf(LocalDate.now().getYear()),6);JButton calc=new JButton("Tính lương");controls.add(new JLabel("Tháng"));controls.add(month);controls.add(new JLabel("Năm"));controls.add(year);controls.add(calc);payroll.add(controls,BorderLayout.NORTH);payroll.add(p,BorderLayout.CENTER);calc.addActionListener(e->{int m=Ui.parseInt(payroll,month.getText(),"Tháng"),y=Ui.parseInt(payroll,year.getText(),"Năm");if(m<1||m>12||y<1){Ui.error(payroll,"Tháng phải từ 1 đến 12 và năm hợp lệ.");return;}try{p.table().setModel(service.luongHDV(m,y));}catch(Exception ex){Ui.error(payroll,ex.getMessage());}});tabs.addTab("Lương hướng dẫn viên",payroll);
        JPanel summary=new JPanel(new BorderLayout(8,8));TablePanel s=new TablePanel(()->service.tongHop(LocalDate.now().withDayOfMonth(1),LocalDate.now()));
        JPanel dates=new JPanel(new FlowLayout(FlowLayout.LEFT));JTextField from=new JTextField(LocalDate.now().withDayOfMonth(1).toString(),10),to=new JTextField(LocalDate.now().toString(),10);JButton view=new JButton("Thống kê");dates.add(new JLabel("Từ ngày"));dates.add(from);dates.add(new JLabel("Đến ngày"));dates.add(to);dates.add(view);summary.add(dates,BorderLayout.NORTH);summary.add(s,BorderLayout.CENTER);view.addActionListener(e->{LocalDate a=Ui.parseDate(summary,from.getText(),"Từ ngày"),b=Ui.parseDate(summary,to.getText(),"Đến ngày");if(a==null||b==null)return;if(b.isBefore(a)){Ui.error(summary,"Ngày kết thúc không được trước ngày bắt đầu.");return;}try{s.table().setModel(service.tongHop(a,b));}catch(Exception ex){Ui.error(summary,ex.getMessage());}});tabs.addTab("Thống kê tổng hợp",summary);
        Ui.frame("Lương - thống kê",tabs).setVisible(true);
    }
}
