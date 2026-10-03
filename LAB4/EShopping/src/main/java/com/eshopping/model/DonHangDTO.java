package com.eshopping.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DonHangDTO {
    private long maDonHang;
    private String soDonHang;
    private int maKH;
    private int maLoaiPhieu;
    private int maKhuVuc;
    private String hoTenNguoiNhan;
    private String diaChiNguoiNhan;
    private String dienThoaiNguoiNhan;
    private BigDecimal tongTienHang;
    private BigDecimal phiGiaoHang;
    private BigDecimal tongThanhToan;
    private LocalDateTime thoiDiemDat;
    private String trangThaiDonHang;

    public long getMaDonHang() {
        return maDonHang;
    }

    public void setMaDonHang(long maDonHang) {
        this.maDonHang = maDonHang;
    }

    public String getSoDonHang() {
        return soDonHang;
    }

    public void setSoDonHang(String soDonHang) {
        this.soDonHang = soDonHang;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaLoaiPhieu() {
        return maLoaiPhieu;
    }

    public void setMaLoaiPhieu(int maLoaiPhieu) {
        this.maLoaiPhieu = maLoaiPhieu;
    }

    public int getMaKhuVuc() {
        return maKhuVuc;
    }

    public void setMaKhuVuc(int maKhuVuc) {
        this.maKhuVuc = maKhuVuc;
    }

    public String getHoTenNguoiNhan() {
        return hoTenNguoiNhan;
    }

    public void setHoTenNguoiNhan(String hoTenNguoiNhan) {
        this.hoTenNguoiNhan = hoTenNguoiNhan;
    }

    public String getDiaChiNguoiNhan() {
        return diaChiNguoiNhan;
    }

    public void setDiaChiNguoiNhan(String diaChiNguoiNhan) {
        this.diaChiNguoiNhan = diaChiNguoiNhan;
    }

    public String getDienThoaiNguoiNhan() {
        return dienThoaiNguoiNhan;
    }

    public void setDienThoaiNguoiNhan(String dienThoaiNguoiNhan) {
        this.dienThoaiNguoiNhan = dienThoaiNguoiNhan;
    }

    public BigDecimal getTongTienHang() {
        return tongTienHang;
    }

    public void setTongTienHang(BigDecimal tongTienHang) {
        this.tongTienHang = tongTienHang;
    }

    public BigDecimal getPhiGiaoHang() {
        return phiGiaoHang;
    }

    public void setPhiGiaoHang(BigDecimal phiGiaoHang) {
        this.phiGiaoHang = phiGiaoHang;
    }

    public BigDecimal getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(BigDecimal tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public LocalDateTime getThoiDiemDat() {
        return thoiDiemDat;
    }

    public void setThoiDiemDat(LocalDateTime thoiDiemDat) {
        this.thoiDiemDat = thoiDiemDat;
    }

    public String getTrangThaiDonHang() {
        return trangThaiDonHang;
    }

    public void setTrangThaiDonHang(String trangThaiDonHang) {
        this.trangThaiDonHang = trangThaiDonHang;
    }
}
