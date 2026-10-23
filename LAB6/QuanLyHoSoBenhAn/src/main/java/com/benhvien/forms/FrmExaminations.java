package com.benhvien.forms;

import com.benhvien.service.ClinicalService;
import com.benhvien.service.DirectoryService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;

public class FrmExaminations extends JFrame {
    private final ClinicalService service = new ClinicalService();
    private final DirectoryService directory = new DirectoryService();
    private final JTextField patient = new JTextField(), episode = new JTextField(), appointment = new JTextField(),
            symptoms = new JTextField(), diagnosis = new JTextField(), notes = new JTextField();
    private final JComboBox<String> doctor = new JComboBox<>(), department = new JComboBox<>(),
            type = new JComboBox<>(new String[] { "Khám điều trị", "Khám định kỳ" });
    private final JTable table = Ui.table("Mã phiếu khám", "Bệnh nhân", "Thời điểm", "Bác sĩ", "Khoa", "Loại",
            "Triệu chứng", "Chẩn đoán", "Trạng thái đợt");

    public FrmExaminations() {
        super("Khám bệnh và lập phiếu khám");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1180, 740);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JPanel f = Ui.fields(3);
        f.add(Ui.labeled("Mã bệnh nhân *", patient));
        f.add(Ui.labeled("Mã đợt điều trị (trống = tạo mới)", episode));
        f.add(Ui.labeled("Mã lịch hẹn định kỳ (nếu có)", appointment));
        f.add(Ui.labeled("Bác sĩ *", doctor));
        f.add(Ui.labeled("Khoa *", department));
        f.add(Ui.labeled("Loại phiếu khám", type));
        f.add(Ui.labeled("Triệu chứng *", symptoms));
        f.add(Ui.labeled("Chẩn đoán", diagnosis));
        f.add(Ui.labeled("Ghi chú lâm sàng / đợt", notes));
        JButton save = Ui.button("Lưu phiếu khám", this::save);
        JButton reload = Ui.button("Làm mới danh sách", this::refresh);
        JPanel n = new JPanel(new BorderLayout());
        n.add(f, BorderLayout.CENTER);
        n.add(Ui.buttons(save, reload), BorderLayout.SOUTH);
        add(n, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        try {
            for (var d : directory.doctors())
                doctor.addItem(d.get("MaBacSi") + " - " + d.get("BacSi") + " | khoa " + d.get("MaKhoa") + " - "
                        + d.get("Khoa"));
            for (var d : directory.departments())
                department.addItem(d.get("MaKhoa") + " - " + d.get("TenKhoa"));
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
        refresh();
    }

    private long comboId(JComboBox<String> c) {
        String s = Ui.comboValue(c);
        if (s.isBlank())
            throw new IllegalArgumentException("Chọn bác sĩ và khoa.");
        return Long.parseLong(s.split(" - ")[0]);
    }

    private void save() {
        try {
            String ep = Ui.str(episode), ap = Ui.str(appointment);
            Long epId = ep.isEmpty() ? null : Long.parseLong(ep);
            Long apId = ap.isEmpty() ? null : Long.parseLong(ap);
            long visit = service.createVisit(Ui.intValue(patient, "Mã bệnh nhân"), (int) comboId(doctor),
                    (int) comboId(department), epId, apId, Ui.comboValue(type), Ui.str(symptoms), Ui.str(diagnosis),
                    Ui.str(notes));
            Ui.info(this, "Đã lưu phiếu khám. Mã phiếu khám: " + visit
                    + (epId == null ? "; đợt điều trị mới đã được tạo." : "."));
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void refresh() {
        try {
            Ui.fill(table, directory.visits());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
