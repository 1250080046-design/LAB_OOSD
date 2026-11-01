package com.caooc.forms;
import com.caooc.service.OfficeService;
public final class FrmDichVu extends FrmModule{
 private final OfficeService service=new OfficeService();
 public FrmDichVu(){super("Dịch vụ và sử dụng","DichVu","SuDungDichVu");action("Chốt hóa đơn dịch vụ cuối tháng",1,()->FormInputs.dialog(this,"Lập hóa đơn dịch vụ cuối tháng",new FormInputs.Spec[]{
  new FormInputs.Spec("Hợp đồng","@MaHD",null),new FormInputs.Spec("Năm","int","2026"),new FormInputs.Spec("Tháng","int","3"),new FormInputs.Spec("Ngày lập","date","2026-03-31")},m->{service.serviceBill(FormInputs.number(m,"Hợp đồng"),FormInputs.number(m,"Năm"),FormInputs.number(m,"Tháng"),FormInputs.date(m,"Ngày lập"));reload();}));}
}
