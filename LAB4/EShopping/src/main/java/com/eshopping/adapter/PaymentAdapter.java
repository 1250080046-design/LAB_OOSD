package com.eshopping.adapter;

import com.eshopping.model.PaymentInfo;
import java.math.BigDecimal;

public interface PaymentAdapter {
    PaymentResult kiemTraThe(PaymentInfo paymentInfo);
    PaymentResult thucHienThanhToan(PaymentInfo paymentInfo, BigDecimal soTien);
}
