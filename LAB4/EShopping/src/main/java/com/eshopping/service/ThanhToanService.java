package com.eshopping.service;

import com.eshopping.adapter.OnlinePaymentAdapter;
import com.eshopping.adapter.PaymentAdapter;
import com.eshopping.adapter.PaymentResult;
import com.eshopping.model.PaymentInfo;
import java.math.BigDecimal;

public class ThanhToanService {
    private final PaymentAdapter adapter;

    public ThanhToanService() {
        this(new OnlinePaymentAdapter());
    }

    public ThanhToanService(PaymentAdapter adapter) {
        this.adapter = adapter;
    }

    public PaymentResult thanhToan(PaymentInfo info, BigDecimal soTien) {
        return adapter.thucHienThanhToan(info, soTien);
    }
}
