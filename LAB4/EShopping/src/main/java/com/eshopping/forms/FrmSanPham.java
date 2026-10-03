package com.eshopping.forms;

import com.eshopping.model.SanPhamDTO;
import com.eshopping.service.GioHangService;
import com.eshopping.service.SanPhamService;
import com.eshopping.util.FormatUtil;
import com.eshopping.util.UiUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class FrmSanPham extends JFrame {
    private final SanPhamService service;
    private final GioHangService gioHang;
    private final com.eshopping.service.DonHangService donHangService;
    private final JComboBox<String> cboNhom = new JComboBox<>();
    private final DefaultTableModel model = new DefaultTableModel(
            new String[] { "Mã SP", "Tên sản phẩm", "Nhà sản xuất", "Nhóm", "Giá", "Tình trạng", "Tồn kho" }, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(model);

    public FrmSanPham(Frame owner, SanPhamService service, GioHangService gioHang,
            com.eshopping.service.DonHangService donHangService) {
        super("Danh sách sản phẩm");
        this.service = service;
        this.gioHang = gioHang;
        this.donHangService = donHangService;
        buildUI(owner);
        loadCategories();
        loadProducts();
    }

    private void buildUI(Frame owner) {
        setSize(980, 600);
        setLayout(new BorderLayout(10, 10));
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Nhóm sản phẩm:"));
        cboNhom.setPreferredSize(new Dimension(260, 32));
        top.add(cboNhom);
        JButton btnFilter = new JButton("Lọc");
        JButton btnDetail = new JButton("Xem chi tiết");
        JButton btnAdd = new JButton("Thêm vào giỏ");
        JButton btnCart = new JButton("Mở giỏ hàng");
        for (JButton b : new JButton[] { btnFilter, btnDetail, btnAdd, btnCart })
            UiUtil.styleButton(b);
        top.add(btnFilter);
        top.add(btnDetail);
        top.add(btnAdd);
        top.add(btnCart);
        add(top, BorderLayout.NORTH);

        table.setRowHeight(28);
        table.setAutoCreateRowSorter(true);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnFilter.addActionListener(e -> loadProducts());
        btnDetail.addActionListener(e -> showDetail(owner));
        btnAdd.addActionListener(e -> addSelected());
        btnCart.addActionListener(e -> new FrmGioHang(owner, gioHang, donHangService).setVisible(true));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2)
                    showDetail(owner);
            }
        });
        setLocationRelativeTo(owner);
    }

    private void loadCategories() {
        List<SanPhamDTO> all = service.layDanhSachTheoNhom("Tất cả");
        List<String> groups = all.stream().map(SanPhamDTO::getNhomSanPham).distinct().sorted()
                .collect(Collectors.toList());
        cboNhom.addItem("Tất cả");
        groups.forEach(cboNhom::addItem);
    }

    private void loadProducts() {
        model.setRowCount(0);
        String group = String.valueOf(cboNhom.getSelectedItem());
        for (SanPhamDTO p : service.layDanhSachTheoNhom(group)) {
            model.addRow(new Object[] { p.getMaSP(), p.getTenSP(), p.getNhaSanXuat(), p.getNhomSanPham(),
                    FormatUtil.currency(p.getGiaBan()), p.getTinhTrang(), p.getTonKho() });
        }
    }

    private SanPhamDTO selectedProduct() {
        int row = table.getSelectedRow();
        if (row < 0)
            return null;
        int modelRow = table.convertRowIndexToModel(row);
        return service.layChiTiet(model.getValueAt(modelRow, 0).toString());
    }

    private void showDetail(Frame owner) {
        SanPhamDTO p = selectedProduct();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sản phẩm.");
            return;
        }
        new FrmChiTietSanPham(owner, p, gioHang).setVisible(true);
    }

    private void addSelected() {
        SanPhamDTO p = selectedProduct();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một sản phẩm.");
            return;
        }
        String input = JOptionPane.showInputDialog(this, "Nhập số lượng:", "1");
        if (input == null)
            return;
        try {
            int q = Integer.parseInt(input);
            gioHang.themSanPham(p, q);
            JOptionPane.showMessageDialog(this, "Đã thêm vào giỏ hàng.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
