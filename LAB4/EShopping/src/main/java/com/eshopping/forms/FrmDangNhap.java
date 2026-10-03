package com.eshopping.forms;

import com.eshopping.model.TaiKhoan;
import com.eshopping.service.Session;
import com.eshopping.service.TaiKhoanService;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class FrmDangNhap extends JDialog {
    private final TaiKhoanService service;
    private final Runnable onSuccess;
    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();

    public FrmDangNhap(Frame owner, TaiKhoanService service, Runnable onSuccess) {
        super(owner, "Đăng nhập", true);
        this.service = service;
        this.onSuccess = onSuccess;
        buildUI();
    }

    private void buildUI() {
        setSize(430, 250);
        setResizable(false);
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        JLabel title = new JLabel("ĐĂNG NHẬP e-SHOPPING", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.add(new JLabel("Tên đăng nhập:"));
        form.add(txtUsername);
        form.add(new JLabel("Mật khẩu:"));
        form.add(txtPassword);
        root.add(form, BorderLayout.CENTER);

        JButton btnLogin = new JButton("Đăng nhập");
        JButton btnClose = new JButton("Đóng");
        UiUtil.styleButton(btnLogin);
        UiUtil.styleButton(btnClose);
        JPanel buttons = new JPanel();
        buttons.add(btnLogin);
        buttons.add(btnClose);
        root.add(buttons, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> doLogin());
        btnClose.addActionListener(e -> dispose());
        getRootPane().setDefaultButton(btnLogin);
        setContentPane(root);
        UiUtil.center(this, getOwner());
    }

    private void doLogin() {
        try {
            TaiKhoan tk = service.dangNhap(txtUsername.getText(), new String(txtPassword.getPassword()));
            Session.login(tk);
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công. Xin chào " + tk.getKhachHang().getHoTen() + "!");
            if (onSuccess != null)
                onSuccess.run();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        }
    }
}
