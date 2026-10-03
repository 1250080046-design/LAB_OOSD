package com.eshopping.adapter;

import com.eshopping.model.SanPhamDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Prototype: mô phỏng Hệ thống quản lý sản phẩm bên ngoài. */
public class ProductServiceAdapter implements ProductAdapter {
    private final Map<String, SanPhamDTO> products = new ConcurrentHashMap<>();

    public ProductServiceAdapter() {
        add("SP001", "Laptop Acer Aspire 5", "Acer", "Thiết bị điện tử", "Laptop học tập/văn phòng",
                "Core i5; 16GB RAM; 512GB SSD", 15990000, 12);
        add("SP002", "Laptop Lenovo IdeaPad", "Lenovo", "Thiết bị điện tử", "Laptop đa năng cho sinh viên",
                "Core i5; 16GB RAM; 512GB SSD", 14990000, 8);
        add("SP003", "Tai nghe Bluetooth", "Sony", "Thiết bị điện tử", "Tai nghe không dây", "Bluetooth 5.x; chống ồn",
                1290000, 20);
        add("SP004", "Bàn phím cơ", "Rapoo", "Thiết bị máy tính", "Bàn phím cơ có đèn", "Switch cơ; LED", 890000, 15);
        add("SP005", "Máy ảnh kỹ thuật số", "Canon", "Máy chụp hình kỹ thuật số", "Máy ảnh du lịch", "24MP; quay 4K",
                12490000, 5);
        add("SP006", "Máy xay sinh tố", "Philips", "Đồ gia dụng", "Máy xay gia đình", "600W; cối 1.5L", 1190000, 10);
        add("SP007", "Robot đồ chơi", "ToyCo", "Đồ chơi", "Robot điều khiển", "Điều khiển từ xa; pin sạc", 750000, 25);
        add("SP008", "Màn hình 24 inch", "LG", "Thiết bị máy tính", "Màn hình Full HD", "24 inch; IPS; 75Hz", 3290000,
                9);
    }

    private void add(String ma, String ten, String nsx, String nhom, String moTa, String spec,
            double gia, int tonKho) {
        products.put(ma, new SanPhamDTO(ma, ten, nsx, "", moTa, spec,
                BigDecimal.valueOf(gia), tonKho > 0 ? "Còn hàng" : "Hết hàng", nhom, tonKho));
    }

    @Override
    public List<SanPhamDTO> layDanhSachSanPham(String nhomSanPham) {
        if (nhomSanPham == null || nhomSanPham.isBlank() || "Tất cả".equals(nhomSanPham)) {
            return new ArrayList<>(products.values());
        }
        List<SanPhamDTO> result = new ArrayList<>();
        for (SanPhamDTO p : products.values()) {
            if (nhomSanPham.equalsIgnoreCase(p.getNhomSanPham()))
                result.add(p);
        }
        return result;
    }

    @Override
    public SanPhamDTO layChiTietSanPham(String maSP) {
        return products.get(maSP);
    }

    @Override
    public boolean kiemTraTonKho(String maSP, int soLuong) {
        SanPhamDTO p = products.get(maSP);
        return p != null && soLuong > 0 && p.getTonKho() >= soLuong && "Còn hàng".equals(p.getTinhTrang());
    }
}
