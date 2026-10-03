package com.eshopping.forms;

import com.eshopping.adapter.PaymentResult;
import com.eshopping.model.PaymentInfo;
import com.eshopping.service.ThanhToanService;
import com.eshopping.util.FormatUtil;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class FrmThanhToan extends JDialog {
    private final ThanhToanService service;
    private final BigDecimal soTien;
    private PaymentInfo paymentInfo;
    private PaymentResult result;

    private final JComboBox<String> cboLoaiThe = new JComboBox<>(
            new String[] { "VISA", "Mastercard", "Discover", "American Express" });
    private final JTextField txtSoThe = new JTextField();
    private final JTextField txtHetHan = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JPasswordField txtCsv = new JPasswordField();

    public FrmThanhToan(Frame owner, ThanhToanService service, BigDecimal soTien) {
        super(owner, "Thanh toán đơn hàng", true);
        this.service = service;
        this.soTien = soTien;
        buildUI();
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public PaymentResult getResult() {
        return result;
    }

    private void buildUI() {
        setSize(500, 360);
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("THANH TOÁN - " + FormatUtil.currency(soTien), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));
        header.add(title, BorderLayout.CENTER);
        JPanel form = new JPanel(new GridLayout(5, 2, 8, 8));
        form.add(new JLabel("Loại thẻ *"));
        form.add(cboLoaiThe);
        form.add(new JLabel("Số thẻ *"));
        form.add(txtSoThe);
        form.add(new JLabel("Ngày hết hạn *"));
        form.add(txtHetHan);
        form.add(new JLabel("Tên chủ thẻ *"));
        form.add(txtTen);
        form.add(new JLabel("CSV/CVV *"));
        form.add(txtCsv);
        root.add(form, BorderLayout.CENTER);
        JLabel note = new JLabel("Prototype: dịch vụ thanh toán được mô phỏng, không lưu CSV/CVV.");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        header.add(note, BorderLayout.SOUTH);
        root.add(header, BorderLayout.NORTH);
        JPanel buttons = new JPanel();
        JButton pay = new JButton("Thanh toán");
        JButton cancel = new JButton("Hủy");
        UiUtil.styleButton(pay);
        UiUtil.styleButton(cancel);
        buttons.add(pay);
        buttons.add(cancel);
        root.add(buttons, BorderLayout.SOUTH);
        pay.addActionListener(e -> pay());
        cancel.addActionListener(e -> {
            result = null;
            dispose();
        });
        setContentPane(root);
        UiUtil.center(this, getOwner());
    }

    private void pay() {
        try {
            LocalDate exp = LocalDate.parse(txtHetHan.getText().trim() + "-01");
            PaymentInfo info = new PaymentInfo();
            info.setLoaiThe(String.valueOf(cboLoaiThe.getSelectedItem()));
            info.setSoThe(txtSoThe.getText().trim());
            info.setNgayHetHan(exp);
            info.setTenChuThe(txtTen.getText().trim());
            info.setCsv(new String(txtCsv.getPassword()));
            PaymentResult r = service.thanhToan(info, soTien);
            if (!r.isSuccess()) {
                JOptionPane.showMessageDialog(this, r.getMessage(), "Thanh toán thất bại", JOptionPane.ERROR_MESSAGE);
                return;
            }
            paymentInfo = info;
            result = r;
            JOptionPane.showMessageDialog(this, "Thanh toán thành công. Mã giao dịch: " + r.getTransactionId());
            dispose();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Ngày hết hạn nhập dạng yyyy-MM.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
