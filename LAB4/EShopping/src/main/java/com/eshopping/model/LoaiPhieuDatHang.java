package com.eshopping.model;

import java.math.BigDecimal;

public class LoaiPhieuDatHang {
    private int maLoaiPhieu;
    private String tenLoaiPhieu;
    private int thoiGianXuLyGio;
    private BigDecimal mienPhiTu;
    private String moTa;
    private boolean trangThai;

    public int getMaLoaiPhieu() {
        return maLoaiPhieu;
    }

    public void setMaLoaiPhieu(int maLoaiPhieu) {
        this.maLoaiPhieu = maLoaiPhieu;
    }

    public String getTenLoaiPhieu() {
        return tenLoaiPhieu;
    }

    public void setTenLoaiPhieu(String tenLoaiPhieu) {
        this.tenLoaiPhieu = tenLoaiPhieu;
    }

    public int getThoiGianXuLyGio() {
        return thoiGianXuLyGio;
    }

    public void setThoiGianXuLyGio(int thoiGianXuLyGio) {
        this.thoiGianXuLyGio = thoiGianXuLyGio;
    }

    public BigDecimal getMienPhiTu() {
        return mienPhiTu;
    }

    public void setMienPhiTu(BigDecimal mienPhiTu) {
        this.mienPhiTu = mienPhiTu;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return tenLoaiPhieu;
    }
}
