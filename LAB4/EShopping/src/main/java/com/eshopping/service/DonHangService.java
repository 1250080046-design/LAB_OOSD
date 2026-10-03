package com.eshopping.service;

import com.eshopping.adapter.PaymentResult;
import com.eshopping.dao.*;
import com.eshopping.data.Db;
import com.eshopping.model.*;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DonHangService {
    private final SanPhamService sanPhamService;
    private final GioHangService gioHangService;
    private final LoaiPhieuDatHangDAO loaiPhieuDAO = new LoaiPhieuDatHangDAO();
    private final KhuVucGiaoHangDAO khuVucDAO = new KhuVucGiaoHangDAO();
    private final BangPhiGiaoHangDAO phiDAO = new BangPhiGiaoHangDAO();
    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final ChiTietDonHangDAO chiTietDAO = new ChiTietDonHangDAO();
    private final GiaoDichThanhToanDAO giaoDichDAO = new GiaoDichThanhToanDAO();
    private final ThanhToanService thanhToanService;
    private final EmailService emailService;

    public DonHangService(SanPhamService sp, GioHangService gh,
            ThanhToanService tt, EmailService email) {
        this.sanPhamService = sp;
        this.gioHangService = gh;
        this.thanhToanService = tt;
        this.emailService = email;
    }

    public List<LoaiPhieuDatHang> layLoaiPhieu() throws Exception {
        return loaiPhieuDAO.findAllActive();
    }

    public List<KhuVucGiaoHang> layKhuVuc() throws Exception {
        return khuVucDAO.findAllActive();
    }

    public BigDecimal tinhPhiGiaoHang(BigDecimal tongTienHang, LoaiPhieuDatHang loai, KhuVucGiaoHang khuVuc)
            throws Exception {
        if (loai == null || khuVuc == null)
            throw new IllegalArgumentException("Chưa chọn loại phiếu/khu vực.");
        BigDecimal fee = phiDAO.findFee(khuVuc.getMaKhuVuc(), loai.getMaLoaiPhieu());
        if (fee == null)
            throw new IllegalArgumentException("Chưa cấu hình phí giao hàng.");
        if (loai.getMienPhiTu() != null && tongTienHang.compareTo(loai.getMienPhiTu()) >= 0)
            return BigDecimal.ZERO;
        return fee;
    }

    public BigDecimal tinhTongThanhToan(BigDecimal tongHang, BigDecimal phiShip) {
        return tongHang.add(phiShip);
    }

    public OrderResult datHang(TaiKhoan taiKhoan, LoaiPhieuDatHang loai, KhuVucGiaoHang khuVuc,
            String tenNguoiNhan, String diaChiNguoiNhan, String dienThoaiNguoiNhan,
            PaymentInfo paymentInfo) throws Exception {
        if (taiKhoan == null || taiKhoan.getKhachHang() == null)
            throw new IllegalArgumentException("Bạn chưa đăng nhập.");
        if (loai == null || khuVuc == null)
            throw new IllegalArgumentException("Chưa chọn loại phiếu hoặc khu vực giao hàng.");
        if (isBlank(tenNguoiNhan) || isBlank(diaChiNguoiNhan) || isBlank(dienThoaiNguoiNhan)) {
            throw new IllegalArgumentException("Thông tin người nhận chưa đầy đủ.");
        }
        List<CartItem> cart = gioHangService.getItems();
        if (cart.isEmpty())
            throw new IllegalArgumentException("Giỏ hàng đang trống.");

        for (CartItem item : cart) {
            if (!sanPhamService.kiemTraTonKho(item.getSanPham().getMaSP(), item.getSoLuong())) {
                throw new IllegalArgumentException("Sản phẩm " + item.getSanPham().getMaSP() + " không đủ tồn kho.");
            }
        }

        BigDecimal tongHang = gioHangService.tinhTamTinh();
        BigDecimal phiShip = tinhPhiGiaoHang(tongHang, loai, khuVuc);
        BigDecimal tongThanhToan = tinhTongThanhToan(tongHang, phiShip);

        PaymentResult paymentResult = thanhToanService.thanhToan(paymentInfo, tongThanhToan);
        if (!paymentResult.isSuccess()) {
            throw new IllegalArgumentException("Thanh toán thất bại: " + paymentResult.getMessage());
        }

        DonHangDTO dh = new DonHangDTO();
        dh.setSoDonHang("DH-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        dh.setMaKH(taiKhoan.getMaKH());
        dh.setMaLoaiPhieu(loai.getMaLoaiPhieu());
        dh.setMaKhuVuc(khuVuc.getMaKhuVuc());
        dh.setHoTenNguoiNhan(tenNguoiNhan.trim());
        dh.setDiaChiNguoiNhan(diaChiNguoiNhan.trim());
        dh.setDienThoaiNguoiNhan(dienThoaiNguoiNhan.trim());
        dh.setTongTienHang(tongHang);
        dh.setPhiGiaoHang(phiShip);
        dh.setTongThanhToan(tongThanhToan);
        dh.setThoiDiemDat(LocalDateTime.now());
        dh.setTrangThaiDonHang("Đã đặt");

        try (Connection cn = Db.getConnection()) {
            cn.setAutoCommit(false);
            try {
                long id = donHangDAO.insert(cn, dh);
                for (CartItem item : cart)
                    chiTietDAO.insert(cn, id, item);
                giaoDichDAO.insert(cn, id, paymentInfo, tongThanhToan, paymentResult);
                donHangDAO.updateStatus(cn, id, "Đã đặt");
                cn.commit();
            } catch (Exception e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }

        String email = taiKhoan.getKhachHang().getEmail();
        boolean emailSent = emailService.guiEmailXacNhan(email, dh.getSoDonHang(), dh.getHoTenNguoiNhan(),
                com.eshopping.util.FormatUtil.currency(tongThanhToan));
        gioHangService.xoaHet();
        return new OrderResult(dh, paymentResult, emailSent);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }

    public record OrderResult(DonHangDTO donHang, PaymentResult payment, boolean emailSent) {
    }
}
