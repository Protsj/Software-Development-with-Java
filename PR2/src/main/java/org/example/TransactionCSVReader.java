package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {
    public static List<Transaction> readTransactions(String filePath) {
        List<String> lines = readLinesFromUrl(filePath);
        return parseTransactions(lines);
    }

    private static List<String> readLinesFromUrl(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            URL url = new URL(filePath);
            try(BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while((line = br.readLine()) != null) {
                    lines.add(line);
                }
            }
        } catch(IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        }
        return lines;
    }

    public static List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for(int i = 1; i < lines.size(); i++) {
            String[] values = lines.get(i).split(",");
            if(values.length == 3) {
                try {
                    Transaction transaction = new Transaction(values[0], Double.parseDouble(values[1]), values[2]);
                    transactions.add(transaction);
                } catch(NumberFormatException e) {
                    System.err.println("Помилка формату числа у рядку: " + lines.get(i));
                }
            }
        }
        return transactions;
    }
}
