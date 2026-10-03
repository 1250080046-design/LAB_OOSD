package com.eshopping.adapter;

import com.eshopping.model.PaymentInfo;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

/** Prototype: mô phỏng dịch vụ thanh toán trực tuyến bên ngoài. */
public class OnlinePaymentAdapter implements PaymentAdapter {
    @Override
    public PaymentResult kiemTraThe(PaymentInfo p) {
        if (p == null)
            return new PaymentResult(false, null, "Thiếu thông tin thẻ.");
        String loai = p.getLoaiThe();
        String soThe = p.getSoThe() == null ? "" : p.getSoThe().replaceAll("\\s+", "");
        String csv = p.getCsv() == null ? "" : p.getCsv().trim();
        if (loai == null || loai.isBlank())
            return new PaymentResult(false, null, "Chưa chọn loại thẻ.");
        if (!soThe.matches("\\d+"))
            return new PaymentResult(false, null, "Số thẻ chỉ được chứa chữ số.");
        int requiredLength = "American Express".equalsIgnoreCase(loai) ? 15 : 16;
        if (soThe.length() != requiredLength)
            return new PaymentResult(false, null, "Số thẻ không đúng độ dài theo loại thẻ.");
        int csvLen = "American Express".equalsIgnoreCase(loai) ? 4 : 3;
        if (!csv.matches("\\d{" + csvLen + "}"))
            return new PaymentResult(false, null, "Mã CSV/CVV không hợp lệ.");
        if (p.getNgayHetHan() == null || p.getNgayHetHan().isBefore(LocalDate.now())) {
            return new PaymentResult(false, null, "Thẻ đã hết hạn.");
        }
        if (p.getTenChuThe() == null || p.getTenChuThe().isBlank()) {
            return new PaymentResult(false, null, "Chưa nhập tên chủ thẻ.");
        }
        return new PaymentResult(true, null, "Thông tin thẻ hợp lệ.");
    }

    @Override
    public PaymentResult thucHienThanhToan(PaymentInfo p, BigDecimal soTien) {
        PaymentResult check = kiemTraThe(p);
        if (!check.isSuccess())
            return check;
        if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(false, null, "Số tiền thanh toán không hợp lệ.");
        }
        String id = "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new PaymentResult(true, id, "Thanh toán mô phỏng thành công.");
    }
}
