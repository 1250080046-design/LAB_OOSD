package com.benhvien.forms;

import com.benhvien.service.BillingService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class FrmBilling extends JFrame {
    private final BillingService service = new BillingService();
    private final JTextField episode = new JTextField(), total = new JTextField(), invoiceId = new JTextField(),
            amount = new JTextField(), reference = new JTextField();
    private final JComboBox<String> method = new JComboBox<>(
            new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ", "Ví điện tử" });
    private final JTable episodes = Ui.table("Mã đợt điều trị", "Mã bệnh nhân", "Bệnh nhân", "Bắt đầu", "Trạng thái");
    private final JTable invoices = Ui.table("Mã hóa đơn", "Mã đợt điều trị", "Bệnh nhân", "Tổng viện phí",
            "Đã thanh toán", "Còn lại", "Trạng thái", "Ngày lập");

    public FrmBilling() {
        super("Viện phí và hoàn tất đợt điều trị");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 780);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JPanel create = Ui.fields(3);
        create.setBorder(
                BorderFactory.createTitledBorder("Lập / cập nhật hóa đơn (chỉ cập nhật trước khi có thanh toán)"));
        create.add(Ui.labeled("Mã đợt điều trị *", episode));
        create.add(Ui.labeled("Tổng viện phí (VNĐ) *", total));
        create.add(new JLabel("Hãy chọn đợt đang điều trị ở bảng dưới."));
        JPanel pay = Ui.fields(4);
        pay.setBorder(BorderFactory.createTitledBorder("Ghi nhận thanh toán viện phí"));
        pay.add(Ui.labeled("Mã hóa đơn *", invoiceId));
        pay.add(Ui.labeled("Số tiền thanh toán *", amount));
        pay.add(Ui.labeled("Hình thức", method));
        pay.add(Ui.labeled("Mã tham chiếu", reference));
        JButton createBtn = Ui.button("Lập hóa đơn", this::createInvoice);
        JButton payBtn = Ui.button("Ghi nhận thanh toán", this::pay);
        JButton complete = Ui.button("Hoàn tất đợt đã chọn", this::complete);
        JButton reload = Ui.button("Làm mới", this::refresh);
        JPanel top = new JPanel(new BorderLayout());
        top.add(create, BorderLayout.NORTH);
        top.add(pay, BorderLayout.CENTER);
        top.add(Ui.buttons(createBtn, payBtn, complete, reload), BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(episodes),
                new JScrollPane(invoices));
        split.setResizeWeight(.35);
        split.setBorder(BorderFactory.createTitledBorder("Các đợt điều trị và hóa đơn viện phí"));
        add(split, BorderLayout.CENTER);
        episodes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && episodes.getSelectedRow() >= 0)
                episode.setText(String.valueOf(Ui.cell(episodes, episodes.getSelectedRow(), "MaDotDieuTri")));
        });
        invoices.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && invoices.getSelectedRow() >= 0) {
                invoiceId.setText(String.valueOf(Ui.cell(invoices, invoices.getSelectedRow(), "MaHoaDon")));
                episode.setText(String.valueOf(Ui.cell(invoices, invoices.getSelectedRow(), "MaDotDieuTri")));
            }
        });
        refresh();
    }

    private void createInvoice() {
        try {
            long id = service.createInvoice(Long.parseLong(Ui.str(episode)), new BigDecimal(Ui.str(total)));
            Ui.info(this, "Đã lưu hóa đơn viện phí. Mã hóa đơn: " + id);
            invoiceId.setText(String.valueOf(id));
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void pay() {
        try {
            service.payInvoice(Long.parseLong(Ui.str(invoiceId)), new BigDecimal(Ui.str(amount)), Ui.comboValue(method),
                    Ui.str(reference));
            Ui.info(this, "Đã ghi nhận khoản thanh toán.");
            amount.setText("");
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void complete() {
        try {
            long id;
            if (episodes.getSelectedRow() >= 0)
                id = Long.parseLong(Ui.cell(episodes, episodes.getSelectedRow(), "MaDotDieuTri").toString());
            else
                id = Long.parseLong(Ui.str(episode));
            service.completeEpisode(id);
            Ui.info(this, "Đợt điều trị đã hoàn tất vì viện phí đã được thanh toán đủ.");
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void refresh() {
        try {
            Ui.fill(episodes, service.episodesOpen());
            Ui.fill(invoices, service.invoices());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
