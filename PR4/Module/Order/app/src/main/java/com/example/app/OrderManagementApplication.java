package com.example.app;

import com.example.order.processing.GenericTypeMismatchException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.ProcessedOrder;
import com.example.order.storage.OrderStorage;
import com.example.order.stream.OrderStreamManager;
import com.example.product.model.Clothing;
import com.example.product.model.Electronics;
import com.example.product.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** Entry point that wires all JPMS modules together. */
public final class OrderManagementApplication {

    public static void main(String[] args) {
        if (Arrays.asList(args).contains("--demo")) {
            runDemo();
        } else {
            new OrderCli().run();
        }
    }

    /** Runs the original non-interactive demonstration. */
    public static void runDemo() {
        DemoDataFactory data = new DemoDataFactory(42L);
        List<Electronics> electronics = data.electronics(12);
        List<Clothing> clothing = data.clothing(12);

        System.out.println("Generated " + electronics.size() + " electronics and "
                + clothing.size() + " clothing orders with JavaFaker.");

        OrderProcessor<Electronics> electronicsProcessor =
                new OrderProcessor<>(Electronics.class, "electronics-line");
        electronicsProcessor.addStep(product -> System.out.printf(
                "[%s] warranty check: %s (%s)%n",
                Thread.currentThread().getName(), product.getName(), product.getBrand()));

        OrderProcessor<Clothing> clothingProcessor =
                new OrderProcessor<>(Clothing.class, "clothing-line");
        clothingProcessor.addStep(product -> System.out.printf(
                "[%s] size/material check: %s (%s, %s)%n",
                Thread.currentThread().getName(), product.getName(),
                product.getSize(), product.getMaterial()));

        OrderStreamManager streams = new OrderStreamManager();
        OrderStorage<Electronics> electronicsStorage = new OrderStorage<>();
        OrderStorage<Clothing> clothingStorage = new OrderStorage<>();

        // Fixed pool: each Callable lambda is one processing task.
        List<ProcessedOrder<Electronics>> processedElectronics =
                streams.processWithExecutor(electronics, electronicsProcessor, 4);
        processedElectronics.forEach(electronicsStorage::save); // method reference

        // Parallel stream: method-reference mapping plus method-reference saving.
        streams.processInParallel(clothing, clothingProcessor)
                .forEach(clothingStorage::save);

        // Lambda predicate + method-reference cast filter a mixed list by subtype.
        List<Product> mixed = new ArrayList<>();
        mixed.addAll(electronics);
        mixed.addAll(clothing);
        List<Electronics> onlyElectronics = streams.filterByType(mixed, Electronics.class);
        List<Clothing> onlyClothing = streams.filterByType(mixed, Clothing.class);

        System.out.println("\nFiltered from mixed list: "
                + onlyElectronics.size() + " electronics, " + onlyClothing.size() + " clothing.");
        streams.visitAll(onlyElectronics,
                product -> System.out.println("  matched electronics: " + product.getName()));

        demonstrateGenericTypeMismatch(electronicsProcessor, clothing.get(0));

        System.out.println("\n--- Summary ---");
        System.out.println("Electronics stored: " + electronicsStorage.size());
        System.out.println("Clothing stored:    " + clothingStorage.size());
        System.out.println("Threads used (electronics): "
                + electronicsStorage.getProcessingThreads().stream().distinct().sorted().toList());
        System.out.println("Threads used (clothing):    "
                + clothingStorage.getProcessingThreads().stream().distinct().sorted().toList());
    }

    /**
     * Circumvents compile-time generic checking with a raw cast so the runtime
     * Class&lt;T&gt; token check can be demonstrated. Normal code cannot pass a
     * Clothing to an OrderProcessor&lt;Electronics&gt;.
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void demonstrateGenericTypeMismatch(
            OrderProcessor<? extends Product> processor, Product wrongProduct) {
        try {
            ((OrderProcessor) processor).process(wrongProduct);
        } catch (GenericTypeMismatchException ex) {
            System.out.println("\nExpected generic-type error caught: " + ex.getMessage());
        }
    }
}
