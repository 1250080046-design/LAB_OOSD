package com.eshopping.forms;
import com.eshopping.model.CartItem;
import com.eshopping.service.DonHangService;
import com.eshopping.service.GioHangService;
import com.eshopping.service.Session;
import com.eshopping.util.FormatUtil;
import com.eshopping.util.UiUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class FrmGioHang extends JDialog {
    private final GioHangService gioHang;
    private final DonHangService donHangService;
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Mã SP", "Tên sản phẩm", "Đơn giá", "Số lượng", "Thành tiền" }, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    };
    private final JTable table = new JTable(model);
    private final JLabel lblTotal = new JLabel();
    public FrmGioHang(Frame owner, GioHangService gioHang, DonHangService donHangService) {
        super(owner, "Giỏ hàng", true);
        this.gioHang = gioHang;
        this.donHangService = donHangService;
        buildUI();
        reload();
    }
    private void buildUI() {
        setSize(1000, 520);
        setMinimumSize(new Dimension(900, 500));
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        table.setRowHeight(28);
        root.add(new JScrollPane(table), BorderLayout.CENTER);
        // Khu vực dưới được chia thành 2 hàng để "Tạm tính" không đè lên các nút.
        JPanel bottom = new JPanel();
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalPanel.add(lblTotal);
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        JButton btnUpdate = new JButton("Cập nhật số lượng");
        JButton btnRemove = new JButton("Xóa sản phẩm");
        JButton btnClear = new JButton("Xóa hết");
        JButton btnOrder = new JButton("Đặt hàng");
        JButton btnClose = new JButton("Đóng");
        for (JButton b : new JButton[] { btnUpdate, btnRemove, btnClear, btnOrder, btnClose }) {
            UiUtil.styleButton(b);
        }
        buttons.add(btnUpdate);
        buttons.add(btnRemove);
        buttons.add(btnClear);
        buttons.add(btnOrder);
        buttons.add(btnClose);
        bottom.add(totalPanel);
        bottom.add(buttons);
        root.add(bottom, BorderLayout.SOUTH);
        btnUpdate.addActionListener(e -> updateQty());
        btnRemove.addActionListener(e -> remove());
        btnClear.addActionListener(e -> {
            gioHang.xoaHet();
            reload();
        });
        btnOrder.addActionListener(e -> order());
        btnClose.addActionListener(e -> dispose());
        setContentPane(root);
        UiUtil.center(this, getOwner());
    }
    private void reload() {
        model.setRowCount(0);
        for (CartItem item : gioHang.getItems())
            model.addRow(new Object[] { item.getSanPham().getMaSP(), item.getSanPham().getTenSP(),
                    FormatUtil.currency(item.getSanPham().getGiaBan()), item.getSoLuong(),
                    FormatUtil.currency(item.getThanhTien()) });
        lblTotal.setText("Tạm tính: " + FormatUtil.currency(gioHang.tinhTamTinh()));
    }
    private CartItem selected() {
        int row = table.getSelectedRow();
        if (row < 0)
            return null;
        String ma = model.getValueAt(table.convertRowIndexToModel(row), 0).toString();
        for (CartItem x : gioHang.getItems())
            if (x.getSanPham().getMaSP().equals(ma))
                return x;
        return null;
    }
    private void updateQty() {
        CartItem x = selected();
        if (x == null) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm.");
            return;
        }
        String s = JOptionPane.showInputDialog(this, "Số lượng mới:", x.getSoLuong());
        if (s == null)
            return;
        try {
            gioHang.capNhatSoLuong(x.getSanPham().getMaSP(), Integer.parseInt(s));
            reload();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void remove() {
        CartItem x = selected();
        if (x == null) {
            JOptionPane.showMessageDialog(this, "Chọn sản phẩm.");
            return;
        }
        gioHang.xoaSanPham(x.getSanPham().getMaSP());
        reload();
    }
    private void order() {
        if (!Session.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, "Bạn cần đăng nhập trước khi đặt hàng.");
            return;
        }
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống.");
            return;
        }
        if (donHangService == null) {
            JOptionPane.showMessageDialog(this, "Không thể mở chức năng đặt hàng từ màn hình này.");
            return;
        }
        new FrmDatHang((Frame) getOwner(), donHangService, gioHang).setVisible(true);
        reload();
    }
}
