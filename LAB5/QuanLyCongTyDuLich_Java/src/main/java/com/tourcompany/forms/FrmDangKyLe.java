package com.tourcompany.forms;

import com.tourcompany.services.DangKyLeService;
import javax.swing.*;

public class FrmDangKyLe {
    private final DangKyLeService service=new DangKyLeService();
    public void open(){
        TablePanel table=new TablePanel(service::danhSach);
        table.addAction("Lập phiếu đăng ký khách lẻ + thu tiền",()->{
            String[] v=Ui.fields(table,"Đăng ký khách lẻ",new String[]{"Số phiếu (ví dụ DKL003)","Mã chuyến đang mở","Mã điểm bán vé","Tên người đăng ký","Điện thoại","Số người (1-11)"},new String[]{null,"CL002","DB01",null,null,"1"});
            if(v==null)return;int n=Ui.parseInt(table,v[5],"Số người");if(n==Integer.MIN_VALUE)return;
            Ui.showResult(table,service.dangKy(v[0],v[1],v[2],v[3],v[4],n));table.reload();
        });
        Ui.frame("Đăng ký khách lẻ",table).setVisible(true);
    }
}
