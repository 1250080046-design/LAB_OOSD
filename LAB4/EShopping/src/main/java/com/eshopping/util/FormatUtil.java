package com.eshopping.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public final class FormatUtil {
    private FormatUtil() {
    }

    public static String currency(BigDecimal amount) {
        if (amount == null)
            return "0 đ";
        return NumberFormat.getNumberInstance(new Locale("vi", "VN"))
                .format(amount) + " đ";
    }
}
