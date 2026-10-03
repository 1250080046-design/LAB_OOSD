package com.eshopping.forms;

import com.eshopping.model.SanPhamDTO;
import com.eshopping.service.GioHangService;
import com.eshopping.util.FormatUtil;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import java.awt.*;

public class FrmChiTietSanPham extends JDialog {
    private final SanPhamDTO sp;
    private final GioHangService gioHang;
    private final JSpinner spnQty = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));

    public FrmChiTietSanPham(Frame owner, SanPhamDTO sp, GioHangService gioHang) {
        super(owner, "Chi tiết sản phẩm", true);
        this.sp = sp;
        this.gioHang = gioHang;
        buildUI();
    }

    private void buildUI() {
        setSize(600, 430);
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        JLabel title = new JLabel(sp.getTenSP(), SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        root.add(title, BorderLayout.NORTH);

        JPanel info = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(5, 5, 5, 5);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;
        String[][] rows = {
                { "Mã sản phẩm", sp.getMaSP() }, { "Nhà sản xuất", sp.getNhaSanXuat() },
                { "Nhóm", sp.getNhomSanPham() }, { "Giá", FormatUtil.currency(sp.getGiaBan()) },
                { "Tình trạng", sp.getTinhTrang() }, { "Tồn kho", String.valueOf(sp.getTonKho()) },
                { "Mô tả", sp.getMoTa() }, { "Thông số kỹ thuật", sp.getThongSoKyThuat() }
        };
        for (int i = 0; i < rows.length; i++) {
            g.gridx = 0;
            g.gridy = i;
            g.weightx = 0;
            info.add(new JLabel(rows[i][0] + ":"), g);
            g.gridx = 1;
            g.weightx = 1;
            info.add(new JLabel(rows[i][1] == null ? "" : rows[i][1]), g);
        }
        root.add(new JScrollPane(info), BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.add(new JLabel("Số lượng:"));
        bottom.add(spnQty);
        JButton btnAdd = new JButton("Thêm vào giỏ hàng");
        JButton btnClose = new JButton("Đóng");
        UiUtil.styleButton(btnAdd);
        UiUtil.styleButton(btnClose);
        bottom.add(btnAdd);
        bottom.add(btnClose);
        root.add(bottom, BorderLayout.SOUTH);
        btnAdd.addActionListener(e -> add());
        btnClose.addActionListener(e -> dispose());
        setContentPane(root);
        UiUtil.center(this, getOwner());
    }

    private void add() {
        try {
            int qty = (Integer) spnQty.getValue();
            gioHang.themSanPham(sp, qty);
            JOptionPane.showMessageDialog(this, "Đã thêm sản phẩm vào giỏ hàng.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể thêm", JOptionPane.ERROR_MESSAGE);
        }
    }
}
