package com.benhvien.model;

public record PrescriptionItemData(String medicineName, String dosage, String frequency,
        int durationDays, int quantity, String instructions) {
    public PrescriptionItemData {
        if (medicineName == null || medicineName.isBlank())
            throw new IllegalArgumentException("Tên thuốc không được để trống.");
        if (dosage == null || dosage.isBlank())
            throw new IllegalArgumentException("Liều dùng không được để trống.");
        if (frequency == null || frequency.isBlank())
            throw new IllegalArgumentException("Cách dùng không được để trống.");
        if (durationDays <= 0 || quantity <= 0)
            throw new IllegalArgumentException("Số ngày và số lượng phải lớn hơn 0.");
    }
}
