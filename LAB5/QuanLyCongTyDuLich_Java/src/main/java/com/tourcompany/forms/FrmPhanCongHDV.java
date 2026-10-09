package com.tourcompany.forms;

import com.tourcompany.services.PhanCongService;
import javax.swing.*;
import java.math.BigDecimal;

public class FrmPhanCongHDV {
    private final PhanCongService service=new PhanCongService();
    public void open(){
        TablePanel table=new TablePanel(service::danhSach);
        table.addAction("Phân công hướng dẫn viên",()->{
            String[] v=Ui.fields(table,"Phân công HDV",new String[]{"Mã phân công (ví dụ PC010)","Mã HDV đang làm việc","Loại đối tượng: LE hoặc DOAN","Mã chuyến (LE) hoặc số phiếu đoàn (DOAN)","Thù lao tour"},new String[]{null,"HDV01","LE","CL002","1000000"});
            if(v==null)return;BigDecimal fee=Ui.parseMoney(table,v[4],"Thù lao tour");if(fee==null)return;Ui.showResult(table,service.phanCong(v[0],v[1],v[2].trim().toUpperCase(),v[3],fee));table.reload();
        });
        table.addAction("Danh sách hướng dẫn viên",()->{try{JTable t=new JTable(service.hdv());JOptionPane.showMessageDialog(table,new JScrollPane(t),"Hướng dẫn viên đang làm việc",JOptionPane.INFORMATION_MESSAGE);}catch(Exception e){Ui.error(table,e.getMessage());}});
        table.addAction("Danh sách chuyến lẻ",()->{try{JTable t=new JTable(service.chuyenLe());JOptionPane.showMessageDialog(table,new JScrollPane(t),"Chuyến khách lẻ",JOptionPane.INFORMATION_MESSAGE);}catch(Exception e){Ui.error(table,e.getMessage());}});
        table.addAction("Danh sách đoàn",()->{try{JTable t=new JTable(service.doan());JOptionPane.showMessageDialog(table,new JScrollPane(t),"Đăng ký đoàn",JOptionPane.INFORMATION_MESSAGE);}catch(Exception e){Ui.error(table,e.getMessage());}});
        Ui.frame("Phân công hướng dẫn viên",table).setVisible(true);
    }
}
