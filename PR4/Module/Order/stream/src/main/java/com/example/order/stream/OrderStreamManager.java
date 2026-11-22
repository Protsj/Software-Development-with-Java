package com.example.order.stream;

import com.example.order.processing.OrderProcessingException;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.ProcessedOrder;
import com.example.product.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Predicate;

/** Stream and thread orchestration for order processing. */
public final class OrderStreamManager {

    /** Sequential stream; {@code processor::process} is a method reference. */
    public <T extends Product> List<ProcessedOrder<T>> processSequentially(
            List<T> orders, OrderProcessor<T> processor) {
        return orders.stream()
                .map(processor::process)
                .toList();
    }

    /** Parallel stream uses the ForkJoin common pool. */
    public <T extends Product> List<ProcessedOrder<T>> processInParallel(
            List<T> orders, OrderProcessor<T> processor) {
        return orders.parallelStream()
                .map(processor::process)
                .toList();
    }

    /**
     * Filters a mixed list down to one product subtype.
     * The predicate is a lambda; the mapping uses a method reference.
     */
    public <T extends Product> List<T> filterByType(List<? extends Product> orders, Class<T> type) {
        Predicate<Product> isOfType = product -> type.isInstance(product);
        return orders.stream()
                .filter(isOfType)
                .map(type::cast)
                .toList();
    }

    /** Generic visitor usable with a lambda or a method reference. */
    public <T extends Product> void visitAll(List<T> orders, Consumer<T> action) {
        orders.forEach(action);
    }

    /**
     * Explicit multithreading: each order becomes a {@link Callable} lambda,
     * executed on a fixed thread pool.
     */
    public <T extends Product> List<ProcessedOrder<T>> processWithExecutor(
            List<T> orders, OrderProcessor<T> processor, int threadCount) {
        if (threadCount < 1) {
            throw new IllegalArgumentException("threadCount must be at least 1");
        }

        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        try {
            List<Callable<ProcessedOrder<T>>> tasks = orders.stream()
                    .map(order -> (Callable<ProcessedOrder<T>>) () -> processor.process(order))
                    .toList();

            List<Future<ProcessedOrder<T>>> futures;
            try {
                futures = pool.invokeAll(tasks);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new OrderProcessingException("Order processing was interrupted", ex);
            }

            List<ProcessedOrder<T>> results = new ArrayList<>();
            for (Future<ProcessedOrder<T>> future : futures) {
                try {
                    results.add(future.get());
                } catch (ExecutionException ex) {
                    throw new OrderProcessingException(
                            "Worker failed while processing an order", ex.getCause());
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    throw new OrderProcessingException("Order processing was interrupted", ex);
                }
            }
            return results;
        } finally {
            pool.shutdown();
        }
    }
}
