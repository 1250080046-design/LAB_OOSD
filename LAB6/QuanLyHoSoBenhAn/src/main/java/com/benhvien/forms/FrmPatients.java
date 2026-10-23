package com.benhvien.forms;

import com.benhvien.service.PatientService;
import com.benhvien.util.Ui;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class FrmPatients extends JFrame {
    private final PatientService service = new PatientService();
    private final JTextField name = new JTextField(), address = new JTextField(), dob = new JTextField("2000-01-01"),
            phone = new JTextField(), photo = new JTextField(),
            year = new JTextField(String.valueOf(LocalDate.now().getYear())), reference = new JTextField();
    private final JComboBox<String> method = new JComboBox<>(new String[] { "Tiền mặt", "Chuyển khoản", "Thẻ" });
    private final JTextField search = new JTextField();
    private final JTable table = Ui.table("Mã bệnh nhân", "Họ tên", "Ngày sinh", "Điện thoại", "Địa chỉ", "Mã hồ sơ",
            "Ngày cấp");
    private final JLabel feeLabel = new JLabel("Lệ phí năm: đang tải...");

    public FrmPatients() {
        super("Đăng ký bệnh nhân - lệ phí năm - cấp hồ sơ");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1120, 760);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));
        JPanel form = Ui.fields(3);
        form.add(Ui.labeled("Họ tên *", name));
        form.add(Ui.labeled("Ngày sinh (yyyy-MM-dd) *", dob));
        form.add(Ui.labeled("Điện thoại *", phone));
        form.add(Ui.labeled("Địa chỉ *", address));
        form.add(Ui.labeled("Đường dẫn ảnh 3x4 (nếu có)", photo));
        form.add(Ui.labeled("Năm lệ phí", year));
        form.add(Ui.labeled("Hình thức thu lệ phí", method));
        form.add(Ui.labeled("Mã tham chiếu (nếu có)", reference));
        form.add(feeLabel);
        JButton register = Ui.button("Đăng ký + thu lệ phí + cấp hồ sơ", this::register);
        JButton annual = Ui.button("Thu lệ phí năm cho bệnh nhân đã chọn", this::payAnnualFee);
        JButton reload = Ui.button("Làm mới danh sách", this::refresh);
        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(Ui.buttons(register, annual, reload), BorderLayout.SOUTH);
        add(north, BorderLayout.NORTH);
        JPanel searchPanel = new JPanel(new BorderLayout(6, 6));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tra cứu bệnh nhân / hồ sơ"));
        searchPanel.add(search, BorderLayout.CENTER);
        searchPanel.add(Ui.buttons(Ui.button("Tìm kiếm", this::refresh), Ui.button("Xóa bộ lọc", () -> {
            search.setText("");
            refresh();
        })), BorderLayout.EAST);
        JPanel bottom = new JPanel(new BorderLayout(5, 5));
        bottom.add(searchPanel, BorderLayout.NORTH);
        bottom.add(new JScrollPane(table), BorderLayout.CENTER);
        bottom.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        add(bottom, BorderLayout.CENTER);
        year.addActionListener(e -> updateFee());
        refresh();
        updateFee();
    }

    private void updateFee() {
        try {
            feeLabel.setText("Lệ phí năm " + Ui.str(year) + ": "
                    + service.annualFee(Ui.intValue(year, "Năm lệ phí")).toPlainString() + " VNĐ (cấu hình demo)");
        } catch (Exception ex) {
            feeLabel.setText("Chưa cấu hình lệ phí cho năm đã chọn.");
        }
    }

    private void register() {
        try {
            int id = service.registerFirstTime(Ui.str(name), Ui.str(address), Ui.date(Ui.str(dob), "Ngày sinh"),
                    Ui.str(phone), Ui.str(photo), Ui.intValue(year, "Năm lệ phí"), Ui.comboValue(method),
                    Ui.str(reference));
            Ui.info(this, "Đăng ký thành công. Mã bệnh nhân: " + id
                    + ". Hệ thống đã ghi nhận lệ phí năm và cấp hồ sơ bệnh án.");
            refresh();
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void payAnnualFee() {
        try {
            if (table.getSelectedRow() < 0)
                throw new IllegalArgumentException("Chọn bệnh nhân ở bảng bên dưới trước.");
            int id = Integer.parseInt(Ui.cell(table, table.getSelectedRow(), "MaBenhNhan").toString());
            int feeYear = Ui.intValue(year, "Năm lệ phí");
            service.payAnnualFee(id, feeYear, Ui.comboValue(method), Ui.str(reference));
            Ui.info(this, "Đã ghi nhận lệ phí năm " + feeYear + " cho bệnh nhân " + id + ".");
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private void refresh() {
        try {
            Ui.fill(table, service.searchPatients(Ui.str(search)));
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
