package com.eshopping.model;

import java.time.LocalDate;

public class PaymentInfo {
    private String loaiThe;
    private String soThe;
    private LocalDate ngayHetHan;
    private String tenChuThe;
    private String csv;

    public String getLoaiThe() {
        return loaiThe;
    }

    public void setLoaiThe(String loaiThe) {
        this.loaiThe = loaiThe;
    }

    public String getSoThe() {
        return soThe;
    }

    public void setSoThe(String soThe) {
        this.soThe = soThe;
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public String getTenChuThe() {
        return tenChuThe;
    }

    public void setTenChuThe(String tenChuThe) {
        this.tenChuThe = tenChuThe;
    }

    public String getCsv() {
        return csv;
    }

    public void setCsv(String csv) {
        this.csv = csv;
    }
}
