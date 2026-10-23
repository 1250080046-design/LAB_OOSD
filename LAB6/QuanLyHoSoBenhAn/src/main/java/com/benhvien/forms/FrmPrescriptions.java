package com.benhvien.forms;

import com.benhvien.model.PrescriptionItemData;
import com.benhvien.service.ClinicalService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrmPrescriptions extends JFrame {
    private final ClinicalService service = new ClinicalService();
    private final JTextField visitId = new JTextField(), notes = new JTextField(), medicine = new JTextField(),
            dosage = new JTextField(), frequency = new JTextField("Ngày 2 lần"), duration = new JTextField("5"),
            quantity = new JTextField("10"), instructions = new JTextField();
    private final JTable draft = Ui.table("Tên thuốc", "Liều dùng", "Cách dùng", "Số ngày", "Số lượng", "Hướng dẫn");
    private final JTable saved = Ui.table("Mã toa", "Mã phiếu khám", "Bệnh nhân", "Bác sĩ", "Ngày lập", "Ghi chú",
            "Trạng thái", "Số loại thuốc");
    private final List<PrescriptionItemData> items = new ArrayList<>();

    public FrmPrescriptions() {
        super("Lập toa thuốc");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1200, 790);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JPanel head = Ui.fields(2);
        head.add(Ui.labeled("Mã phiếu khám *", visitId));
        head.add(Ui.labeled("Ghi chú toa", notes));
        JPanel itemFields = Ui.fields(3);
        itemFields.setBorder(BorderFactory.createTitledBorder("Thêm thuốc vào toa"));
        itemFields.add(Ui.labeled("Tên thuốc *", medicine));
        itemFields.add(Ui.labeled("Liều dùng *", dosage));
        itemFields.add(Ui.labeled("Cách dùng / tần suất *", frequency));
        itemFields.add(Ui.labeled("Số ngày *", duration));
        itemFields.add(Ui.labeled("Số lượng *", quantity));
        itemFields.add(Ui.labeled("Hướng dẫn thêm", instructions));
        JButton add = Ui.button("Thêm thuốc", this::addItem);
        JButton remove = Ui.button("Xóa thuốc đang chọn", this::removeItem);
        JButton save = Ui.button("Lưu toa thuốc + chi tiết", this::save);
        JButton reload = Ui.button("Làm mới toa đã lưu", this::refreshSaved);
        JPanel north = new JPanel(new BorderLayout());
        north.add(head, BorderLayout.NORTH);
        north.add(itemFields, BorderLayout.CENTER);
        north.add(Ui.buttons(add, remove, save, reload), BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(draft), new JScrollPane(saved));
        split.setResizeWeight(.45);
        split.setBorder(BorderFactory.createTitledBorder("Danh sách thuốc chờ lưu / toa thuốc đã ghi nhận"));
        add(split, BorderLayout.CENTER);
        refreshSaved();
    }

    private void addItem() {
        try {
            PrescriptionItemData item = new PrescriptionItemData(Ui.str(medicine), Ui.str(dosage), Ui.str(frequency),
                    Ui.intValue(duration, "Số ngày"), Ui.intValue(quantity, "Số lượng"), Ui.str(instructions));
            items.add(item);
            refreshDraft();
            medicine.setText("");
            dosage.setText("");
            instructions.setText("");
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void removeItem() {
        int i = draft.getSelectedRow();
        if (i >= 0) {
            items.remove(i);
            refreshDraft();
        }
    }

    private void refreshDraft() {
        var m = (javax.swing.table.DefaultTableModel) draft.getModel();
        m.setRowCount(0);
        for (var x : items)
            m.addRow(new Object[] { x.medicineName(), x.dosage(), x.frequency(), x.durationDays(), x.quantity(),
                    x.instructions() });
    }

    private void save() {
        try {
            long id = service.savePrescription(Long.parseLong(Ui.str(visitId)), Ui.str(notes), items);
            Ui.info(this, "Lập toa thuốc thành công. Mã toa: " + id);
            items.clear();
            refreshDraft();
            refreshSaved();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void refreshSaved() {
        try {
            Ui.fill(saved, service.prescriptions());
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
