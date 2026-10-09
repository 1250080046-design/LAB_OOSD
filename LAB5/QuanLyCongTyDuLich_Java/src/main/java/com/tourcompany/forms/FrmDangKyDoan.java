package com.tourcompany.forms;

import com.tourcompany.services.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FrmDangKyDoan {
    private final DangKyDoanService service=new DangKyDoanService();
    public void open(){
        TablePanel table=new TablePanel(service::danhSach);
        table.addAction("Lập phiếu đăng ký đoàn",()->{
            String[] v=Ui.fields(table,"Lập phiếu đăng ký đoàn",new String[]{"Số phiếu (ví dụ DD003)","Mã đoàn khách (ví dụ DK03)","Tên cơ quan / đại diện gia đình","Địa chỉ","Điện thoại","Người đại diện","Mã tour đang mở bán","Ngày đi (yyyy-MM-dd)","Số người (>12)","Địa điểm đón","Mua bảo hiểm? true/false","Tiền cọc"},new String[]{null,null,null,null,null,null,"T001",LocalDate.now().plusDays(30).toString(),"13",null,"false","1000000"});
            if(v==null)return;int count=Ui.parseInt(table,v[8],"Số người");BigDecimal deposit=Ui.parseMoney(table,v[11],"Tiền cọc");LocalDate start=Ui.parseDate(table,v[7],"Ngày đi");if(count==Integer.MIN_VALUE||deposit==null||start==null)return;
            boolean insured=Boolean.parseBoolean(v[10]);List<ThanhVienDoanItem> members=new ArrayList<>();
            if(insured){JTextArea area=new JTextArea(12,44);area.setText("Nguyễn Văn A|2000-01-01|CCCD001\n");int ok=JOptionPane.showConfirmDialog(table,new JScrollPane(area),"Danh sách người đi: mỗi dòng HoTen|yyyy-MM-dd|SoGiayTo (đủ "+count+" dòng)",JOptionPane.OK_CANCEL_OPTION);if(ok!=JOptionPane.OK_OPTION)return;for(String line:area.getText().split("\\R")){if(line.isBlank())continue;String[] parts=line.split("\\|",-1);String name=parts[0].trim();LocalDate dob=null;String id="";try{if(parts.length>1&&!parts[1].isBlank())dob=LocalDate.parse(parts[1].trim());}catch(DateTimeParseException ex){Ui.error(table,"Ngày sinh không hợp lệ ở dòng: "+line);return;}if(parts.length>2)id=parts[2].trim();members.add(new ThanhVienDoanItem(name,dob,id));}}
            Ui.showResult(table,service.dangKy(v[0],v[1],v[2],v[3],v[4],v[5],v[6],start,count,v[9],insured,deposit,members));table.reload();
        });
        table.addAction("Hủy phiếu đoàn - mất cọc",()->{Object selected=table.selected(0);String code=selected==null?Ui.ask(table,"Số phiếu đoàn cần hủy",""):String.valueOf(selected);if(code!=null&&Ui.confirm(table,"Hủy phiếu "+code+"? Đoàn sẽ mất cọc theo quy định.")){Ui.showResult(table,service.huyDangKy(code));table.reload();}});
        table.addAction("Xem thành viên",()->{Object selected=table.selected(0);String code=selected==null?Ui.ask(table,"Số phiếu đoàn","DD001"):String.valueOf(selected);if(code!=null){try{JTable members=new JTable(service.thanhVien(code));JOptionPane.showMessageDialog(table,new JScrollPane(members),"Thành viên đoàn "+code,JOptionPane.INFORMATION_MESSAGE);}catch(Exception ex){Ui.error(table,ex.getMessage());}}});
        Ui.frame("Đăng ký theo đoàn",table).setVisible(true);
    }
}
