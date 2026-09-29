package com.quanlykhachsan.model;

import java.math.BigDecimal;

public record DenBuItem(
        String maTienNghi,
        String tenLoaiTN,
        String mucDoThietHai,
        BigDecimal soTien
) {
}
