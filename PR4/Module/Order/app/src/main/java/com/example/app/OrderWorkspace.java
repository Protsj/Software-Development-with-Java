package com.example.app;

import com.example.order.processing.GenericTypeMismatchException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.ProcessedOrder;
import com.example.order.storage.OrderStorage;
import com.example.order.stream.OrderStreamManager;
import com.example.product.model.Clothing;
import com.example.product.model.Electronics;
import com.example.product.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

/** State behind the CLI: pending, processed, and stored orders. */
final class OrderWorkspace {

    enum ProcessingMode {
        SEQUENTIAL("sequential stream"),
        PARALLEL("parallel stream"),
        EXECUTOR("fixed thread pool");

        private final String label;

        ProcessingMode(String label) {
            this.label = label;
        }

        String label() {
            return label;
        }
    }

    private final OrderStreamManager streams = new OrderStreamManager();

    private final OrderProcessor<Electronics> electronicsProcessor =
            new OrderProcessor<>(Electronics.class, "electronics-line")
                    .addStep(product -> System.out.printf(
                            "[%s] warranty check: %s (%s)%n",
                            Thread.currentThread().getName(),
                            product.getName(),
                            product.getBrand()));

    private final OrderProcessor<Clothing> clothingProcessor =
            new OrderProcessor<>(Clothing.class, "clothing-line")
                    .addStep(product -> System.out.printf(
                            "[%s] size/material check: %s (%s, %s)%n",
                            Thread.currentThread().getName(),
                            product.getName(),
                            product.getSize(),
                            product.getMaterial()));

    private final List<Electronics> pendingElectronics = new ArrayList<>();
    private final List<Clothing> pendingClothing = new ArrayList<>();

    private DemoDataFactory dataFactory;
    private long seed;

    private List<ProcessedOrder<Electronics>> processedElectronics = List.of();
    private List<ProcessedOrder<Clothing>> processedClothing = List.of();

    private OrderStorage<Electronics> electronicsStorage = new OrderStorage<>();
    private OrderStorage<Clothing> clothingStorage = new OrderStorage<>();

    OrderWorkspace() {
        setSeed(42L);
    }

    long seed() {
        return seed;
    }

    int pendingElectronicsCount() {
        return pendingElectronics.size();
    }

    int pendingClothingCount() {
        return pendingClothing.size();
    }

    int processedElectronicsCount() {
        return processedElectronics.size();
    }

    int processedClothingCount() {
        return processedClothing.size();
    }

    long storedElectronicsCount() {
        return electronicsStorage.size();
    }

    long storedClothingCount() {
        return clothingStorage.size();
    }

    void setSeed(long seed) {
        this.seed = seed;
        this.dataFactory = new DemoDataFactory(seed);
    }

    void reset() {
        pendingElectronics.clear();
        pendingClothing.clear();
        processedElectronics = List.of();
        processedClothing = List.of();
        electronicsStorage = new OrderStorage<>();
        clothingStorage = new OrderStorage<>();
    }

    void generateElectronics(int count) {
        pendingElectronics.addAll(dataFactory.electronics(count));
    }

    void generateClothing(int count) {
        pendingClothing.addAll(dataFactory.clothing(count));
    }

    void addElectronics(String name, BigDecimal price, String brand, int warrantyMonths) {
        pendingElectronics.add(Electronics.builder()
                .id(UUID.randomUUID())
                .name(name)
                .price(price)
                .brand(brand)
                .warrantyMonths(warrantyMonths)
                .build());
    }

    void addClothing(String name, BigDecimal price, String size, String material) {
        pendingClothing.add(Clothing.builder()
                .id(UUID.randomUUID())
                .name(name)
                .price(price)
                .size(size)
                .material(material)
                .build());
    }

    List<Electronics> pendingElectronics() {
        return List.copyOf(pendingElectronics);
    }

    List<Clothing> pendingClothing() {
        return List.copyOf(pendingClothing);
    }

    List<ProcessedOrder<Electronics>> processedElectronics() {
        return processedElectronics;
    }

    List<ProcessedOrder<Clothing>> processedClothing() {
        return processedClothing;
    }

    List<Product> mixedPending() {
        List<Product> mixed = new ArrayList<>(pendingElectronics);
        mixed.addAll(pendingClothing);
        return mixed;
    }

    List<Electronics> filterPendingElectronics() {
        return streams.filterByType(mixedPending(), Electronics.class);
    }

    List<Clothing> filterPendingClothing() {
        return streams.filterByType(mixedPending(), Clothing.class);
    }

    <T extends Product> void visitAll(List<T> orders, Consumer<T> action) {
        streams.visitAll(orders, action);
    }

    int processElectronics(ProcessingMode mode, int threadCount) {
        processedElectronics = process(pendingElectronics(), electronicsProcessor, mode, threadCount);
        return processedElectronics.size();
    }

    int processClothing(ProcessingMode mode, int threadCount) {
        processedClothing = process(pendingClothing(), clothingProcessor, mode, threadCount);
        return processedClothing.size();
    }

    int storeElectronics() {
        processedElectronics.forEach(electronicsStorage::save);
        return processedElectronics.size();
    }

    int storeClothing() {
        processedClothing.forEach(clothingStorage::save);
        return processedClothing.size();
    }

    List<ProcessedOrder<Electronics>> allStoredElectronics() {
        return electronicsStorage.findAll();
    }

    List<ProcessedOrder<Clothing>> allStoredClothing() {
        return clothingStorage.findAll();
    }

    Optional<ProcessedOrder<Electronics>> findElectronics(UUID id) {
        return electronicsStorage.findById(id);
    }

    Optional<ProcessedOrder<Clothing>> findClothing(UUID id) {
        return clothingStorage.findById(id);
    }

    List<String> electronicsThreads() {
        return electronicsStorage.getProcessingThreads();
    }

    List<String> clothingThreads() {
        return clothingStorage.getProcessingThreads();
    }

    void demonstrateGenericTypeMismatch() {
        Product wrongType = dataFactory.clothing(1).get(0);
        try {
            processWithRawProcessor(wrongType);
        } catch (GenericTypeMismatchException ex) {
            System.out.println("Expected generic-type error caught: " + ex.getMessage());
        }
    }

    private <T extends Product> List<ProcessedOrder<T>> process(
            List<T> orders, OrderProcessor<T> processor, ProcessingMode mode, int threadCount) {
        return switch (mode) {
            case SEQUENTIAL -> streams.processSequentially(orders, processor);
            case PARALLEL -> streams.processInParallel(orders, processor);
            case EXECUTOR -> streams.processWithExecutor(orders, processor, threadCount);
        };
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void processWithRawProcessor(Product product) {
        ((OrderProcessor) electronicsProcessor).process(product);
    }
}
