package com.eshopping.service;

import com.eshopping.adapter.ProductAdapter;
import com.eshopping.adapter.ProductServiceAdapter;
import com.eshopping.model.SanPhamDTO;
import java.util.List;

public class SanPhamService {
    private final ProductAdapter adapter;

    public SanPhamService() {
        this(new ProductServiceAdapter());
    }

    public SanPhamService(ProductAdapter adapter) {
        this.adapter = adapter;
    }

    public List<SanPhamDTO> layDanhSachTheoNhom(String nhom) {
        return adapter.layDanhSachSanPham(nhom);
    }

    public SanPhamDTO layChiTiet(String maSP) {
        return adapter.layChiTietSanPham(maSP);
    }

    public boolean kiemTraTonKho(String maSP, int soLuong) {
        return adapter.kiemTraTonKho(maSP, soLuong);
    }
}
