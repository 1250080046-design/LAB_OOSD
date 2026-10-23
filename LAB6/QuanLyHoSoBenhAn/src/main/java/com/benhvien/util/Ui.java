package com.benhvien.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public final class Ui {
    private Ui() {
    }

    public static JFrame frame(String title, int w, int h) {
        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setSize(w, h);
        f.setMinimumSize(new Dimension(Math.min(w, 760), Math.min(h, 500)));
        f.setLocationRelativeTo(null);
        f.setLayout(new BorderLayout(8, 8));
        return f;
    }

    public static JButton button(String text, Runnable action) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setMargin(new Insets(8, 12, 8, 12));
        b.addActionListener(e -> action.run());
        return b;
    }

    public static JPanel fields(int cols) {
        JPanel p = new JPanel(new GridLayout(0, cols, 8, 7));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 8, 10));
        return p;
    }

    public static JPanel labeled(String label, JComponent c) {
        JPanel p = new JPanel(new BorderLayout(3, 3));
        p.add(new JLabel(label), BorderLayout.NORTH);
        p.add(c, BorderLayout.CENTER);
        return p;
    }

    public static JTable table(String... headers) {
        DefaultTableModel m = new DefaultTableModel(headers, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable t = new JTable(m);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        t.setRowHeight(25);
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.getTableHeader().setReorderingAllowed(false);
        return t;
    }

    public static void fill(JTable table, List<Map<String, Object>> rows) {
        if (rows == null)
            return;
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        if (!rows.isEmpty()) {
            model.setColumnIdentifiers(rows.get(0).keySet().toArray());
            model.setRowCount(0);
            for (Map<String, Object> row : rows)
                model.addRow(row.values().stream().map(v -> v == null ? "" : v).toArray());
        } else
            model.setRowCount(0);
    }

    public static Object cell(JTable t, int row, String col) {
        for (int i = 0; i < t.getColumnCount(); i++)
            if (t.getColumnName(i).equalsIgnoreCase(col))
                return t.getValueAt(row, i);
        return null;
    }

    public static void info(Component c, String msg) {
        JOptionPane.showMessageDialog(c, msg, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component c, Throwable ex) {
        JOptionPane.showMessageDialog(c, ex.getMessage() == null ? ex.toString() : ex.getMessage(), "Lỗi",
                JOptionPane.ERROR_MESSAGE);
    }

    public static String str(JTextField f) {
        return f.getText().trim();
    }

    public static int intValue(JTextField f, String label) {
        try {
            return Integer.parseInt(str(f));
        } catch (Exception e) {
            throw new IllegalArgumentException(label + " phải là số nguyên.");
        }
    }

    public static java.time.LocalDate date(String s, String label) {
        try {
            return java.time.LocalDate.parse(s.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(label + " phải có dạng yyyy-MM-dd.");
        }
    }

    public static java.time.LocalDateTime dateTime(String s, String label) {
        try {
            return java.time.LocalDateTime.parse(s.trim().replace(' ', 'T'));
        } catch (Exception e) {
            throw new IllegalArgumentException(label + " phải có dạng yyyy-MM-dd HH:mm.");
        }
    }

    public static JPanel buttons(JButton... bs) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        for (JButton b : bs)
            p.add(b);
        return p;
    }

    public static String comboValue(JComboBox<?> c) {
        Object o = c.getSelectedItem();
        return o == null ? "" : o.toString();
    }
}
