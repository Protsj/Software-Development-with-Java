package com.example.app;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.UUID;

/** Input helpers that accept numbers plus b/h/q shortcuts. */
final class Console {
    private final Scanner scanner = new Scanner(System.in);
    private final Runnable help;

    Console(Runnable help) {
        this.help = help;
    }

    String line(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            System.out.println();
            throw new CliExitException("Input closed.");
        }
        return scanner.nextLine().trim();
    }

    String requiredText(String prompt, String example) {
        while (true) {
            String value = line(prompt);
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("  Please enter a value" + (example == null ? "." : ", for example: " + example));
        }
    }

    int choice(String prompt, int min, int max) {
        return choice(prompt, min, max, true);
    }

    int choice(String prompt, int min, int max, boolean allowBack) {
        while (true) {
            String value = line(prompt);
            String normalized = value.toLowerCase();

            if (isQuit(normalized)) {
                throw new CliExitException("Quit requested.");
            }
            if (isHelp(normalized)) {
                help.run();
                continue;
            }
            if (isBack(normalized)) {
                if (allowBack && min == 0) {
                    return 0;
                }
                System.out.println("  There is no back option here. Type q if you want to quit.");
                continue;
            }

            try {
                int parsed = Integer.parseInt(value);
                if (parsed >= min && parsed <= max) {
                    return parsed;
                }
                System.out.printf("  Please choose a number from %d to %d.%n", min, max);
            } catch (NumberFormatException ignored) {
                System.out.printf("  Please choose a number from %d to %d, or type h for help.%n", min, max);
            }
        }
    }

    long longNumber(String prompt) {
        while (true) {
            String value = line(prompt);
            String normalized = value.toLowerCase();

            if (isQuit(normalized)) {
                throw new CliExitException("Quit requested.");
            }
            if (isHelp(normalized)) {
                help.run();
                continue;
            }

            try {
                return Long.parseLong(value);
            } catch (NumberFormatException ignored) {
                System.out.println("  Please enter a whole number, for example: 42");
            }
        }
    }

    BigDecimal price(String prompt) {
        while (true) {
            String value = line(prompt);
            String normalized = value.toLowerCase();

            if (isQuit(normalized)) {
                throw new CliExitException("Quit requested.");
            }
            if (isHelp(normalized)) {
                help.run();
                continue;
            }

            try {
                BigDecimal parsed = new BigDecimal(value);
                if (parsed.signum() >= 0) {
                    return parsed;
                }
                System.out.println("  Price cannot be negative. Try something like 24.99");
            } catch (NumberFormatException ignored) {
                System.out.println("  Please enter a decimal amount, for example: 24.99");
            }
        }
    }

    UUID uuid(String prompt) {
        while (true) {
            String value = line(prompt);
            String normalized = value.toLowerCase();

            if (isQuit(normalized)) {
                throw new CliExitException("Quit requested.");
            }
            if (isHelp(normalized)) {
                help.run();
                continue;
            }

            try {
                return UUID.fromString(value);
            } catch (IllegalArgumentException ignored) {
                System.out.println("  That is not a valid ID. Copy the full ID from a listing.");
            }
        }
    }

    boolean confirm(String prompt) {
        while (true) {
            String value = line(prompt + " [y/N]: ").toLowerCase();
            if (isQuit(value)) {
                throw new CliExitException("Quit requested.");
            }
            if (isHelp(value)) {
                help.run();
                continue;
            }
            return value.equals("y") || value.equals("yes");
        }
    }

    private boolean isQuit(String value) {
        return value.equals("q") || value.equals("quit") || value.equals("exit");
    }

    private boolean isHelp(String value) {
        return value.equals("h") || value.equals("help") || value.equals("?");
    }

    private boolean isBack(String value) {
        return value.equals("b") || value.equals("back");
    }
}

/** Used to leave the menu loop cleanly. */
final class CliExitException extends RuntimeException {
    CliExitException(String message) {
        super(message);
    }
}
