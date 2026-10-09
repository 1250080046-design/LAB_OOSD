package com.tourcompany.forms;

import com.tourcompany.services.ChuyenLeService;
import javax.swing.*;
import java.time.LocalDate;

public class FrmChuyenLe {
    private final ChuyenLeService service=new ChuyenLeService();
    public void open(){
        TablePanel table=new TablePanel(service::danhSach);
        table.addAction("Tạo chuyến",()->{String[] v=Ui.fields(table,"Tạo chuyến khách lẻ",new String[]{"Mã chuyến","Mã tour đang mở bán","Ngày đi (yyyy-MM-dd)","Địa điểm đón"},new String[]{null,"T001",LocalDate.now().plusDays(14).toString(),null});if(v==null)return;LocalDate d=Ui.parseDate(table,v[2],"Ngày đi");if(d==null)return;Ui.showResult(table,service.themChuyen(v[0],v[1],d,v[3]));table.reload();});
        table.addAction("Đóng đăng ký chuyến đã chọn",()->{Object key=table.selected(0);String ma=key==null?Ui.ask(table,"Mã chuyến cần đóng đăng ký",""):String.valueOf(key);if(ma!=null&&Ui.confirm(table,"Đóng đăng ký chuyến "+ma+"?")){Ui.showResult(table,service.dongDangKy(ma));table.reload();}});
        Ui.frame("Lịch chuyến khách lẻ",table).setVisible(true);
    }
}
