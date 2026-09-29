package com.quanlykhachsan.util;

import javax.swing.*;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {
    private DateUtil() {
    }

    public static JSpinner dateSpinner() {
        JSpinner spinner = new JSpinner(
                new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH)
        );
        spinner.setEditor(new JSpinner.DateEditor(spinner, "dd/MM/yyyy"));
        return spinner;
    }

    public static Date value(JSpinner spinner) {
        return (Date) spinner.getValue();
    }

    public static java.sql.Date sqlDate(JSpinner spinner) {
        return new java.sql.Date(value(spinner).getTime());
    }
}
