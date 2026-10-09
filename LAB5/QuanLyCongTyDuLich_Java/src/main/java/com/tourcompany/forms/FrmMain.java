package com.tourcompany.forms;

import com.tourcompany.data.Db;
import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {
    public FrmMain() {
        super("QUẢN LÝ CÔNG TY DU LỊCH VĂN HÓA VIỆT");
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(900, 560);
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                thoat();
            }
        });
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(20, 26, 18, 26));
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ CÔNG TY DU LỊCH", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setForeground(new Color(32, 75, 120));
        root.add(title, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 3, 14, 14));
        grid.setBorder(BorderFactory.createEmptyBorder(24, 10, 24, 10));
        addButton(grid, "Danh mục", () -> new FrmDanhMuc().open());
        addButton(grid, "Tour - hành trình", () -> new FrmTour().open());
        addButton(grid, "Lịch chuyến khách lẻ", () -> new FrmChuyenLe().open());
        addButton(grid, "Đăng ký khách lẻ", () -> new FrmDangKyLe().open());
        addButton(grid, "Đăng ký theo đoàn", () -> new FrmDangKyDoan().open());
        addButton(grid, "Phân công hướng dẫn viên", () -> new FrmPhanCongHDV().open());
        addButton(grid, "Kết thúc tour - khảo sát", () -> new FrmKetThucKhaoSat().open());
        addButton(grid, "Lương - thống kê", () -> new FrmLuongThongKe().open());
        root.add(grid, BorderLayout.CENTER);
        JButton exit = new JButton("Thoát");
        exit.addActionListener(e -> thoat());
        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(exit);
        root.add(south, BorderLayout.SOUTH);
        setContentPane(root);
    }

    private void addButton(JPanel panel, String label, Runnable action) {
        JButton b = new JButton(label);
        b.setFont(new Font("SansSerif", Font.BOLD, 15));
        b.setFocusPainted(false);
        b.addActionListener(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                Ui.error(this, ex.getMessage());
            }
        });
        panel.add(b);
    }

    private void thoat() {
        if (Ui.confirm(this, "Bạn có chắc chắn muốn thoát?"))
            dispose();
    }
}
