package com.eshopping.adapter;

import com.eshopping.model.SanPhamDTO;
import java.util.List;

public interface ProductAdapter {
    List<SanPhamDTO> layDanhSachSanPham(String nhomSanPham);

    SanPhamDTO layChiTietSanPham(String maSP);

    boolean kiemTraTonKho(String maSP, int soLuong);
}
