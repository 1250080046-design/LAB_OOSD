package com.caooc.forms;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class FrmMain extends JFrame {
    public FrmMain() {
        super("QUẢN LÝ CHO THUÊ VĂN PHÒNG CAO ỐC");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(970, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(18, 18));

        JLabel title = new JLabel("QUẢN LÝ CHO THUÊ VĂN PHÒNG CAO ỐC", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setBorder(BorderFactory.createEmptyBorder(28, 8, 20, 8));
        add(title, BorderLayout.NORTH);

        JPanel menu = new JPanel(new GridLayout(3, 3, 16, 16));
        menu.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button(menu, "Tầng & văn phòng", () -> new FrmTangVanPhong().setVisible(true));
        button(menu, "Khách hàng", () -> new FrmKhachHang().setVisible(true));
        button(menu, "Hợp đồng thuê", () -> new FrmHopDong().setVisible(true));
        button(menu, "Hóa đơn & thanh toán", () -> new FrmThanhToan().setVisible(true));
        button(menu, "Dịch vụ", () -> new FrmDichVu().setVisible(true));
        button(menu, "Gia hạn", () -> new FrmGiaHan().setVisible(true));
        button(menu, "Nhân viên", () -> new FrmNhanVien().setVisible(true));
        button(menu, "Thẻ giữ xe", () -> new FrmTheXe().setVisible(true));
        button(menu, "Kiểm tra phòng", () -> new FrmKiemTra().setVisible(true));
        add(menu, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 8));
        JButton exitButton = new JButton("Thoát chương trình");
        exitButton.setFont(new Font("Arial", Font.BOLD, 15));
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> confirmExit());
        footer.add(exitButton);
        add(footer, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmExit();
            }
        });
    }

    private void button(JPanel panel, String name, Runnable run) {
        JButton button = new JButton(name);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.addActionListener(e -> run.run());
        panel.add(button);
    }

    private void confirmExit() {
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn thoát chương trình?",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }
}
