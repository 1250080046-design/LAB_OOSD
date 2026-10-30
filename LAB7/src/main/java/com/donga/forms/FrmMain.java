package com.donga.forms;

import com.donga.service.Module;
import com.donga.data.Db;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Dashboard arranged like the reference medical-record project: one window per
 * domain.
 */
public class FrmMain extends JFrame {
  private static final Color NAVY = new Color(28, 75, 125);
  private static final Color BG = new Color(244, 247, 251);

  public FrmMain() {
    super("QUẢN LÝ NHÂN SỰ - CÔNG TY SƠN ĐÔNG Á");
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setMinimumSize(new Dimension(960, 680));
    setSize(1160, 760);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout(14, 14));
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent e) {
        exit();
      }
    });
    JPanel top = new JPanel(new BorderLayout(4, 8));
    top.setBackground(BG);
    top.setBorder(BorderFactory.createEmptyBorder(24, 18, 10, 18));
    JLabel title = new JLabel("HỆ THỐNG QUẢN LÝ NHÂN SỰ", SwingConstants.CENTER);
    title.setForeground(NAVY);
    title.setFont(new Font("SansSerif", Font.BOLD, 27));
    top.add(title, BorderLayout.CENTER);
    add(top, BorderLayout.NORTH);
    JPanel grid = new JPanel(new GridLayout(3, 3, 16, 16));
    grid.setBackground(BG);
    grid.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
    grid.add(nav("Nhân viên & phòng ban", FrmNhanVien::new));
    grid.add(nav("Tuyển dụng & thử việc", FrmTuyenDung::new));
    grid.add(nav("Hợp đồng & bảo hiểm", FrmHopDongBaoHiem::new));
    grid.add(nav("Lương & chức vụ", FrmLuongChucVu::new));
    grid.add(nav("Kế hoạch & ca làm", FrmKeHoach::new));
    grid.add(nav("Chấm công & phụ trội", FrmChamCong::new));
    grid.add(nav("Thưởng & kỷ luật", FrmThuongKyLuat::new));
    grid.add(nav("Bảng lương tháng", FrmBangLuong::new));
    JButton exit = tile("Thoát chương trình");
    exit.addActionListener(e -> exit());
    grid.add(exit);
    add(grid, BorderLayout.CENTER);
    JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER));
    bottom.setBackground(BG);
    JButton check = new JButton("Kiểm tra kết nối SQL Server");
    check.addActionListener(e -> {
      try (var c = Db.connect()) {
        JOptionPane.showMessageDialog(this, "Kết nối thành công: " + c.getCatalog());
      } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Không kết nối được", JOptionPane.ERROR_MESSAGE);
      }
    });
    bottom.add(check);
    add(bottom, BorderLayout.SOUTH);
    getContentPane().setBackground(BG);
  }

  private JButton nav(String name, java.util.function.Supplier<? extends JFrame> factory) {
    JButton b = tile(name);
    b.addActionListener(e -> factory.get().setVisible(true));
    return b;
  }

  private JButton tile(String title) {
    JButton b = new JButton(title);
    b.setFont(new Font("SansSerif", Font.BOLD, 16));
    b.setForeground(NAVY);
    b.setBackground(Color.WHITE);
    b.setFocusPainted(false);
    b.setOpaque(true);
    b.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(208, 219, 231)),
        BorderFactory.createEmptyBorder(12, 10, 12, 10)));
    return b;
  }

  private void exit() {
    if (JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn thoát hệ thống?", "Xác nhận",
        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
      System.exit(0);
  }
}