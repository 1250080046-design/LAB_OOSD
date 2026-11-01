package com.caooc.forms;
import com.caooc.service.OfficeService;
public final class FrmGiaHan extends FrmModule{
 private final OfficeService service=new OfficeService();
 public FrmGiaHan(){super("Gia hạn hợp đồng","GiaHan");action("Lập phiếu gia hạn",0,()->FormInputs.dialog(this,"Gia hạn - không còn nợ tiền thuê và dịch vụ",new FormInputs.Spec[]{
  new FormInputs.Spec("Hợp đồng","@MaHD",null),new FormInputs.Spec("Văn phòng","@MaPhong",null),new FormInputs.Spec("Số tháng gia hạn","int","3"),new FormInputs.Spec("Ngày lập","date","2026-10-30")},m->{service.extend(FormInputs.number(m,"Hợp đồng"),FormInputs.number(m,"Văn phòng"),FormInputs.number(m,"Số tháng gia hạn"),FormInputs.date(m,"Ngày lập"));reload();}));}
}
