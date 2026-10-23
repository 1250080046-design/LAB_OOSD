package com.benhvien.forms;

import com.benhvien.service.ReportService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;

public class FrmReports extends JFrame {
    private final ReportService service = new ReportService();
    private final JTextArea summary = new JTextArea(8, 80);
    private final JTable table = Ui.table("Năm", "Quý", "Số lịch hẹn");

    public FrmReports() {
        super("Thống kê quản lý hồ sơ bệnh án");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        summary.setEditable(false);
        summary.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 15));
        JButton reload = Ui.button("Cập nhật thống kê", this::refresh);
        JPanel top = new JPanel(new BorderLayout());
        top.setBorder(BorderFactory.createTitledBorder("Tổng quan hệ thống"));
        top.add(new JScrollPane(summary), BorderLayout.CENTER);
        top.add(Ui.buttons(reload), BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBorder(BorderFactory.createTitledBorder("Số lịch khám định kỳ theo quý"));
        bottom.add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.CENTER);
        refresh();
    }

    private void refresh() {
        try {
            var rows = service.summary();
            if (!rows.isEmpty()) {
                var r = rows.get(0);
                summary.setText(
                        "Tổng bệnh nhân             : " + r.get("TongBenhNhan") + "\nHồ sơ đang hoạt động        : "
                                + r.get("HoSoDangHoatDong") + "\nĐợt đang điều trị           : "
                                + r.get("DotDangDieuTri") + "\nĐợt đã hoàn tất             : " + r.get("DotDaHoanTat")
                                + "\nHóa đơn chưa thanh toán     : " + r.get("HoaDonChuaThanhToan")
                                + "\nLịch hẹn đang chờ            : " + r.get("LichHenSapToi"));
            }
            Ui.fill(table, service.periodicCoverage());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
