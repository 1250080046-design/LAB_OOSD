package com.benhvien.forms;

import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {
    public FrmMain() {
        super("QUẢN LÝ HỒ SƠ BỆNH ÁN CỦA BỆNH NHÂN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 760);
        setMinimumSize(new Dimension(900, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16, 16));
        JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ HỒ SƠ BỆNH ÁN", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 26));
        title.setForeground(new Color(28, 75, 125));
        title.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(title, BorderLayout.NORTH);
        JPanel grid = new JPanel(new GridLayout(3, 3, 16, 16));
        grid.setBorder(BorderFactory.createEmptyBorder(14, 20, 18, 20));
        grid.add(nav("Đăng ký bệnh nhân\nLệ phí năm + cấp hồ sơ", () -> new FrmPatients().setVisible(true)));
        grid.add(nav("Tra cứu\nLịch sử bệnh án", () -> new FrmHistory().setVisible(true)));
        grid.add(nav("Khám bệnh\nLập phiếu khám", () -> new FrmExaminations().setVisible(true)));
        grid.add(nav("Xét nghiệm\nYêu cầu và kết quả", () -> new FrmLabTests().setVisible(true)));
        grid.add(nav("Toa thuốc\nLập toa và chi tiết", () -> new FrmPrescriptions().setVisible(true)));
        grid.add(nav("Lịch khám định kỳ\nTheo quý", () -> new FrmAppointments().setVisible(true)));
        grid.add(nav("Viện phí\nHoàn tất đợt điều trị", () -> new FrmBilling().setVisible(true)));
        grid.add(nav("Thống kê\nTổng quan", () -> new FrmReports().setVisible(true)));
        grid.add(nav("Thoát", () -> {
            if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
                dispose();
        }));
        add(grid, BorderLayout.CENTER);
    }

    private JButton nav(String label, Runnable action) {
        JButton b = new JButton("<html><center>" + label.replace("\n", "<br>") + "</center></html>");
        b.setFont(new Font("SansSerif", Font.BOLD, 16));
        b.setMargin(new Insets(15, 12, 15, 12));
        b.addActionListener(e -> action.run());
        return b;
    }
}
