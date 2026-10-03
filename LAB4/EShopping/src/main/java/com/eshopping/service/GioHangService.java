package com.eshopping.service;

import com.eshopping.model.CartItem;
import com.eshopping.model.SanPhamDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GioHangService {
    private final Map<String, CartItem> items = new LinkedHashMap<>();

    public void themSanPham(SanPhamDTO sp, int soLuong) {
        if (sp == null || soLuong <= 0)
            throw new IllegalArgumentException("Sản phẩm/số lượng không hợp lệ.");
        CartItem current = items.get(sp.getMaSP());
        int newQty = (current == null ? 0 : current.getSoLuong()) + soLuong;
        if (newQty > sp.getTonKho())
            throw new IllegalArgumentException("Số lượng vượt tồn kho hiện tại: " + sp.getTonKho());
        if (current == null)
            items.put(sp.getMaSP(), new CartItem(sp, soLuong));
        else
            current.setSoLuong(newQty);
    }

    public void capNhatSoLuong(String maSP, int soLuong) {
        CartItem item = items.get(maSP);
        if (item == null)
            return;
        if (soLuong <= 0) {
            items.remove(maSP);
            return;
        }
        if (soLuong > item.getSanPham().getTonKho())
            throw new IllegalArgumentException("Số lượng vượt tồn kho.");
        item.setSoLuong(soLuong);
    }

    public void xoaSanPham(String maSP) {
        items.remove(maSP);
    }

    public void xoaHet() {
        items.clear();
    }

    public List<CartItem> getItems() {
        return new ArrayList<>(items.values());
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public BigDecimal tinhTamTinh() {
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem x : items.values())
            total = total.add(x.getThanhTien());
        return total;
    }
}
