package com.eshopping.service;

import com.eshopping.model.TaiKhoan;

public final class Session {
    private static TaiKhoan currentAccount;

    private Session() {
    }

    public static TaiKhoan getCurrentAccount() {
        return currentAccount;
    }

    public static void login(TaiKhoan account) {
        currentAccount = account;
    }

    public static void logout() {
        currentAccount = null;
    }

    public static boolean isLoggedIn() {
        return currentAccount != null;
    }
}
