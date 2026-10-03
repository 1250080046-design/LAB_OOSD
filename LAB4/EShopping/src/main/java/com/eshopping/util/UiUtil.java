package com.eshopping.util;

import javax.swing.*;
import java.awt.*;

public final class UiUtil {
    private UiUtil() {
    }

    public static void styleButton(AbstractButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(150, 36));
    }

    public static void center(Window window, Window parent) {
        if (parent == null) {
            window.setLocationRelativeTo(null);
        } else {
            window.setLocationRelativeTo(parent);
        }
    }
}
