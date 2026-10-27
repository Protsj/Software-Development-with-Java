package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalculatorTest {

    private final Calculator calculator = new Calculator();

    @Test
    void addsTwoNumbers() {
        assertEquals(5.0, calculator.add(2.0, 3.0), 0.0);
    }

    @Test
    void addsNegativeNumbers() {
        assertEquals(-5.0, calculator.add(-2.0, -3.0), 0.0);
    }

    @Test
    void subtractsTwoNumbers() {
        assertEquals(-1.0, calculator.subtract(2.0, 3.0), 0.0);
    }

    @Test
    void multipliesTwoNumbers() {
        assertEquals(6.0, calculator.multiply(2.0, 3.0), 0.0);
    }

    @Test
    void multipliesNegativeNumbers() {
        assertEquals(-6.0, calculator.multiply(-2.0, 3.0), 0.0);
    }

    @Test
    void dividesTwoNumbers() {
        assertEquals(2.0, calculator.divide(6.0, 3.0), 0.0);
    }

    @Test
    void divisionByZeroThrowsArithmeticException() {
        ArithmeticException exception = assertThrows(
                ArithmeticException.class,
                () -> calculator.divide(1.0, 0.0));

        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    @Test
    void divisionByNegativeZeroThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> calculator.divide(1.0, -0.0));
    }

    @Test
    void addRejectsNaN() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> calculator.add(Double.NaN, 1.0));

        assertTrue(exception.getMessage().contains("finite"));
    }

    @Test
    void subtractRejectsPositiveInfinity() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.subtract(1.0, Double.POSITIVE_INFINITY));
    }

    @Test
    void multiplyRejectsNegativeInfinity() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.multiply(Double.NEGATIVE_INFINITY, 1.0));
    }

    @Test
    void divideRejectsNaNBeforeCheckingForZero() {
        assertThrows(
                InvalidInputException.class,
                () -> calculator.divide(Double.NaN, 0.0));
    }

    @Test
    void invalidInputExceptionIsUnchecked() {
        assertTrue(RuntimeException.class.isAssignableFrom(InvalidInputException.class));
    }
}
