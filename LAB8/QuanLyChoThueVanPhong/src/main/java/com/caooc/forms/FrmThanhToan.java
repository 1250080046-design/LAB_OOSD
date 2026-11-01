package com.caooc.forms;
import com.caooc.service.OfficeService;
public final class FrmThanhToan extends FrmModule{
 private final OfficeService service=new OfficeService();
 public FrmThanhToan(){super("Hóa đơn và thanh toán","HoaDon","ThanhToan");
  action("Lập tiền thuê tháng 7+",0,()->FormInputs.dialog(this,"Lập hóa đơn thuê tháng 7 trở đi",new FormInputs.Spec[]{new FormInputs.Spec("Hợp đồng","@MaHD",null),new FormInputs.Spec("Thứ tự tháng thuê","int","7"),new FormInputs.Spec("Ngày lập","date","2026-07-01")},m->{service.rentBill(FormInputs.number(m,"Hợp đồng"),FormInputs.number(m,"Thứ tự tháng thuê"),FormInputs.date(m,"Ngày lập"));reload();}));
  action("Thu tiền hóa đơn",1,()->FormInputs.dialog(this,"Thu tiền thuê/dịch vụ",new FormInputs.Spec[]{new FormInputs.Spec("Hóa đơn","@MaHoaDon",null),new FormInputs.Spec("Số tiền","decimal","1000000"),new FormInputs.Spec("Ngày thu","date","2026-10-30"),new FormInputs.Spec("Phương thức","method","CHUYEN_KHOAN"),new FormInputs.Spec("Mã tham chiếu","text","")},m->{service.payment(FormInputs.number(m,"Hóa đơn"),FormInputs.decimal(m,"Số tiền"),FormInputs.date(m,"Ngày thu"),m.get("Phương thức"),m.get("Mã tham chiếu"));reload();}));
 }
}
