package com.example.order.processing;

/**
 * A generic-specific exception.
 *
 * <p>Because of type erasure, {@code OrderProcessor<T>} cannot check {@code T}
 * with {@code instanceof T} at runtime. It therefore carries a {@code Class<T>}
 * token and throws this exception when the token and the actual product differ.
 * Without that explicit check the wrong subtype could silently enter a
 * processor and fail later with an opaque {@link ClassCastException}.
 */
public final class GenericTypeMismatchException extends OrderProcessingException {
    private final Class<?> expectedType;
    private final Class<?> actualType;

    public GenericTypeMismatchException(Class<?> expectedType, Class<?> actualType) {
        super("Generic type mismatch: expected " + expectedType.getName()
                + " but received " + (actualType == null ? "null" : actualType.getName()));
        this.expectedType = expectedType;
        this.actualType = actualType;
    }

    public Class<?> getExpectedType() {
        return expectedType;
    }

    public Class<?> getActualType() {
        return actualType;
    }
}
