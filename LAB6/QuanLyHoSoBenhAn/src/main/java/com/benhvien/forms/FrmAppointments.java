package com.benhvien.forms;

import com.benhvien.service.AppointmentService;
import com.benhvien.service.DirectoryService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
public class FrmAppointments extends JFrame {
    private final AppointmentService service = new AppointmentService();
    private final JTextField patientId = new JTextField(),
            dateTime = new JTextField(
                    LocalDateTime.now().plusDays(7).withSecond(0).withNano(0).toString().replace('T', ' ')),
            notes = new JTextField();
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JTable table = Ui.table("Mã lịch hẹn", "Mã bệnh nhân", "Bệnh nhân", "Ngày giờ", "Năm", "Quý",
            "Bác sĩ", "Trạng thái", "Ghi chú");

    public FrmAppointments() {
        super("Lịch khám sức khỏe định kỳ theo quý");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JPanel f = Ui.fields(3);
        f.add(Ui.labeled("Mã bệnh nhân *", patientId));
        f.add(Ui.labeled("Ngày giờ hẹn (yyyy-MM-dd HH:mm) *", dateTime));
        f.add(Ui.labeled("Bác sĩ (không bắt buộc)", doctor));
        f.add(Ui.labeled("Ghi chú", notes));
        JButton book = Ui.button("Đặt lịch định kỳ", this::book);
        JButton cancel = Ui.button("Hủy lịch đã chọn", this::cancel);
        JButton reload = Ui.button("Làm mới", this::refresh);
        JPanel n = new JPanel(new BorderLayout());
        n.add(f, BorderLayout.CENTER);
        n.add(Ui.buttons(book, cancel, reload), BorderLayout.SOUTH);
        add(n, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        try {
            for (var d : new DirectoryService().doctors())
                doctor.addItem(d.get("MaBacSi") + " - " + d.get("BacSi") + " (" + d.get("Khoa") + ")");
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
        refresh();
    }

    private Integer doctorId() {
        String s = Ui.comboValue(doctor);
        if (s.isBlank())
            return null;
        return Integer.parseInt(s.split(" - ")[0]);
    }

    private void book() {
        try {
            long id = service.bookQuarterly(Ui.intValue(patientId, "Mã bệnh nhân"), doctorId(),
                    Ui.dateTime(Ui.str(dateTime), "Ngày giờ hẹn"), Ui.str(notes));
            Ui.info(this,
                    "Đặt lịch khám định kỳ thành công. Mã lịch hẹn: " + id + ". Không thu thêm phí khám định kỳ.");
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void cancel() {
        try {
            if (table.getSelectedRow() < 0)
                throw new IllegalArgumentException("Hãy chọn lịch cần hủy.");
            long id = Long.parseLong(Ui.cell(table, table.getSelectedRow(), "MaLichHen").toString());
            if (JOptionPane.showConfirmDialog(this, "Hủy lịch " + id + "?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                service.cancel(id);
                refresh();
            }
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void refresh() {
        try {
            Ui.fill(table, service.list());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
