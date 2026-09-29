package com.quanlykhachsan.util;

import com.quanlykhachsan.model.KetQuaXuLy;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public final class UiUtil {
    private static final DecimalFormat MONEY = new DecimalFormat("#,##0");

    private UiUtil() {
    }

    public static void initLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 14));
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("ComboBox.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("Table.font", new Font("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 12));
    }

    public static void setupFrame(JFrame frame, String title, int width, int height) {
        frame.setTitle(title);
        frame.setSize(width, height);
        frame.setMinimumSize(new Dimension(Math.min(width, 900), Math.min(height, 600)));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public static JPanel formRowPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panel.setBorder(new EmptyBorder(6, 8, 4, 8));
        return panel;
    }

    public static JLabel label(String text) {
        return new JLabel(text);
    }

    public static void addLabeled(JPanel panel, String label, JComponent comp, int width) {
        JPanel p = new JPanel(new BorderLayout(4, 0));
        JLabel l = new JLabel(label);
        p.add(l, BorderLayout.WEST);
        if (width > 0) comp.setPreferredSize(new Dimension(width, 28));
        p.add(comp, BorderLayout.CENTER);
        panel.add(p);
    }

    public static JScrollPane table(JTable table) {
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(25);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.getTableHeader().setReorderingAllowed(false);
        return new JScrollPane(table);
    }

    public static void moneyColumn(JTable table, String columnName) {
        int idx = table.getColumnModel().getColumnIndex(columnName);
        table.getColumnModel().getColumn(idx).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public void setValue(Object value) {
                if (value instanceof BigDecimal bd) setText(MONEY.format(bd));
                else if (value instanceof Number n) setText(MONEY.format(n.doubleValue()));
                else setText(value == null ? "" : value.toString());
            }
        });
    }

    public static void result(Component parent, KetQuaXuLy result) {
        JOptionPane.showMessageDialog(
                parent,
                result.thongBao(),
                result.thanhCong() ? "Thông báo" : "Từ chối",
                result.thanhCong() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
        );
    }

    public static boolean confirm(Component parent, String message) {
        return JOptionPane.showConfirmDialog(
                parent,
                message,
                "Xác nhận",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        ) == JOptionPane.YES_OPTION;
    }
}
