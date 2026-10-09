package com.tourcompany.services;

public final class KetQuaXuLy {
    private final boolean thanhCong;
    private final String thongBao;
    private KetQuaXuLy(boolean thanhCong, String thongBao) { this.thanhCong = thanhCong; this.thongBao = thongBao; }
    public boolean isThanhCong() { return thanhCong; }
    public String getThongBao() { return thongBao; }
    public static KetQuaXuLy ok(String msg) { return new KetQuaXuLy(true, msg); }
    public static KetQuaXuLy fail(String msg) { return new KetQuaXuLy(false, msg); }
}
