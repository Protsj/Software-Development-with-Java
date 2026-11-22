package com.example.order.processing;

/** Unchecked failure raised when an order cannot be processed. */
public class OrderProcessingException extends RuntimeException {
    public OrderProcessingException(String message) {
        super(message);
    }

    public OrderProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
