package com.example.order.storage.internal;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Internal helper of the storage module. The package is not exported by
 * module-info.java, so other modules cannot see this class at compile time.
 */
public final class StorageIndex {
    private final ConcurrentMap<UUID, String> processedBy = new ConcurrentHashMap<>();

    public void record(UUID orderId, String threadName) {
        processedBy.put(orderId, threadName);
    }

    public List<String> threadNames() {
        return List.copyOf(processedBy.values());
    }
}
