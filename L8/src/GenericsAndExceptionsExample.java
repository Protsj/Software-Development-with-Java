import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

interface TestScore<T> {
    boolean isSatisfactory(T score, T threshold);
}

interface FileAction {
    boolean processFile(BufferedReader read) throws IOException;
}

public class GenericsAndExceptionsExample {
    public static void main(String[] args) {
        TestScore<Integer> intTest = (score, threshold) -> score >= threshold;
        TestScore<Double> doubleTest = (score, threshold) -> score >= threshold;

        System.out.println("Цілі числа (85 >= 60): " + intTest.isSatisfactory(85, 60));
        System.out.println("Дробові числа (72.5 >= 75.0): " + doubleTest.isSatisfactory(72.5, 75.0));

        FileAction fileReader = (reader) -> {
            int data = reader.read();
            return data != -1;
        };

        try (BufferedReader br = new BufferedReader(new StringReader("Текстові дані"))) {
            System.out.println("\nЧи є дані у потоці? " + fileReader.processFile(br));
        } catch (IOException e) {
            System.out.println("Помилка вводу/виводу");
        }
    }
}
