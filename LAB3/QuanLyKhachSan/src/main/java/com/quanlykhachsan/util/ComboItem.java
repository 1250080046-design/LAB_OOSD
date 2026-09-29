package com.quanlykhachsan.util;

public record ComboItem(String value, String label) {
    @Override
    public String toString() {
        return label;
    }
}
