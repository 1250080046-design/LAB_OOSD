package com.eshopping.forms;

import com.eshopping.service.*;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class FrmMain extends JFrame {

    private final TaiKhoanService taiKhoanService = new TaiKhoanService();
    private final SanPhamService sanPhamService = new SanPhamService();
    private final GioHangService gioHangService = new GioHangService();
    private final ThanhToanService thanhToanService = new ThanhToanService();
    private final EmailService emailService = new EmailService();

    private final DonHangService donHangService = new DonHangService(
            sanPhamService,
            gioHangService,
            thanhToanService,
            emailService
    );

    private final JLabel lblUser = new JLabel("Chưa đăng nhập");
    private final JButton btnLogout = new JButton("Đăng xuất");

    public FrmMain() {
        super("e-SHOPPING - Hệ thống bán hàng online");
        buildUI();
    }

    private void buildUI() {

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // =========================
        // PANEL ROOT
        // =========================
        JPanel root = new JPanel(new BorderLayout(15, 15));
        root.setBorder(
                BorderFactory.createEmptyBorder(
                        15, 15, 15, 15
                )
        );

        // =========================
        // TIÊU ĐỀ
        // =========================
        JLabel title = new JLabel(
                "HỆ THỐNG e-SHOPPING",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        28
                )
        );

        root.add(title, BorderLayout.NORTH);

        // =========================
        // CÁC NÚT
        // =========================
        JButton btnProduct = new JButton("Sản phẩm");
        JButton btnCart = new JButton("Giỏ hàng");
        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnRegister = new JButton("Đăng ký");

        // Style các nút
        for (JButton b : new JButton[]{
                btnProduct,
                btnCart,
                btnLogin,
                btnRegister,
                btnLogout
        }) {
            UiUtil.styleButton(b);
        }

        // ==================================================
        // PANEL CHỨA 2 HÀNG
        // ==================================================
        JPanel center = new JPanel();
        center.setLayout(
                new BoxLayout(
                        center,
                        BoxLayout.Y_AXIS
                )
        );

        // ==================================================
        // HÀNG 1: 2 NÚT
        // Sản phẩm | Giỏ hàng
        // ==================================================
        JPanel row1 = new JPanel(
                new GridLayout(1, 2, 20, 0)
        );

        row1.setAlignmentX(Component.CENTER_ALIGNMENT);

        row1.add(btnProduct);
        row1.add(btnCart);

        // ==================================================
        // HÀNG 2: 3 NÚT
        // Đăng nhập | Đăng ký | Đăng xuất
        // ==================================================
        JPanel row2 = new JPanel(
                new GridLayout(1, 3, 20, 0)
        );

        row2.setAlignmentX(Component.CENTER_ALIGNMENT);

        row2.add(btnLogin);
        row2.add(btnRegister);
        row2.add(btnLogout);

        // ==================================================
        // CĂN KÍCH THƯỚC 2 HÀNG
        // ==================================================

        // Kích thước tối đa để các hàng không bị co lệch
        Dimension rowSize = new Dimension(820, 220);

        row1.setPreferredSize(rowSize);
        row1.setMinimumSize(rowSize);
        row1.setMaximumSize(rowSize);

        row2.setPreferredSize(rowSize);
        row2.setMinimumSize(rowSize);
        row2.setMaximumSize(rowSize);

        // Khoảng cách giữa 2 hàng
        center.add(row1);
        center.add(Box.createVerticalStrut(20));
        center.add(row2);

        // Khoảng trống phía trên và dưới
        center.add(Box.createVerticalGlue());

        root.add(center, BorderLayout.CENTER);

        // =========================
        // PANEL BOTTOM
        // =========================
        JPanel bottom = new JPanel(
                new BorderLayout()
        );

        lblUser.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        bottom.add(
                lblUser,
                BorderLayout.WEST
        );

        JButton btnExit = new JButton("Thoát");

        UiUtil.styleButton(btnExit);

        bottom.add(
                btnExit,
                BorderLayout.EAST
        );

        root.add(
                bottom,
                BorderLayout.SOUTH
        );

        // ==================================================
        // XỬ LÝ SỰ KIỆN
        // ==================================================

        // Mở sản phẩm
        btnProduct.addActionListener(e ->
                new FrmSanPham(
                        this,
                        sanPhamService,
                        gioHangService,
                        donHangService
                ).setVisible(true)
        );

        // Mở giỏ hàng
        btnCart.addActionListener(e ->
                new FrmGioHang(
                        this,
                        gioHangService,
                        donHangService
                ).setVisible(true)
        );

        // Đăng nhập
        btnLogin.addActionListener(e ->
                new FrmDangNhap(
                        this,
                        taiKhoanService,
                        this::updateUser
                ).setVisible(true)
        );

        // Đăng ký
        btnRegister.addActionListener(e ->
                new FrmDangKy(
                        this,
                        taiKhoanService
                ).setVisible(true)
        );

        // Đăng xuất
        btnLogout.addActionListener(e ->
                confirmLogout()
        );

        // Thoát
        btnExit.addActionListener(e ->
                confirmExit()
        );

        setContentPane(root);

        updateUser();
    }

    /**
     * Xác nhận trước khi đăng xuất.
     */
    private void confirmLogout() {

        // Nếu chưa đăng nhập thì không làm gì
        if (!Session.isLoggedIn()) {
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn đăng xuất",
                "Xác nhận đăng xuất",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {

            Session.logout();

            updateUser();

            JOptionPane.showMessageDialog(
                    this,
                    "Đã đăng xuất thành công.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    /**
     * Xác nhận trước khi thoát ứng dụng.
     */
    private void confirmExit() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Bạn có chắc chắn muốn thoát",
                "Xác nhận thoát",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    /**
     * Cập nhật trạng thái tài khoản.
     */
    private void updateUser() {

        if (Session.isLoggedIn()) {

            lblUser.setText(
                    "Đang đăng nhập: "
                            + Session.getCurrentAccount()
                            .getKhachHang()
                            .getHoTen()
                            + " | "
                            + Session.getCurrentAccount()
                            .getTenDangNhap()
            );

        } else {

            lblUser.setText("Chưa đăng nhập");
        }

        // Chỉ cho phép Đăng xuất khi đã đăng nhập
        btnLogout.setEnabled(
                Session.isLoggedIn()
        );
    }
}