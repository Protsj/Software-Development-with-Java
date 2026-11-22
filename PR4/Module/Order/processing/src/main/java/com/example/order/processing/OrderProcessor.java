package com.example.order.processing;

import com.example.product.model.Product;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Processes orders of one concrete product type.
 *
 * @param <T> the accepted product type, restricted to {@link Product} subtypes
 */
public class OrderProcessor<T extends Product> {
    private final Class<T> productType;
    private final String name;
    private final List<Consumer<T>> steps = new CopyOnWriteArrayList<>();

    public OrderProcessor(Class<T> productType) {
        this(productType, productType.getSimpleName() + "-processor");
    }

    public OrderProcessor(Class<T> productType, String name) {
        this.productType = Objects.requireNonNull(productType, "productType must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
    }

    /** Adds a processing step. Lambdas and method references both fit here. */
    public OrderProcessor<T> addStep(Consumer<T> step) {
        steps.add(Objects.requireNonNull(step, "step must not be null"));
        return this;
    }

    /**
     * Validates the runtime type (erasure-safe), runs every step and returns the result.
     */
    public ProcessedOrder<T> process(T product) {
        T checked = requireMatchingType(product);

        for (Consumer<T> step : steps) {
            try {
                step.accept(checked);
            } catch (RuntimeException ex) {
                throw new OrderProcessingException(
                        "Processing step failed for '" + checked.getName() + "'", ex);
            }
        }

        return new ProcessedOrder<>(checked, Thread.currentThread().getName(), Instant.now());
    }

    /** Void variant intended for {@code list.forEach(processor::processOrder)}. */
    public void processOrder(T product) {
        process(product);
    }

    private T requireMatchingType(Product product) {
        if (product == null) {
            throw new OrderProcessingException("Order product must not be null");
        }
        if (!productType.isInstance(product)) {
            throw new GenericTypeMismatchException(productType, product.getClass());
        }
        return productType.cast(product);
    }

    public Class<T> getProductType() {
        return productType;
    }

    public String getName() {
        return name;
    }
}
