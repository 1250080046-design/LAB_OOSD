package com.benhvien;

import com.benhvien.forms.FrmMain;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            JFrame.setDefaultLookAndFeelDecorated(false);
            FrmMain main = new FrmMain();
            main.setVisible(true);
        });
    }
}
