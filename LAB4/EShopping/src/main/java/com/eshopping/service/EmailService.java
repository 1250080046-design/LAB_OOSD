package com.eshopping.service;

import com.eshopping.adapter.EmailAdapter;
import com.eshopping.adapter.SmtpEmailAdapter;

public class EmailService {
    private final EmailAdapter adapter;

    public EmailService() {
        this(new SmtpEmailAdapter());
    }

    public EmailService(EmailAdapter adapter) {
        this.adapter = adapter;
    }

    public boolean guiEmailXacNhan(String email, String soDon, String tenNguoiNhan,
            String tongTien) {
        if (email == null || email.isBlank())
            return false;
        String noiDung = "Xin chào,\n\nĐơn hàng " + soDon + " đã được đặt thành công.\n" +
                "Người nhận: " + tenNguoiNhan + "\n" +
                "Tổng thanh toán: " + tongTien + "\n\n" +
                "Email này không chứa thông tin thẻ tín dụng.\n\n" +
                "e-SHOPPING";
        return adapter.guiEmail(email, "Xác nhận đơn hàng " + soDon, noiDung);
    }
}
