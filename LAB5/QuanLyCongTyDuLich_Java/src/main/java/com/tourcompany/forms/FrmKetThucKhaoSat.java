package com.tourcompany.forms;

import com.tourcompany.services.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FrmKetThucKhaoSat {
    private final KetThucService service=new KetThucService();
    public void open(){
        JTabbedPane tabs=new JTabbedPane();
        TablePanel payments=new TablePanel(service::phieuDoanThanhToan);
        payments.addAction("Ghi nhận thanh toán sau tour",()->{String[] v=Ui.fields(payments,"Thanh toán kinh phí đoàn",new String[]{"Mã thanh toán (ví dụ TT003)","Số phiếu đoàn","Ngày thanh toán (yyyy-MM-dd)","Số tiền","Ghi chú"},new String[]{null,"DD001",LocalDate.now().toString(),"1000000",null});if(v==null)return;LocalDate date=Ui.parseDate(payments,v[2],"Ngày thanh toán");BigDecimal amount=Ui.parseMoney(payments,v[3],"Số tiền");if(date==null||amount==null)return;Ui.showResult(payments,service.thanhToanDoan(v[0],v[1],date,amount,v[4]));payments.reload();});tabs.addTab("Thanh toán đoàn",payments);
        TablePanel surveys=new TablePanel(service::khaoSat);
        surveys.addAction("Gửi phiếu khảo sát",()->{String[] v=Ui.fields(surveys,"Gửi khảo sát",new String[]{"Mã khảo sát (ví dụ KS010)","Loại khách: LE hoặc DOAN","Số phiếu đăng ký","Ngày gửi (yyyy-MM-dd)"},new String[]{null,"LE","DKL001",LocalDate.now().toString()});if(v==null)return;LocalDate date=Ui.parseDate(surveys,v[3],"Ngày gửi");if(date==null)return;Ui.showResult(surveys,service.guiKhaoSat(v[0],v[1].toUpperCase(),v[2],date));surveys.reload();});
        surveys.addAction("Ghi nhận góp ý",()->{Object key=surveys.selected(0);String ma=key==null?Ui.ask(surveys,"Mã khảo sát cần phản hồi",""):String.valueOf(key);if(ma==null)return;String[] v=Ui.fields(surveys,"Ghi nhận phản hồi",new String[]{"Ngày phản hồi (yyyy-MM-dd)","Điểm đánh giá 1-5","Góp ý"},new String[]{LocalDate.now().toString(),"5",""});if(v==null)return;LocalDate date=Ui.parseDate(surveys,v[0],"Ngày phản hồi");int score=Ui.parseInt(surveys,v[1],"Điểm đánh giá");if(date==null||score==Integer.MIN_VALUE)return;Ui.showResult(surveys,service.ghiPhanHoi(ma,date,score,v[2]));surveys.reload();});
        surveys.addAction("Đăng ký chờ khảo sát",()->{String type=Ui.ask(surveys,"Loại khách: LE hoặc DOAN","LE");if(type==null)return;try{JTable t=new JTable(service.dangKyChoKhaoSat(type.toUpperCase()));JOptionPane.showMessageDialog(surveys,new JScrollPane(t),"Đăng ký đã kết thúc tour chưa có khảo sát",JOptionPane.INFORMATION_MESSAGE);}catch(Exception e){Ui.error(surveys,e.getMessage());}});tabs.addTab("Khảo sát & góp ý",surveys);
        Ui.frame("Kết thúc tour - khảo sát",tabs).setVisible(true);
    }
}
