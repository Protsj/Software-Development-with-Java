import org.example.Transaction;
import org.example.TransactionCSVReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

public class TransactionCSVReaderTest {
    @Test
    public void testParseTransactions() {
        List<String> mockData = Arrays.asList(
                "Дата,Сума,Категорія",
                "01-02-2023,50.0,Дохід",
                "15-02-2023,-20.0,Витрата"
        );

        List<Transaction> transactions = TransactionCSVReader.parseTransactions(mockData);

        Assertions.assertEquals(2, transactions.size(), "Має бути зчитано 2 транзакції");
        Assertions.assertEquals(50.0, transactions.get(0).getAmount(), "Сума першої транзакції хибна");
        Assertions.assertEquals("Витрата", transactions.get(1).getDescription(), "Категорія другої транзакції хибна");
    }
}
