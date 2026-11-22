package com.example.order.storage;

import com.example.order.processing.ProcessedOrder;
import com.example.product.model.Electronics;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStorageTest {
    @Test
    void savesAndFindsProcessedOrders() {
        Electronics product = Electronics.builder()
                .id(UUID.randomUUID())
                .name("Tablet")
                .price(BigDecimal.valueOf(300))
                .brand("Acme")
                .warrantyMonths(12)
                .build();
        ProcessedOrder<Electronics> processed =
                new ProcessedOrder<>(product, "worker-1", Instant.now());

        OrderStorage<Electronics> storage = new OrderStorage<>();
        storage.save(processed);

        assertEquals(1, storage.size());
        assertTrue(storage.findById(product.getId()).isPresent());
        assertEquals(List.of(processed), storage.findAll());
        assertEquals(List.of("worker-1"), storage.getProcessingThreads());
    }
}
