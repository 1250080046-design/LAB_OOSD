package com.caooc.forms;
import com.caooc.service.OfficeService;
public final class FrmHopDong extends FrmModule{
 private final OfficeService service=new OfficeService();
 public FrmHopDong(){super("Hợp đồng thuê","HopDong","ChiTietHopDong");action("Ký hợp đồng và thu trước 6 tháng",0,()->FormInputs.dialog(this,"Ký hợp đồng (danh sách phòng nhập mã cách nhau bởi dấu phẩy)",new FormInputs.Spec[]{
  new FormInputs.Spec("Số hợp đồng","text","HD-NEW"),new FormInputs.Spec("Khách hàng","@MaKH",null),new FormInputs.Spec("Ngày ký","date","2026-10-30"),new FormInputs.Spec("Ngày bắt đầu","date","2026-11-01"),new FormInputs.Spec("Số tháng","int","12"),new FormInputs.Spec("Danh sách phòng (Ctrl để chọn nhiều)","@rooms",null),new FormInputs.Spec("Hình thức thanh toán","method","CHUYEN_KHOAN")},m->{int id=service.createContract(m.get("Số hợp đồng"),FormInputs.number(m,"Khách hàng"),FormInputs.date(m,"Ngày ký"),FormInputs.date(m,"Ngày bắt đầu"),FormInputs.number(m,"Số tháng"),m.get("Danh sách phòng (Ctrl để chọn nhiều)"),m.get("Hình thức thanh toán"));reload();}));}
}
