package com.example.product.model;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Common supertype for every orderable item. All generic processors in the
 * system are bounded by {@code <T extends Product>}.
 */
public abstract class Product {
    private final UUID id;
    private final String name;
    private final BigDecimal price;
    private final Category category;

    protected Product(UUID id, String name, BigDecimal price, Category category) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.name = requireText(name, "name");
        this.price = requirePrice(price);
        this.category = Objects.requireNonNull(category, "category must not be null");
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    /** Human-readable, type-specific description used by processing steps. */
    public abstract String describe();

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field + " must not be null");
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value;
    }

    private static BigDecimal requirePrice(BigDecimal price) {
        Objects.requireNonNull(price, "price must not be null");
        if (price.signum() < 0) {
            throw new IllegalArgumentException("price must not be negative");
        }
        return price;
    }
}
