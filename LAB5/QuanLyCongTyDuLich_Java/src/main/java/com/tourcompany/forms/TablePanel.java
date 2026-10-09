package com.tourcompany.forms;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class TablePanel extends JPanel {
    @FunctionalInterface
    public interface Loader {
        DefaultTableModel load() throws Exception;
    }

    private final Loader loader;
    private final JTable table = new JTable();
    private final JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
    private final JScrollPane scrollPane = new JScrollPane(table);

    public TablePanel(Loader loader) {
        super(new BorderLayout(6, 6));
        this.loader = loader;

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setGridColor(new Color(210, 210, 210));
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 32));
        header.setReorderingAllowed(false);

        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);
        add(buttons, BorderLayout.NORTH);

        JButton refresh = new JButton("Làm mới");
        refresh.addActionListener(e -> reload());
        buttons.add(refresh);
        reload();
    }

    public void addAction(String title, Runnable action) {
        JButton button = new JButton(title);
        button.addActionListener(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                Ui.error(this, ex.getMessage() == null ? "Có lỗi xảy ra." : ex.getMessage());
            }
        });
        buttons.add(button);
    }

    public void reload() {
        try {
            table.setModel(loader.load());
            // Đảm bảo cài đặt giãn cột còn hiệu lực sau khi thay model.
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            table.setFillsViewportHeight(true);
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        } catch (Exception e) {
            Ui.error(this, "Không tải được dữ liệu: " + e.getMessage());
        }
    }

    public int selectedRow() {
        return table.getSelectedRow();
    }

    public Object selected(int column) {
        int row = table.getSelectedRow();
        if (row < 0) return null;
        return table.getValueAt(row, column);
    }

    public JTable table() {
        return table;
    }
}
