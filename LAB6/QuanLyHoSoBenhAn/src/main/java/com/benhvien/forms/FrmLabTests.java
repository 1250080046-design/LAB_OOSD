package com.benhvien.forms;

import com.benhvien.service.ClinicalService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;

public class FrmLabTests extends JFrame {
    private final ClinicalService service = new ClinicalService();
    private final JTextField visitId = new JTextField(), testName = new JTextField(), notes = new JTextField(),
            result = new JTextField();
    private final JTable table = Ui.table("Mã xét nghiệm", "Mã phiếu khám", "Bệnh nhân", "Tên xét nghiệm",
            "Ngày yêu cầu", "Kết quả", "Ngày có kết quả", "Trạng thái");

    public FrmLabTests() {
        super("Quản lý xét nghiệm");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1150, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JPanel f = Ui.fields(3);
        f.add(Ui.labeled("Mã phiếu khám *", visitId));
        f.add(Ui.labeled("Tên xét nghiệm *", testName));
        f.add(Ui.labeled("Ghi chú", notes));
        f.add(Ui.labeled("Kết quả xét nghiệm", result));
        JButton add = Ui.button("Yêu cầu xét nghiệm", this::add);
        JButton saveResult = Ui.button("Ghi kết quả cho dòng đã chọn", this::saveResult);
        JButton pick = Ui.button("Lấy mã xét nghiệm đã chọn", () -> {
            if (table.getSelectedRow() >= 0)
                result.setToolTipText("Mã xét nghiệm: " + Ui.cell(table, table.getSelectedRow(), "MaXetNghiem"));
        });
        JButton reload = Ui.button("Làm mới", this::refresh);
        JPanel n = new JPanel(new BorderLayout());
        n.add(f, BorderLayout.CENTER);
        n.add(Ui.buttons(add, saveResult, pick, reload), BorderLayout.SOUTH);
        add(n, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                Object id = Ui.cell(table, table.getSelectedRow(), "MaXetNghiem");
                result.setToolTipText("Mã xét nghiệm đang chọn: " + id);
            }
        });
        refresh();
    }

    private void add() {
        try {
            long id = service.addLabTest(Long.parseLong(Ui.str(visitId)), Ui.str(testName), Ui.str(notes));
            Ui.info(this, "Đã tạo yêu cầu xét nghiệm, mã: " + id);
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void saveResult() {
        try {
            if (table.getSelectedRow() < 0)
                throw new IllegalArgumentException("Chọn xét nghiệm cần ghi kết quả.");
            long id = Long.parseLong(Ui.cell(table, table.getSelectedRow(), "MaXetNghiem").toString());
            service.recordLabResult(id, Ui.str(result));
            Ui.info(this, "Đã ghi kết quả xét nghiệm.");
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void refresh() {
        try {
            Ui.fill(table, service.labTests());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
