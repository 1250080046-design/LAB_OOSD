package com.tourcompany.forms;

import com.tourcompany.services.DanhMucService;
import javax.swing.*;
import java.math.BigDecimal;

public class FrmDanhMuc {
    private final DanhMucService service=new DanhMucService();
    public void open(){
        JTabbedPane tabs=new JTabbedPane();
        TablePanel pt=new TablePanel(service::phuongTien);pt.addAction("Thêm phương tiện",()->{String[] v=Ui.fields(pt,"Thêm phương tiện",new String[]{"Mã phương tiện","Tên phương tiện","Ghi chú"},null);if(v!=null){Ui.showResult(pt,service.themPhuongTien(v[0],v[1],v[2]));pt.reload();}});tabs.addTab("Phương tiện",pt);
        TablePanel db=new TablePanel(service::diemBanVe);db.addAction("Thêm điểm bán vé",()->{String[] v=Ui.fields(db,"Thêm điểm bán vé",new String[]{"Mã điểm bán","Tên điểm bán","Địa chỉ","Điện thoại"},null);if(v!=null){Ui.showResult(db,service.themDiemBan(v[0],v[1],v[2],v[3]));db.reload();}});tabs.addTab("Điểm bán vé",db);
        TablePanel hdv=new TablePanel(service::huongDanVien);hdv.addAction("Thêm hướng dẫn viên",()->{String[] v=Ui.fields(hdv,"Thêm hướng dẫn viên",new String[]{"Mã HDV","Họ tên","Điện thoại","Lương cơ bản"},null);if(v!=null){BigDecimal money=Ui.parseMoney(hdv,v[3],"Lương cơ bản");if(money!=null){Ui.showResult(hdv,service.themHDV(v[0],v[1],v[2],money));hdv.reload();}}});tabs.addTab("Hướng dẫn viên",hdv);
        TablePanel dtq=new TablePanel(service::diemThamQuan);dtq.addAction("Thêm điểm tham quan",()->{String[] v=Ui.fields(dtq,"Thêm điểm tham quan",new String[]{"Mã điểm tham quan","Tên điểm tham quan","Địa điểm","Nội dung","Ý nghĩa"},null);if(v!=null){Ui.showResult(dtq,service.themDiemTQ(v[0],v[1],v[2],v[3],v[4]));dtq.reload();}});tabs.addTab("Điểm tham quan",dtq);
        Ui.frame("Danh mục",tabs).setVisible(true);
    }
}
