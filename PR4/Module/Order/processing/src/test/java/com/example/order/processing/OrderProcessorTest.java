package com.example.order.processing;

import com.example.product.model.Clothing;
import com.example.product.model.Electronics;
import com.example.product.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderProcessorTest {
    private final Electronics electronics = Electronics.builder()
            .id(UUID.randomUUID())
            .name("Laptop")
            .price(BigDecimal.valueOf(999))
            .brand("Acme")
            .warrantyMonths(24)
            .build();

    private final Clothing clothing = Clothing.builder()
            .id(UUID.randomUUID())
            .name("Jacket")
            .price(BigDecimal.valueOf(80))
            .size("M")
            .material("Wool")
            .build();

    @Test
    void runsRegisteredStepsAndRecordsCurrentThread() {
        AtomicInteger calls = new AtomicInteger();
        OrderProcessor<Electronics> processor = new OrderProcessor<>(Electronics.class)
                .addStep(product -> calls.incrementAndGet())
                .addStep(product -> assertTrue(product.getName().contains("Laptop")));

        ProcessedOrder<Electronics> result = processor.process(electronics);

        assertSame(electronics, result.product());
        assertEquals(1, calls.get());
        assertEquals(Thread.currentThread().getName(), result.processedBy());
    }

    @Test
    void rejectsWrongSubtypeDespiteErasure() {
        OrderProcessor<Electronics> processor = new OrderProcessor<>(Electronics.class);

        GenericTypeMismatchException error = assertThrows(
                GenericTypeMismatchException.class,
                () -> processWithErasedType(processor, clothing));

        assertEquals(Electronics.class, error.getExpectedType());
        assertEquals(Clothing.class, error.getActualType());
    }

    @Test
    void wrapsFailingStepInOrderProcessingException() {
        OrderProcessor<Clothing> processor = new OrderProcessor<>(Clothing.class)
                .addStep(product -> {
                    throw new IllegalStateException("out of stock");
                });

        OrderProcessingException error = assertThrows(
                OrderProcessingException.class,
                () -> processor.process(clothing));

        assertTrue(error.getMessage().contains("Jacket"));
        assertTrue(error.getCause() instanceof IllegalStateException);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void processWithErasedType(OrderProcessor<?> processor, Product product) {
        ((OrderProcessor) processor).process(product);
    }
}
