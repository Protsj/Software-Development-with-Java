package com.example.order.stream;

import com.example.order.processing.OrderProcessor;
import com.example.order.processing.ProcessedOrder;
import com.example.product.model.Clothing;
import com.example.product.model.Electronics;
import com.example.product.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderStreamManagerTest {
    private final OrderStreamManager manager = new OrderStreamManager();

    private final Electronics electronics = Electronics.builder()
            .id(UUID.randomUUID())
            .name("Phone")
            .price(BigDecimal.valueOf(500))
            .brand("Acme")
            .warrantyMonths(12)
            .build();

    private final Clothing clothing = Clothing.builder()
            .id(UUID.randomUUID())
            .name("Shirt")
            .price(BigDecimal.valueOf(40))
            .size("L")
            .material("Cotton")
            .build();

    @Test
    void filtersMixedListBySubtype() {
        List<Product> mixed = new ArrayList<>();
        mixed.add(electronics);
        mixed.add(clothing);
        mixed.add(electronics);

        assertEquals(List.of(electronics, electronics), manager.filterByType(mixed, Electronics.class));
        assertEquals(List.of(clothing), manager.filterByType(mixed, Clothing.class));
    }

    @Test
    void processesOrdersOnExecutorThreads() {
        OrderProcessor<Electronics> processor =
                new OrderProcessor<>(Electronics.class).addStep(product -> { });

        List<ProcessedOrder<Electronics>> results =
                manager.processWithExecutor(List.of(electronics, electronics), processor, 2);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(result -> result.processedBy().startsWith("pool-")));
    }

    @Test
    void processesOrdersWithParallelStream() {
        OrderProcessor<Clothing> processor =
                new OrderProcessor<>(Clothing.class).addStep(product -> { });

        List<ProcessedOrder<Clothing>> results =
                manager.processInParallel(List.of(clothing, clothing), processor);

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(result -> result.product() == clothing));
    }
}
