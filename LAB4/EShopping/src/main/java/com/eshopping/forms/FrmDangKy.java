package com.eshopping.forms;

import com.eshopping.model.KhachHang;
import com.eshopping.service.TaiKhoanService;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FrmDangKy extends JDialog {
    private final TaiKhoanService service;
    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtNgaySinh = new JTextField();
    private final JComboBox<String> cboLoaiGiayTo = new JComboBox<>(new String[] { "CMND", "CCCD", "Passport" });
    private final JTextField txtSoGiayTo = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JTextField txtDienThoai = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtUsername = new JTextField();
    private final JPasswordField txtPassword = new JPasswordField();

    public FrmDangKy(Frame owner, TaiKhoanService service) {
        super(owner, "Đăng ký tài khoản", true);
        this.service = service;
        buildUI();
    }

    private void buildUI() {
        setSize(520, 520);
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel title = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        root.add(title, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(9, 2, 8, 8));
        form.add(new JLabel("Họ tên *"));
        form.add(txtHoTen);
        form.add(new JLabel("Ngày sinh (yyyy-MM-dd)"));
        form.add(txtNgaySinh);
        form.add(new JLabel("Loại giấy tờ"));
        form.add(cboLoaiGiayTo);
        form.add(new JLabel("Số giấy tờ"));
        form.add(txtSoGiayTo);
        form.add(new JLabel("Địa chỉ"));
        form.add(txtDiaChi);
        form.add(new JLabel("Điện thoại"));
        form.add(txtDienThoai);
        form.add(new JLabel("Email"));
        form.add(txtEmail);
        form.add(new JLabel("Tên đăng nhập *"));
        form.add(txtUsername);
        form.add(new JLabel("Mật khẩu *"));
        form.add(txtPassword);
        root.add(form, BorderLayout.CENTER);

        JButton btnSave = new JButton("Đăng ký");
        JButton btnClose = new JButton("Đóng");
        UiUtil.styleButton(btnSave);
        UiUtil.styleButton(btnClose);
        JPanel buttons = new JPanel();
        buttons.add(btnSave);
        buttons.add(btnClose);
        root.add(buttons, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> register());
        btnClose.addActionListener(e -> dispose());
        setContentPane(root);
        UiUtil.center(this, getOwner());
    }

    private void register() {
        try {
            LocalDate birth = null;
            if (!txtNgaySinh.getText().isBlank()) {
                try {
                    birth = LocalDate.parse(txtNgaySinh.getText().trim());
                } catch (DateTimeParseException ex) {
                    throw new IllegalArgumentException("Ngày sinh phải có dạng yyyy-MM-dd.");
                }
            }
            KhachHang kh = new KhachHang();
            kh.setHoTen(txtHoTen.getText().trim());
            kh.setNgaySinh(birth);
            kh.setLoaiGiayTo(String.valueOf(cboLoaiGiayTo.getSelectedItem()));
            kh.setSoGiayTo(txtSoGiayTo.getText().trim());
            kh.setDiaChi(txtDiaChi.getText().trim());
            kh.setDienThoai(txtDienThoai.getText().trim());
            kh.setEmail(txtEmail.getText().trim());
            service.dangKy(kh, txtUsername.getText(), new String(txtPassword.getPassword()));
            JOptionPane.showMessageDialog(this, "Đăng ký thành công. Hãy đăng nhập để tiếp tục.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi đăng ký", JOptionPane.ERROR_MESSAGE);
        }
    }
}
