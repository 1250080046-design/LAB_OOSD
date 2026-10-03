package com.eshopping.model;

import java.math.BigDecimal;

public class CartItem {
    private final SanPhamDTO sanPham;
    private int soLuong;

    public CartItem(SanPhamDTO sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public SanPhamDTO getSanPham() {
        return sanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getThanhTien() {
        return sanPham.getGiaBan().multiply(BigDecimal.valueOf(soLuong));
    }
}
