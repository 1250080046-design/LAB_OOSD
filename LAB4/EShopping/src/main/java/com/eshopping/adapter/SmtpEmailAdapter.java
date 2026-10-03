package com.eshopping.adapter;

/** Prototype: mô phỏng dịch vụ email, không gửi email Internet thật. */
public class SmtpEmailAdapter implements EmailAdapter {
    @Override
    public boolean guiEmail(String email, String tieuDe, String noiDung) {
        if (email == null || email.isBlank())
            return false;
        System.out.println("===== EMAIL MOCK =====");
        System.out.println("To: " + email);
        System.out.println("Subject: " + tieuDe);
        System.out.println(noiDung);
        System.out.println("======================");
        return true;
    }
}
