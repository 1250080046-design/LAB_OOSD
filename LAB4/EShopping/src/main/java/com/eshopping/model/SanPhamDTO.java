package com.eshopping.model;

import java.math.BigDecimal;

public class SanPhamDTO {
    private String maSP;
    private String tenSP;
    private String nhaSanXuat;
    private String hinhAnh;
    private String moTa;
    private String thongSoKyThuat;
    private BigDecimal giaBan;
    private String tinhTrang;
    private String nhomSanPham;
    private int tonKho;

    public SanPhamDTO() {
    }

    public SanPhamDTO(String maSP, String tenSP, String nhaSanXuat, String hinhAnh,
            String moTa, String thongSoKyThuat, BigDecimal giaBan,
            String tinhTrang, String nhomSanPham, int tonKho) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.nhaSanXuat = nhaSanXuat;
        this.hinhAnh = hinhAnh;
        this.moTa = moTa;
        this.thongSoKyThuat = thongSoKyThuat;
        this.giaBan = giaBan;
        this.tinhTrang = tinhTrang;
        this.nhomSanPham = nhomSanPham;
        this.tonKho = tonKho;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getNhaSanXuat() {
        return nhaSanXuat;
    }

    public void setNhaSanXuat(String nhaSanXuat) {
        this.nhaSanXuat = nhaSanXuat;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getThongSoKyThuat() {
        return thongSoKyThuat;
    }

    public void setThongSoKyThuat(String thongSoKyThuat) {
        this.thongSoKyThuat = thongSoKyThuat;
    }

    public BigDecimal getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(BigDecimal giaBan) {
        this.giaBan = giaBan;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public String getNhomSanPham() {
        return nhomSanPham;
    }

    public void setNhomSanPham(String nhomSanPham) {
        this.nhomSanPham = nhomSanPham;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }

    @Override
    public String toString() {
        return maSP + " - " + tenSP;
    }
}
