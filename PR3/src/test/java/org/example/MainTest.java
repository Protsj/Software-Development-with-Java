package org.example;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    @Test
    void performsValidCalculation() {
        String output = runMain("6\n3\n/\n");

        assertTrue(output.contains("Result: 2.0"));
        assertTrue(output.contains("Request processing is complete."));
    }

    @Test
    void handlesDivisionByZero() {
        String output = runMain("6\n0\n/\n");

        assertTrue(output.contains("Error: Cannot divide by zero"));
        assertTrue(output.contains("Request processing is complete."));
    }

    @Test
    void handlesNonNumericFirstNumber() {
        String output = runMain("abc\n3\n+\n");

        assertTrue(output.contains("Error: please enter a valid number"));
        assertTrue(output.contains("Request processing is complete."));
    }

    @Test
    void handlesNonNumericSecondNumber() {
        String output = runMain("3\nabc\n+\n");

        assertTrue(output.contains("Error: please enter a valid number"));
        assertTrue(output.contains("Request processing is complete."));
    }

    @Test
    void handlesUnsupportedOperation() {
        String output = runMain("3\n4\n%\n");

        assertTrue(output.contains("Unsupported operation"));
        assertTrue(output.contains("Request processing is complete."));
    }

    @Test
    void handlesCustomExceptionForNonFiniteInput() {
        String output = runMain("NaN\n3\n+\n");

        assertTrue(output.contains("Error: Input must be a finite number"));
        assertTrue(output.contains("Request processing is complete."));
    }

    private String runMain(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream capturedOut = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(capturedOut, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        return capturedOut.toString(StandardCharsets.UTF_8);
    }
}
