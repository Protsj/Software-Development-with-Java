package org.example;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class TransactionReportGenerator {
    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("\n--- 10 найбільших витрат ---");
        for(Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExpenseCategoryReport(List<Transaction> transactions) {
        System.out.println("\n--- Звіт по витратах (1 '*' = 1000 грн) ---");

        Map<String, Double> expensesByCategory = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                Transaction::getDescription,
                Collectors.summingDouble(t -> Math.abs(t.getAmount()))
                ));

        expensesByCategory.forEach((category, amount) -> {
            int starsCount = (int)(amount / 1000);
            String stars = "*".repeat(Math.max(0, starsCount));
            System.out.println(category + ": " + amount + " грн " + stars);
        });
    }
}
