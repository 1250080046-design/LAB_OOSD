package com.quanlykhachsan.model;

public record KetQuaXuLy(boolean thanhCong, String thongBao) {
    public static KetQuaXuLy ok(String message) {
        return new KetQuaXuLy(true, message);
    }

    public static KetQuaXuLy fail(String message) {
        return new KetQuaXuLy(false, message);
    }
}
