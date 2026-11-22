package com.example.app;

import com.example.app.OrderWorkspace.ProcessingMode;
import com.example.order.processing.ProcessedOrder;
import com.example.product.model.Clothing;
import com.example.product.model.Electronics;
import com.example.product.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/** Friendly, guided menu for creating, processing, and storing orders. */
final class OrderCli {
    private static final String[] TYPE_OPTIONS = {"Electronics", "Clothing", "Both"};

    private final OrderWorkspace workspace = new OrderWorkspace();
    private final Console console = new Console(this::printHelp);

    void run() {
        printWelcome();

        try {
            while (true) {
                printStatus();
                printMainMenu();

                int choice = console.choice("Your choice: ", 0, 10, false);
                switch (choice) {
                    case 0 -> {
                        System.out.println("Bye!");
                        return;
                    }
                    case 1 -> safe(this::guidedTour);
                    case 2 -> safe(this::addOrders);
                    case 3 -> safe(this::showWaitingOrders);
                    case 4 -> safe(this::processWaitingOrders);
                    case 5 -> safe(this::showProcessedResults);
                    case 6 -> safe(this::saveResults);
                    case 7 -> safe(this::browseSavedOrders);
                    case 8 -> safe(this::filterWaitingOrders);
                    case 9 -> safe(this::typeSafetyDemo);
                    case 10 -> safe(this::moreOptions);
                    default -> throw new IllegalStateException("Unexpected option: " + choice);
                }
            }
        } catch (CliExitException ex) {
            System.out.println("Bye!");
        }
    }

    private void printWelcome() {
        System.out.println("""
                ================================================================
                 WELCOME TO THE ORDER DESK
                ================================================================
                Here you can create orders, check them on worker threads,
                save them, and find them again later.

                First time here? Choose 1 for a short guided tour.
                Type h at any menu if you want explanations.
                ================================================================
                """);
    }

    private void printStatus() {
        System.out.printf("%n-- CURRENT STATUS -------------------------------------------%n");
        System.out.printf(
                "Random seed: %-8d | Waiting E/C: %-4d/%-4d | Checked E/C: %-4d/%-4d | Saved E/C: %-4d/%-4d%n",
                workspace.seed(),
                workspace.pendingElectronicsCount(),
                workspace.pendingClothingCount(),
                workspace.processedElectronicsCount(),
                workspace.processedClothingCount(),
                workspace.storedElectronicsCount(),
                workspace.storedClothingCount());
        System.out.println("Normal path: 2 Add -> 4 Check -> 6 Save -> 7 Browse");
    }

    private void printMainMenu() {
        System.out.println("""
                WHAT WOULD YOU LIKE TO DO?

                 1  Guided tour ............... recommended first run
                 2  Add orders ................ type your own or generate fake ones
                 3  See waiting orders ........ created but not checked yet
                 4  Check waiting orders ...... run warranty / size-material checks
                 5  See checked results ....... ready to save
                 6  Save results .............. keep them so you can search by ID
                 7  Browse saved orders ....... list them or find one by ID
                 8  Find orders of one type ... filter a mixed list
                 9  Try the type-safety demo .. see a wrong-type error caught
                10  More options .............. seed, reset, original demo
                 0  Quit

                Shortcuts: b = back in submenus,  h = help,  q = quit
                """);
    }

    private void printHelp() {
        section("HELP - THE WORDS AND THE FLOW", null);
        System.out.println("""
                WAITING   Orders you created but have not checked yet.
                CHECKED   Orders that passed the checks. They are ready to save.
                SAVED     Orders kept in storage. You can find them by their full ID.
                SEED      Controls fake data. The same seed gives the same orders.
                THREAD    A worker. Several workers can check orders at the same time.

                USUAL PATH
                  1. Add orders            (menu 2)
                  2. Check waiting orders  (menu 4)
                  3. Save results          (menu 6)
                  4. Browse saved orders   (menu 7)

                WHERE THE JAVA FEATURES SHOW UP
                  - Lombok builders:   menu 2 -> type an order yourself
                  - JavaFaker:         menu 2 -> generate fake orders
                  - Lambdas / methods: menus 4 and 8
                  - Threads:           menu 4 -> choose a checking mode
                  - JPMS:              saved storage hides its internal index
                """);
    }

    private void guidedTour() {
        section("GUIDED TOUR", "I will do one full pass through the system. You only answer 2 questions.");

        int count = console.choice("How many of each type should I create? (1-10): ", 1, 10);
        workspace.generateElectronics(count);
        workspace.generateClothing(count);
        System.out.printf("Created %d electronics and %d clothing waiting orders with JavaFaker.%n", count, count);
        printWaitingLists();

        if (!console.confirm("Continue to the checking step?")) {
            next("menu 4 when you are ready.");
            return;
        }

        section("STEP 2 - CHECK ORDERS", "Electronics get a warranty check. Clothing gets a size/material check.");
        System.out.println("Each line beginning with [thread] shows the worker that did that check.");
        int checked = workspace.processElectronics(ProcessingMode.EXECUTOR, 2)
                + workspace.processClothing(ProcessingMode.EXECUTOR, 2);
        System.out.printf("Finished. %d checked result(s) are ready.%n", checked);

        if (!console.confirm("Continue by saving these checked orders?")) {
            next("menu 6 to save them later.");
            return;
        }

        storeResultsFor(3);
        printSavedLists();
        next("menu 7 to explore saved orders, or menu 1 to repeat the tour.");
    }

    private void addOrders() {
        section("ADD ORDERS", "New orders go into WAITING. They are not checked until you choose menu 4.");

        int choice = choose("HOW DO YOU WANT TO ADD ORDERS?", new String[]{
                "Type an electronics order yourself",
                "Type a clothing order yourself",
                "Generate fake orders with JavaFaker"
        });
        switch (choice) {
            case 1 -> typeElectronics();
            case 2 -> typeClothing();
            case 3 -> generateFakeOrders();
            case 0 -> { }
            default -> throw new IllegalStateException("Unexpected option: " + choice);
        }
    }

    private void typeElectronics() {
        section("NEW ELECTRONICS ORDER", "Example: Wireless Mouse | 24.99 | Acme | 18");
        String name = console.requiredText("Product name: ", "Wireless Mouse");
        BigDecimal price = console.price("Price (for example 24.99): ");
        String brand = console.requiredText("Brand: ", "Acme");
        int warranty = console.choice("Warranty in months (0-600): ", 0, 600);

        workspace.addElectronics(name, price, brand, warranty);
        System.out.println("Added to WAITING orders.");
        next("menu 3 to see it, or menu 4 to check it.");
    }

    private void typeClothing() {
        section("NEW CLOTHING ORDER", "Example: Cotton T-Shirt | 19.99 | M | Cotton");
        String name = console.requiredText("Product name: ", "Cotton T-Shirt");
        BigDecimal price = console.price("Price (for example 19.99): ");
        String size = console.requiredText("Size (XS, S, M, L or XL): ", "M");
        String material = console.requiredText("Material: ", "Cotton");

        workspace.addClothing(name, price, size, material);
        System.out.println("Added to WAITING orders.");
        next("menu 3 to see it, or menu 4 to check it.");
    }

    private void generateFakeOrders() {
        section("GENERATE FAKE ORDERS", "JavaFaker creates random-looking data. The seed controls it.");

        int target = choose("WHICH TYPE SHOULD BE GENERATED?", TYPE_OPTIONS);
        if (target == 0) {
            return;
        }

        int count = console.choice("How many per selected type? (1-1000): ", 1, 1000);
        switch (target) {
            case 1 -> {
                workspace.generateElectronics(count);
                System.out.printf("Added %d electronics order(s) to WAITING.%n", count);
            }
            case 2 -> {
                workspace.generateClothing(count);
                System.out.printf("Added %d clothing order(s) to WAITING.%n", count);
            }
            case 3 -> {
                workspace.generateElectronics(count);
                workspace.generateClothing(count);
                System.out.printf("Added %d electronics and %d clothing order(s) to WAITING.%n", count, count);
            }
            default -> throw new IllegalStateException("Unexpected option: " + target);
        }
        next("menu 3 to see them, or menu 4 to check them.");
    }

    private void showWaitingOrders() {
        section("WAITING ORDERS", "These were created but not checked yet. Checking does not remove them.");
        printWaitingLists();
        next("menu 4 to check them.");
    }

    private void processWaitingOrders() {
        section("CHECK WAITING ORDERS", "Checks create results that you can save. Waiting orders stay available.");

        if (workspace.pendingElectronicsCount() == 0 && workspace.pendingClothingCount() == 0) {
            System.out.println("There are no waiting orders yet. Choose menu 2 first.");
            return;
        }

        int target = choose("WHICH WAITING ORDERS SHOULD BE CHECKED?", TYPE_OPTIONS);
        if (target == 0) {
            return;
        }

        ProcessingMode mode = chooseProcessingMode();
        if (mode == null) {
            return;
        }

        int threads = mode == ProcessingMode.EXECUTOR
                ? console.choice("How many workers? (1-32): ", 1, 32)
                : 1;

        System.out.println("Running checks now. Watch the [thread] names in the output.");
        int checked = switch (target) {
            case 1 -> workspace.processElectronics(mode, threads);
            case 2 -> workspace.processClothing(mode, threads);
            case 3 -> workspace.processElectronics(mode, threads)
                    + workspace.processClothing(mode, threads);
            default -> throw new IllegalStateException("Unexpected option: " + target);
        };

        System.out.printf("Finished. %d checked result(s) are ready.%n", checked);
        System.out.println("The [thread] name on each line shows which worker did that check.");
        System.out.println("Previous checked results for the selected type(s) were replaced.");

        if (checked > 0 && console.confirm("Save these checked orders now?")) {
            storeResultsFor(target);
        } else {
            next("menu 5 to review results, or menu 6 to save them.");
        }
    }

    private void showProcessedResults() {
        section("CHECKED RESULTS", "These passed their checks and are ready to save. Saving does not remove them.");
        if (workspace.processedElectronicsCount() == 0 && workspace.processedClothingCount() == 0) {
            System.out.println("No checked results yet. Choose menu 4 to check waiting orders.");
            return;
        }

        System.out.println("Checked electronics:");
        printProcessedList(workspace.processedElectronics(), "None yet.");
        System.out.println("Checked clothing:");
        printProcessedList(workspace.processedClothing(), "None yet.");
        next("menu 6 to save them.");
    }

    private void saveResults() {
        section("SAVE CHECKED RESULTS", "Saving keeps results in storage. Saving the same ID again updates that order.");

        if (workspace.processedElectronicsCount() == 0 && workspace.processedClothingCount() == 0) {
            System.out.println("There is nothing to save yet. Check some waiting orders first (menu 4).");
            return;
        }

        int target = choose("WHICH RESULTS SHOULD BE SAVED?", TYPE_OPTIONS);
        if (target == 0) {
            return;
        }
        storeResultsFor(target);
        next("menu 7 to browse saved orders.");
    }

    private void browseSavedOrders() {
        section("BROWSE SAVED ORDERS", "Saved orders stay available until you reset or quit.");

        int choice = choose("WHAT DO YOU WANT TO SEE?", new String[]{
                "All saved electronics",
                "All saved clothing",
                "Find electronics by full ID",
                "Find clothing by full ID",
                "Show which workers did the checks"
        });
        if (choice == 0) {
            return;
        }

        switch (choice) {
            case 1 -> {
                System.out.println("Saved electronics:");
                printProcessedList(workspace.allStoredElectronics(), "None saved yet.");
            }
            case 2 -> {
                System.out.println("Saved clothing:");
                printProcessedList(workspace.allStoredClothing(), "None saved yet.");
            }
            case 3 -> {
                System.out.println("Copy the full ID from a saved electronics listing, then paste it below.");
                UUID id = console.uuid("Electronics ID: ");
                Optional<ProcessedOrder<Electronics>> found = workspace.findElectronics(id);
                if (found.isPresent()) {
                    printProcessedList(List.of(found.get()), "");
                } else {
                    System.out.println("No saved electronics order with that ID.");
                }
            }
            case 4 -> {
                System.out.println("Copy the full ID from a saved clothing listing, then paste it below.");
                UUID id = console.uuid("Clothing ID: ");
                Optional<ProcessedOrder<Clothing>> found = workspace.findClothing(id);
                if (found.isPresent()) {
                    printProcessedList(List.of(found.get()), "");
                } else {
                    System.out.println("No saved clothing order with that ID.");
                }
            }
            case 5 -> {
                System.out.println("These are the workers that checked at least one saved order:");
                System.out.println("Electronics workers:");
                printThreadNames(workspace.electronicsThreads());
                System.out.println("Clothing workers:");
                printThreadNames(workspace.clothingThreads());
            }
            default -> throw new IllegalStateException("Unexpected option: " + choice);
        }
    }

    private void filterWaitingOrders() {
        section("FIND ORDERS OF ONE TYPE", "Waiting orders are combined into one mixed list, then split by product type.");

        int target = choose("WHICH TYPE DO YOU WANT TO FIND?", TYPE_OPTIONS);
        if (target == 0) {
            return;
        }

        if (target == 1 || target == 3) {
            List<Electronics> matched = workspace.filterPendingElectronics();
            System.out.println("Electronics found: " + matched.size());
            workspace.visitAll(matched, product -> System.out.println("  " + product.describe()));
        }
        if (target == 2 || target == 3) {
            List<Clothing> matched = workspace.filterPendingClothing();
            System.out.println("Clothing found: " + matched.size());
            workspace.visitAll(matched, product -> System.out.println("  " + product.describe()));
        }
        System.out.println("This uses a lambda filter and a method reference internally.");
    }

    private void typeSafetyDemo() {
        section("TYPE-SAFETY DEMO", "I will deliberately give a Clothing to the Electronics checker.");
        System.out.println("The checker carries a Class<T> token, so it should reject the wrong type clearly.");
        workspace.demonstrateGenericTypeMismatch();
        System.out.println("That error is expected. It proves the generic type check caught the problem.");
    }

    private void moreOptions() {
        section("MORE OPTIONS", "Settings and the original scripted demonstration.");

        int choice = choose("CHOOSE AN OPTION", new String[]{
                "Run the original automated demo (no menu)",
                "Change the JavaFaker seed",
                "Reset the workspace (clear all orders)"
        });
        switch (choice) {
            case 1 -> {
                System.out.println("Running the original demo...");
                OrderManagementApplication.runDemo();
            }
            case 2 -> {
                long seed = console.longNumber("New seed (for example 42): ");
                workspace.setSeed(seed);
                System.out.println("Seed changed. It affects fake orders generated from now on.");
            }
            case 3 -> {
                if (console.confirm("Clear all waiting, checked and saved orders?")) {
                    workspace.reset();
                    System.out.println("Workspace cleared.");
                } else {
                    System.out.println("Nothing was cleared.");
                }
            }
            case 0 -> { }
            default -> throw new IllegalStateException("Unexpected option: " + choice);
        }
    }

    private ProcessingMode chooseProcessingMode() {
        int choice = choose("HOW SHOULD ORDERS BE CHECKED?", new String[]{
                "One by one - easiest to follow",
                "All at once - uses the common worker pool",
                "Fixed team of workers - you choose how many"
        });
        return switch (choice) {
            case 1 -> ProcessingMode.SEQUENTIAL;
            case 2 -> ProcessingMode.PARALLEL;
            case 3 -> ProcessingMode.EXECUTOR;
            case 0 -> null;
            default -> throw new IllegalStateException("Unexpected option: " + choice);
        };
    }

    private void storeResultsFor(int target) {
        long electronicsBefore = workspace.storedElectronicsCount();
        long clothingBefore = workspace.storedClothingCount();

        int savedElectronics = (target == 1 || target == 3) ? workspace.storeElectronics() : 0;
        int savedClothing = (target == 2 || target == 3) ? workspace.storeClothing() : 0;

        System.out.printf("Saved %d electronics and %d clothing result(s).%n", savedElectronics, savedClothing);
        System.out.printf("Storage: electronics %d -> %d, clothing %d -> %d.%n",
                electronicsBefore,
                workspace.storedElectronicsCount(),
                clothingBefore,
                workspace.storedClothingCount());
    }

    private int choose(String title, String... options) {
        section(title, null);
        for (int i = 0; i < options.length; i++) {
            System.out.printf("  %d) %s%n", i + 1, options[i]);
        }
        System.out.println("  0) Back");
        return console.choice("Your choice: ", 0, options.length);
    }

    private void section(String title, String description) {
        System.out.printf("%n=== %s ===%n", title);
        if (description != null && !description.isBlank()) {
            System.out.println(description);
        }
    }

    private void next(String message) {
        System.out.println("Next: " + message);
    }

    private void printWaitingLists() {
        System.out.println("Waiting electronics:");
        printProductList(workspace.pendingElectronics(), "None yet. Use menu 2 to add some.");
        System.out.println("Waiting clothing:");
        printProductList(workspace.pendingClothing(), "None yet. Use menu 2 to add some.");
    }

    private void printSavedLists() {
        System.out.println("Saved electronics:");
        printProcessedList(workspace.allStoredElectronics(), "None yet.");
        System.out.println("Saved clothing:");
        printProcessedList(workspace.allStoredClothing(), "None yet.");
    }

    private void printProductList(List<? extends Product> orders, String emptyMessage) {
        if (orders.isEmpty()) {
            System.out.println("  " + emptyMessage);
            return;
        }
        int index = 1;
        for (Product product : orders) {
            System.out.printf("  [%d] %s%n", index++, product.getName());
            System.out.println("      ID: " + product.getId());
            System.out.println("      Price: $" + product.getPrice());
            System.out.println("      Details: " + product.describe());
        }
    }

    private void printProcessedList(List<?> orders, String emptyMessage) {
        if (orders.isEmpty()) {
            if (!emptyMessage.isBlank()) {
                System.out.println("  " + emptyMessage);
            }
            return;
        }
        int index = 1;
        for (Object order : orders) {
            ProcessedOrder<?> processed = (ProcessedOrder<?>) order;
            System.out.printf("  [%d] %s%n", index++, processed.product().getName());
            System.out.println("      ID: " + processed.product().getId());
            System.out.println("      Checked by: " + processed.processedBy());
            System.out.println("      Checked at: " + processed.processedAt());
            System.out.println("      Details: " + processed.product().describe());
        }
    }

    private void printThreadNames(List<String> threadNames) {
        List<String> unique = threadNames.stream().distinct().sorted().toList();
        if (unique.isEmpty()) {
            System.out.println("  (none yet)");
            return;
        }
        unique.forEach(name -> System.out.println("  " + name));
    }

    private void safe(Runnable action) {
        try {
            action.run();
        } catch (CliExitException ex) {
            throw ex;
        } catch (RuntimeException ex) {
            String message = ex.getMessage();
            System.out.println("That did not work: " + (message == null ? ex.getClass().getSimpleName() : message));
            System.out.println("Check the input and try again.");
        }
    }
}
