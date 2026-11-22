package com.example.product.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/** A clothing product, created through Lombok's generated builder. */
public final class Clothing extends Product {
    private final String size;
    private final String material;

    @Builder
    public Clothing(UUID id, String name, BigDecimal price, String size, String material) {
        super(id, name, price, Category.CLOTHING);
        this.size = Objects.requireNonNull(size, "size must not be null");
        this.material = Objects.requireNonNull(material, "material must not be null");
    }

    public String getSize() {
        return size;
    }

    public String getMaterial() {
        return material;
    }

    @Override
    public String describe() {
        return "Clothing{name='" + getName() + "', size='" + size
                + "', material='" + material + "'}";
    }

    @Override
    public String toString() {
        return describe();
    }
}
