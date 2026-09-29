package com.quanlykhachsan.form;
import com.quanlykhachsan.util.UiUtil;
import javax.swing.*;
import java.awt.*;
public class FrmMain extends JFrame {
    public FrmMain() {
        UiUtil.setupFrame(
                this,
                "Quản lý khách sạn",
                860,
                560
        );
        setResizable(false);
        getContentPane().setBackground(
                new Color(245, 245, 245)
        );
        // =====================================================
        // ROOT
        // =====================================================
        JPanel root = new JPanel(
                new BorderLayout(8, 8)
        );
        root.setBorder(
                BorderFactory.createEmptyBorder(
                        10, 30, 18, 30
                )
        );
        root.setBackground(
                new Color(245, 245, 245)
        );
        // =====================================================
        // TIÊU ĐỀ
        // =====================================================
        root.add(
                UI.title(
                        "HỆ THỐNG QUẢN LÝ KHÁCH SẠN"
                ),
                BorderLayout.NORTH
        );
        // =====================================================
        // MENU CHÍNH
        // =====================================================
        JPanel menu = new JPanel(
                new GridLayout(
                        2,
                        3,
                        12,
                        12
                )
        );
        menu.setOpaque(false);
        JButton btnDanhMuc =
                UI.menuButton(
                        "📋",
                        "Danh mục"
                );
        JButton btnPhong =
                UI.menuButton(
                        "🛏",
                        "Phòng - Tiện nghi"
                );
        JButton btnDatPhong =
                UI.menuButton(
                        "🔑",
                        "Đặt / Nhận phòng"
                );
        JButton btnDichVu =
                UI.menuButton(
                        "⚙",
                        "Sử dụng dịch vụ"
                );
        JButton btnTraPhong =
                UI.menuButton(
                        "💵",
                        "Trả phòng - Thanh toán"
                );
        JButton btnThongKe =
                UI.menuButton(
                        "📊",
                        "Thống kê"
                );
        menu.add(btnDanhMuc);
        menu.add(btnPhong);
        menu.add(btnDatPhong);
        menu.add(btnDichVu);
        menu.add(btnTraPhong);
        menu.add(btnThongKe);
        root.add(
                menu,
                BorderLayout.CENTER
        );
        // =====================================================
        // NÚT THOÁT
        // =====================================================
        JButton btnThoat =
                UI.menuButton(
                        "🚪",
                        "Thoát"
                );
        btnThoat.setPreferredSize(
                new Dimension(
                        245,
                        62
                )
        );
        JPanel bottom =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER
                        )
                );
        bottom.setOpaque(false);
        bottom.add(btnThoat);
        root.add(
                bottom,
                BorderLayout.SOUTH
        );
        // =====================================================
        // EVENT
        // =====================================================
        btnDanhMuc.addActionListener(
                e -> new FrmDanhMuc()
                        .setVisible(true)
        );
        btnPhong.addActionListener(
                e -> new FrmPhongTienNghi()
                        .setVisible(true)
        );
        btnDatPhong.addActionListener(
                e -> new FrmDatPhong()
                        .setVisible(true)
        );
        btnDichVu.addActionListener(
                e -> new FrmDichVu()
                        .setVisible(true)
        );
        btnTraPhong.addActionListener(
                e -> new FrmTraPhong()
                        .setVisible(true)
        );
        btnThongKe.addActionListener(
                e -> new FrmThongKe()
                        .setVisible(true)
        );
        btnThoat.addActionListener(
                e -> {
                    if (UiUtil.confirm(
                            this,
                            "Bạn có thực sự muốn thoát?"
                    )) {
                        System.exit(0);
                    }
                }
        );
        // =====================================================
        // HIỂN THỊ
        // =====================================================
        add(root);
    }
}