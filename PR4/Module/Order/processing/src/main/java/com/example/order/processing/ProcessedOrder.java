package com.example.order.processing;

import com.example.product.model.Product;

import java.time.Instant;
import java.util.Objects;

/** Immutable result of one processing run. */
public record ProcessedOrder<T extends Product>(T product, String processedBy, Instant processedAt) {
    public ProcessedOrder {
        Objects.requireNonNull(product, "product must not be null");
        Objects.requireNonNull(processedBy, "processedBy must not be null");
        Objects.requireNonNull(processedAt, "processedAt must not be null");
    }
}
