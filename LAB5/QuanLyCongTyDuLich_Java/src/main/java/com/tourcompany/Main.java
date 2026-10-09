package com.tourcompany;

import com.tourcompany.forms.FrmMain;
import javax.swing.*;

public final class Main {
    private Main() {}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
            catch (Exception ignored) { }
            new FrmMain().setVisible(true);
        });
    }
}
