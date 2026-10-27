package org.example;

public class Calculator {

    public double add(double a, double b) {
        validate(a, b);
        return a + b;
    }

    public double subtract(double a, double b) {
        validate(a, b);
        return a - b;
    }

    public double multiply(double a, double b) {
        validate(a, b);
        return a * b;
    }

    public double divide(double a, double b) {
        validate(a, b);
        if (b == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        return a / b;
    }

    private void validate(double... values) {
        for (double value : values) {
            if (!Double.isFinite(value)) {
                throw new InvalidInputException("Input must be a finite number, but was: " + value);
            }
        }
    }
}
