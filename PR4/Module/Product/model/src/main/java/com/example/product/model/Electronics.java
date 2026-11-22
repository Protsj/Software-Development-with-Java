package com.example.product.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/** An electronic product, created through Lombok's generated builder. */
public final class Electronics extends Product {
    private final String brand;
    private final int warrantyMonths;

    @Builder
    public Electronics(UUID id, String name, BigDecimal price, String brand, int warrantyMonths) {
        super(id, name, price, Category.ELECTRONICS);
        this.brand = Objects.requireNonNull(brand, "brand must not be null");
        if (warrantyMonths < 0) {
            throw new IllegalArgumentException("warrantyMonths must not be negative");
        }
        this.warrantyMonths = warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String describe() {
        return "Electronics{name='" + getName() + "', brand='" + brand
                + "', warranty=" + warrantyMonths + " months}";
    }

    @Override
    public String toString() {
        return describe();
    }
}
