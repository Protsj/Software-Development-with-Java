import java.io.*;

public class StudentInfoReader {
    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new FileReader("students.txt"))) {
            String line;
            System.out.println("\n--- Список студентів з файлу ---");
            while((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch(IOException e) {
            System.out.println("Помилка читання: " + e.getMessage());
        }
    }
}
