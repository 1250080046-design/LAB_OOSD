package com.eshopping.forms;

import com.eshopping.model.KhachHang;
import com.eshopping.model.KhuVucGiaoHang;
import com.eshopping.model.LoaiPhieuDatHang;
import com.eshopping.model.TaiKhoan;
import com.eshopping.adapter.PaymentResult;
import com.eshopping.model.PaymentInfo;
import com.eshopping.service.*;
import com.eshopping.util.FormatUtil;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class FrmDatHang extends JDialog {
    private final DonHangService service;
    private final GioHangService gioHang;
    private final JComboBox<LoaiPhieuDatHang> cboLoai = new JComboBox<>();
    private final JComboBox<KhuVucGiaoHang> cboKhuVuc = new JComboBox<>();
    private final JTextField txtTenNhan = new JTextField();
    private final JTextField txtDiaChiNhan = new JTextField();
    private final JTextField txtDienThoaiNhan = new JTextField();
    private final JLabel lblTienHang = new JLabel("0 đ");
    private final JLabel lblPhi = new JLabel("0 đ");
    private final JLabel lblTong = new JLabel("0 đ");

    public FrmDatHang(Frame owner, DonHangService service, GioHangService gioHang) {
        super(owner, "Đặt hàng", true);
        this.service = service;
        this.gioHang = gioHang;
        buildUI();
        loadData();
    }

    private void buildUI() {
        setSize(650, 500);
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel title = new JLabel("XÁC NHẬN ĐẶT HÀNG", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 21));
        root.add(title, BorderLayout.NORTH);
        JPanel form = new JPanel(new GridLayout(7, 2, 8, 8));
        form.add(new JLabel("Loại phiếu *"));
        form.add(cboLoai);
        form.add(new JLabel("Khu vực giao hàng *"));
        form.add(cboKhuVuc);
        form.add(new JLabel("Họ tên người nhận *"));
        form.add(txtTenNhan);
        form.add(new JLabel("Địa chỉ người nhận *"));
        form.add(txtDiaChiNhan);
        form.add(new JLabel("Điện thoại người nhận *"));
        form.add(txtDienThoaiNhan);
        form.add(new JLabel("Tiền hàng"));
        form.add(lblTienHang);
        form.add(new JLabel("Phí giao hàng"));
        form.add(lblPhi);
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.add(form, BorderLayout.NORTH);
        JPanel total = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel lt = new JLabel("TỔNG THANH TOÁN: ");
        lt.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTong.setFont(new Font("Segoe UI", Font.BOLD, 17));
        total.add(lt);
        total.add(lblTong);
        center.add(total, BorderLayout.SOUTH);
        root.add(center, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton btnCalc = new JButton("Tính tiền");
        JButton btnPay = new JButton("Thanh toán & Đặt hàng");
        JButton btnClose = new JButton("Hủy");
        for (JButton b : new JButton[] { btnCalc, btnPay, btnClose })
            UiUtil.styleButton(b);
        buttons.add(btnCalc);
        buttons.add(btnPay);
        buttons.add(btnClose);
        root.add(buttons, BorderLayout.SOUTH);
        cboLoai.addActionListener(e -> recalc());
        cboKhuVuc.addActionListener(e -> recalc());
        btnCalc.addActionListener(e -> recalc());
        btnPay.addActionListener(e -> submit());
        btnClose.addActionListener(e -> dispose());
        setContentPane(root);
        UiUtil.center(this, getOwner());
    }

    private void loadData() {
        try {
            List<LoaiPhieuDatHang> lp = service.layLoaiPhieu();
            lp.forEach(cboLoai::addItem);
            List<KhuVucGiaoHang> kv = service.layKhuVuc();
            kv.forEach(cboKhuVuc::addItem);
            TaiKhoan tk = Session.getCurrentAccount();
            KhachHang kh = tk.getKhachHang();
            txtTenNhan.setText(kh.getHoTen());
            txtDiaChiNhan.setText(kh.getDiaChi() == null ? "" : kh.getDiaChi());
            txtDienThoaiNhan.setText(kh.getDienThoai() == null ? "" : kh.getDienThoai());
            recalc();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không tải được dữ liệu đặt hàng: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal currentShip() {
        try {
            return service.tinhPhiGiaoHang(gioHang.tinhTamTinh(), (LoaiPhieuDatHang) cboLoai.getSelectedItem(),
                    (KhuVucGiaoHang) cboKhuVuc.getSelectedItem());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private void recalc() {
        BigDecimal hang = gioHang.tinhTamTinh();
        BigDecimal ship = currentShip();
        lblTienHang.setText(FormatUtil.currency(hang));
        lblPhi.setText(FormatUtil.currency(ship));
        lblTong.setText(FormatUtil.currency(hang.add(ship)));
    }

    private void submit() {
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống.");
            return;
        }
        BigDecimal total = gioHang.tinhTamTinh().add(currentShip());
        FrmThanhToan paymentDialog = new FrmThanhToan((Frame) getOwner(), new ThanhToanService(), total);
        paymentDialog.setVisible(true);
        PaymentInfo info = paymentDialog.getPaymentInfo();
        PaymentResult payResult = paymentDialog.getResult();
        if (info == null || payResult == null || !payResult.isSuccess())
            return;
        try {
            DonHangService.OrderResult result = service.datHang(Session.getCurrentAccount(),
                    (LoaiPhieuDatHang) cboLoai.getSelectedItem(), (KhuVucGiaoHang) cboKhuVuc.getSelectedItem(),
                    txtTenNhan.getText(), txtDiaChiNhan.getText(), txtDienThoaiNhan.getText(), info);
            String emailText = result.emailSent() ? "\nĐã gửi email xác nhận cho khách hàng."
                    : "\nKhách hàng không có email nên hệ thống không gửi email.";
            JOptionPane.showMessageDialog(this,
                    "ĐẶT HÀNG THÀNH CÔNG\nSố đơn: " + result.donHang().getSoDonHang() + "\nTổng thanh toán: "
                            + FormatUtil.currency(result.donHang().getTongThanhToan()) + emailText,
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Không thể ghi nhận đơn hàng: " + e.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
