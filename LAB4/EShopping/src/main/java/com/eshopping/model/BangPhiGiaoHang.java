package com.eshopping.model;

import java.math.BigDecimal;

public class BangPhiGiaoHang {
    private int maKhuVuc;
    private int maLoaiPhieu;
    private BigDecimal phiGiaoHang;

    public int getMaKhuVuc() {
        return maKhuVuc;
    }

    public void setMaKhuVuc(int maKhuVuc) {
        this.maKhuVuc = maKhuVuc;
    }

    public int getMaLoaiPhieu() {
        return maLoaiPhieu;
    }

    public void setMaLoaiPhieu(int maLoaiPhieu) {
        this.maLoaiPhieu = maLoaiPhieu;
    }

    public BigDecimal getPhiGiaoHang() {
        return phiGiaoHang;
    }

    public void setPhiGiaoHang(BigDecimal phiGiaoHang) {
        this.phiGiaoHang = phiGiaoHang;
    }
}
