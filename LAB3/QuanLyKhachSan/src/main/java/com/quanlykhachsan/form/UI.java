package com.quanlykhachsan.form;

import com.quanlykhachsan.util.ComboItem;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public final class UI {
    public static final Color BLUE = new Color(34, 71, 119);
    public static final Color LIGHT_BLUE = new Color(229, 238, 249);
    public static final Color BORDER = new Color(180, 190, 205);

    private UI() {}

    public static JLabel title(String text) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(new Font("Segoe UI", Font.BOLD, 23));
        l.setForeground(BLUE);
        l.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));
        return l;
    }

    public static JButton menuButton(String icon, String text) {
        JButton b = new JButton("  " + icon + "  " + text);
        b.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 15));
        b.setHorizontalAlignment(SwingConstants.CENTER);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(245, 78));
        return b;
    }

    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new LineBorder(BORDER, 1, true));
        p.setBackground(Color.WHITE);
        return p;
    }

    public static JPanel row() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        p.setBackground(new Color(245, 247, 250));
        return p;
    }

    public static void field(JPanel row, String label, JComponent comp, int width) {
        JLabel l = new JLabel(label);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        row.add(l);
        if (width > 0) comp.setPreferredSize(new Dimension(width, 26));
        row.add(comp);
    }

    public static JTable table() {
        JTable t = new JTable();
        t.setRowHeight(24);
        t.setAutoCreateRowSorter(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setReorderingAllowed(false);
        return t;
    }

    public static JScrollPane scroll(JTable table) {
        return new JScrollPane(table);
    }

    public static JPanel buttonRow(JButton... buttons) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 4));
        p.setBackground(new Color(245, 247, 250));
        for (JButton b : buttons) p.add(b);
        return p;
    }

    public static JComboBox<ComboItem> combo() {
        return new JComboBox<>();
    }

    public static TitledBorder titled(String text) {
        return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BORDER), text);
    }
}
