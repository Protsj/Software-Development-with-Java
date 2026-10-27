package org.example;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter the first number: ");
            double first = parseNumber(scanner.nextLine());

            System.out.print("Enter the second number: ");
            double second = parseNumber(scanner.nextLine());

            System.out.print("Enter the operation (+, -, *, /): ");
            String operation = scanner.nextLine().trim();

            Calculator calculator = new Calculator();
            double result = switch (operation) {
                case "+" -> calculator.add(first, second);
                case "-" -> calculator.subtract(first, second);
                case "*" -> calculator.multiply(first, second);
                case "/" -> calculator.divide(first, second);
                default -> throw new InvalidInputException(
                        "Unsupported operation: \"" + operation + "\". Use one of: +, -, *, /");
            };

            System.out.println("Result: " + result);
        } catch (NumberFormatException e) {
            System.out.println("Error: please enter a valid number, e.g. 12.5");
        } catch (InvalidInputException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            System.out.println("Request processing is complete.");
        }
    }

    private static double parseNumber(String input) {
        return Double.parseDouble(input.trim());
    }
}
