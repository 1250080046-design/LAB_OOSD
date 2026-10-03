package com.eshopping.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class KhachHang {
    private int maKH;
    private String hoTen;
    private LocalDate ngaySinh;
    private String soGiayTo;
    private String loaiGiayTo;
    private String diaChi;
    private String dienThoai;
    private String email;
    private LocalDateTime ngayDangKy;

    public KhachHang() {
    }

    public KhachHang(int maKH, String hoTen, LocalDate ngaySinh, String soGiayTo,
            String loaiGiayTo, String diaChi, String dienThoai, String email,
            LocalDateTime ngayDangKy) {
        this.maKH = maKH;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.soGiayTo = soGiayTo;
        this.loaiGiayTo = loaiGiayTo;
        this.diaChi = diaChi;
        this.dienThoai = dienThoai;
        this.email = email;
        this.ngayDangKy = ngayDangKy;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getSoGiayTo() {
        return soGiayTo;
    }

    public void setSoGiayTo(String soGiayTo) {
        this.soGiayTo = soGiayTo;
    }

    public String getLoaiGiayTo() {
        return loaiGiayTo;
    }

    public void setLoaiGiayTo(String loaiGiayTo) {
        this.loaiGiayTo = loaiGiayTo;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getDienThoai() {
        return dienThoai;
    }

    public void setDienThoai(String dienThoai) {
        this.dienThoai = dienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDateTime ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }
}
