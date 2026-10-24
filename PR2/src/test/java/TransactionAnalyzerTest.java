import org.example.Transaction;
import org.example.TransactionAnalyzer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class TransactionAnalyzerTest {
    @Test
    public void testFindTopExpenses() {
        List<Transaction> mockTransactions = Arrays.asList(
                new Transaction("01-01-2023", -50.0, "Витрата 1"),
                new Transaction("02-01-2023", 100.0, "Дохід"),
                new Transaction("03-01-2023", -200.0, "Витрата 2"),
                new Transaction("04-01-2023", -10.0, "Витрата 3")
        );

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(mockTransactions);

        Assertions.assertEquals(3, topExpenses.size(), "Повинно бути знайдено 3 витрати");
        Assertions.assertEquals(-200.0, topExpenses.get(0).getAmount(), "Найбільша витрата відсортована неправильно");
    }
}
