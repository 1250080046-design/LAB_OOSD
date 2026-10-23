package com.benhvien.forms;

import com.benhvien.service.PatientService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class FrmHistory extends JFrame {
    private final PatientService service = new PatientService();
    private final JTextField keyword = new JTextField();
    private final JTable patients = Ui.table("Mã bệnh nhân", "Họ tên", "Ngày sinh", "Điện thoại", "Địa chỉ", "Mã hồ sơ",
            "Ngày cấp");
    private final JTable history = Ui.table("Loại", "Thời điểm", "Tham chiếu", "Nội dung", "Trạng thái");

    public FrmHistory() {
        super("Tra cứu bệnh nhân và lịch sử bệnh án");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6, 6));
        JPanel s = new JPanel(new BorderLayout(6, 6));
        s.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        s.add(new JLabel("Mã bệnh nhân / mã hồ sơ / họ tên / điện thoại:"), BorderLayout.WEST);
        s.add(keyword, BorderLayout.CENTER);
        s.add(Ui.buttons(Ui.button("Tìm", this::search), Ui.button("Hiện tất cả", () -> {
            keyword.setText("");
            search();
        })), BorderLayout.EAST);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(patients),
                new JScrollPane(history));
        split.setResizeWeight(.42);
        split.setBorder(BorderFactory.createTitledBorder("Chọn bệnh nhân ở bảng trên để xem lịch sử ở bảng dưới"));
        patients.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && patients.getSelectedRow() >= 0)
                loadHistory();
        });
        add(s, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);
        search();
    }

    private void search() {
        try {
            Ui.fill(patients, service.searchPatients(Ui.str(keyword)));
            if (patients.getRowCount() > 0)
                patients.setRowSelectionInterval(0, 0);
            else
                Ui.fill(history, List.of());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void loadHistory() {
        try {
            Object id = Ui.cell(patients, patients.getSelectedRow(), "MaBenhNhan");
            if (id != null)
                Ui.fill(history, service.history(Integer.parseInt(id.toString())));
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
