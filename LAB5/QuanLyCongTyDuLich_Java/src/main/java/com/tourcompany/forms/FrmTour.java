package com.tourcompany.forms;

import com.tourcompany.services.TourService;
import javax.swing.*;
import java.math.BigDecimal;

public class FrmTour {
    private final TourService service=new TourService();
    public void open(){
        JTabbedPane tabs=new JTabbedPane();
        TablePanel tours=new TablePanel(service::tours);
        tours.addAction("Thêm tour",()->{String[] v=Ui.fields(tours,"Thêm tour",new String[]{"Mã tour","Tên tour","Số ngày","Số đêm","Đơn giá/khách","Mô tả"},new String[]{null,null,"1","0","0",null});if(v==null)return;int days=Ui.parseInt(tours,v[2],"Số ngày"),nights=Ui.parseInt(tours,v[3],"Số đêm");BigDecimal price=Ui.parseMoney(tours,v[4],"Đơn giá");if(days==Integer.MIN_VALUE||nights==Integer.MIN_VALUE||price==null)return;Ui.showResult(tours,service.themTour(v[0],v[1],days,nights,price,v[5]));tours.reload();});tabs.addTab("Tour",tours);
        TablePanel stops=new TablePanel(()->service.diemDung("T001"));
        stops.addAction("Xem điểm dừng theo tour",()->{String tour=Ui.ask(stops,"Mã tour","T001");if(tour!=null)try{stops.table().setModel(service.diemDung(tour));}catch(Exception ex){Ui.error(stops,ex.getMessage());}});
        stops.addAction("Thêm điểm dừng",()->{String[] v=Ui.fields(stops,"Thêm điểm dừng",new String[]{"Mã tour","Thứ tự","Tên điểm dừng","Đổi phương tiện (true/false)","Có nơi ăn (true/false)","Có khách sạn (true/false)","Hạng sao 2-5 (bỏ trống nếu không KS)","Ghi chú"},new String[]{null,"1",null,"false","false","false","",null});if(v==null)return;int order=Ui.parseInt(stops,v[1],"Thứ tự");if(order==Integer.MIN_VALUE)return;boolean hasHotel=Boolean.parseBoolean(v[5]);Integer stars=null;if(hasHotel){int s=Ui.parseInt(stops,v[6],"Hạng sao");if(s==Integer.MIN_VALUE)return;stars=s;}Ui.showResult(stops,service.themDiemDung(v[0],order,v[2],Boolean.parseBoolean(v[3]),Boolean.parseBoolean(v[4]),hasHotel,stars,v[7]));stops.reload();});tabs.addTab("Điểm dừng",stops);
        TablePanel route=new TablePanel(()->service.chang("T001"));route.addAction("Xem chặng theo tour",()->{String tour=Ui.ask(route,"Mã tour","T001");if(tour!=null)try{route.table().setModel(service.chang(tour));}catch(Exception ex){Ui.error(route,ex.getMessage());}});
        route.addAction("Gắn phương tiện cho chặng",()->{String[] v=Ui.fields(route,"Gắn phương tiện",new String[]{"Mã tour","Thứ tự chặng","Mã phương tiện","Ghi chú"},new String[]{null,"1",null,null});if(v==null)return;int order=Ui.parseInt(route,v[1],"Thứ tự chặng");if(order==Integer.MIN_VALUE)return;Ui.showResult(route,service.themChang(v[0],order,v[2],v[3]));route.reload();});tabs.addTab("Phương tiện theo chặng",route);
        TablePanel visits=new TablePanel(()->service.diemTQTour("T001"));visits.addAction("Xem điểm tham quan theo tour",()->{String tour=Ui.ask(visits,"Mã tour","T001");if(tour!=null)try{visits.table().setModel(service.diemTQTour(tour));}catch(Exception ex){Ui.error(visits,ex.getMessage());}});
        visits.addAction("Gắn điểm tham quan",()->{String[] v=Ui.fields(visits,"Gắn điểm tham quan cho tour",new String[]{"Mã tour","Mã điểm tham quan","Thứ tự"},new String[]{null,null,"1"});if(v==null)return;int order=Ui.parseInt(visits,v[2],"Thứ tự");if(order==Integer.MIN_VALUE)return;Ui.showResult(visits,service.themDiemTQ(v[0],v[1],order));visits.reload();});tabs.addTab("Điểm tham quan",visits);
        Ui.frame("Tour - hành trình",tabs).setVisible(true);
    }
}
