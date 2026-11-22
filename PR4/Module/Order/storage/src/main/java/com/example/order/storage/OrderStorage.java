package com.example.order.storage;

import com.example.order.processing.ProcessedOrder;
import com.example.order.storage.internal.StorageIndex;
import com.example.product.model.Product;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/** Thread-safe, generic storage for processed orders. */
public final class OrderStorage<T extends Product> {
    private final ConcurrentMap<UUID, ProcessedOrder<T>> orders = new ConcurrentHashMap<>();
    private final StorageIndex index = new StorageIndex();

    public void save(ProcessedOrder<T> processed) {
        Objects.requireNonNull(processed, "processed must not be null");
        UUID id = processed.product().getId();
        orders.put(id, processed);
        index.record(id, processed.processedBy());
    }

    public Optional<ProcessedOrder<T>> findById(UUID id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<ProcessedOrder<T>> findAll() {
        return List.copyOf(orders.values());
    }

    public long size() {
        return orders.size();
    }

    /** Audit helper backed by the module's non-exported internal package. */
    public List<String> getProcessingThreads() {
        return index.threadNames();
    }
}
