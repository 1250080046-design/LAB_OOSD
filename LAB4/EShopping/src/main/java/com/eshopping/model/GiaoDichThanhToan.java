package com.eshopping.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class GiaoDichThanhToan {
    private long maGiaoDich;
    private long maDonHang;
    private String loaiThe;
    private String soTheDaChe;
    private LocalDate ngayHetHan;
    private String tenChuThe;
    private BigDecimal soTienThanhToan;
    private String maGiaoDichCongThanhToan;
    private LocalDateTime thoiDiemThanhToan;
    private String trangThaiThanhToan;
    private String thongBaoGateway;

    public long getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(long maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public long getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(long maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getLoaiThe() {
        return loaiThe;
    }

    public void setLoaiThe(String loaiThe) {
        this.loaiThe = loaiThe;
    }

    public String getSoTheDaChe() {
        return soTheDaChe;
    }

    public void setSoTheDaChe(String soTheDaChe) {
        this.soTheDaChe = soTheDaChe;
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

    public BigDecimal getSoTienThanhToan() {
        return soTienThanhToan;
    }

    public void setSoTienThanhToan(BigDecimal soTienThanhToan) {
        this.soTienThanhToan = soTienThanhToan;
    }

    public String getMaGiaoDichCongThanhToan() {
        return maGiaoDichCongThanhToan;
    }

    public void setMaGiaoDichCongThanhToan(String maGiaoDichCongThanhToan) {
        this.maGiaoDichCongThanhToan = maGiaoDichCongThanhToan;
    }

    public LocalDateTime getThoiDiemThanhToan() {
        return thoiDiemThanhToan;
    }

    public void setThoiDiemThanhToan(LocalDateTime thoiDiemThanhToan) {
        this.thoiDiemThanhToan = thoiDiemThanhToan;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public String getThongBaoGateway() {
        return thongBaoGateway;
    }

    public void setThongBaoGateway(String thongBaoGateway) {
        this.thongBaoGateway = thongBaoGateway;
    }
}
