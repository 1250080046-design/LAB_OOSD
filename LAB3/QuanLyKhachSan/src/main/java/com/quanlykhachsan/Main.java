package com.quanlykhachsan;
import com.quanlykhachsan.form.FrmMain;
import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }
            FrmMain form = new FrmMain();
            form.setVisible(true);
        });
    }
}