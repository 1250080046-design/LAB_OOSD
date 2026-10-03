package com.eshopping.service;

import com.eshopping.dao.KhachHangDAO;
import com.eshopping.dao.TaiKhoanDAO;
import com.eshopping.data.Db;
import com.eshopping.model.KhachHang;
import com.eshopping.model.TaiKhoan;
import com.eshopping.util.PasswordUtil;

import java.sql.Connection;

public class TaiKhoanService {
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();
    private final TaiKhoanDAO taiKhoanDAO = new TaiKhoanDAO();

    public void dangKy(KhachHang kh, String tenDangNhap, String matKhau) throws Exception {
        if (kh == null || isBlank(kh.getHoTen()) || isBlank(tenDangNhap) || isBlank(matKhau)) {
            throw new IllegalArgumentException("Họ tên, tên đăng nhập và mật khẩu không được để trống.");
        }
        if (matKhau.length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
        try (Connection cn = Db.getConnection()) {
            cn.setAutoCommit(false);
            try {
                int maKH = khachHangDAO.insert(cn, kh);
                TaiKhoan tk = new TaiKhoan();
                tk.setMaKH(maKH);
                tk.setTenDangNhap(tenDangNhap.trim());
                tk.setMatKhau(PasswordUtil.sha256(matKhau));
                tk.setTrangThai(true);
                taiKhoanDAO.insert(cn, tk);
                cn.commit();
            } catch (Exception e) {
                cn.rollback();
                throw e;
            } finally {
                cn.setAutoCommit(true);
            }
        }
    }

    public TaiKhoan dangNhap(String username, String password) throws Exception {
        if (isBlank(username) || isBlank(password))
            throw new IllegalArgumentException("Vui lòng nhập tài khoản và mật khẩu.");
        TaiKhoan tk = taiKhoanDAO.findByUsername(username.trim());
        if (tk == null)
            throw new IllegalArgumentException("Tài khoản không tồn tại.");
        if (!tk.isTrangThai())
            throw new IllegalArgumentException("Tài khoản đang bị khóa.");
        if (!PasswordUtil.sha256(password).equalsIgnoreCase(tk.getMatKhau())) {
            throw new IllegalArgumentException("Mật khẩu không đúng.");
        }
        Session.login(tk);
        return tk;
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}
