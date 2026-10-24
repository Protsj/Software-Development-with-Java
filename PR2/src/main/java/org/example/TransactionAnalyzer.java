package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {
    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return(int) transactions.stream()
                .filter(t -> LocalDate.parse(t.getDate(), dateFormatter)
                .format(DateTimeFormatter.ofPattern("MM-yyyy")).equals(monthYear)).count();
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static void findExtremesForPeriod(List<Transaction> transactions, String monthYear) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        DoubleSummaryStatistics stats = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> LocalDate.parse(t.getDate(), dateFormatter)
                .format(DateTimeFormatter.ofPattern("MM-yyyy")).equals(monthYear))
                .mapToDouble(Transaction::getAmount)
                .summaryStatistics();

        if(stats.getCount() > 0) {
            System.out.println("Найбільша витрата за " + monthYear + ": " + Math.abs(stats.getMin()) + " грн");
            System.out.println("Найменша витрата за " + monthYear + ": " + Math.abs(stats.getMax()) + " грн");
        } else {
            System.out.println("Витрат за " + monthYear + " не знайдено.");
        }
    }
}
